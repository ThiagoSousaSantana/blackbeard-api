package br.com.blackbeard.blackbeardapi.controllers;

import br.com.blackbeard.blackbeardapi.dtos.address.AddressRequest;
import br.com.blackbeard.blackbeardapi.dtos.barbershop.BarberShopRequest;
import br.com.blackbeard.blackbeardapi.dtos.barbershop.BarberShopResponse;
import br.com.blackbeard.blackbeardapi.exceptions.ObjectNotFoundException;
import br.com.blackbeard.blackbeardapi.models.Address;
import br.com.blackbeard.blackbeardapi.models.BarberShop;
import br.com.blackbeard.blackbeardapi.service.BarberShopService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Map;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BarberShopController.class)
class BarberShopControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BarberShopService service;

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    void shouldReturnCreatedWhenPostAValidRequest() throws Exception {
        var addressRequest = AddressRequest.builder()
                .city("Test")
                .district("Test")
                .street("Test")
                .number("42")
                .build();


        var barberShopRequest = BarberShopRequest.builder()
                .name("Test")
                .imageUrl("https://www.google.com")
                .address(addressRequest)
                .build();

        var address = Address.builder()
                .city(addressRequest.getCity())
                .district(addressRequest.getDistrict())
                .street(addressRequest.getStreet())
                .number(addressRequest.getNumber())
                .build();

        var barberShop = BarberShop.builder()
                .name(barberShopRequest.getName())
                .imageUrl(barberShopRequest.getImageUrl())
                .address(address)
                .build();

        var barberShopReturned = BarberShop.builder()
                .id(UUID.randomUUID())
                .name(barberShopRequest.getName())
                .imageUrl(barberShopRequest.getImageUrl())
                .build();

        var barberShopResponseExpected = BarberShopResponse.builder()
                .id(barberShopReturned.getId())
                .name(barberShopReturned.getName())
                .imageUrl(barberShopReturned.getImageUrl())
                .build();

        when(service.save(barberShop)).thenReturn(barberShopReturned);

        var json = mapper.writeValueAsString(barberShopRequest);

        mockMvc.perform(post("/barberShop")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andExpect(content().string(this.mapper.writeValueAsString(barberShopResponseExpected)));
    }

    @Test
    void shouldReturnBadRequestAndErrorListWhenPostAnInvalidRequest() throws Exception {
        var barberShopRequest = BarberShopRequest.builder().build();

        var errors = Map.of(
                "address", "must not be null",
                "imageUrl", "must not be blank",
                "name", "must not be blank"
        );

        var requestJson = mapper.writeValueAsString(barberShopRequest);

        mockMvc.perform(post("/barberShop")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is(errors)))
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())));
    }

    @Test
    void shouldReturnAcceptWhenPutAValidRequest() throws Exception {
        var addressRequest = AddressRequest.builder()
                .city("Test")
                .district("Test")
                .street("Test")
                .number("42")
                .build();


        var barberShopRequest = BarberShopRequest.builder()
                .name("Test")
                .imageUrl("https://www.google.com")
                .address(addressRequest)
                .build();

        var address = Address.builder()
                .city(addressRequest.getCity())
                .district(addressRequest.getDistrict())
                .street(addressRequest.getStreet())
                .number(addressRequest.getNumber())
                .build();

        var barberShop = BarberShop.builder()
                .name(barberShopRequest.getName())
                .imageUrl(barberShopRequest.getImageUrl())
                .address(address)
                .build();

        var barberShopId = UUID.randomUUID();

        var json = mapper.writeValueAsString(barberShopRequest);

        mockMvc.perform(put("/barberShop")
                        .param("barberShopId", barberShopId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isAccepted());

        verify(service).update(barberShop, barberShopId);
    }

    @Test
    void shouldReturnBadRequestWhenPutAnInvalidRequest() throws Exception {
        var barberShopRequest = BarberShopRequest.builder().build();
        var barberShopId = UUID.randomUUID();

        var errors = Map.of(
                "address", "must not be null",
                "imageUrl", "must not be blank",
                "name", "must not be blank"
        );

        var requestJson = mapper.writeValueAsString(barberShopRequest);

        mockMvc.perform(put("/barberShop")
                        .param("barberShopId", barberShopId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is(errors)))
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())));
    }

    @Test
    void shouldReturnOkWhenGetByValidId() throws Exception {
        var barberShopId = UUID.randomUUID();
        var barberShop = BarberShop.builder().id(barberShopId).build();

        var expectedResponse = BarberShopResponse.builder().id(barberShopId).build();

        when(service.findById(barberShopId)).thenReturn(barberShop);

        var json = mapper.writeValueAsString(expectedResponse);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/barberShop/{id}", barberShopId)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(content().string(json))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnNotFoundWhenGetByInvalidId() throws Exception {
        when(service.findById(any())).thenThrow(ObjectNotFoundException.class);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/barberShop/{id}", UUID.randomUUID())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}
