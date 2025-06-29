package com.utec.model;

import jakarta.validation.constraints.*;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name="Estado")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Estado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_Estado")
    private Integer idestado;

    @Column(name="descripcion", nullable = false,length = 100)
    @NotBlank(message = "La descripción no puede estar vacía")
    @Size(min = 1, max = 100, message = "La descripción debe tener entre 1 y 100 caracteres")
    @Pattern(regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñü,\\- ]+$", message = "La descripción contiene caracteres inválidos")
    private String descripcion;

    @CreatedBy
    @Column(name = "crea_Por")
    private String creadoPor;

    @CreatedDate
    @Column(name = "fech_Creacion")
    private java.time.LocalDate fechaCreacion;

    @LastModifiedBy
    @Column(name = "modi_Por")
    private String modificadoPor;

    @LastModifiedDate
    @Column(name = "fech_Modificacion")
    private java.time.LocalDate fechaModificacion;

    public Estado(Integer id, String descripcion) {
        this.idestado = id;
        this.descripcion = descripcion;
    }
}
