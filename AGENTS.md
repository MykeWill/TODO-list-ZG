# Contexto do projeto

## 1. Visão geral

TODO List ZG é um projeto de gerenciamento de tarefas com duas interfaces independentes: uma aplicação de terminal em Java e uma interface web em HTML, CSS e JavaScript.

Funcionalidades existentes:

- **Java:** criar, listar e excluir tarefas; buscar por ID; filtrar por categoria, prioridade ou status; ordenar por prioridade (1 é a mais urgente); consultar estatísticas por status; e atualizar o status de uma tarefa.
- **Java:** persistir tarefas em texto simples no arquivo `~/todo-list-zg/tarefas.txt`, com campos separados por `|`. Os status são `TODO`, `DOING` e `DONE`; a prioridade aceita valores de 1 a 5. A interface de terminal valida datas entre hoje e até 100 anos no futuro.
- **Frontend:** criar, listar, editar e excluir tarefas; filtrar por status; ordenar por prioridade; e validar a data de término para não aceitar uma data anterior à atual. Os dados ficam em memória e não persistem ao recarregar a página.

As duas interfaces têm implementações próprias: o frontend não está conectado ao backend Java.

## 2. Stack e estrutura

- **Java:** Java 21, Gradle 9.7 (Gradle Wrapper), aplicação CLI sem framework web. O Gradle configura JUnit Jupiter para testes e Guava como dependência de implementação.
- **Frontend:** HTML, CSS e JavaScript puro, com módulos ES (`import`/`export`), sem framework ou biblioteca.
- `app/src/main/java/org/example/`
  - `App.java`: inicialização da aplicação e composição das dependências.
  - `model/`: entidades e tipos do domínio (`Tarefa`, `Status`).
  - `repository/`: contrato e persistência em arquivo, incluindo conversão de/para texto.
  - `service/`: operações e regras de negócio de tarefas.
  - `ui/`: menu e interação pelo terminal.
- `app/src/test/`: diretório reservado a testes Java; não há arquivos de teste Java no estado atual.
- `Frontend/`: `index.html`, estilos em `css/style.css` e módulos JavaScript em `js/` (`model.js`, `repository.js`, `service.js`, `ui.js`). O repositório do frontend mantém os dados em memória.
- `gradlew` e `gradlew.bat`: wrappers para executar tarefas Gradle sem depender de uma instalação local do Gradle.

## 3. Como executar e testar

Pré-requisito para o backend: JDK 21.

Na raiz do projeto:

```bash
./gradlew run --console=plain
```

O modo `--console=plain` permite que o menu interativo leia corretamente a entrada do terminal. Para compilar e executar os testes Java:

```bash
./gradlew test
```

O projeto configura a execução de testes com JUnit Platform, mas ainda não contém arquivos de teste Java.

Para abrir o frontend, sirva a pasta `Frontend/` localmente (módulos ES não devem ser abertos diretamente via `file://`):

```bash
cd Frontend
python3 -m http.server 8000
```

Em seguida, acesse `http://localhost:8000`. Não há configuração ou comando de testes automatizados do frontend no projeto atualmente.

## 4. Convenções

- Java usa pacotes em minúsculas, classes em `PascalCase` e métodos/variáveis em `camelCase`; a organização segue camadas de model, repository, service e UI.
- JavaScript usa módulos ES, funções e variáveis em `camelCase`, classes em `PascalCase` e arquivos com nomes em minúsculas.
- Os nomes e os valores de status são `TODO`, `DOING` e `DONE`; preserve esses valores ao alterar código ou dados.
- Mantenha textos e mensagens voltados ao usuário em português, seguindo a linguagem já usada pelas interfaces.
- Preserve o estilo de indentação de cada arquivo e a separação existente entre interface, regras/serviços e acesso a dados.

## 5. Restrições

- Não altere arquivos de `Frontend/` sem autorização explícita para trabalhar no frontend.
- Não mude regras de negócio existentes fora do escopo solicitado, especialmente validações, prioridade, status, filtros, ordenação ou operações disponíveis.
- Não altere o formato, o caminho ou o comportamento da persistência Java sem autorização explícita e avaliação de compatibilidade com os dados existentes.
- Não conecte o frontend ao backend nem introduza frameworks, bibliotecas ou novas dependências sem solicitação.
- Prefira mudanças focadas e preserve o comportamento existente. Se o pedido exigir uma mudança incompatível com essas restrições, peça confirmação antes de implementá-la.
