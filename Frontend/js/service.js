import { Tarefa } from "./model.js"
import { salvarTarefaRepository, buscarPorIdRepository, listarTodasRepository, deletarRepository } from "./repository.js"

export function criarTarefaService(nome, descricao, dataTermino, prioridade, categoria) {
    const tarefa = new Tarefa(nome, descricao, dataTermino, prioridade, categoria)
    return salvarTarefaRepository(tarefa)
}

export function listarTodasOrdenadasPorPrioridadeService() {
    return listarTodasRepository().sort((a, b) => a.prioridade - b.prioridade)
}

export function listarPorStatusService(status) {
    return listarTodasOrdenadasPorPrioridadeService().filter(t => t.status === status)
}

export function atualizarService(id, nome, descricao, dataTermino, prioridade, categoria, status) {
    const tarefa = buscarPorIdRepository(id)
    if (!tarefa) {
        throw new Error("Tarefa não encontrada.")
    }

    if (prioridade < 1 || prioridade > 5) {
        throw new Error("Prioridade deve estar entre 1 e 5.")
    }

    tarefa.nome = nome
    tarefa.descricao = descricao
    tarefa.dataTermino = dataTermino
    tarefa.prioridade = Number(prioridade)
    tarefa.categoria = categoria
    tarefa.status = status

    return salvarTarefaRepository(tarefa)
}

export function deleteService(id) {
    const tarefa = buscarPorIdRepository(id)
    if (!tarefa) {
        throw new Error("Tarefa não encontrada.")
    }
    deletarRepository(id)
}

export function buscarPorIdService(id) {
    return buscarPorIdRepository(id)
}