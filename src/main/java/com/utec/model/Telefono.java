package com.utec.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import java.time.LocalDate;

@Entity
@Builder
@Getter
@Setter
@ToString(exclude = "usuario")
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "telefonos")
public class Telefono {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_telefono")
    private Integer idTelefono;

    @ManyToOne
    @JoinColumn(name = "id_usuario",nullable = false ,foreignKey = @ForeignKey (name = "FK_TEL_USUARIO"))
    private Usuario usuario;

    @Column(name = "num_telefono")
    private Integer numTelefono;

    @CreatedBy
    @Column(name = "Crea_Por")
    private String creaPor;

    @CreatedDate
    @Column(name = "Fech_Creacion")
    private LocalDate fechaCreacion;

    @LastModifiedBy
    @Column(name = "Modi_Por")
    private String modificadoPor;

    @LastModifiedDate
    @Column(name = "Fech_Modificacion")
    private LocalDate fechaModificacion;
}
