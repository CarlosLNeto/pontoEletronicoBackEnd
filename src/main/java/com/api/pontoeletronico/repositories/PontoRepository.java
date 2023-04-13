package com.api.pontoeletronico.repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import com.api.pontoeletronico.models.PontoModel;
import com.api.pontoeletronico.models.UserModel;

@Repository
public interface PontoRepository extends JpaRepository<PontoModel, UUID>{
    List<PontoModel> findByUserAndData(UserModel user, LocalDate data);
    List<PontoModel> findByUserAndDataBetween(UserModel user, LocalDate dataInicial, LocalDate dataFinal);
}
