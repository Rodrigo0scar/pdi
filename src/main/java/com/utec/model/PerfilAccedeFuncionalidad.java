package com.utec.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "perfil_accede_funcionalidad")
@EntityListeners(AuditingEntityListener.class)
public class PerfilAccedeFuncionalidad {
    @EmbeddedId
    private PerfilAccedeFuncionalidadId id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "Id_Perfil", insertable = false, updatable = false)
    private Perfil perfil;

    @ManyToOne(optional = false)
    @JoinColumn(name = "Id_Funcionalidad", insertable = false, updatable = false)
    private Funcionalidad funcionalidad;

    @ManyToOne
    @JoinColumn(name = "Id_Estado")
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

    public PerfilAccedeFuncionalidad(PerfilAccedeFuncionalidadId id) {
        this.id = id;
    }
}
