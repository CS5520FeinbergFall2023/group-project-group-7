package edu.northeastern.jetpackcomposev1.ui.screens

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
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import edu.northeastern.jetpackcomposev1.models.job.JobApplicationModel
import edu.northeastern.jetpackcomposev1.models.resume.ResumeModel
import edu.northeastern.jetpackcomposev1.ui.sheets.EventUpdateScreen
import edu.northeastern.jetpackcomposev1.viewmodels.ApplicationViewModel
import edu.northeastern.jetpackcomposev1.viewmodels.JobViewModel


@Composable
fun ApplicationUpdateScreen(
    jobViewModel: JobViewModel,
    applicationViewModel: ApplicationViewModel,
    onCancel: () -> Unit,
    onNext: () -> Unit
) {
    val job by jobViewModel.selectedJob
    val application by applicationViewModel.selectedApplication
    val resume: ResumeModel? = application?.resume
    var newApplication = JobApplicationModel()

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Update Application Screen")
        //if job is not null, that we are creating a new application
        //if application is not null, that we are updating an existing application
        //Split the work into two screens create new and update existing
        if (job != null) {
            Text(text = job!!.title)
            Text(text = job!!.company.display_name)
            Text(text = job!!.location.display_name)
            Log.d("ApplicationUpdateScreen", "ApplicationUpdateScreen:get a job  ${job?.title}")

        } else if (application != null) {
            Text(text = application!!.job.title)
            Text(text = application!!.job.company.display_name)
            Text(text = application!!.job.location.display_name)
            Log.d(
                "ApplicationUpdateScreen",
                "ApplicationUpdateScreen: get an application ${application?.resume?.nickName}"
            )

        }
        //if resume is not null, that we are updating an existing application
        //else we are creating a new application

        ResumeInput(resume)
//        if (application != null) {
//            EventUpdateScreen(
//                applicationViewModel = applicationViewModel,
//                application = application!!
//            ) {}
//        } else {
//            EventUpdateScreen(
//                applicationViewModel = applicationViewModel,
//                application = newApplication
//            ) {}
//        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = { onCancel },
                modifier = Modifier.padding(16.dp)
            ) {
                Text(text = "Cancel")
            }
            //Todo: update the application or create a new application and update to db in applicationViewModel

            Button(
                onClick = { onNext },
                modifier = Modifier.padding(16.dp)
            ) {
                Text(text = "Next")
            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResumeInput(
    resume: ResumeModel? = null
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedResume by remember { mutableStateOf("") }
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
                value = selectedResume,
                onValueChange = { selectedResume = it },
                label = { Text("Resume") },

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
                            expanded = false
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                    )
                }
            }
        }

    }
}