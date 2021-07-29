package br.com.blackbeard.blackbeardapi.serviceTest;

import br.com.blackbeard.blackbeardapi.exceptions.ObjectNotFoundException;
import br.com.blackbeard.blackbeardapi.models.Address;
import br.com.blackbeard.blackbeardapi.models.BarberShop;
import br.com.blackbeard.blackbeardapi.repositories.BarberShopRepository;
import br.com.blackbeard.blackbeardapi.service.AddressService;
import br.com.blackbeard.blackbeardapi.service.BarberShopService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BarberShopServiceTest {

    @InjectMocks
    private BarberShopService service;

    @Mock
    private BarberShopRepository repository;

    @Mock
    private AddressService addressService;

    private BarberShop barberShop;

    @BeforeEach
    public void setup() {
        var address = Address.builder()
                .id(UUID.randomUUID())
                .build();

        this.barberShop = BarberShop.builder()
                .id(UUID.randomUUID())
                .address(address).build();
    }

    @Test
    void shouldFindABarberShopById() {
        when(repository.findById(barberShop.getId())).thenReturn(Optional.of(barberShop));

        var result = service.findById(barberShop.getId());

        assertThat(result).isEqualTo(barberShop);
        verify(repository, times(1)).findById(barberShop.getId());
    }

    @Test
    void shouldThrowExceptionWhenFindABarberShopById() {
        var barberShopId = barberShop.getId();

        when(repository.findById(barberShopId)).thenReturn(Optional.empty());

        var exception = assertThrows(ObjectNotFoundException.class,
                () -> service.findById(barberShopId));

        assertThat(exception).hasMessage("Object not found");
    }

    @Test
    void shouldCreateAnBarberShop() {
        doNothing().when(addressService).save(barberShop.getAddress());
        when(repository.save(barberShop)).thenReturn(barberShop);

        var result = service.create(barberShop);

        assertThat(result).isEqualTo(barberShop);
        verify(addressService, times(1)).save(barberShop.getAddress());
    }

    @Test
    void shouldFindAllBarberShop() {
        final Pageable pageable = PageRequest.of(20, 20);

        var barberShop1 = BarberShop.builder().build();
        var barberShop2 = BarberShop.builder().build();

        var listBarberShop = new ArrayList<BarberShop>();
        listBarberShop.add(barberShop);
        listBarberShop.add(barberShop1);
        listBarberShop.add(barberShop2);

        var listPageBarberShop = new PageImpl<>(listBarberShop);

        when(repository.findAll(pageable)).thenReturn(listPageBarberShop);

        var result = service.listAll(pageable);

        assertThat(result.getSize()).isEqualTo(3);
    }

    @Test
    void shouldUpdateAnBarberShopById() {
        var barberShopID = barberShop.getId();
        var newBarberShop = BarberShop.builder().build();

        doNothing().when(addressService).update(barberShop.getAddress(), newBarberShop.getAddress());
        when(repository.findById(barberShopID)).thenReturn(Optional.of(barberShop));
        when(repository.save(barberShop)).thenReturn(newBarberShop);

        service.update(newBarberShop, barberShopID);

        verify(repository, times(1)).save(barberShop);
        verify(addressService, times(1)).update(barberShop.getAddress(), newBarberShop.getAddress());
        assertThat(repository.save(barberShop)).isEqualTo(newBarberShop);
    }
}