package org.example.service;

import org.example.model.Status;
import org.example.model.Tarefa;
import org.example.repository.TarefasRepository;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class TarefasService {

    private final TarefasRepository repository;

    public TarefasService(TarefasRepository repository) {
        this.repository = repository;
    }

    public Tarefa criar(String nome, String descricao, LocalDate dataTermino, int prioridade, String categoria) {
        Tarefa tarefa = new Tarefa(nome, descricao, dataTermino, prioridade, categoria);
        return repository.salvar(tarefa);
    }

    public Optional<Tarefa> buscarPorId(Long id) {
        return repository.buscarPorId(id);
    }

    public List<Tarefa> listarTodasOrdenadasPorPrioridade() {
        List<Tarefa> tarefas = repository.listarTodas();
        tarefas.sort(Comparator.comparingInt(Tarefa::getPrioridade));
        return tarefas;
    }

    public List<Tarefa> listarPorCategoria(String categoria) {
        return listarTodasOrdenadasPorPrioridade().stream()
                .filter(t -> t.getCategoria().equalsIgnoreCase(categoria))
                .toList();
    }

    public List<Tarefa> listarPorStatus(Status status) {
        return listarTodasOrdenadasPorPrioridade().stream()
                .filter(t -> t.getStatus() == status)
                .toList();
    }

    public List<Tarefa> listarPorPrioridade(int prioridade) {
        return listarTodasOrdenadasPorPrioridade().stream()
                .filter(t -> t.getPrioridade() == prioridade)
                .toList();
    }

    public void deletar(Long id) {
        Optional<Tarefa>  tarefa = repository.buscarPorId(id);
        if (tarefa.isEmpty()) {
            System.out.println("⚠️ Tentativa de deletar tarefa inexistente - ID: " + id);
            throw new RuntimeException("Tarefa não encontrada");
        }
        System.out.println("✅ Tarefa deletada com sucesso - ID: " + id + ", Título: " + tarefa.get().getNome());
        repository.deletar(id);
    }
}