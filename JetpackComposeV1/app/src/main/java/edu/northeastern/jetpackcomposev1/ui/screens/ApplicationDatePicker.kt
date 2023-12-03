package edu.northeastern.jetpackcomposev1.ui.screens

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import java.net.CacheRequest
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZoneOffset

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApplicationDatePicker(
    state: DatePickerState,
    isOpen: Boolean,
    confirmButtonText: String = "OK",
    dismissButtonText: String = "Cancel",
    onDismissRequest: () -> Unit,
    onConfirmButtonClicked: () -> Unit
) {
    if (isOpen) {
        DatePickerDialog(
            onDismissRequest = onDismissRequest,
            confirmButton = {
                TextButton(onClick = onConfirmButtonClicked) {
                    Text(text = confirmButtonText)
                }
            },
            dismissButton = {
                TextButton(onClick =  onDismissRequest ) {
                    Text(text = dismissButtonText)
                }
            },
            content = {
                DatePicker(state = state,
                    dateValidator = { timestamp ->
                    val selectedDate = Instant.ofEpochMilli(timestamp).atOffset(ZoneOffset.UTC).toLocalDate()
                    val currentDate = LocalDate.now(ZoneId.systemDefault()).atStartOfDay().toLocalDate()
                    selectedDate <= currentDate
                })
            }
        )
    }
}