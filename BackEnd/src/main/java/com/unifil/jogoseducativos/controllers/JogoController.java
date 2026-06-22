package com.unifil.jogoseducativos.controllers;

import com.unifil.jogoseducativos.dto.PartidaDTO;
import com.unifil.jogoseducativos.services.JogoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/jogo")
@CrossOrigin(origins = "*")
public class JogoController {

    @Autowired
    private JogoService jogoService;

    // POST /jogo/iniciar?nomeJogador=Thiago
    // Inicia uma nova partida e distribui as cartas
    @PostMapping("/iniciar")
    public ResponseEntity<Map<String, Object>> iniciar(@RequestParam String nomeJogador) {
        Map<String, Object> resposta = jogoService.iniciarPartida(nomeJogador);
        return ResponseEntity.ok(resposta);
    }

    // POST /jogo/pedir
    // Jogador pede mais uma carta
    @PostMapping("/pedir")
    public ResponseEntity<Map<String, Object>> pedir() {
        Map<String, Object> resposta = jogoService.pedirCarta();
        return ResponseEntity.ok(resposta);
    }

    // POST /jogo/parar
    // Jogador para: dealer joga e resultado é definido
    @PostMapping("/parar")
    public ResponseEntity<Map<String, Object>> parar() {
        Map<String, Object> resposta = jogoService.parar();
        return ResponseEntity.ok(resposta);
    }

    // GET /jogo/historico
    // Retorna todas as partidas salvas no banco
    @GetMapping("/historico")
    public ResponseEntity<List<PartidaDTO>> historico() {
        List<PartidaDTO> partidas = jogoService.historico();
        return ResponseEntity.ok(partidas);
    }
}