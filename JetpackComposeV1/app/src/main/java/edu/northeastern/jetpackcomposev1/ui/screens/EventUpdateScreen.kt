package edu.northeastern.jetpackcomposev1.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import edu.northeastern.jetpackcomposev1.models.application.Event
import edu.northeastern.jetpackcomposev1.models.job.ApplicationStatus
import edu.northeastern.jetpackcomposev1.models.job.JobApplicationModel
import edu.northeastern.jetpackcomposev1.utility.dateToMillis
import edu.northeastern.jetpackcomposev1.viewmodels.ApplicationViewModel



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventUpdateScreen(
    applicationViewModel: ApplicationViewModel,
    application: JobApplicationModel,
    onNavigateToApplicationDetail: () -> Unit
) {
    val selectedEvent by applicationViewModel.selectedEvent
    val event = selectedEvent
    var selectedStatus by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        val dateState = rememberDatePickerState(initialDisplayMode = DisplayMode.Input)
        if (event != null) {
            val initialDateMillis = dateToMillis(event.date)
            val date by remember { mutableLongStateOf(initialDateMillis) }
            // Remember the DatePicker state with the initial date

            val dateState = rememberDatePickerState(initialSelectedDateMillis = date)


        }
        DatePicker(state = dateState, modifier = Modifier.padding(16.dp))

        //choose a status
        Box(modifier = Modifier.padding(30.dp)) {
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
            ) {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),

                    readOnly = false,
                    value = selectedStatus,
                    onValueChange = { selectedStatus = it },
                    label = { Text("State") },

                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    },

                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    singleLine = true
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    ApplicationStatus.values().forEach { selectedOption ->
                        DropdownMenuItem(
                            text = { Text(selectedOption.displayName) },
                            onClick = {
                                selectedStatus = selectedOption.displayName
                                expanded = false
                            },
                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                        )
                    }
                }
            }

        }

        Button(
            onClick = {
                if (selectedStatus.isNotBlank()) {
                    val newEvent = selectedStatus?.let { Event(it, dateState.toString()) }
                    if (event != null) {
                        applicationViewModel.updateEventToDB(application, event, newEvent!!)
                        }
                    }
                    selectedStatus = ""
                    onNavigateToApplicationDetail

            },
            modifier = Modifier.padding(12.dp)
        ) {
            Text("Submit")

        }
        CancelButton(onClick = {
            onNavigateToApplicationDetail
        })
    }
}


@Composable
fun CancelButton(onClick: () -> Unit) {
    Button(
        onClick = {
            onClick()
        },
        modifier = Modifier.padding(12.dp)
    ) {
        Text("Cancel")
    }
}









