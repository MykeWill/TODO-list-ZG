package org.example.repository;

import org.example.model.Tarefa;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class TarefasArquivoRepository implements TarefasRepository {

    private final Path arquivo;
    private final ConversorTarefaTexto conversor = new ConversorTarefaTexto();
    private final AtomicLong contadorId = new AtomicLong(0);

    public TarefasArquivoRepository(String caminhoArquivo) {
        this.arquivo = Path.of(caminhoArquivo);
        criarArquivoSeNaoExistir();
        inicializarContadorId();
    }

    private void criarArquivoSeNaoExistir() {
        try {
            if (arquivo.getParent() != null) {
                Files.createDirectories(arquivo.getParent());
            }
            if (!Files.exists(arquivo)) {
                Files.createFile(arquivo);
            }
        } catch (IOException e) {
            throw new RuntimeException("Erro ao criar arquivo de tarefas.", e);
        }
    }

    private void inicializarContadorId() {
        long maiorId = listarTodas().stream()
                .mapToLong(Tarefa::getId)
                .max()
                .orElse(0);
        contadorId.set(maiorId);
    }

    @Override
    public Tarefa salvar(Tarefa tarefa) {
        List<Tarefa> tarefas = listarTodas();

        if (tarefa.getId() == null) {
            tarefa.setId(contadorId.incrementAndGet());
            tarefas.add(tarefa);
        } else {
            tarefas.removeIf(t -> t.getId().equals(tarefa.getId()));
            tarefas.add(tarefa);
        }

        escreverTodas(tarefas);
        return tarefa;
    }

    @Override
    public Optional<Tarefa> buscarPorId(Long id) {
        return listarTodas().stream()
                .filter(t -> t.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<Tarefa> listarTodas() {
        try {
            List<String> linhas = Files.readAllLines(arquivo);
            List<Tarefa> tarefas = new ArrayList<>();

            for (String linha : linhas) {
                if (!linha.isBlank()) {
                    tarefas.add(conversor.paraTarefa(linha));
                }
            }
            return tarefas;
        } catch (IOException e) {
            throw new RuntimeException("Erro ao ler arquivo de tarefas.", e);
        }
    }

    @Override
    public void deletar(Long id) {
        List<Tarefa> tarefas = listarTodas();
        tarefas.removeIf(t -> t.getId().equals(id));
        escreverTodas(tarefas);
    }

    private void escreverTodas(List<Tarefa> tarefas) {
        try {
            List<String> linhas = new ArrayList<>();
            for (Tarefa tarefa : tarefas) {
                linhas.add(conversor.paraLinha(tarefa));
            }
            Files.write(arquivo, linhas);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao escrever arquivo de tarefas.", e);
        }
    }
}