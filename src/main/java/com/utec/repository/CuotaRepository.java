package com.utec.repository;

import com.utec.model.Cuota;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CuotaRepository extends JpaRepository<Cuota,Integer> {

    //busca por mes
    List<Cuota> findByMes(Integer mes);

    //obtiene todas las cuotas
    List<Cuota> findAll();


}
