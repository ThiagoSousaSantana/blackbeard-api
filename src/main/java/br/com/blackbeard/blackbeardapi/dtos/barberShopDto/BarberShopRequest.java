package br.com.blackbeard.blackbeardapi.dtos.barberShopDto;

import br.com.blackbeard.blackbeardapi.dtos.addressDto.AddressRequest;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class BarberShopRequest {

    @NotBlank
    private String name;

    @URL
    @NotBlank
    private String imageUrl;

    @NotNull
    private AddressRequest address;
}