# TODO List ZG

## Backend

Aplicação backend em Java para gerenciamento de tarefas (TODO List), desenvolvida como parte do desafio ZG-Hero da trilha Java do Acelera ZG.

**Autor:** Myke William Silva e Silva

## Tecnologias utilizadas

- Java 21
- Gradle (build tool)
- Sem frameworks (Spring, Micronaut, etc.) — Java puro
- Persistência em arquivo de texto (sem uso de bibliotecas externas)

## Como executar

Pré-requisitos: Java 21 instalado.

No terminal, na raiz do projeto:

```bash
./gradlew run --console=plain
```

O parâmetro `--console=plain` é necessário para que o menu interativo consiga ler corretamente as entradas digitadas pelo usuário.

## Funcionalidades

- Criar, listar e deletar tarefas (CRD)
- Atualizar o status de uma tarefa (opcional)
- Listagem com filtros por categoria, prioridade e status
- Estatísticas de tarefas por status (opcional)
- Ordenação automática por prioridade (prioridade 1 = mais urgente)
- Validação de datas (não aceita data retroativa nem superior a 100 anos no futuro)
- Persistência dos dados em arquivo de texto, localizado em `~/todo-list-zg/tarefas.txt`

## Arquitetura

O projeto é organizado em camadas, separadas por responsabilidade:
- **model/** — `Tarefa`, `Status` (dados e regras próprias do domínio)
- **repository/** — interface `TarefasRepository` + implementação em arquivo de texto
- **service/** — `TarefasService` (regras de negócio: ordenação, validações, estatísticas)
- **ui/** — `MenuTerminal` (interface com o usuário via terminal)

Essa separação existe para que, futuramente, seja possível conectar um frontend (via API REST, por exemplo) reaproveitando toda a camada de `service` sem necessidade de reescrever a lógica de negócio.

## Formato de persistência

Os dados são salvos em texto simples, um registro por linha, com campos separados por `|`:
```
id|nome|descricao|dataTermino|prioridade|categoria|status
```

## Frontend

O frontend está na pasta `Frontend/`, desenvolvido em HTML, CSS e JavaScript puro (sem frameworks ou bibliotecas), utilizando módulos ES (`import`/`export`).

### Como executar

Como o projeto usa módulos JavaScript (`type="module"`), é necessário servir os arquivos por um servidor local — abrir o `index.html` diretamente no navegador (`file://`) não funciona, devido a restrições de segurança (CORS) dos navegadores para módulos.

Uma forma simples de servir os arquivos, usando Python (geralmente já disponível no sistema):

**Linux/macOS:**
```bash
cd Frontend
python3 -m http.server 8000
```

**Windows:**
```bash
cd Frontend
python -m http.server 8000
```

Depois, acesse no navegador:
```
http://localhost:8000
```


### Funcionalidades

- Criar, listar, editar e excluir tarefas (CRUD completo)
- Filtro de listagem por status (TODO, DOING, DONE)
- Validação de data de término (não aceita data anterior à atual)
- Dados mantidos em memória (sem persistência), conforme escopo desta etapa do desafio

### Arquitetura

- **model/** — classe `Tarefa` e objeto `Status` (simula um enum, já que JavaScript não possui esse tipo nativamente)
- **repository/** — array em memória + operações de armazenamento (criar, buscar, listar, deletar)
- **service/** — regras de negócio (ordenação por prioridade, filtros, validações)
- **ui/** — manipulação do DOM, formulário e eventos