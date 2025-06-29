package com.utec.model;


import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "auditoria")
public class Auditoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_Auditoria")
    private Integer id;

    @Column(name = "Usuario", nullable = false, length = 50)
    private String usuario;

    @Column(name = "fecha_hora", insertable = false, updatable = false)
    private LocalDateTime fechaHora;

    /*@Column(name = "Fecha_Hora")
    private LocalDateTime fechaHora;*/

    @Column(name = "Terminal", nullable = false, length = 50)
    private String terminal;

    @Column(name = "Operacion", nullable = false, length = 100)
    private String operacion;

}

