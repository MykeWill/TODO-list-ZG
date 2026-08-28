import { criarTarefaService, atualizarService, listarTodasOrdenadasPorPrioridadeService, listarPorStatusService, deleteService, buscarPorIdService } from "./service.js"

const form = document.getElementById("form-tarefa")
const inputId = document.getElementById("tarefa-id")
const inputNome = document.getElementById("nome")
const inputDescricao = document.getElementById("descricao")
const inputData = document.getElementById("dataTermino")
const inputPrioridade = document.getElementById("prioridade")
const inputCategoria = document.getElementById("categoria")
const statusContainer = document.getElementById("status-container")
const selectStatus = document.getElementById("status")
const btnSubmit = document.getElementById("btn-submit")
const btnCancelar = document.getElementById("btn-cancelar")
const filtroStatus = document.getElementById("filtro-status")
const listaTarefas = document.getElementById("lista-tarefas")

function renderizarLista() {
    const filtro = filtroStatus.value
    const tarefas = filtro === "TODAS" ? listarTodasOrdenadasPorPrioridadeService() : listarPorStatusService(filtro)

    listaTarefas.innerHTML = ""

    tarefas.forEach(tarefa => {
        const item = document.createElement("li")

        item.innerHTML = `
            <strong>${tarefa.nome}</strong> (Prioridade ${tarefa.prioridade})
            <p>${tarefa.descricao}</p>
            <p>Categoria: ${tarefa.categoria} | Prazo: ${tarefa.dataTermino} | Status: ${tarefa.status}</p>
            <button class="btn-editar" data-id="${tarefa.id}">Editar</button>
            <button class="btn-excluir" data-id="${tarefa.id}">Excluir</button>
        `

        listaTarefas.appendChild(item)
    })
}

function limparFormulario() {
    form.reset()
    inputId.value = ""
    statusContainer.style.display = "none"
    btnSubmit.textContent = "Criar tarefa"
    btnCancelar.style.display = "none"
}

function preencherFormularioParaEdicao(tarefa) {
    inputId.value = tarefa.id
    inputNome.value = tarefa.nome
    inputDescricao.value = tarefa.descricao
    inputData.value = tarefa.dataTermino
    inputPrioridade.value = tarefa.prioridade
    inputCategoria.value = tarefa.categoria
    selectStatus.value = tarefa.status

    statusContainer.style.display = "block"
    btnSubmit.textContent = "Salvar edição"
    btnCancelar.style.display = "inline-block"
}

form.addEventListener("submit", (evento) => {
    evento.preventDefault()

    const id = inputId.value ? Number(inputId.value) : null
    const dataTermino = inputData.value
    const hoje = new Date().toISOString().split("T")[0]

    if (dataTermino < hoje) {
        alert("A data de término não pode ser anterior a hoje.")
        return
    }

    try {
        if (id === null) {
            criarTarefaService(
                inputNome.value,
                inputDescricao.value,
                inputData.value,
                inputPrioridade.value,
                inputCategoria.value
            )
        } else {
            atualizarService(
                id,
                inputNome.value,
                inputDescricao.value,
                inputData.value,
                inputPrioridade.value,
                inputCategoria.value,
                selectStatus.value
            )
        }

        limparFormulario()
        renderizarLista()
    } catch (erro) {
        alert(erro.message)
    }
})

btnCancelar.addEventListener("click", limparFormulario)

filtroStatus.addEventListener("change", renderizarLista)

listaTarefas.addEventListener("click", (evento) => {
    const id = Number(evento.target.dataset.id)

    if (evento.target.classList.contains("btn-editar")) {
        const tarefa = buscarPorIdService(id)
        if (tarefa) {
            preencherFormularioParaEdicao(tarefa)
        }
    }

    if (evento.target.classList.contains("btn-excluir")) {
        deleteService(id)
        renderizarLista()
    }
})

renderizarLista()