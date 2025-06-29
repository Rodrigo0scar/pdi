package com.utec.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "socio_paga_cuota")
public class SocioPagaCuota {
    @EmbeddedId
    private SocioPagaCuotaId id;

    @MapsId("idUsuario")
    @ManyToOne(optional = false)
    @JoinColumn(name = "id_usuario", nullable = false,
            foreignKey = @ForeignKey(name = "FK_Socio_Paga"))
    private Socio socio;

    @MapsId("idCuota")
    @ManyToOne(optional = false)
    @JoinColumn(name = "id_cuota", nullable = false,
            foreignKey = @ForeignKey(name = "FK_Cuota_Paga"))
    private Cuota cuota;

    @Column(name = "fech_cobro", nullable = false)
    private LocalDate fechCobro;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_modo_pago", nullable = false,
            foreignKey = @ForeignKey(name = "FK_Modo_Pago_Cuota"))
    private ModoPago modoPago;
}
