package com.example.week1.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.week1.model.Task
import com.example.week1.viewmodel.TaskViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Add

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(taskViewModel: TaskViewModel, onNavigateToCalendar: () -> Unit) {
    val tasks by taskViewModel.tasks.collectAsState()
    var editingTask by remember { mutableStateOf<Task?>(null) }
    var showDoneOnly by remember { mutableStateOf(false) }
    var nextId by remember { mutableIntStateOf(0) }
    val closeDialog = { editingTask = null }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tehtävät") },
                actions = {
                    IconButton(onClick = {
                        nextId = (tasks.maxOfOrNull { it.id } ?: 0) + 1
                        editingTask = null
                    }) {
                        Icon(Icons.Filled.Add, contentDescription = "Lisää tehtävä")
                    }
                    IconButton(onClick = onNavigateToCalendar) {
                        Icon(
                            imageVector = Icons.Filled.DateRange,
                            contentDescription = "Kalenteri"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->

        Column(modifier = Modifier.fillMaxSize().padding(innerPadding).padding(16.dp))
        {
            Spacer(modifier = Modifier.height(12.dp))

            Text("Omat tehtävät", style = MaterialTheme.typography.titleLarge)

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                Button(onClick = { showDoneOnly = !showDoneOnly })
                {
                    Text(if (showDoneOnly) "Näytä kaikki" else "Näytä vain valmiit")
                }

                Button(onClick = { taskViewModel.sortByDueDate() })
                {
                    Text("Lajittele")
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter)
            {
                val displayedTasks = if (showDoneOnly) taskViewModel.filterByDone(true) else tasks

                LazyColumn(modifier = Modifier.fillMaxWidth(0.95f))
                {
                    item {
                        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp))
                        {
                            Text("Tehtävä", Modifier.weight(2.4f))
                            Text("Kuvaus", Modifier.weight(2.2f))
                            Text("Prio.", Modifier.weight(2f))
                            Text("Eräpäivä", Modifier.weight(4f), textAlign = TextAlign.Left)
                            Text("Tila", Modifier.weight(2f))
                        }
                        HorizontalDivider()
                    }

                    items(displayedTasks) { task ->
                        Row(
                            modifier = Modifier.fillMaxWidth().heightIn(min = 56.dp)
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                task.title,
                                modifier = Modifier.weight(2f),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                task.description,
                                modifier = Modifier.weight(3f),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(task.priority.toString(), modifier = Modifier.weight(1f))
                            Text(task.dueDate, modifier = Modifier.weight(1.5f))
                            Text(
                                if (task.done) "Valmis" else "Kesken",
                                modifier = Modifier.weight(1f)
                            )

                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Button(
                                    onClick = { taskViewModel.toggleDone(task.id) },
                                    modifier = Modifier
                                        .height(36.dp)
                                        .width(90.dp)
                                ) {
                                    Text("Vaihda")
                                }

                                Button(
                                    onClick = { editingTask = task },
                                    modifier = Modifier
                                        .height(36.dp)
                                        .width(90.dp)
                                ) {
                                    Text("Muokkaa")
                                }
                            }
                        }
                        HorizontalDivider()
                    }
                }
            }
        }
        if (editingTask != null || nextId != 0) {
            TaskDetailDialog(
                task = editingTask,
                nextId = nextId,
                onDismiss = {
                    closeDialog()
                    nextId = 0
                },
                onSave = { task ->
                    if (editingTask == null) {
                        taskViewModel.addTask(task)
                    } else {
                        taskViewModel.updateTask(task)
                    }
                    closeDialog()
                },
                onDelete = { id ->
                    taskViewModel.removeTask(id)
                    closeDialog()
                }
            )
        }
    }
}