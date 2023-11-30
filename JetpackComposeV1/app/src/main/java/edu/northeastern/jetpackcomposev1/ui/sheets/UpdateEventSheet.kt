package edu.northeastern.jetpackcomposev1.ui.sheets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import edu.northeastern.jetpackcomposev1.models.Application.Event
import edu.northeastern.jetpackcomposev1.models.job.ApplicationStatus
import edu.northeastern.jetpackcomposev1.utility.dateToMillis
import edu.northeastern.jetpackcomposev1.utility.millisToDate
import edu.northeastern.jetpackcomposev1.viewmodels.ApplicationViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateEventSheet(
    applicationViewModel: ApplicationViewModel,
    onCloseSheet: () -> Unit,
    modifier: Modifier = Modifier
) {
    val sheetState = rememberModalBottomSheetState()
    var selectedStatus by rememberSaveable { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var newDate by rememberSaveable { mutableStateOf("") }
    val application by applicationViewModel.selectedApplication
    val event by applicationViewModel.selectedEvent
    var initialDateMillis: Long = 0
    initialDateMillis = if(event!!.date!="") {
        dateToMillis(event!!.date)
    }else{
        dateToMillis(millisToDate(System.currentTimeMillis()))
    }
    ModalBottomSheet(
        onDismissRequest = { onCloseSheet() },
        sheetState = sheetState,
    ) {
        Column(modifier = modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            if (event != null) {
                //val date by remember { mutableLongStateOf(initialDateMillis) }
                val dateState = rememberDatePickerState(initialDisplayMode = DisplayMode.Input)
                DatePicker(state = dateState, modifier = Modifier.padding(16.dp))
                newDate = dateState.selectedDateMillis?.let { millisToDate(it) }.toString()
            }
            //choose a status
            Column(modifier = Modifier.padding(30.dp)) {
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

            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Button(
                    onClick = onCloseSheet,
                    modifier = Modifier.padding(4.dp),
                ) {
                    Text(
                        text = "Cancel",
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = {
                        if (selectedStatus.isNotBlank()) {
                            val newEvent = selectedStatus?.let { Event( it, newDate) }
                            if (event != null) {
                                applicationViewModel.updateEventToDB(
                                    application!!,
                                    event!!,
                                    newEvent!!
                                )
                            }
                        }
                        selectedStatus = ""
                        onCloseSheet()
                    },
                    modifier = Modifier.padding(4.dp)
                ) {
                    Text(
                        text = "Save",
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

            }
            Spacer(modifier = modifier.height(64.dp))
        }

    }
}






