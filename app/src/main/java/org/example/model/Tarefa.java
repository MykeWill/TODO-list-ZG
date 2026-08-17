package org.example.model;

import java.time.LocalDate;

public class Tarefa {
    private Long id;
    private String nome;
    private String descricao;
    private LocalDate dataTermino;
    private int prioridade;
    private String categoria;
    private Status status;

    public Tarefa(String nome, String descricao, LocalDate dataTermino ,int prioridade, String categoria) {
        this.nome = nome;
        this.descricao = descricao;
        this.dataTermino = dataTermino;
        this.prioridade = validarPrioridade(prioridade);
        this.categoria = categoria;
        this.status = Status.TODO;
    }

    private  int validarPrioridade(int prioridade) {
         if (prioridade < 1 || prioridade  > 5) {
             throw new IllegalArgumentException("Prioridade deve estar entre 1 e 5.");
         }
        return prioridade;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public LocalDate getDataTermino() {
        return dataTermino;
    }

    public void setDataTermino(LocalDate dataTermino) {
        this.dataTermino = dataTermino;
    }

    public int getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(int prioridade) {
        this.prioridade = validarPrioridade(prioridade);
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Tarefa{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", descricao='" + descricao + '\'' +
                ", dataTermino=" + dataTermino +
                ", prioridade=" + prioridade +
                ", categoria='" + categoria + '\'' +
                ", status=" + status +
                '}';
    }
}

