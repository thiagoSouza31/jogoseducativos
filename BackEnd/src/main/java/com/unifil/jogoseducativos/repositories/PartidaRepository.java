package com.unifil.jogoseducativos.repositories;

import com.unifil.jogoseducativos.models.Partida;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PartidaRepository extends JpaRepository<Partida, Long> {
    // O JpaRepository já fornece os métodos básicos:
    // save(), findById(), findAll(), deleteById()
    // Não precisamos escrever nada aqui por enquanto
}