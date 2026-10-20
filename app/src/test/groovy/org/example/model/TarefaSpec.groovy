package org.example.model

import spock.lang.Specification

import java.time.LocalDate

class TarefaSpec extends Specification {

    def "new task starts with TODO status"() {
        given:
        def tarefa = new Tarefa("Estudar", "Revisar conteúdo", LocalDate.of(2026, 10, 20), 3, "Estudos")

        expect:
        tarefa.status == Status.TODO
    }

    def "constructor accepts valid priority #prioridade"() {
        when:
        def tarefa = new Tarefa("Estudar", "Revisar conteúdo", LocalDate.of(2026, 10, 20), prioridade, "Estudos")

        then:
        tarefa.prioridade == prioridade

        where:
        prioridade << [1, 5]
    }

    def "setPrioridade accepts valid priority #prioridade"() {
        given:
        def tarefa = new Tarefa("Estudar", "Revisar conteúdo", LocalDate.of(2026, 10, 20), 3, "Estudos")

        when:
        tarefa.setPrioridade(prioridade)

        then:
        tarefa.prioridade == prioridade

        where:
        prioridade << [1, 5]
    }

    def "constructor rejects invalid priority #prioridade"() {
        when:
        new Tarefa("Estudar", "Revisar conteúdo", LocalDate.of(2026, 10, 20), prioridade, "Estudos")

        then:
        thrown(IllegalArgumentException)

        where:
        prioridade << [0, 6]
    }

    def "setPrioridade rejects invalid priority #prioridade"() {
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
