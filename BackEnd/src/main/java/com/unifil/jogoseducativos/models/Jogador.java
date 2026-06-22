package com.unifil.jogoseducativos.models;

import java.util.ArrayList;
import java.util.List;

public class Jogador extends Pessoa {

    private List<Carta> mao;
    private int pontuacao;

    public Jogador() {
        this.mao = new ArrayList<>();
        this.pontuacao = 0;
    }

    public Jogador(String nome) {
        super(nome);
        this.mao = new ArrayList<>();
        this.pontuacao = 0;
    }

    public List<Carta> getMao() { return mao; }
    public void setMao(List<Carta> mao) { this.mao = mao; }

    public int getPontuacao() { return pontuacao; }
    public void setPontuacao(int pontuacao) { this.pontuacao = pontuacao; }
}
