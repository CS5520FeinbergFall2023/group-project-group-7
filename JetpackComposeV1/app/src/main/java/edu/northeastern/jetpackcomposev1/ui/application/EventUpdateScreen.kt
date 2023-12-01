package edu.northeastern.jetpackcomposev1.ui.sheets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
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
fun EventUpdateScreen(
    applicationViewModel: ApplicationViewModel,
    onNavigateToApplicationDetail: () -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier,

    ) {
    val sheetState = rememberModalBottomSheetState()
    var selectedStatus by rememberSaveable { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var newDate by rememberSaveable { mutableStateOf("") }
    val application by applicationViewModel.selectedApplication
    val event by applicationViewModel.selectedEvent
    var initialDateMillis: Long = 0
    initialDateMillis = if (event!!.date != "") {
        dateToMillis(event!!.date)
    } else {
        dateToMillis(millisToDate(System.currentTimeMillis()))
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
            //add this for the keyboard to not overlap the input
            .imePadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (event != null) {
            //val date by remember { mutableLongStateOf(initialDateMillis) }
            val dateState = rememberDatePickerState(initialDisplayMode = DisplayMode.Input)
            DatePicker(state = dateState, modifier = Modifier.padding(16.dp))
            newDate = dateState.selectedDateMillis?.let { millisToDate(it) }.toString()
        }
        //choose a status
        Column(
            modifier = Modifier
                .padding(38.dp)
                //add this
                .imePadding()
        ) {
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
            modifier = Modifier.fillMaxWidth().padding(48.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Button(
                //onCloseSheet,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                ),
                onClick = onCancel,
            ) {
                Text(
                    text = "Cancel",
                    modifier = Modifier.padding(4.dp),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Button(colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
                onClick = {
                    if (selectedStatus.isNotBlank()) {
                        val newEvent = selectedStatus?.let { Event(it, newDate) }
                        if (event != null) {
                            applicationViewModel.updateEventToDB(
                                application!!,
                                event!!,
                                newEvent!!
                            )
                        }
                    }
                    selectedStatus = ""
                    //onCloseSheet()
                    onNavigateToApplicationDetail()
                },
            ) {
                Text(
                    text = "Save",
                    modifier = Modifier.padding(4.dp),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

        }
        Spacer(modifier = modifier.height(16.dp))
    }

}







