package edu.northeastern.jetpackcomposev1.ui.sheets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import edu.northeastern.jetpackcomposev1.models.application.Event
import edu.northeastern.jetpackcomposev1.models.job.ApplicationStatus
import edu.northeastern.jetpackcomposev1.ui.screens.ApplicationDatePicker
import edu.northeastern.jetpackcomposev1.ui.screens.DropdownMenu
import edu.northeastern.jetpackcomposev1.utility.changeMillisToDateString
import edu.northeastern.jetpackcomposev1.utility.millisToDate
import edu.northeastern.jetpackcomposev1.viewmodels.ApplicationViewModel
import java.time.LocalDate
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventUpdateSheet(
    applicationViewModel: ApplicationViewModel,
    onCloseSheet: () -> Unit,
    modifier: Modifier = Modifier
) {
    var datePickerExpanded by rememberSaveable { mutableStateOf(false) }
    //Default date is today
    val dateState = rememberDatePickerState(
        initialSelectedDateMillis = LocalDate.now().atStartOfDay(
            ZoneId.systemDefault()
        ).toInstant().toEpochMilli(),
        )
    val application by applicationViewModel.selectedApplication
    val event by applicationViewModel.selectedEvent
    var selectedStatus by rememberSaveable { mutableStateOf(event!!.status) }
    val sheetState = rememberModalBottomSheetState()
    var showDateEmpty = rememberSaveable{ mutableStateOf(false)}
    var showStatusEmpty = rememberSaveable{ mutableStateOf(false)}


    ModalBottomSheet(
        onDismissRequest = { onCloseSheet() },
        sheetState = sheetState,
    ) {
        LazyColumn(
            modifier = modifier
                .padding(horizontal = 8.dp)
                .fillMaxHeight(0.95f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Text(
                    text = "Update  Status",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                // Date Picker Field
                Row(modifier = Modifier.fillMaxWidth().padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween){
                    OutlinedTextField(
                        value = event!!.date.ifEmpty { dateState.selectedDateMillis.changeMillisToDateString() },
                        onValueChange = { /* Read Only */ },
                        readOnly = true,
                        label = { Text("Application Date") },
                        trailingIcon = {
                            Icon(
                                Icons.Default.DateRange,
                                "Select Date",
                                Modifier.clickable { datePickerExpanded = true })
                        },
                        modifier = Modifier.fillMaxWidth()
                            .clickable { datePickerExpanded = true }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    // DatePickerDialog
                    ApplicationDatePicker(
                        state = dateState,
                        isOpen = datePickerExpanded,
                        onDismissRequest = { datePickerExpanded = false },
                        onConfirmButtonClicked = { datePickerExpanded = false }
                    )

                    if(showDateEmpty.value){
                        Text(
                            text = "Please select a date",
                            modifier = Modifier.padding(16.dp),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.error
                        )
                    }

                }

                // Status Dropdown
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween){
                    DropdownMenu(
                        options = ApplicationStatus.values().map { it.displayName },
                        selectedOption = selectedStatus,
                        onSelectionChange = { status ->
                            selectedStatus = status
                        },
                        label = "Application Status"
                    )
                    if(showStatusEmpty.value){
                        Text(
                            text = "Please select a status",
                            modifier = Modifier.padding(16.dp),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.error
                        )
                    }

                }

                Spacer(modifier = Modifier.height(16.dp))

                //Buttons
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 48.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Button(
                        //onCloseSheet,

                        onClick = onCloseSheet,
                    ) {
                        Text(
                            text = "Cancel",
                            modifier = Modifier.padding(4.dp),
                            style = MaterialTheme.typography.bodyLarge,
                        )
                    }
                    Button(

                        onClick = {
                            if(selectedStatus.isBlank()){
                                showStatusEmpty.value = true
                            }
                            if(dateState.selectedDateMillis == null){
                                showDateEmpty.value = true
                            }
                            else if (selectedStatus.isNotBlank()) {
                                val newEvent = selectedStatus?.takeIf { it.isNotBlank() }?.let { Event(it, millisToDate(dateState.selectedDateMillis!!)) }
                                event?.let { applicationViewModel.updateEventToDB(application!!, it, newEvent!!) }
                                onCloseSheet()
                            }

                        },
                    ) {
                        Text(
                            text = "Save",
                            modifier = Modifier.padding(4.dp),
                            style = MaterialTheme.typography.bodyLarge,
                        )
                    }
                }
                Spacer(modifier = modifier.height(128.dp))
            }
        }
    }
}


