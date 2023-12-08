package edu.northeastern.jetpackcomposev1.ui.screens

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import edu.northeastern.jetpackcomposev1.models.application.Event
import edu.northeastern.jetpackcomposev1.models.application.TimeLine
import edu.northeastern.jetpackcomposev1.models.job.ApplicationStatus
import edu.northeastern.jetpackcomposev1.models.job.JobApplicationModel
import edu.northeastern.jetpackcomposev1.models.resume.ResumeModel
import edu.northeastern.jetpackcomposev1.utility.changeMillisToDateString
import edu.northeastern.jetpackcomposev1.utility.millisToDate
import edu.northeastern.jetpackcomposev1.viewmodels.ApplicationViewModel
import edu.northeastern.jetpackcomposev1.viewmodels.JobViewModel
import edu.northeastern.jetpackcomposev1.viewmodels.ResumeViewModel
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNewApplicationScreen(
    jobViewModel: JobViewModel,
    applicationViewModel: ApplicationViewModel,
    resumeViewModel: ResumeViewModel,
    onNavigateToResume: () -> Unit,
    onNavigateToApplicationDetails: () -> Unit
) {
    //job id passed from job detail screen
    val job by jobViewModel.selectedJob

    var newApplication = JobApplicationModel()
    var selectedResume by remember { mutableStateOf<String?>(null) }
    var selectedStatus by remember { mutableStateOf(ApplicationStatus.APPLIED.displayName) }
    //Default date is today
    val dateState = rememberDatePickerState(
        initialSelectedDateMillis = LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()
            .toEpochMilli()
    )
    var eventList = mutableListOf<Event>()
    var datePickerExpanded by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val resumeList =
        resumeViewModel.resumeList.filter { it.activeStatus.equals("true", ignoreCase = true) }
            .map { it.nickName }
    val resumeOptions = resumeList + "Add a Resume"

    LazyColumn(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        item {
            Text(
                text = "Create A New Application Record",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Job Title
            TextField(
                value = job?.title ?: "",
                onValueChange = { /* Read Only */ },
                label = { Text("Job Title") },
                readOnly = true,
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Company Name
            TextField(
                value = job?.company?.display_name ?: "",
                onValueChange = { /* Read Only */ },
                readOnly = true,
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Company Name") }
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Location Input
            TextField(
                value = job?.location?.display_name ?: "",
                onValueChange = { /* Read Only */ },
                readOnly = true,
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Location") }
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Date Picker Field
            OutlinedTextField(
                value = dateState.selectedDateMillis.changeMillisToDateString(),
                onValueChange = { /* Read Only */ },
                readOnly = true,
                enabled = false,
                label = { Text("Application Date") },
                trailingIcon = {
                    Icon(
                        Icons.Default.DateRange,
                        "Select Date",
                        Modifier.clickable { datePickerExpanded = true })
                },
                modifier = Modifier.fillMaxWidth().clickable { datePickerExpanded = true },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    disabledTextColor = MaterialTheme.colorScheme.onSurface,
                    disabledBorderColor = MaterialTheme.colorScheme.outline,
                    disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant)
            )
            Spacer(modifier = Modifier.height(16.dp))

            // DatePickerDialog
            ApplicationDatePicker(
                state = dateState,
                isOpen = datePickerExpanded,
                onDismissRequest = { datePickerExpanded = false },
                onConfirmButtonClicked = { datePickerExpanded = false })

            // Resume Dropdown
            DropdownMenu(
                options = resumeOptions,
                selectedOption = selectedResume ?: "Select or Add Resume",
                onSelectionChange = { resume ->
                    if (resume == "Add a Resume") {
                        onNavigateToResume()
                    } else {
                        selectedResume = resume
                    }
                },
                label = "Resume Used"
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Status Dropdown
            DropdownMenu(
                options = ApplicationStatus.values().map { it.displayName },
                selectedOption = selectedStatus,
                onSelectionChange = { status ->
                    selectedStatus = status
                },
                label = "Application Status",
            )
            Spacer(modifier = Modifier.height(16.dp))

            //Buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = {
                        dateState.setSelection(Instant.now().toEpochMilli())
                        selectedResume = null
                        selectedStatus = ApplicationStatus.APPLIED.displayName
                    },
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(text = "Reset")
                }

                // create a new application and update to db in applicationViewModel

                Button(
                    onClick = {
                        val localSelectedResume = selectedResume

                        // Find the actual ResumeModel
                        val actualResume = resumeViewModel.resumeList
                            .firstOrNull { it.nickName == localSelectedResume }

                        if (localSelectedResume == null || actualResume == null) {
                            // Ask the user to select a resume
                            Toast.makeText(
                                context,
                                "Please select a resume before submitting.",
                                Toast.LENGTH_LONG
                            ).show()
                        } else {
                            if (job != null) {
                                newApplication = JobApplicationModel(job = job!!)
                            }
                            val newEvent: Event
                            if (selectedStatus.isNotBlank()) {
                                newEvent = Event(
                                    date = millisToDate(dateState.selectedDateMillis!!),
                                    status = selectedStatus
                                )
                            } else {
                                newEvent = Event(
                                    date = millisToDate(dateState.selectedDateMillis!!),
                                    status = ApplicationStatus.APPLIED.displayName
                                )
                            }
                            eventList.add(newEvent)
                            val timeLine = TimeLine(results = eventList, count = eventList.size)
                            newApplication.timeLine = timeLine
                            newApplication.status = timeLine.results.first().status
                            newApplication.resume = actualResume
                            applicationViewModel.setJobApplicationToDB(
                                job!!,
                                actualResume,
                                timeLine
                            )
                            applicationViewModel.selectApplication(newApplication)
                            onNavigateToApplicationDetails()
                            //navController.navigateUp()
                        }
                    },
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(text = "Save")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownMenu(
    options: List<String>,
    selectedOption: String,
    onSelectionChange: (String) -> Unit,
    label: String
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        OutlinedTextField(
            value = selectedOption,
            onValueChange = { /* ReadOnly, no action needed */ },
            readOnly = true,
            enabled = false,
            label = { Text(label) },
            trailingIcon = {
                Icon(
                    Icons.Filled.ArrowDropDown,
                    "drop-down icon",
                    Modifier.clickable { expanded = true })
            },
            modifier = Modifier.fillMaxWidth().clickable { expanded = true },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                disabledTextColor = MaterialTheme.colorScheme.onSurface,
                disabledBorderColor = MaterialTheme.colorScheme.outline,
                disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant)
        )

        androidx.compose.material3.DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onSelectionChange(option)
                        expanded = false
                    })
            }
        }
    }
}

