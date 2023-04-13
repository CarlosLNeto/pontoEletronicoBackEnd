package com.api.pontoeletronico.controllers;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.api.pontoeletronico.dtos.PontoDto;
import com.api.pontoeletronico.models.PontoModel;
import com.api.pontoeletronico.models.UserModel;
import com.api.pontoeletronico.repositories.PontoRepository;
import com.api.pontoeletronico.services.PontoService;
import com.api.pontoeletronico.services.UserService;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/pontos")
public class PontoController {
    // injeção do serviço de pontos e do serviço de usuários

    final PontoService PontoService;

    public PontoController(PontoService PontoService) {
        this.PontoService = PontoService;
    }

    @Autowired 
    private PontoRepository pontoRepository;

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<?> registrarPonto(@RequestBody PontoDto pontoDto) {
        UUID teste = pontoDto.getUserId();
        UserModel usuario = userService.findById(teste)
                                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
            List<PontoModel> pontosDoDia = pontoRepository.findByUserAndData(usuario, LocalDate.now());
            if (pontosDoDia.size() < 6) {
                PontoModel ponto = new PontoModel();
                ponto.setUser(usuario);
                ponto.setHora(LocalTime.now());
                ponto.setData(LocalDate.now());
                PontoModel pontoSalvo = pontoRepository.save(ponto);
                return ResponseEntity.ok(pontoSalvo);
            } else {
                return ResponseEntity.badRequest().body("Número máximo de pontos cadastrados para o dia atingido");
            }
        }

    @GetMapping
    public ResponseEntity<List<PontoModel>> getAllUsers() {
        return ResponseEntity.status(HttpStatus.OK).body(PontoService.findAll());
    }
        
    @GetMapping("/{userId}")
    public ResponseEntity<List<PontoModel>> getPontosByUserIdAndDate(@PathVariable UUID userId) {

        UserModel usuario = userService.findById(userId).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        List<PontoModel> pontosDoDia = pontoRepository.findByUserAndData(usuario, LocalDate.now());
        return ResponseEntity.ok(pontosDoDia);
    }


    @GetMapping("/consultar")
    public ResponseEntity<List<PontoModel>> consultarDados(
    @RequestParam String login,
    @RequestParam String startDate,
    @RequestParam String endDate
    ) {
    LocalDate dataInicial = LocalDate.parse(startDate);
    LocalDate dataFinal = LocalDate.parse(endDate);

    UserModel usuario = userService.findByLogin(login).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

    List<PontoModel> pontosDoPeriodo = pontoRepository.findByUserAndDataBetween(usuario, dataInicial, dataFinal);

    return ResponseEntity.ok(pontosDoPeriodo);
    }


    }
    
    // outros métodos da camada de controle
