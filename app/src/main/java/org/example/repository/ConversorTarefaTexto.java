package org.example.repository;

import org.example.model.Status;
import org.example.model.Tarefa;

import java.time.LocalDate;

public class ConversorTarefaTexto {

    private static final String SEPARADOR = "\\|";

    public String paraLinha(Tarefa tarefa) {
        return tarefa.getId() + "|"
                + tarefa.getNome() + "|"
                + tarefa.getDescricao() + "|"
                + tarefa.getDataTermino() + "|"
                + tarefa.getPrioridade() + "|"
                + tarefa.getCategoria() + "|"
                + tarefa.getStatus();
    }

    public Tarefa paraTarefa(String linha) {
        String[] campos = linha.split(SEPARADOR);

        Long id = Long.parseLong(campos[0]);
        String nome = campos[1];
        String descricao = campos[2];
        LocalDate dataTermino = LocalDate.parse(campos[3]);
        int prioridade = Integer.parseInt(campos[4]);
        String categoria = campos[5];
        Status status = Status.valueOf(campos[6]);

        Tarefa tarefa = new Tarefa(nome, descricao,  dataTermino, prioridade, categoria);
        tarefa.setId(id);
        tarefa.setStatus(status);
        return tarefa;
    }
}