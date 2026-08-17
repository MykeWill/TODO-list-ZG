package org.example.ui;

import org.example.model.Status;
import org.example.model.Tarefa;
import org.example.service.TarefasService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class MenuTerminal {

    private final TarefasService service;
    private final Scanner scanner = new Scanner(System.in);
    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public MenuTerminal(TarefasService service) {
        this.service = service;
    }

    public void iniciar() {
        boolean continuar = true;

        while (continuar) {
            exibirMenu();
            int opcao = lerOpcao();

            switch (opcao) {
                case 1 -> criarTarefa();
                case 2 -> listarTodas();
                case 3 -> listarPorCategoria();
                case 4 -> listarPorPrioridade();
                case 5 -> listarPorStatus();
                case 6 -> exibirEstatisticas();
                case 7 -> editarStatusTarefa();
                case 8 -> deletarTarefa();
                case 0 -> continuar = false;
                default -> System.out.println("Opção inválida.");
            }
        }

        System.out.println("Encerrando o programa...");
    }

    private void exibirMenu() {
        System.out.println();
        System.out.println("===== TODO LIST =====");
        System.out.println("1. Criar tarefa");
        System.out.println("2. Listar todas as tarefas");
        System.out.println("3. Listar por categoria");
        System.out.println("4. Listar por prioridade");
        System.out.println("5. Listar por status");
        System.out.println("6. Estatísticas");
        System.out.println("7. Editar status da tarefa");
        System.out.println("8. Deletar tarefa");

        System.out.println("0. Sair");
        System.out.print("Escolha uma opção: ");
    }

    private int lerOpcao() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private void criarTarefa() {
        System.out.print("Nome: ");
        String nome = scanner.nextLine();

        System.out.print("Descrição: ");
        String descricao = scanner.nextLine();

        LocalDate dataTermino = lerData();

        int prioridade = lerPrioridade();

        System.out.print("Categoria: ");
        String categoria = scanner.nextLine();

        try {Tarefa tarefa = service.criar(nome, descricao, dataTermino, prioridade, categoria);
            System.out.println("Tarefa criada com sucesso! ID: " + tarefa.getId());
        } catch (IllegalArgumentException e) {
            System.out.println("Erro ao criar tarefa: " + e.getMessage());
        }
    }

    private LocalDate lerData() {
        while (true) {
            System.out.print("Data de término (dd/MM/yyyy): ");
            String entrada = scanner.nextLine();
            try {
                LocalDate data = LocalDate.parse(entrada, FORMATO_DATA);
                LocalDate hoje = LocalDate.now();

                if (data.isBefore(hoje)) {
                    System.out.println("Data não pode ser anterior a hoje!");
                    continue;
                }

                if (data.isAfter(hoje.plusYears(100))) {
                    System.out.println("Data não pode ser mais de 100 anos no futuro!");
                    continue;
                }

                return data;

            } catch (DateTimeParseException e) {
                System.out.println("Data inválida. Use o formato dd/MM/yyyy.");
            }
        }
    }

    private int lerPrioridade() {
        while (true) {
            System.out.print("Prioridade (1 a 5): ");
            try {
                int valor = Integer.parseInt(scanner.nextLine().trim());
                if (valor >= 1 && valor <= 5) {
                    return valor;
                }
                System.out.println("Prioridade deve estar entre 1 e 5.");
            } catch (NumberFormatException e) {
                System.out.println("Digite um número válido.");
            }
        }
    }

    private void listarTodas() {
        exibirLista(service.listarTodasOrdenadasPorPrioridade());
    }

    private void listarPorCategoria() {
        System.out.print("Categoria: ");
        String categoria = scanner.nextLine();
        exibirLista(service.listarPorCategoria(categoria));
    }

    private void listarPorPrioridade() {
        int prioridade = lerPrioridade();
        exibirLista(service.listarPorPrioridade(prioridade));
    }

    private void listarPorStatus() {
        System.out.print("Status (TODO, DOING, DONE): ");
        String entrada = scanner.nextLine().trim().toUpperCase();
        try {
            Status status = Status.valueOf(entrada);
            exibirLista(service.listarPorStatus(status));
        } catch (IllegalArgumentException e) {
            System.out.println("Status inválido. Use TODO, DOING ou DONE.");
        }
    }

    private void exibirLista(List<Tarefa> tarefas) {
        if (tarefas.isEmpty()) {
            System.out.println("Nenhuma tarefa encontrada.");
            return;
        }

        System.out.println();
        for (Tarefa tarefa : tarefas) {
            System.out.printf("ID: %d | %s | Prioridade: %d | Categoria: %s | Status: %s | Prazo: %s%n",
                    tarefa.getId(),
                    tarefa.getNome(),
                    tarefa.getPrioridade(),
                    tarefa.getCategoria(),
                    tarefa.getStatus(),
                    tarefa.getDataTermino().format(FORMATO_DATA)
            );
        }
    }


    private void exibirEstatisticas() {
        service.exibirEstatisticas();
    }

    private void editarStatusTarefa() {
        try {
            System.out.println("\n📋 Tarefas disponíveis:");
            List<Tarefa> todas = service.listarTodasOrdenadasPorPrioridade();
            if (todas.isEmpty()) {
                System.out.println("❌ Nenhuma tarefa cadastrada.");
                return;
            }

            for (Tarefa t : todas) {
                System.out.printf("ID: %d | %s | Status: %s%n",
                        t.getId(), t.getNome(), t.getStatus());
            }

            System.out.print("\nID da tarefa a editar: ");
            Long id = Long.parseLong(scanner.nextLine().trim());


            var tarefaOpt = service.buscarPorId(id);
            if (tarefaOpt.isEmpty()) {
                System.out.println("Tarefa não encontrada.");
                return;
            }

            Tarefa tarefa = tarefaOpt.get();
            System.out.println("\nTarefa selecionada:");
            System.out.printf("ID: %d | Nome: %s | Status atual: %s%n",
                    tarefa.getId(), tarefa.getNome(), tarefa.getStatus());


            System.out.print("\nNovo status (TODO, DOING, DONE): ");
            String entrada = scanner.nextLine().trim().toUpperCase();

            try {
                Status novoStatus = Status.valueOf(entrada);
                service.atualizarStatus(id, novoStatus);
                System.out.println("Status atualizado com sucesso!");
            } catch (IllegalArgumentException e) {
                System.out.println("Status inválido. Use TODO, DOING ou DONE.");
            }

        } catch (NumberFormatException e) {
            System.out.println("ID inválido. Digite um número.");
        } catch (RuntimeException e) {
            System.out.println(" " + e.getMessage());
        }
    }

    private void deletarTarefa() {
        try {
            System.out.println("\n📋 Tarefas disponíveis para deletar:");
            List<Tarefa> todas = service.listarTodasOrdenadasPorPrioridade();
            if (todas.isEmpty()) {
                System.out.println("❌ Nenhuma tarefa cadastrada.");
                return;
            }

            for (Tarefa t : todas) {
                System.out.printf("   ID: %d - %s%n", t.getId(), t.getNome());
            }

            System.out.print("\nID da tarefa a deletar: ");
            Long id = Long.parseLong(scanner.nextLine().trim());

            var tarefaOptional = service.buscarPorId(id);
            if (tarefaOptional.isPresent()) {
                Tarefa tarefa = tarefaOptional.get();
                System.out.println("📝 Você está deletando: " + tarefa.getNome());
                System.out.print("Confirma? (s/n): ");
                String confirmacao = scanner.nextLine().trim().toLowerCase();

                if (confirmacao.equals("s") || confirmacao.equals("sim")) {
                    service.deletar(id);
                    System.out.println("✅ Tarefa deletada com sucesso!");
                } else {
                    System.out.println("❌ Operação cancelada.");
                }
            }

        } catch (NumberFormatException e) {
            System.out.println("❌ ID inválido. Digite um número.");

        } catch (RuntimeException e) {
            System.out.println("❌ " + e.getMessage());
            System.out.println("💡 Dica: Use '2' para listar todas e ver os IDs.");
        }
    }
}