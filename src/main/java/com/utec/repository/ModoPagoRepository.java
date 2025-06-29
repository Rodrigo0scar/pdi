package com.utec.repository;

import com.utec.model.ModoPago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModoPagoRepository extends JpaRepository<ModoPago, Integer> {

    //obtiene todos los modos de pago
    List<ModoPago> findAll();
}
