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

    public long contarPorStatus(Status status) {
        return repository.listarTodas().stream()
                .filter(t -> t.getStatus() == status)
                .count();
    }

    public void exibirEstatisticas() {
        long total = repository.listarTodas().size();
        long todo = contarPorStatus(Status.TODO);
        long doing = contarPorStatus(Status.DOING);
        long done = contarPorStatus(Status.DONE);

        System.out.println("\nESTATÍSTICAS DAS TAREFAS");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━");
        System.out.println("Total de tarefas: " + total);
        System.out.println("A fazer (TODO): " + todo);
        System.out.println("Em andamento (DOING): " + doing);
        System.out.println("Concluídas (DONE): " + done);
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━");
    }

    public Tarefa atualizarStatus(Long id, Status novoStatus) {
        Optional<Tarefa> tarefaOpt = repository.buscarPorId(id);
        if (tarefaOpt.isEmpty()) {
            throw new RuntimeException("Tarefa com ID " + id + " não encontrada");
        }

        Tarefa tarefa = tarefaOpt.get();
        tarefa.setStatus(novoStatus);

        return repository.salvar(tarefa);
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