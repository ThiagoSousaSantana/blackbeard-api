package br.com.blackbeard.blackbeardapi.dtos.barbershop;

import br.com.blackbeard.blackbeardapi.models.Address;
import br.com.blackbeard.blackbeardapi.models.Image;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BarberShopResponse {
    private UUID id;
    private String name;
    private String urlLogo;
    private List<Image> images;
    private Address address;
}
