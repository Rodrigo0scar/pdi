package com.utec.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Espacio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_Espacio")
    private Integer idEspacio;

    @Column(name = "Nombre", nullable = false, unique = true, length = 50)
    private String nombre;

    @ManyToOne
    @JoinColumn(name = "Id_Estado", nullable = false)
    private Estado estado;

    @Column(name = "Capa_Maxima", nullable = false)
    private Integer capacidadMaxima;

    @Column(name = "Prec_Rese_Socio", nullable = false)
    private BigDecimal precioReservaSocio;

    @Column(name = "Prec_Rese_No_Socio", nullable = false)
    private BigDecimal precioReservaNoSocio;

    @Column(name = "Observaciones", length = 250)
    private String observaciones;

    @Column(name = "Fech_Vige_Precio", nullable = false)
    private LocalDate fechaVigenciaPrecio;

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
