package edu.northeastern.jetpackcomposev1.ui.screens

import android.app.job.JobInfo
import androidx.compose.runtime.Composable
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import edu.northeastern.jetpackcomposev1.models.Application.Event
import edu.northeastern.jetpackcomposev1.models.Application.TimeLine
import edu.northeastern.jetpackcomposev1.models.job.ApplicationStatus
import edu.northeastern.jetpackcomposev1.models.job.JobApplicationModel
import edu.northeastern.jetpackcomposev1.models.job.JobModel
import edu.northeastern.jetpackcomposev1.models.resume.ResumeModel
import edu.northeastern.jetpackcomposev1.navigation.Screens
import edu.northeastern.jetpackcomposev1.utility.dateToMillis
import edu.northeastern.jetpackcomposev1.utility.getCurrentZonedDateTime
import edu.northeastern.jetpackcomposev1.utility.millisToDate
import edu.northeastern.jetpackcomposev1.viewmodels.ApplicationViewModel
import edu.northeastern.jetpackcomposev1.viewmodels.JobViewModel
import edu.northeastern.jetpackcomposev1.viewmodels.ResumeViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateNewApplicationScreen(
    jobViewModel: JobViewModel,
    applicationViewModel: ApplicationViewModel,
    resumeViewModel: ResumeViewModel,
    onNavigateToApplicationDetail: () -> Unit,
) {
    //job id passed from job detail screen
    val job by jobViewModel.selectedJob
    var newApplication = JobApplicationModel()
    var selectedResume by remember { mutableStateOf("") }
    var selectedStatus by remember { mutableStateOf("") }
    //Default date is today
    val today = dateToMillis(getCurrentZonedDateTime())
    val date by remember { mutableLongStateOf(today) }
    val dateState = rememberDatePickerState(initialSelectedDateMillis = date)
    var eventList = mutableListOf<Event>()
    var resumeExpanded by remember { mutableStateOf(false) }
    var statusExpanded by remember { mutableStateOf(false) }


    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Create New Application Record")
        //Display job info
        //Todo: format it to a better display
        JobInfo(job)
        //Choose Resume

        Box(modifier = Modifier.padding(30.dp)) {
            ExposedDropdownMenuBox(
                expanded = resumeExpanded,
                onExpandedChange = { resumeExpanded = !resumeExpanded },

                ) {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),

                    readOnly = false,
                    value = selectedResume,
                    onValueChange = {
                        selectedResume = it
                    },
                    label = { Text("Resume") },

                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = resumeExpanded)
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    singleLine = true
                )

                ExposedDropdownMenu(
                    expanded = resumeExpanded,
                    onDismissRequest = { resumeExpanded = false }
                ) {
                    //TODO: replace with actual resume list
                    val resumeList = listOf(
                        "Software Engineer",
                        "Full Stack Developer",
                        "BackEnd Developer",
                        "FrontEnd Developer"
                    )

                    resumeList.forEach { selectedOption ->
                        DropdownMenuItem(
                            text = { Text(selectedOption) },
                            onClick = {
                                selectedResume = selectedOption
                                resumeExpanded = false
                            },
                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                        )
                    }
                }
            }

        }

        //Choose Date
        DatePicker(state = dateState, modifier = Modifier.padding(16.dp))
        //Choose state
        Box(modifier = Modifier.padding(30.dp)) {
            ExposedDropdownMenuBox(
                expanded = statusExpanded,
                onExpandedChange = { statusExpanded = !statusExpanded },
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
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = statusExpanded)
                    },

                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    singleLine = true
                )

                ExposedDropdownMenu(
                    expanded = statusExpanded,
                    onDismissRequest = { statusExpanded = false }
                ) {
                    ApplicationStatus.values().forEach { selectedOption ->
                        DropdownMenuItem(
                            text = { Text(selectedOption.displayName) },
                            onClick = {
                                selectedStatus = selectedOption.displayName
                                statusExpanded = false
                            },
                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                        )
                    }
                }
            }

        }


        //Buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = { /*TODO*/},
                modifier = Modifier.padding(16.dp)
            ) {
                Text(text = "Cancel")
            }
            // create a new application and update to db in applicationViewModel

            Button(
                onClick = {
                    if (job != null) {
                        newApplication = JobApplicationModel(job = job!!)
                    }
                    //Todo: replace with actual resume list
                    val resume = ResumeModel("1",selectedResume, "Resume1")
                    val newEvent: Event
                    if (selectedStatus.isNotBlank()) {
                        newEvent = Event(date = millisToDate(dateState.selectedDateMillis!!), status = selectedStatus)
                    }else{
                        newEvent = Event(date = millisToDate(dateState.selectedDateMillis!!), status = ApplicationStatus.APPLIED.displayName)
                    }
                    eventList.add(newEvent)
                    val timeLine = TimeLine(results = eventList, count = eventList.size)
                    //Todo: update the application or create a new application and update to db in applicationViewModel
                    applicationViewModel.setJobApplicationToDB(job!!, resume, timeLine)
                    onNavigateToApplicationDetail()
                },
                modifier = Modifier.padding(16.dp)
            ) {
                Text(text = "Save")
            }
        }

    }
}

@Composable
fun JobInfo(job: JobModel?) {
    if (job != null) {
        Text(text = job.title)
        Text(text = job.company.display_name)
        Text(text = job.location.display_name)
        Log.d("CreateNewApplication", "CreateNewApplication:get a job  ${job.title}")
    }
}

