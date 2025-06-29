package com.utec.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity(name = "administrador")
@Table(name = "administrador")
@EqualsAndHashCode(callSuper = true)
@PrimaryKeyJoinColumn(name = "id_usuario")
public class Administrador extends Usuario {
    public Administrador() {
        super();
    }
}
