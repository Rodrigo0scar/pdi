package com.utec.model;

import java.io.Serializable;
import java.util.Objects;

public class UsuarioConcurreActividadId implements Serializable {

    private Integer usuario;  // nombre igual a la propiedad en la entidad
    private Integer actividad;

    public UsuarioConcurreActividadId() {}

    public UsuarioConcurreActividadId(Integer usuario, Integer actividad) {
        this.usuario = usuario;
        this.actividad = actividad;
    }

    // equals y hashCode son obligatorios para que funcione bien JPA

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UsuarioConcurreActividadId)) return false;
        UsuarioConcurreActividadId that = (UsuarioConcurreActividadId) o;
        return Objects.equals(usuario, that.usuario) && Objects.equals(actividad, that.actividad);
    }

    @Override
    public int hashCode() {
        return Objects.hash(usuario, actividad);
    }

    // getters y setters

    public Integer getUsuario() {
        return usuario;
    }

    public void setUsuario(Integer usuario) {
        this.usuario = usuario;
    }

    public Integer getActividad() {
        return actividad;
    }

    public void setActividad(Integer actividad) {
        this.actividad = actividad;
    }
}
