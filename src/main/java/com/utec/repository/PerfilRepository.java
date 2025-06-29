package com.utec.repository;

import com.utec.model.Perfil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PerfilRepository extends JpaRepository <Perfil, Integer> {
    Optional<Perfil> findByNombre(String administrador);
}
