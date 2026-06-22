package com.unifil.jogoseducativos.dto;

// DTO usado para retornar os dados da partida sem expor a entidade completa
public class PartidaDTO {

    private Long id;
    private String nomeJogador;
    private int pontuacaoJogador;
    private int pontuacaoDealer;
    private String resultado;

    public PartidaDTO(Long id, String nomeJogador, int pontuacaoJogador, int pontuacaoDealer, String resultado) {
        this.id = id;
        this.nomeJogador = nomeJogador;
        this.pontuacaoJogador = pontuacaoJogador;
        this.pontuacaoDealer = pontuacaoDealer;
        this.resultado = resultado;
    }

    public Long getId() { return id; }
    public String getNomeJogador() { return nomeJogador; }
    public int getPontuacaoJogador() { return pontuacaoJogador; }
    public int getPontuacaoDealer() { return pontuacaoDealer; }
    public String getResultado() { return resultado; }
}