# Plano de implementação — testes unitários com Spock

O plano segue a [spec.md](./spec.md). As tarefas estão ordenadas por dependência: primeiro habilitar Spock no Gradle e depois implementar os testes e correções de comportamento. Cada correção de bug fica isolada dos testes normais e das demais correções.

## 1. Configurar Spock no build Gradle

- **O que será feito:** habilitar Groovy para testes, adicionar Spock e configurar a execução com JUnit Platform, mantendo JUnit Jupiter e suas dependências disponíveis.
- **Arquivos criados ou alterados:** `app/build.gradle`; `gradle/libs.versions.toml` se as dependências forem registradas no catálogo de versões existente.
- **Como validar:** executar `./gradlew :app:test` e confirmar que o build configura e executa a tarefa de testes sem remover o suporte JUnit existente.

## 2. Especificar as regras do modelo `Tarefa` — Concluída

- **O que será feito:** criar testes Spock para status inicial `TODO`, prioridades válidas 1 e 5, rejeição de prioridades 0 e 6 na criação e no setter de prioridade. Não incluir aqui o caso de status nulo, que pertence à tarefa 6.
- **Arquivos criados ou alterados:** `app/src/test/groovy/org/example/model/TarefaSpec.groovy`.
- **Como validar:** executar `./gradlew :app:test --tests org.example.model.TarefaSpec`.

## 3. Especificar criação, busca, ordenação de resultados e filtros — Concluída

- **O que será feito:** criar testes unitários para `criar`, `buscarPorId`, `listarTodasOrdenadasPorPrioridade`, `listarPorCategoria`, `listarPorStatus` e `listarPorPrioridade`. Cobrir busca e repositório vazios, resultados sem correspondência, comparação de categoria sem diferenciar maiúsculas, os três status e ordenação dos resultados. Não testar ainda a preservação da lista original do repositório nem os casos de categoria nula, que pertencem às tarefas 5 e 7.
- **Arquivos criados ou alterados:** `app/src/test/groovy/org/example/service/TarefasServiceSpec.groovy`.
- **Como validar:** executar `./gradlew :app:test --tests org.example.service.TarefasServiceSpec`.

## 4. Especificar contagem, estatísticas, atualização e exclusão

- **O que será feito:** completar os testes normais para `contarPorStatus`, `exibirEstatisticas`, `atualizarStatus` e `deletar`, incluindo estatísticas e contagens com repositório vazio, atualização para cada status suportado, busca de IDs inexistentes e interações esperadas com o repositório. Não incluir status nulo, tratado na tarefa 6.
- **Arquivos criados ou alterados:** `app/src/test/groovy/org/example/service/TarefasServiceSpec.groovy`.
- **Como validar:** executar `./gradlew :app:test --tests org.example.service.TarefasServiceSpec` e conferir as interações com repositório e a saída capturada das estatísticas.

## 5. Corrigir e testar o filtro por categoria nula

- **O que será feito:** adicionar testes para tarefas com categoria nula e para uma consulta com categoria nula; assegurar que o filtro não lança `NullPointerException` e que esses casos retornam lista vazia. Corrigir somente a comparação do filtro necessária para cumprir esse contrato.
- **Arquivos criados ou alterados:** `app/src/test/groovy/org/example/service/TarefasServiceSpec.groovy`; `app/src/main/java/org/example/service/TarefasService.java`.
- **Como validar:** executar `./gradlew :app:test --tests org.example.service.TarefasServiceSpec` e confirmar que ambos os cenários nulos passam, sem alterar os cenários normais de categoria.

## 6. Corrigir e testar a rejeição de status nulo

- **O que será feito:** testar que `Tarefa` rejeita status nulo sem perder o status válido atual, que a consulta e a contagem por status nulo retornam lista vazia e zero, e que uma tentativa de atualização para status nulo falha sem salvar a tarefa. Corrigir a validação do setter de status, mantendo o serviço sem persistir a atualização rejeitada.
- **Arquivos criados ou alterados:** `app/src/test/groovy/org/example/model/TarefaSpec.groovy`; `app/src/test/groovy/org/example/service/TarefasServiceSpec.groovy`; `app/src/main/java/org/example/model/Tarefa.java` e, somente se necessário para o contrato, `app/src/main/java/org/example/service/TarefasService.java`.
- **Como validar:** executar `./gradlew :app:test --tests org.example.model.TarefaSpec --tests org.example.service.TarefasServiceSpec` e verificar que não ocorre salvamento nos casos rejeitados.

## 7. Corrigir e testar a ordenação sem mutar o repositório

- **O que será feito:** acrescentar testes que preservem a ordem e o conteúdo da lista original devolvida pelo repositório após listagem e filtros; corrigir o serviço para ordenar uma cópia, sem mudar a prioridade crescente dos resultados.
- **Arquivos criados ou alterados:** `app/src/test/groovy/org/example/service/TarefasServiceSpec.groovy`; `app/src/main/java/org/example/service/TarefasService.java`.
- **Como validar:** executar `./gradlew :app:test --tests org.example.service.TarefasServiceSpec` e conferir simultaneamente a ordem do resultado e a ordem inalterada da lista original.

## 8. Executar a suíte completa

- **O que será feito:** validar em conjunto os testes Spock e a compatibilidade do build com JUnit Jupiter; corrigir apenas problemas introduzidos por esta implementação.
- **Arquivos criados ou alterados:** nenhum arquivo planejado; ajustes somente se os testes ou o build identificarem falhas relacionadas às tarefas anteriores.
- **Como validar:** executar `./gradlew test` e confirmar que toda a suíte passa.
