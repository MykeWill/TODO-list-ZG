let tarefas = JSON.parse(localStorage.getItem("tarefas")) || []
let proximoId = 1

if (tarefas.length > 0) {
    let maiorId = tarefas[0].id
    for (let i = 1; i < tarefas.length; i++) {
        if (tarefas[i].id > maiorId) {
            maiorId = tarefas[i].id
        }
    }
    proximoId = maiorId + 1
}

function salvarNoLocalStorage() {
    localStorage.setItem("tarefas", JSON.stringify(tarefas))
}

export function salvarTarefaRepository(tarefa) {
    if (tarefa.id === null) {
        tarefa.id = proximoId
        proximoId = proximoId + 1
        tarefas.push(tarefa)
    } else {
        tarefas = tarefas.filter(t => t.id !== tarefa.id)
        tarefas.push(tarefa)
    }
    salvarNoLocalStorage()
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
    salvarNoLocalStorage()
}

export function atualizarStatusEmLoteRepository(ids, novoStatus) {
    for (let i = 0; i < tarefas.length; i++) {
        if (ids.includes(tarefas[i].id)) {
            tarefas[i].status = novoStatus
        }
    }
    salvarNoLocalStorage()
}