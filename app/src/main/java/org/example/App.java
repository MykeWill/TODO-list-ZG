package org.example;

import org.example.repository.TarefasRepository;
import org.example.repository.TarefasArquivoRepository;
import org.example.service.TarefasService;
import org.example.ui.MenuTerminal;

public class App {

    public static void main(String[] args) {
        String pastaUsuario = System.getProperty("user.home");
        String caminhoArquivo = pastaUsuario + "/todo-list-zg/tarefas.txt";
        TarefasRepository repository = new TarefasArquivoRepository(caminhoArquivo);

        TarefasService service = new TarefasService(repository);

        MenuTerminal menu = new MenuTerminal(service);

        menu.iniciar();
    }
}