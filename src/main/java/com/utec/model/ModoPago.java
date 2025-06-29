package com.utec.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity(name = "modo_pago")
@Table(name = "modo_pago")
public class ModoPago {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_modo_pago")
    private Integer id;

    @Column(name = "modo", nullable = false, length = 50)
    @Pattern(regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñü ]+$", message = "Solo se permiten letras y espacios")
    @Size(min = 1, max = 50, message = "debe tener entre 1 y 50 caracteres")
    private String modo;
}
