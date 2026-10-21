package org.example.service

import org.example.model.Status
import org.example.model.Tarefa
import org.example.repository.TarefasRepository
import spock.lang.Specification

import java.time.LocalDate

class TarefasServiceSpec extends Specification {

    TarefasRepository repository = Mock()
    TarefasService service = new TarefasService(repository)

    def "create saves the task and returns the saved task"() {
        given:
        Tarefa tarefaSalva = tarefa(3, "Work")

        when:
        def resultado = service.criar("Task", "Description", LocalDate.of(2026, 10, 20), 3, "Work")

        then:
        1 * repository.salvar({
            it.nome == "Task" &&
                    it.descricao == "Description" &&
                    it.dataTermino == LocalDate.of(2026, 10, 20) &&
                    it.prioridade == 3 &&
                    it.categoria == "Work" &&
                    it.status == Status.TODO
        }) >> tarefaSalva
        resultado.is(tarefaSalva)
    }

    def "findById returns the repository Optional when a task exists"() {
        given:
        def id = 42L
        Tarefa tarefaEncontrada = tarefa(2, "Work")
        Optional<Tarefa> resultadoEsperado = Optional.of(tarefaEncontrada)

        when:
        def resultado = service.buscarPorId(id)

        then:
        1 * repository.buscarPorId(id) >> resultadoEsperado
        resultado.is(resultadoEsperado)
    }

    def "findById returns the empty repository Optional when no task exists"() {
        given:
        def id = 99L
        Optional<Tarefa> resultadoEsperado = Optional.empty()

        when:
        def resultado = service.buscarPorId(id)

        then:
        1 * repository.buscarPorId(id) >> resultadoEsperado
        resultado.is(resultadoEsperado)
    }

    def "listAll returns tasks ordered by ascending priority"() {
        given:
        def tarefas = [tarefa(5, "Work"), tarefa(2, "Work"), tarefa(4, "Work"), tarefa(1, "Work"), tarefa(3, "Work")]

        when:
        def resultado = service.listarTodasOrdenadasPorPrioridade()

        then:
        1 * repository.listarTodas() >> tarefas
        resultado*.prioridade == [1, 2, 3, 4, 5]
    }

    def "listAll does not mutate the repository task list"() {
        given:
        def tarefas = [tarefa(5, "Work"), tarefa(2, "Work"), tarefa(4, "Work")]
        def ordemOriginal = tarefas.toList()

        when:
        def resultado = service.listarTodasOrdenadasPorPrioridade()

        then:
        1 * repository.listarTodas() >> tarefas
        resultado*.prioridade == [2, 4, 5]
        tarefas == ordemOriginal
    }

    def "filter by category without case sensitivity"() {
        given:
        def tarefas = [tarefa(4, "Personal"), tarefa(3, "WORK"), tarefa(1, "Work")]

        when:
        def resultado = service.listarPorCategoria("wOrK")

        then:
        1 * repository.listarTodas() >> tarefas
        resultado*.categoria == ["Work", "WORK"]
        resultado*.prioridade == [1, 3]
    }

    def "filter by category returns an empty list when there are no matches"() {
        given:
        def tarefas = [tarefa(1, "Personal"), tarefa(2, "Home")]

        when:
        def resultado = service.listarPorCategoria("Work")

        then:
        1 * repository.listarTodas() >> tarefas
        resultado.empty
    }

    def "filter by category excludes tasks with a null category"() {
        given:
        def tarefaSemCategoria = tarefa(1, null)
        def tarefaDaCategoria = tarefa(2, "Work")
        def tarefas = [tarefaSemCategoria, tarefaDaCategoria]

        when:
        def resultado = service.listarPorCategoria("Work")

        then:
        1 * repository.listarTodas() >> tarefas
        resultado == [tarefaDaCategoria]
    }

    def "filter by a null category returns an empty list"() {
        given:
        def tarefas = [tarefa(1, null), tarefa(2, "Work")]

        when:
        def resultado = service.listarPorCategoria(null)

        then:
        1 * repository.listarTodas() >> tarefas
        resultado.empty
    }

    def "filter by status #status"() {
        given:
        def tarefas = [tarefa(3, "Work", Status.TODO), tarefa(1, "Home", Status.DOING), tarefa(2, "Work", Status.DONE)]

        when:
        def resultado = service.listarPorStatus(status)

        then:
        1 * repository.listarTodas() >> tarefas
        resultado.size() == 1
        resultado.first().status == status

        where:
        status << [Status.TODO, Status.DOING, Status.DONE]
    }

