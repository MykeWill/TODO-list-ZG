package org.example.model

import spock.lang.Specification

import java.time.LocalDate

class TarefaSpec extends Specification {

    def "nova tarefa inicia com status TODO"() {
        given:
        def tarefa = new Tarefa("Estudar", "Revisar conteúdo", LocalDate.of(2026, 10, 20), 3, "Estudos")

        expect:
        tarefa.status == Status.TODO
    }

    def "construtor não cria tarefa com status nulo"() {
        when:
        def tarefa = new Tarefa("Estudar", "Revisar conteúdo", LocalDate.of(2026, 10, 20), 3, "Estudos")

        then:
        tarefa.status != null
        tarefa.status == Status.TODO
    }

    def "setStatus rejeita nulo e mantém o status anterior"() {
        given:
        def tarefa = new Tarefa("Estudar", "Revisar conteúdo", LocalDate.of(2026, 10, 20), 3, "Estudos")

        when:
        tarefa.setStatus(null)

        then:
        thrown(IllegalArgumentException)
        tarefa.status == Status.TODO
    }

    def "construtor aceita prioridade válida #prioridade"() {
        when:
        def tarefa = new Tarefa("Estudar", "Revisar conteúdo", LocalDate.of(2026, 10, 20), prioridade, "Estudos")

        then:
        tarefa.prioridade == prioridade

        where:
        prioridade << [1, 5]
    }

    def "setPrioridade aceita prioridade válida #prioridade"() {
        given:
        def tarefa = new Tarefa("Estudar", "Revisar conteúdo", LocalDate.of(2026, 10, 20), 3, "Estudos")

        when:
        tarefa.setPrioridade(prioridade)

        then:
        tarefa.prioridade == prioridade

        where:
        prioridade << [1, 5]
    }

    def "construtor rejeita prioridade inválida #prioridade"() {
        when:
        new Tarefa("Estudar", "Revisar conteúdo", LocalDate.of(2026, 10, 20), prioridade, "Estudos")

        then:
        thrown(IllegalArgumentException)

        where:
        prioridade << [0, 6]
    }

    def "setPrioridade rejeita prioridade inválida #prioridade"() {
        given:
        def tarefa = new Tarefa("Estudar", "Revisar conteúdo", LocalDate.of(2026, 10, 20), 3, "Estudos")

        when:
        tarefa.setPrioridade(prioridade)

        then:
        thrown(IllegalArgumentException)

        where:
        prioridade << [0, 6]
    }
}
