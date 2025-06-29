package com.utec.repository;

import com.utec.model.Subcomision;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubcomisionRepository extends JpaRepository<Subcomision, Integer> {}
