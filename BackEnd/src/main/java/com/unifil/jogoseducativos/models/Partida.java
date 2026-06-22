package com.unifil.jogoseducativos.models;

import jakarta.persistence.*;

@Entity
@Table(name = "partidas")
public class Partida {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Nome do jogador
    private String nomeJogador;

    // Pontuação final do jogador e do dealer
    private int pontuacaoJogador;
    private int pontuacaoDealer;

    // Resultado da partida: "JOGADOR", "DEALER" ou "EMPATE"
    private String resultado;

    public Partida() {}

    public Partida(String nomeJogador, int pontuacaoJogador, int pontuacaoDealer, String resultado) {
        this.nomeJogador = nomeJogador;
        this.pontuacaoJogador = pontuacaoJogador;
        this.pontuacaoDealer = pontuacaoDealer;
        this.resultado = resultado;
    }

    public Long getId() { return id; }

    public String getNomeJogador() { return nomeJogador; }
    public void setNomeJogador(String nomeJogador) { this.nomeJogador = nomeJogador; }

    public int getPontuacaoJogador() { return pontuacaoJogador; }
    public void setPontuacaoJogador(int pontuacaoJogador) { this.pontuacaoJogador = pontuacaoJogador; }

    public int getPontuacaoDealer() { return pontuacaoDealer; }
    public void setPontuacaoDealer(int pontuacaoDealer) { this.pontuacaoDealer = pontuacaoDealer; }

    public String getResultado() { return resultado; }
    public void setResultado(String resultado) { this.resultado = resultado; }
}