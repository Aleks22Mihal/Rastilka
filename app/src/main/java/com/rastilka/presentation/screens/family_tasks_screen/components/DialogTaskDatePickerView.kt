package com.rastilka.presentation.screens.family_tasks_screen.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.rastilka.presentation.screens.family_tasks_screen.data.FamilyTasksScreenEvent
import com.rastilka.presentation.ui.theme.RastilkaTheme
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DialogTaskDatePickerView(
    taskUrl: String,
    taskDateMillis: Long,
    onEvent: (FamilyTasksScreenEvent) -> Unit
) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = taskDateMillis,
        yearRange = LocalDate.now().year..DatePickerDefaults.YearRange.last,
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                val now = LocalDateTime.now().minusDays(1)
                val parForConSecToMillisecond = 1000
                val nowInSeconds =
                    now.toEpochSecond(ZoneOffset.UTC) * parForConSecToMillisecond

                return utcTimeMillis >= nowInSeconds
            }

            override fun isSelectableYear(year: Int): Boolean {
                return year >= LocalDate.now().year
            }
        }
    )

    DatePickerDialog(
        shape = MaterialTheme.shapes.extraSmall,
        onDismissRequest = {
            onEvent(FamilyTasksScreenEvent.ChangeStateDateDialog(false))
        },
        dismissButton = {
            TextButton(onClick = {
                onEvent(FamilyTasksScreenEvent.ChangeStateDateDialog(false))
            }) {
                Text(text = "Отмена")
            }
        },
        confirmButton = {
            TextButton(onClick = {
                if (datePickerState.selectedDateMillis != null) {
                    val apiFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
                    val instant =
                        Instant.ofEpochMilli(datePickerState.selectedDateMillis!!)
                    val date =
                        LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
                    val dateFormatter = apiFormat.format(date)
                    onEvent(
                        FamilyTasksScreenEvent.EditTask(
                            productUrl = taskUrl,
                            property = "date",
                            value = dateFormatter
                        )
                    )
                    onEvent(FamilyTasksScreenEvent.ChangeStateDateDialog(false))
                } else {
                    onEvent(FamilyTasksScreenEvent.ChangeStateDateDialog(false))
                }
            }) {
                Text(text = "Изменить")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}

@Preview(showBackground = true)
@Composable
private fun DemoDialogTaskDatePickerView() {
    RastilkaTheme {
        Box(Modifier.fillMaxSize()) {
            DialogTaskDatePickerView(
                taskUrl = "",
                taskDateMillis = 1715947960306,
                onEvent = {}
            )
        }
    }
}
