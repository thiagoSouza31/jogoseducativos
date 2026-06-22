package com.unifil.jogoseducativos.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Cartas {

    private final List<Carta> cartas;

    private static final String[] NAIPES = {"Espadas", "Copas", "Ouros", "Paus"};
    private static final String[] SIMBOLOS = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};
    private static final int[] VALORES = {11, 2, 3, 4, 5, 6, 7, 8, 9, 10, 10, 10, 10};

    public Cartas() {
        this.cartas = new ArrayList<>();
    }

    public Cartas(List<Carta> cartas) {
        this.cartas = cartas;
    }

    public static Cartas criarBaralho() {
        List<Carta> baralho = new ArrayList<>();
        for (String naipe : NAIPES) {
            for (int i = 0; i < SIMBOLOS.length; i++) {
                baralho.add(new Carta(naipe, SIMBOLOS[i], VALORES[i]));
            }
        }
        return new Cartas(baralho);
    }

    public void embaralhar() {
        Collections.shuffle(this.cartas);
    }

    public Carta comprarCarta() {
        if (cartas.isEmpty()) {
            throw new IllegalStateException("Baralho vazio");
        }
        return cartas.remove(cartas.size() - 1);
    }

    public int tamanho() {
        return cartas.size();
    }

    public List<Carta> getCartas() { return cartas; }
}
