package com.utec.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "tipo_documento")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class TipoDocumento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_Tipo_Documento")
    private Integer id;

    @NotNull
    @Size(min = 1, max = 50)
    @Pattern(regexp = "^[A-Za-z0-9ÁÉÍÓÚáéíóúÑñü., \\-]+$", message = "Nombre de documento inválido")
    @Column(name = "Nomb_Documento", nullable = false, unique = true)
    private String nombre;

    @NotNull
    @Size(min = 1, max = 250)
    @Pattern(regexp = "^[A-Za-z0-9ÁÉÍÓÚáéíóúÑñü., \\-]+$", message = "Descripción inválida")
    @Column(name = "Descripcion", nullable = false)
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
}
