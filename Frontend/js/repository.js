let tarefas = []
let proximoId = 1

export function salvarTarefaRepository(tarefa) {
    if (tarefa.id === null) {
        tarefa.id = proximoId++
        tarefas.push(tarefa)
    } else {
        tarefas = tarefas.filter(t => t.id !== tarefa.id)
        tarefas.push(tarefa)
    }
    return tarefa
}

export function buscarPorIdRepository(id) {
    return tarefas.find(t => t.id === id) ?? null
}

export function listarTodasRepository() {
    return [...tarefas]
}

export function deletarRepository(id) {
    tarefas = tarefas.filter(t => t.id !== id)
}