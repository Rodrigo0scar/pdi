package com.utec.repository;

import com.utec.model.Socio;
import com.utec.model.SocioPagaCuota;
import com.utec.model.SocioPagaCuotaId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SocioPagaCuotaRepository extends JpaRepository<SocioPagaCuota, SocioPagaCuotaId> {

   Optional<SocioPagaCuota> findById(SocioPagaCuotaId id);


}
