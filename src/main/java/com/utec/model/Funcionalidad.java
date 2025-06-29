package com.utec.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "funcionalidad")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Funcionalidad {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
@Column(name = "id_funcionalidad")
private Integer id;

@Column(name = "nombre", nullable = false, unique = true, length = 50)
private String nombre;

@Column(name = "descripcion", nullable = false, length = 250)
private String descripcion;

@ManyToOne
@JoinColumn(name = "id_estado", nullable = false)
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
