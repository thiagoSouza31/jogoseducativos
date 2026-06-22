package com.unifil.jogoseducativos.services;

import com.unifil.jogoseducativos.dto.PartidaDTO;
import com.unifil.jogoseducativos.models.*;
import com.unifil.jogoseducativos.repositories.PartidaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class JogoService {

    @Autowired
    private PartidaRepository partidaRepository;

    // Armazena as partidas em andamento na memória (sem banco ainda)
    private Map<String, Object> estadoJogo = new HashMap<>();

    // Inicia uma nova partida: cria baralho, distribui cartas
    public Map<String, Object> iniciarPartida(String nomeJogador) {
        Cartas baralho = Cartas.criarBaralho();
        baralho.embaralhar();

        Jogador jogador = new Jogador(nomeJogador);
        Dealer dealer = new Dealer();

        // Distribui 2 cartas para cada um
        jogador.getMao().add(baralho.comprarCarta());
        jogador.getMao().add(baralho.comprarCarta());
        dealer.getMao().add(baralho.comprarCarta());

        // Segunda carta do dealer fica oculta
        Carta cartaOculta = baralho.comprarCarta();
        dealer.setCartaOculta(cartaOculta);

        // Calcula pontuação inicial do jogador
        jogador.setPontuacao(calcularPontuacao(jogador.getMao()));
        dealer.setPontuacao(calcularPontuacao(dealer.getMao()));

        // Salva o estado atual na memória
        estadoJogo.put("baralho", baralho);
        estadoJogo.put("jogador", jogador);
        estadoJogo.put("dealer", dealer);

        // Monta a resposta para o front-end
        Map<String, Object> resposta = new HashMap<>();
        resposta.put("nomeJogador", jogador.getNome());
        resposta.put("maoJogador", jogador.getMao());
        resposta.put("maoDealer", dealer.getMao());
        resposta.put("pontuacaoJogador", jogador.getPontuacao());
        resposta.put("status", "EM_ANDAMENTO");

        return resposta;
    }

    // Jogador pede uma carta
    public Map<String, Object> pedirCarta() {
        Cartas baralho = (Cartas) estadoJogo.get("baralho");
        Jogador jogador = (Jogador) estadoJogo.get("jogador");

        // Compra carta do baralho e adiciona à mão do jogador
        Carta novaCarta = baralho.comprarCarta();
        jogador.getMao().add(novaCarta);

        int pontuacao = calcularPontuacao(jogador.getMao());
        jogador.setPontuacao(pontuacao);

        Map<String, Object> resposta = new HashMap<>();
        resposta.put("novaCarta", novaCarta);
        resposta.put("maoJogador", jogador.getMao());
        resposta.put("pontuacaoJogador", pontuacao);

        // Se passou de 21, jogador perde
        if (pontuacao > 21) {
            resposta.put("status", "DERROTA");
            salvarPartida(jogador);
        } else {
            resposta.put("status", "EM_ANDAMENTO");
        }

        return resposta;
    }

    // Jogador para: dealer joga e resultado é definido
    public Map<String, Object> parar() {
        Cartas baralho = (Cartas) estadoJogo.get("baralho");
        Jogador jogador = (Jogador) estadoJogo.get("jogador");
        Dealer dealer = (Dealer) estadoJogo.get("dealer");

        // Revela a carta oculta do dealer
        dealer.getMao().add(dealer.getCartaOculta());

        // Dealer compra cartas até ter 17 ou mais
        while (calcularPontuacao(dealer.getMao()) < 17) {
            dealer.getMao().add(baralho.comprarCarta());
        }

        int pontJogador = calcularPontuacao(jogador.getMao());
        int pontDealer = calcularPontuacao(dealer.getMao());

        dealer.setPontuacao(pontDealer);

        // Define o resultado
        String resultado;
        if (pontDealer > 21 || pontJogador > pontDealer) {
            resultado = "JOGADOR";
        } else if (pontDealer > pontJogador) {
            resultado = "DEALER";
        } else {
            resultado = "EMPATE";
        }

        // Salva a partida no banco
        Partida partida = new Partida(jogador.getNome(), pontJogador, pontDealer, resultado);
        partidaRepository.save(partida);

        Map<String, Object> resposta = new HashMap<>();
        resposta.put("maoDealer", dealer.getMao());
        resposta.put("pontuacaoJogador", pontJogador);
        resposta.put("pontuacaoDealer", pontDealer);
        resposta.put("resultado", resultado);
        resposta.put("status", "FINALIZADO");

        return resposta;
    }

    // Busca o histórico de todas as partidas salvas no banco
    // Busca o histórico e converte cada Partida em PartidaDTO
    public List<PartidaDTO> historico() {
        return partidaRepository.findAll().stream()
                .map(p -> new PartidaDTO(p.getId(), p.getNomeJogador(), p.getPontuacaoJogador(), p.getPontuacaoDealer(), p.getResultado()))
                .collect(Collectors.toList());
    }

    // Calcula pontuação tratando o Ás como 1 se necessário
    private int calcularPontuacao(List<Carta> mao) {
        int total = mao.stream().mapToInt(Carta::getValor).sum();
        long ases = mao.stream().filter(c -> c.getSimbolo().equals("A")).count();

        while (total > 21 && ases > 0) {
            total -= 10;
            ases--;
        }

        return total;
    }

    // Salva partida com derrota por bust (passou de 21)
    private void salvarPartida(Jogador jogador) {
        Dealer dealer = (Dealer) estadoJogo.get("dealer");
        int pontDealer = calcularPontuacao(dealer.getMao());
        Partida partida = new Partida(jogador.getNome(), jogador.getPontuacao(), pontDealer, "DEALER");
        partidaRepository.save(partida);
    }
}