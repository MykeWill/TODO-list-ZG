package org.example.repository;

import org.example.model.Tarefa;

import java.util.List;
import java.util.Optional;

public interface TarefasRepository {

    Tarefa salvar(Tarefa tarefa);

    Optional<Tarefa> buscarPorId(Long id);

    List<Tarefa> listarTodas();

    void deletar(Long id);
}