    def "filter by status returns an empty list when there are no matches"() {
        given:
        def tarefas = [tarefa(1, "Work", Status.TODO), tarefa(2, "Home", Status.DOING)]

        when:
        def resultado = service.listarPorStatus(Status.DONE)

        then:
        1 * repository.listarTodas() >> tarefas
        resultado.empty
    }

    def "filter by a null status returns an empty list"() {
        when:
        def resultado = service.listarPorStatus(null)

        then:
        0 * repository.listarTodas()
        resultado.empty
    }

    def "filter by exact priority"() {
        given:
        def tarefas = [tarefa(3, "Work"), tarefa(2, "Home"), tarefa(2, "Personal"), tarefa(1, "Work")]

        when:
        def resultado = service.listarPorPrioridade(2)

        then:
        1 * repository.listarTodas() >> tarefas
        resultado*.prioridade == [2, 2]
    }

    def "filter by priority returns an empty list when there are no matches"() {
        given:
        def tarefas = [tarefa(1, "Work"), tarefa(3, "Home")]

        when:
        def resultado = service.listarPorPrioridade(5)

        then:
        1 * repository.listarTodas() >> tarefas
        resultado.empty
    }

    def "countByStatus returns the number of tasks with the requested status"() {
        given:
        def tarefas = [
                tarefa(1, "Work", Status.TODO),
                tarefa(2, "Home", Status.DOING),
                tarefa(3, "Personal", Status.TODO)
        ]

        when:
        def resultado = service.contarPorStatus(Status.TODO)

        then:
        1 * repository.listarTodas() >> tarefas
        resultado == 2
    }

    def "countByStatus returns zero for a null status"() {
        when:
        def resultado = service.contarPorStatus(null)

        then:
        0 * repository.listarTodas()
        resultado == 0
    }

    def "displayStatistics prints the total and counts for each status"() {
        given:
        def tarefas = [
                tarefa(1, "Work", Status.TODO),
                tarefa(2, "Home", Status.DOING),
                tarefa(3, "Personal", Status.TODO),
                tarefa(4, "Work", Status.DONE)
        ]
        def output = new ByteArrayOutputStream()
        def originalOut = System.out

        when:
        try {
            System.setOut(new PrintStream(output))
            service.exibirEstatisticas()
        } finally {
            System.setOut(originalOut)
        }

        then:
        4 * repository.listarTodas() >>> [tarefas, tarefas, tarefas, tarefas]
        output.toString() contains "Total de tarefas: 4"
        output.toString() contains "A fazer (TODO): 2"
        output.toString() contains "Em andamento (DOING): 1"
        output.toString() contains "Concluídas (DONE): 1"
    }

    def "updateStatus updates the task, saves it, and returns it"() {
        given:
        def id = 42L
        Tarefa tarefaExistente = tarefa(2, "Work", Status.TODO)

        when:
        def resultado = service.atualizarStatus(id, Status.DOING)

        then:
        1 * repository.buscarPorId(id) >> Optional.of(tarefaExistente)
        1 * repository.salvar({ it.is(tarefaExistente) && it.status == Status.DOING }) >> tarefaExistente
        resultado.is(tarefaExistente)
        resultado.status == Status.DOING
    }

    def "updateStatus rejects null and does not save the task"() {
        given:
        def id = 42L
        Tarefa tarefaExistente = tarefa(2, "Work", Status.TODO)

        when:
        service.atualizarStatus(id, null)

        then:
        1 * repository.buscarPorId(id) >> Optional.of(tarefaExistente)
        0 * repository.salvar(_)
        thrown(IllegalArgumentException)
        tarefaExistente.status == Status.TODO
    }

    def "updateStatus throws and does not save when the task does not exist"() {
        given:
        def id = 99L

        when:
        service.atualizarStatus(id, Status.DONE)

        then:
        1 * repository.buscarPorId(id) >> Optional.empty()
        0 * repository.salvar(_)
        thrown(RuntimeException)
    }

    def "delete removes an existing task"() {
        given:
        def id = 42L
        Tarefa tarefaExistente = tarefa(2, "Work")

        when:
        service.deletar(id)

        then:
        1 * repository.buscarPorId(id) >> Optional.of(tarefaExistente)
        1 * repository.deletar(id)
    }

    def "delete throws and does not call repository delete when the task does not exist"() {
        given:
        def id = 99L

        when:
        service.deletar(id)

        then:
        1 * repository.buscarPorId(id) >> Optional.empty()
        0 * repository.deletar(_)
        thrown(RuntimeException)
    }

    private static Tarefa tarefa(int prioridade, String categoria, Status status = Status.TODO) {
        def tarefa = new Tarefa("Task", "Description", LocalDate.of(2026, 10, 20), prioridade, categoria)
        tarefa.setStatus(status)
        return tarefa
    }
}
