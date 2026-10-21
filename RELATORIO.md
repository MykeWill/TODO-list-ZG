# Relatório do trabalho SDD

## 1. Consumo aproximado de tokens por etapa

Não tenho acesso à métrica exata de tokens do Copilot nesta conversa. As faixas abaixo são estimativas qualitativas, baseadas no tamanho das interações, leituras e alterações feitas em cada etapa; não são medições do consumo real.

| Etapa | Estimativa aproximada |
|---|---|
| Contexto e `AGENTS.md` | Algumas centenas de tokens |
| Especificação (`docs/spec.md`) | Poucos milhares de tokens, incluindo perguntas de esclarecimento e a elaboração dos critérios |
| Plano (`docs/plano.md`) | Cerca de mil tokens |
| Implementação (tarefas 1 a 8) | Vários milhares de tokens, considerando configuração Gradle, testes, correções incrementais e validações |
| Skill (`SKILL.md`) | Cerca de mil tokens |
| Pull Request | Algumas centenas de tokens |

As estimativas variam conforme o contexto carregado e não devem ser interpretadas como contagem precisa.

## 2. Situação em que o agente divergiu da spec

Na tarefa 6, foi solicitado verificar que o construtor de `Tarefa` não aceita status nulo. Ao inspecionar o código, o agente constatou que o construtor não recebe status como argumento: ele próprio inicializa o campo com `TODO`. Portanto, não havia como passar `null` ao construtor para testar uma rejeição.

O agente tratou essa limitação com um teste que confirma que uma tarefa recém-construída tem status não nulo e igual a `TODO`. Esse teste verifica a inicialização definida pelo código, mas não comprova literalmente que o construtor rejeita um status nulo fornecido como argumento. A distinção foi explicada antes da implementação dos testes; a rejeição efetiva de `null` foi coberta no setter `setStatus`, que é a interface disponível para tentar atribuir esse valor.

Durante o trabalho também foram identificados e corrigidos três comportamentos incompatíveis com os critérios acordados: o filtro de categoria podia lançar `NullPointerException` com categoria nula; status nulo era aceito ou tratado sem o retorno esperado; e a ordenação alterava a lista original devolvida pelo repositório. Cada correção foi acompanhada de testes direcionados.
