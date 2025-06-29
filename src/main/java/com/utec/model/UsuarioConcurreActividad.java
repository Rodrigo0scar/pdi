package com.utec.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@IdClass(UsuarioConcurreActividadId.class)
@Builder
@Entity
@Table(name = "usuario_concurre_actividad")

public class UsuarioConcurreActividad {

    @Id
    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @Id
    @ManyToOne
    @JoinColumn(name = "id_actividad")
    private Actividad actividad;

    @ManyToOne
    @JoinColumn(name = "id_estado")
    private Estado estado;


    private Date fechCobro;


    @Min(value = 1, message = "El costo no puede ser negativo")
    @Column(name = "mont_cobrado")
    private BigDecimal montCobrado;

    //cambio para pagos de boolean a Boolean(wrapper)
    @Column(name = "asistencia")
    private Boolean asistencia;

    //cambio para pagos de boolean a Boolean(wrapper)
    @Column(name = "pago_ticket")
    private  Boolean pagoTicket;


}
