package com.example.week1.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.week1.model.Task

@Composable
fun TaskDetailDialog(
    task: Task?,
    nextId: Int,
    onDismiss: () -> Unit,
    onSave: (Task) -> Unit,
    onDelete: (Int) -> Unit
) {
    val isEdit = task != null

    var title by remember { mutableStateOf(task?.title ?: "") }
    var description by remember { mutableStateOf(task?.description ?: "") }
    var priority by remember { mutableStateOf(task?.priority?.toString() ?: "2") }
    var dueDate by remember { mutableStateOf(task?.dueDate ?: "2026-02-07") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (isEdit) "Muokkaa tehtävää" else "Uusi tehtävä") },
        text = {
            Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Otsikko") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Kuvaus") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 2
                )
                OutlinedTextField(
                    value = priority,
                    onValueChange = { newValue ->
                        if (newValue.isEmpty() || newValue.all { it.isDigit() }) { priority = newValue }
                    },
                    label = { Text("Prioriteetti") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                val dateIsValid = dueDate.matches(Regex("\\d{4}-\\d{2}-\\d{2}")) || dueDate.isBlank()
                OutlinedTextField(
                    value = dueDate,
                    onValueChange = { dueDate = it },
                    label = { Text("Eräpäivä (yyyy-mm-dd)") },
                    placeholder = { Text("esim. 2026-02-08") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    supportingText = {
                        if (!dateIsValid) {
                            Text(
                                text = "Käytä muotoa yyyy-mm-dd",
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                if (title.isNotBlank()) {
                    val result = if (isEdit) {
                        task!!.copy(
                            title = title.trim(),
                            description = description.trim(),
                            priority = priority.toIntOrNull() ?: task.priority,
                            dueDate = dueDate.trim()
                        )
                    } else {
                        Task(
                            id = nextId,
                            title = title.trim(),
                            description = description.trim(),
                            priority = priority.toIntOrNull() ?: 2,
                            dueDate = dueDate.trim(),
                            done = false
                        )
                    }
                    onSave(result)
                    onDismiss()
                }
            }
            ) {
                Text("Tallenna")
            }
        },
        dismissButton = {
            Row {
                if (isEdit) {
                    TextButton(
                        onClick = {
                            onDelete(task!!.id)
                            onDismiss()
                        },
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Text("Poista")
                    }

                    Spacer(Modifier.width(8.dp))
                }

                TextButton(onClick = onDismiss) {
                    Text("Peruuta")
                }
            }
        }
    )
}