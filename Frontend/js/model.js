export const Status = {
    TODO: "TODO",
    DOING: "DOING",
    DONE: "DONE"
}

export class Tarefa {
    constructor(nome, descricao, dataTermino, prioridade, categoria) {
        if (prioridade < 1 || prioridade > 5) {
            throw new Error("Prioridade deve estar entre 1 e 5.")
        }

        this.id = null
        this.nome = nome
        this.descricao = descricao
        this.dataTermino = dataTermino
        this.prioridade = Number(prioridade)
        this.categoria = categoria
        this.status = Status.TODO
    }
}