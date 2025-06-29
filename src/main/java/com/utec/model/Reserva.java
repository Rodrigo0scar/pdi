package com.utec.model;


import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "reserva")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Reserva {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idReserva;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "id_espacio", nullable = false)
    private Espacio espacio;

    @Column(name = "fech_hora_reserva", nullable = false)
    private LocalDateTime fechHoraReserva;

    @Column(nullable = false)
    private Integer duracion;

    @Column(name = "cant_personas", nullable = false)
    private Integer cantPersonas;

    @ManyToOne
    @JoinColumn(name = "id_estado", nullable = false)
    private Estado estado;

    @Column(name = "impo_total", nullable = false)
    private BigDecimal impoTotal;

    @Column(name = "fech_pago_senia")
    private LocalDate fechPagoSenia;

    @Column(name = "impo_seni_pagado")
    private BigDecimal impoSeniPagado;

    @Column(name = "fech_cobro", nullable = false)
    private LocalDateTime fechCobro;

    @Column(name = "fech_vto_senia", nullable = false)
    private LocalDate fechVtoSenia;

    //@Column(name = "id_modo_pago")
    //private Integer idModoPago;
    @ManyToOne(optional = false)
    @JoinColumn(name = "id_modo_pago", nullable = false,
            foreignKey = @ForeignKey(name = "FK_Modo_Pago_Cuota"))
    private ModoPago modoPago;

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
