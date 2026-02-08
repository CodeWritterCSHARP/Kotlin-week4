package com.example.week1.viewmodel

import androidx.lifecycle.ViewModel
import com.example.week1.model.Task
import kotlinx.coroutines.flow.*

class TaskViewModel : ViewModel() {
    private val _tasks = MutableStateFlow(
        listOf(
            Task(1, "Mennä kauppaan", "Maito, munat, kahvi", 2, "2026-01-15", false),
            Task(2, "Nukkumaan", "9 tuntia", 1, "2026-01-14", true),
            Task(3, "Katsoa TV:ta", "Uutiset", 3, "2026-01-20", false),
            Task(4, "Vaelluksen suunnittelu", "Valita missä metsässä", 1, "2026-01-13", false),
            Task(5, "Peutupa", "Pestä pyykkiä", 2, "2026-01-16", false)
        )
    )

    val tasks: StateFlow<List<Task>> = _tasks.asStateFlow()

    fun addTask(task: Task) {
        _tasks.update { current ->
            current + task
        }
    }

    fun toggleDone(id: Int) {
        _tasks.update { current ->
            current.map { task -> if (task.id == id) task.copy(done = !task.done) else task }
        }
    }

    fun removeTask(id: Int) {
        _tasks.update { current ->
            current.filter { it.id != id }
        }
    }

    fun updateTask(updated: Task) {
        _tasks.update { current ->
            current.map { if (it.id == updated.id) updated else it }
        }
    }

    fun filterByDone(done: Boolean): List<Task> {
        return tasks.value.filter { it.done == done }
    }

    fun sortByDueDate() {
        _tasks.update { it.sortedBy { task -> task.dueDate } }
    }
}