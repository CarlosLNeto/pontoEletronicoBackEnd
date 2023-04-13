package com.api.pontoeletronico.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import com.api.pontoeletronico.models.PontoModel;
import com.api.pontoeletronico.repositories.PontoRepository;

@Service
public class PontoService {
    // injeção do repositório de pontos e do serviço de usuários
    @Autowired
    private PontoRepository pontoRepository;

    public List<PontoModel> findAll() {
        return pontoRepository.findAll();
    }
    
    public Optional<PontoModel> findById(UUID id) {
        return pontoRepository.findById(id);
    }

    
    // outros métodos da camada de serviço
}