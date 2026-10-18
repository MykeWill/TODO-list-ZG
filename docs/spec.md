# Suíte de testes unitários com Spock

## Contexto e objetivo

O projeto já possui uma aplicação Java para gerenciamento de tarefas, mas ainda não possui testes automatizados para as regras do domínio e do serviço. Esta etapa adiciona uma suíte unitária em Spock para Java 21, Groovy e Gradle, mantendo JUnit Jupiter disponível no projeto.

O foco é especificar e verificar o comportamento existente de `TarefasService` e as regras relevantes de `Tarefa` e `Status`. Os testes do serviço devem isolar o repositório com mocks ou stubs Spock, sem depender de arquivos reais, do terminal interativo ou de estado compartilhado entre testes.

Os testes devem cobrir todos os métodos públicos de `TarefasService`: `criar`, `buscarPorId`, `listarTodasOrdenadasPorPrioridade`, `listarPorCategoria`, `listarPorStatus`, `listarPorPrioridade`, `contarPorStatus`, `exibirEstatisticas`, `atualizarStatus` e `deletar`.

## Regras de negócio

- Uma tarefa nova começa com status `TODO`.
- Prioridades de 1 a 5 são válidas; valores menores que 1 ou maiores que 5 devem ser rejeitados com `IllegalArgumentException`, tanto na criação como na alteração da prioridade da tarefa.
- A listagem geral é ordenada por prioridade crescente (1 é a mais urgente). Os filtros por categoria, status e prioridade também retornam resultados nessa ordem.
- A comparação de categorias não diferencia maiúsculas de minúsculas.
- A busca por ID retorna o `Optional` informado pelo repositório.
- A contagem por status retorna o número de tarefas naquele status; as estatísticas exibem total e contagens de `TODO`, `DOING` e `DONE`.
- A atualização de status persiste a tarefa alterada e retorna essa tarefa. Para ID inexistente, lança `RuntimeException` e não salva.
- A exclusão de uma tarefa existente delega a remoção ao repositório. Para ID inexistente, lança `RuntimeException` e não solicita remoção.
- **Categorias nulas:** o modelo pode receber uma categoria nula, mas isso não pode fazer o filtro lançar `NullPointerException`. Categorias nulas não correspondem a uma categoria de busca; uma busca com categoria nula também não corresponde a tarefas e retorna lista vazia.
- **Status nulo:** uma tarefa não pode ficar com status nulo. O setter de status deve rejeitá-lo com `IllegalArgumentException`, sem substituir o status válido atual. Consultas e contagens por status nulo retornam, respectivamente, lista vazia e zero; tentar atualizar uma tarefa para status nulo deve lançar `IllegalArgumentException` e não persistir a alteração.
- **Ordenação sem efeito colateral:** a ordenação e os filtros não devem reordenar ou modificar a lista pertencente ao repositório. O serviço deve ordenar uma cópia dos resultados.
- Os cenários de borda devem também verificar repositórios vazios, resultados de filtro vazios e ausência de interações de persistência quando uma operação falha na validação.

## Critérios de aceite

Os cenários abaixo usam `Given / When / Then`, em inglês, para manter os critérios diretamente utilizáveis como referência para especificações Spock.

### Criação e modelo

**Scenario: Create a task with valid data**

- Given a repository that assigns and returns a task when saved
- When the service creates a task with priority 3
- Then it returns the saved task with the supplied fields and initial status `TODO`
- And it saves exactly one task

**Scenario Outline: Accept priority boundaries**

- Given a task with priority `<priority>`
- When the task is created
- Then the task has priority `<priority>`

Examples:

| priority |
| 1        |
| 5        |

**Scenario Outline: Reject priorities outside the allowed range**

- Given a priority of `<priority>`
- When a task is created or its priority is changed to that value
- Then `IllegalArgumentException` is thrown

Examples:

| priority |
| 0        |
| 6        |

**Scenario: Reject a null status without changing the current status**

- Given a task with status `TODO`
- When its status is set to null
- Then `IllegalArgumentException` is thrown
- And the task status remains `TODO`

### Busca e listagens

**Scenario: Find a task by ID**

- Given the repository returns a task for the requested ID
- When the service searches for that ID
- Then it returns the repository's `Optional` result

**Scenario: Return an empty result when an ID is not found**

- Given the repository has no task for the requested ID
- When the service searches for that ID
- Then it returns an empty `Optional`

**Scenario: List tasks by ascending priority**

- Given the repository returns tasks in unsorted priority order
- When the service lists all tasks
- Then the result is ordered by ascending priority
- And the repository's original list order remains unchanged

**Scenario: List no tasks when the repository is empty**

- Given the repository returns an empty list
- When the service lists all tasks
- Then it returns an empty list

**Scenario: Filter categories without case sensitivity**

- Given tasks whose categories include `Work` and `Personal`
- When the service filters by category `wOrK`
- Then it returns only the `Work` task
- And the result is ordered by ascending priority

