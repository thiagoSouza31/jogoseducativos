package com.unifil.jogoseducativos.models;

public class Carta {

    private String naipe;
    private String simbolo;
    private int valor;

    public Carta() {}

    public Carta(String naipe, String simbolo, int valor) {
        this.naipe = naipe;
        this.simbolo = simbolo;
        this.valor = valor;
    }

    public String getNaipe() { return naipe; }
    public void setNaipe(String naipe) { this.naipe = naipe; }

    public String getSimbolo() { return simbolo; }
    public void setSimbolo(String simbolo) { this.simbolo = simbolo; }

    public int getValor() { return valor; }
    public void setValor(int valor) { this.valor = valor; }
}
