package com.utec.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "Tipo_Actividad")
@EntityListeners(AuditingEntityListener.class)
public class TipoActividad {

    //En la letra menciona que al crear estado=inactivo. En el script no tiene el atributo Estado.

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_Tipo_Actividad")
    private Integer idTipoActividad;

    @Column(name = "Tipo",nullable = false,length = 30)
    //@Pattern(regexp = "^[A-Za-z0-9ÁÉÍÓÚáéíóúÑñü,.\\- ]+$")
    private String tipo;

    @Column(name = "Descripcion",length = 50)
    //@Pattern(regexp = "^[A-Za-z0-9ÁÉÍÓÚáéíóúÑñü,.\\- ]+$")
    private String descripcion;

    //Los atributos Fech_Baja,Razo_Baja,Comentarios_Baja los quitaria por los atributos de aduitoria.
    /*@Column(name = "Fech_Baja")
    private LocalDate fechaBaja;

    @Column(name = "Razo_Baja",length = 50)
    //@Pattern(regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñü,.\\- ]+$")
    private String razonBaja;*/

    @Column(name = "Comentarios_Baja", length = 200)
    private String comentariosBaja;

    /*@ManyToOne
    @JoinColumn(name = "Id_Usuario_Baja",foreignKey = @ForeignKey(name = "Id_Usuario_Baja"),referencedColumnName = "Id_Usuario")
    private Usuario usuarioBaja;*/

    @ManyToOne
    @JoinColumn(name = "Id_Estado",nullable = false,foreignKey = @ForeignKey(name = "Fk_TipoActividad_Estado"),referencedColumnName = "Id_Estado")
    private Estado estado;

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