**Scenario: Do not fail when a task has a null category**

- Given the repository contains a task with a null category
- When the service filters by a non-null category
- Then it does not throw an exception
- And it does not include the task with the null category

**Scenario: Return no category matches for a null category query**

- Given the repository contains tasks with null and non-null categories
- When the service filters using a null category
- Then it returns an empty list without throwing an exception

**Scenario: Return an empty category result when there are no matches**

- Given the repository contains tasks but none in the requested category
- When the service filters by that category
- Then it returns an empty list

**Scenario: Filter tasks by each supported status**

- Given the repository contains tasks with statuses `TODO`, `DOING` and `DONE`
- When the service filters by each supported status
- Then each result contains only tasks with the requested status
- And each result is ordered by ascending priority

**Scenario: Return no tasks when filtering by a null status**

- Given the repository contains tasks with valid statuses
- When the service filters by a null status
- Then it returns an empty list

**Scenario: Return an empty status result when there are no matches**

- Given the repository contains no task with the requested status
- When the service filters by that status
- Then it returns an empty list

**Scenario: Filter tasks by priority**

- Given the repository contains tasks with different priorities
- When the service filters by priority 2
- Then it returns only tasks with priority 2
- And the result is ordered by ascending priority

**Scenario: Return an empty priority result when there are no matches**

- Given the repository contains no task with the requested priority
- When the service filters by that priority
- Then it returns an empty list

### Contagem e estatísticas

**Scenario: Count tasks for a status**

- Given the repository contains two `TODO` tasks, one `DOING` task and one `DONE` task
- When the service counts tasks by status
- Then the counts are 2 for `TODO`, 1 for `DOING` and 1 for `DONE`

**Scenario: Return zero when counting a null status**

- Given the repository contains tasks with valid statuses
- When the service counts tasks for a null status
- Then it returns zero

**Scenario: Count no tasks in an empty repository**

- Given the repository is empty
- When the service counts tasks for any supported status
- Then it returns zero

**Scenario: Display statistics for an empty repository**

- Given the repository is empty
- When the service displays statistics
- Then the output reports total 0 and 0 for `TODO`, `DOING` and `DONE`

**Scenario: Display statistics for tasks in all statuses**

- Given the repository contains two `TODO` tasks, one `DOING` task and one `DONE` task
- When the service displays statistics
- Then the output reports total 4, `TODO` 2, `DOING` 1 and `DONE` 1

### Atualização de status

**Scenario Outline: Update a task to a supported status**

- Given the repository contains a task with ID 10 and status `<oldStatus>`
- When the service updates task 10 to `<newStatus>`
- Then it returns the task with status `<newStatus>`
- And it saves the updated task exactly once

Examples:

| oldStatus | newStatus |
| TODO      | DOING     |
| DOING     | DONE      |
| DONE      | TODO      |

**Scenario: Reject an update to a null status**

- Given the repository contains a task with ID 10 and a valid status
- When the service updates task 10 to a null status
- Then `IllegalArgumentException` is thrown
- And the repository does not save the task
- And the task retains its original status

**Scenario: Fail to update a task that does not exist**

- Given the repository has no task with ID 99
- When the service updates task 99
- Then `RuntimeException` is thrown
- And the repository does not save a task

### Exclusão

**Scenario: Delete an existing task**

- Given the repository contains a task with ID 10
- When the service deletes task 10
- Then the repository is asked to delete ID 10 exactly once

**Scenario: Fail to delete a task that does not exist**

- Given the repository has no task with ID 99
- When the service deletes task 99
- Then `RuntimeException` is thrown
- And the repository is not asked to delete any task

## Impacto no que já existe

- A implementação de produção e as regras observáveis existentes devem permanecer inalteradas nesta etapa; o escopo é adicionar a especificação da suíte e, em uma etapa posterior, os testes/configuração necessários para Spock.
- A configuração de testes deve adicionar Spock/Groovy sem remover JUnit Jupiter nem quebrar os testes JUnit que possam ser adicionados.
- Os testes devem executar pelo Gradle Wrapper, junto com a tarefa `test` já configurada.
- Os cenários que expressam o comportamento esperado para valores nulos e imutabilidade da lista podem expor falhas na implementação atual. Essas falhas devem ser tratadas como correções necessárias ao implementar a suíte, sem enfraquecer os critérios para fazer os testes passarem.
- Não há arquivos de teste Java existentes no projeto no momento da especificação.

## Fora do escopo

- Adicionar novas funcionalidades de gerenciamento de tarefas.
- Alterar regras de negócio além dos ajustes necessários para satisfazer os critérios de aceite acordados.
- Testes de integração da persistência em arquivo, interface de terminal, fluxo completo da aplicação ou frontend.
- Alterações em qualquer arquivo de `Frontend/`.
- Conectar o frontend ao backend, alterar formato/caminho dos dados persistidos ou introduzir frameworks de aplicação.
- Testes de desempenho, carga, interface visual ou cobertura de código como meta numérica.
