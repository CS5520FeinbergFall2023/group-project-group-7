package edu.northeastern.jetpackcomposev1.application

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import edu.northeastern.jetpackcomposev1.models.application.Event
import edu.northeastern.jetpackcomposev1.models.job.JobModel
import edu.northeastern.jetpackcomposev1.ui.screens.ApplicationResumeInfo
import edu.northeastern.jetpackcomposev1.ui.sheets.EventUpdateSheet
import edu.northeastern.jetpackcomposev1.utility.convertDateTime
import edu.northeastern.jetpackcomposev1.utility.convertSalary
import edu.northeastern.jetpackcomposev1.viewmodels.ApplicationViewModel
import edu.northeastern.jetpackcomposev1.viewmodels.JobViewModel

@Composable
fun ApplicationDetailScreen(
    jobViewModel: JobViewModel,
    //resumeViewModel: ResumeViewModel,
    applicationViewModel: ApplicationViewModel,
    //onNavigateToJobDetail: () -> Unit,
    //onNavigateToResumeDetail: () -> Unit,
    //onNavigateToEventUpdate: () -> Unit
) {
    val selectedApplication by applicationViewModel.selectedApplication
    var application = selectedApplication
    var showEventUpdateSheet by rememberSaveable { mutableStateOf(false) }
    if (showEventUpdateSheet) {
        EventUpdateSheet(
            applicationViewModel = applicationViewModel,
            onCloseSheet = { showEventUpdateSheet = false }
        )
    }

    LazyColumn(modifier = Modifier.padding(horizontal = 8.dp)) {
        item {
            val job = application!!.job
            ApplicationDetailJobInfo(
                modifier = Modifier, jobViewModel, job, //onNavigateToJobDetail
            )
            ApplicationResumeInfo(modifier = Modifier.padding(4.dp), resume = application.resume)

            Row(){
                Text(
                    text = "Time Line",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(top = 8.dp)
                )
                Spacer(modifier = Modifier.weight(1f))
                AddEventFab(
                    modifier = Modifier.padding(top = 8.dp),
                    applicationViewModel = applicationViewModel,
                    setShowEventUpdate = { showEventUpdateSheet = it }
                )
            }
            if (application.timeLine.count==0) {
                Text(
                    text = "No events yet, add one now!",
                    color = MaterialTheme.colorScheme.secondary,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 8.dp)
                )
            } else{
                TimelineComp(
                    application.timeLine,
                    onDeleteClicked = { event ->
                        applicationViewModel.selectEvent(event)
                        applicationViewModel.updateEventToDB(application, event, Event("", ""))
                    },
                    onEditClicked = { event ->
                        applicationViewModel.selectEvent(event)
                        showEventUpdateSheet = true
                    },
                )}
        }
    }
}

@Composable
fun ApplicationDetailJobInfo(
    modifier: Modifier,
    jobViewModel: JobViewModel,
    job: JobModel,
) {
    Column(
        modifier = modifier
            .padding(8.dp)
            .clickable(onClick = {
                jobViewModel.selectJob(job)
                //onNavigateToJobDetail()
            })
    ) {
        Text(
            text = job.title,
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = modifier.height(4.dp))
        Text(
            text = job.company.display_name,
            color = MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = modifier.height(4.dp))
        Text(
            text = job.location.display_name,
            color = MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.bodyMedium
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Surface(
                shape = MaterialTheme.shapes.small,
                color = MaterialTheme.colorScheme.surfaceVariant,
                modifier = modifier.padding(end = 4.dp)
            ) {
                Text(
                    text = job.contract_time.replaceFirstChar { it.uppercase() },
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = modifier.padding(all = 4.dp)
                )
            }
            Surface(
                modifier = modifier.padding(start = 4.dp),
                shape = MaterialTheme.shapes.small,
                color = MaterialTheme.colorScheme.surfaceVariant
            ) {
                Text(
                    text = convertSalary(job.salary_is_predicted, job.salary_min, job.salary_max),
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyMedium,
                    fontStyle = FontStyle.Italic,
                    modifier = modifier.padding(all = 4.dp)
                )
            }
        }
        Text(
            text = convertDateTime(job.created),
            color = MaterialTheme.colorScheme.tertiary,
            style = MaterialTheme.typography.labelSmall
        )
        Divider(modifier = modifier.padding(vertical = 4.dp))
    }
}

@Composable
fun AddEventFab(
    modifier: Modifier,
    applicationViewModel: ApplicationViewModel,
    setShowEventUpdate: (Boolean) -> Unit
) {
    SmallFloatingActionButton(
        onClick = {
            //create a new event when old event is empty
            applicationViewModel.selectEvent(Event("", ""))
            setShowEventUpdate(true)
        })
    { Icon(Icons.Filled.Add, "Add Event.") }
}
