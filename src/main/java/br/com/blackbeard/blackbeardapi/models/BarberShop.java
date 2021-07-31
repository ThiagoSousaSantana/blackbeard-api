package br.com.blackbeard.blackbeardapi.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class BarberShop {

    @Id
    private UUID id;
    private String name;
    private String imageUrl;

    @ManyToOne
    private Address address;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    @CreatedDate
    private LocalDateTime createDate;

    @JsonIgnore
    @OneToMany(mappedBy = "barberShop")
    private List<Barber> barber;

    public void update(BarberShop barberShop) {
        this.name = barberShop.getName();
        this.imageUrl = barberShop.getImageUrl();
    }

    public void generateId() {
        this.id = UUID.randomUUID();
    }
}

