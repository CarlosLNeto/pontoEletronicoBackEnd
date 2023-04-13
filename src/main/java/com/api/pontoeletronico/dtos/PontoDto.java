package com.api.pontoeletronico.dtos;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public class PontoDto {
    
    private UUID id;
    
    private LocalDate data;
    
    private LocalTime hora;

    private UUID userId;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }
    
}
