package com.utec.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Perfil {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_perfil")
    private Integer idPerfil;

    @Column (name = "Nombre", length = 50, unique = true, nullable = false)
    @Pattern(regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñü ]+$", message = "El nombre debe tener el formato adecuado")
    private String nombre;

    @Column (name = "Descripcion", length = 250, nullable = false)
    @Pattern(regexp = "^[A-Za-z0-9ÁÉÍÓÚáéíóúÑñü,.\\- ]+$")
    private String description;

    @ManyToOne
    @JoinColumn(name = "Id_Estado", nullable = false, foreignKey = @ForeignKey(name = "FK_Perfil_Estado"))
    @NotNull
    private Estado estado;

    @CreatedBy
    @Column(name = "Crea_Por")
    private String creadoPor;

    @CreatedDate
    @Column(name = "fech_creacion")
    private LocalDate fechaCreacion;

    @LastModifiedBy
    @Column(name = "Modi_Por")
    private String modificadoPor;

    @LastModifiedDate
    @Column(name = "Fech_Modificacion")
    private LocalDate fechaModificacion;
}
