package edu.northeastern.jetpackcomposev1.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import edu.northeastern.jetpackcomposev1.models.job.JobApplicationModel
import edu.northeastern.jetpackcomposev1.models.job.JobModel
import edu.northeastern.jetpackcomposev1.models.resume.ResumeModel
import edu.northeastern.jetpackcomposev1.utility.convertDateTime
import edu.northeastern.jetpackcomposev1.utility.convertSalary
import edu.northeastern.jetpackcomposev1.viewmodels.ApplicationViewModel


@Composable
fun JobApplicationScreen(
    applicationViewModel: ApplicationViewModel,
    modifier: Modifier = Modifier,
    onNavigateToApplicationDetail: () -> Unit,
) {
    val applicationList = applicationViewModel.jobApplicationList.toList()
    if (applicationList.isEmpty()) {
        Text(text = "No Application Found")
    } else {
        LazyColumn(modifier = modifier.padding( 4.dp)) {
            item {
                ApplicationList(
                    applications = applicationList,
                    modifier = modifier,
                    onNavigateToApplicationDetail = onNavigateToApplicationDetail,
                    applicationViewModel = applicationViewModel
                )
            }
        }
    }
}

@Composable
fun ApplicationList(
    applications: List<JobApplicationModel>,
    modifier: Modifier,
    onNavigateToApplicationDetail: () -> Unit,
    applicationViewModel: ApplicationViewModel
) {
    Spacer(modifier = modifier.height(4.dp))
    applications.forEach { application ->
        ApplicationCard(
            application = application,
            modifier = modifier,
            onNavigateToApplicationDetail = onNavigateToApplicationDetail,
            applicationViewModel = applicationViewModel
        )
    }
    Spacer(modifier = modifier.height(4.dp))
}
@Composable
fun ApplicationCard(
    application: JobApplicationModel,
    modifier: Modifier,
    onNavigateToApplicationDetail: () -> Unit,
    applicationViewModel: ApplicationViewModel
) {
    OutlinedCard(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onPrimary),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        modifier = modifier.padding(vertical = 4.dp)

    ) {
        Column(modifier = modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp)) {
            ApplicationContent(
                application = application,
                modifier = Modifier.padding(8.dp),
                onNavigateToApplicationDetail = onNavigateToApplicationDetail,
                applicationViewModel = applicationViewModel
            )
        }

    }
}

@Composable
fun ApplicationContent(
    application: JobApplicationModel,
    modifier: Modifier,
    onNavigateToApplicationDetail: () -> Unit,
    applicationViewModel: ApplicationViewModel
) {
    val latestEvent = application.timeLine.results[0]
    val job = application.job
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                applicationViewModel.selectApplication(application)
                onNavigateToApplicationDetail()
            },
        horizontalAlignment = Alignment.Start
    ) {
        //Job Info
        ApplicationJobInfo(modifier = modifier, job = job)

        //Resume Info
        ApplicationResumeInfo(modifier = modifier, resume = application.resume)

        //Timeline Info
        Text(
            text = "Last Update: ${latestEvent.date}",
            color = MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = "Current Status:  ${latestEvent.status}",
            color = MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = modifier.height(4.dp))

    }
}

@Composable
fun ApplicationJobInfo(modifier: Modifier, job: JobModel){
    Column(modifier= Modifier.padding(bottom = 4.dp)){
        Text(
            text = job.title,
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = job.company.display_name,
            color = MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.bodyMedium
        )
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
    }

    Spacer(modifier = Modifier.height(2.dp))

}

@Composable
fun ApplicationResumeInfo(modifier: Modifier, resume: ResumeModel){
    Text(
        text = "Resume:  ${resume.fileName}",
        color = MaterialTheme.colorScheme.secondary,
        style = MaterialTheme.typography.bodyMedium
    )
}
