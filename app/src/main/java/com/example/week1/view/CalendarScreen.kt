package com.example.week1.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.week1.viewmodel.TaskViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Add
import androidx.compose.foundation.lazy.items
import com.example.week1.model.Task
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextOverflow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(taskViewModel: TaskViewModel, onNavigateToHome: () -> Unit) {
    val tasks by taskViewModel.tasks.collectAsState()
    val tasksByDate = remember(tasks) { tasks.groupBy { it.dueDate }.toSortedMap() }
    var editingTask by remember { mutableStateOf<Task?>(null) }
    val closeDialog = { editingTask = null }
    var showAddDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Kalenteri") },
                navigationIcon = {
                    IconButton(onClick = onNavigateToHome) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "Takaisin listaukseen"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { showAddDialog = true }) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "Lisää uusi tehtävä"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->

        LazyColumn(modifier = Modifier.fillMaxSize().padding(innerPadding).padding(horizontal = 16.dp)) {
            tasksByDate.forEach { (date, tasksForDate) ->

                item {
                    Text(
                        text = date,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(top = 24.dp, bottom = 8.dp)
                    )
                    HorizontalDivider()
                }

                items(tasksForDate) { task ->
                    TaskRow(
                        task = task,
                        onToggleDone = { taskViewModel.toggleDone(task.id) },
                        onEdit = { editingTask = task }
                    )
                    HorizontalDivider()
                }
            }

            if (tasksByDate.isEmpty()) {
                item {
                    Text(
                        "Ei tehtäviä",
                        modifier = Modifier.fillMaxWidth().padding(32.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
        editingTask?.let { task ->
            TaskDetailDialog(
                task = task,
                nextId = 0,
                onDismiss = closeDialog,
                onSave = { updated ->
                    taskViewModel.updateTask(updated)
                    closeDialog()
                },
                onDelete = { id ->
                    taskViewModel.removeTask(id)
                    closeDialog()
                }
            )
        }
        if (showAddDialog) {
            TaskDetailDialog(
                task = null,
                nextId = (tasks.maxOfOrNull { it.id } ?: 0) + 1,
                onDismiss = { showAddDialog = false },
                onSave = { taskViewModel.addTask(it) },
                onDelete = {}
            )
        }
    }
}

@Composable
fun TaskRow(task: Task, onToggleDone: () -> Unit, onEdit: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp), verticalAlignment = Alignment.CenterVertically) {
        Checkbox(checked = task.done, onCheckedChange = { onToggleDone() })
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = task.title,
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            if (task.description.isNotBlank()) {
                Text(
                    text = task.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        Text(
            text = task.priority.toString(),
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        IconButton(onClick = onEdit) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Muokkaa"
            )
        }
    }
}