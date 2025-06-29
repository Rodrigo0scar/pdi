package com.utec.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
//lombok

@Entity(name = "socio")
@Table(name = "socio")
@EqualsAndHashCode(callSuper = true)
@PrimaryKeyJoinColumn(name = "id_usuario")
public class Socio extends Usuario{

    //no necesita fk ya que al ser herencia las genera automaticamente

    //atributos propios de la clase

    @Column(name = "dif_auditiva", nullable = false)
    @NotNull(message = "Este campo es obligatorio")
    private Boolean difAuditiva;

    //uso de legua de senias
    @Column(name = "uso_leng_senia", nullable = false)
    @NotNull(message = "Este campo es obligatorio")
    private Boolean usoLengSenias;

    //paga desde
    @Column(name = "paga_desde", nullable = false)
    @Min(value = 1, message = "El valor minimo permitido es 1")
    @Max(value = 12, message = "El valor maximo permitido es 12")
    @NotNull(message = "Este campo es obligatorio")
    private Integer pagaDesde;

    //paga hasta
    @Column(name = "paga_hasta", nullable = false)
    @Min(value = 1, message = "El valor minimo permitido es 1")
    @Max(value = 12, message = "El valor maximo permitido es 12")
    @NotNull(message = "Este campo es obligatorio")
    private Integer pagaHasta;

    @ManyToOne
    @JoinColumn(name = "id_estado", nullable = false, foreignKey = @ForeignKey(name = "FK_Socio_Estado"))
    private Estado estadoSocio;

    @ManyToOne
    @JoinColumn(name = "id_subcomision", nullable = false, foreignKey = @ForeignKey(name = "FK_Socio_Subcomision"))
    private Subcomision subcomision;

    @ManyToOne
    @JoinColumn(name = "id_categoria", nullable = false, foreignKey = @ForeignKey(name = "FK_Socio_Categoria"))
    private Categoria categoria;

}
