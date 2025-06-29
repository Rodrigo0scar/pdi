package com.utec.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "actividad")

public class Actividad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_actividad")
    private Integer id;

    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "nombre")
    private String nombre;

    @NotBlank(message = "La descripción no puede estar vacía")
    @Size(min = 1, max = 250)
    @Pattern(regexp = "^[A-Za-z0-9ÁÉÍÓÚáéíóúÑñü., \\-]+$", message = "La descripción contiene caracteres inválidos")
    @Column(name = "descripcion")
    private String descripcion;

    @NotBlank(message = "Este campo no puede estar vacío")
    @Size(min = 1, max = 100)
    @Pattern(regexp = "^[A-Za-z0-9ÁÉÍÓÚáéíóúÑñü.,\\- ]+$", message = "El campo contiene caracteres inválidos")
    @Column(name = "objetivo")
    private String objetivo;

    @NotNull(message = "La fecha y hora no puede ser nula")
    @Future(message = "La fecha y hora debe ser futura")
    @Column(name = "fech_hora_actividad")
    private LocalDateTime fech_hora_actividad;

    @NotNull(message = "La duracion")
    @Min(value = 1,message = "La duración debe ser mayor a 0")
    @Column(name = "duracion")
    private  Integer duracion;

    @Column(name = "requ_inscripcion")
    private boolean requ_inscripcion;

    @NotNull(message = "La fecha de apertura no puede ser nula")
    @Column(name = "fech_apertura_inscripcion")
    private LocalDate fech_apertura_inscripcion;

    @NotNull
    @Min(value = 0, message = "El costo no puede ser negativo")
    @Column(name = "costo")
    private BigDecimal costo;

    @NotBlank(message = "Las observaciones no pueden estar vacías")
    @Pattern(regexp = "^[A-Za-z0-9ÁÉÍÓÚáéíóúÑñü.,\\- ]+$", message = "El campo contiene caracteres inválidos")
    @Column(name = "observaciones")
    private String observaciones;

    @ManyToOne
    @JoinColumn(name = "id_estado")
    private Estado estado;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "id_tipo_actividad", nullable = false)
    private TipoActividad tipoActividad;

    @ManyToOne
    @JoinColumn(name = "id_espacio")
    private Espacio espacio;

    @ManyToOne
    @JoinColumn(name = "id_modo_pago")
    private ModoPago modoPago;


}
