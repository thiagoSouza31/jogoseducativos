package com.unifil.jogoseducativos.models;

import java.util.ArrayList;
import java.util.List;

public class Dealer {

    private List<Carta> mao;
    private int pontuacao;
    private Carta cartaOculta;

    public Dealer() {
        this.mao = new ArrayList<>();
        this.pontuacao = 0;
    }

    public Dealer(List<Carta> mao, int pontuacao, Carta cartaOculta) {
        this.mao = mao;
        this.pontuacao = pontuacao;
        this.cartaOculta = cartaOculta;
    }

    public List<Carta> getMao() { return mao; }
    public void setMao(List<Carta> mao) { this.mao = mao; }

    public int getPontuacao() { return pontuacao; }
    public void setPontuacao(int pontuacao) { this.pontuacao = pontuacao; }

    public Carta getCartaOculta() { return cartaOculta; }
    public void setCartaOculta(Carta cartaOculta) { this.cartaOculta = cartaOculta; }
}
