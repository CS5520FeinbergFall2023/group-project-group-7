package edu.northeastern.jetpackcomposev1.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import edu.northeastern.jetpackcomposev1.R
import edu.northeastern.jetpackcomposev1.models.job.JobApplicationModel
import edu.northeastern.jetpackcomposev1.models.job.JobFavoriteModel
import edu.northeastern.jetpackcomposev1.models.job.JobModel
import edu.northeastern.jetpackcomposev1.models.job.JobViewedHistoryModel
import edu.northeastern.jetpackcomposev1.viewmodels.JobViewModel
import edu.northeastern.jetpackcomposev1.ui.theme.JetpackComposeV1Theme
import edu.northeastern.jetpackcomposev1.utility.checkIfNew
import edu.northeastern.jetpackcomposev1.utility.convertDateTime
import edu.northeastern.jetpackcomposev1.utility.convertNumberOfJobs
import edu.northeastern.jetpackcomposev1.utility.convertSalary
import edu.northeastern.jetpackcomposev1.viewmodels.ApplicationViewModel

@Composable
fun JobSearchScreen(
    jobViewModel: JobViewModel,
    applicationViewModel: ApplicationViewModel,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier.padding(vertical = 4.dp)) {
        item { SearchSection(jobViewModel.response.count) }
        item { JobLists(
            jobs = jobViewModel.response.results,
            jobViewedHistoryList = jobViewModel.jobViewedHistoryList,
            jobApplicationList = applicationViewModel.jobApplicationList,
            onSetJobViewedHistory = { jobId -> jobViewModel.setJobViewedHistoryToDB(jobId) },
            onFindJobInFavorite = { jobId -> jobViewModel.findJobInFavoriteList(jobId) },
            onSetJobFavorite = {job -> jobViewModel.setJobFavoriteToDB(job) }
        ) }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchSection(count: Int, modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(vertical = 4.dp, horizontal = 8.dp)) {
        OutlinedTextField(
            value = "Not a real search bar",
            onValueChange = {  },
            label = { Text("Search") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            singleLine = true
        )
        OutlinedTextField(
            value = "Not a real search bar",
            onValueChange = {  },
            label = { Text("Location") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            singleLine = true
        )
        LazyRow(verticalAlignment = Alignment.CenterVertically) {
            item { IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_filter_list_24),
                    contentDescription = "Filter",
                    tint = MaterialTheme.colorScheme.outline
                )
            } }
            item { Button(onClick = { /*TODO*/ }) { Text("Other Button") } }
            item { Button(onClick = { /*TODO*/ }) { Text("Other Button") } }
            item { Button(onClick = { /*TODO*/ }) { Text("Other Button") } }
            item { Button(onClick = { /*TODO*/ }) { Text("Other Button") } }
            item { Button(onClick = { /*TODO*/ }) { Text("Other Button") } }
            item { Button(onClick = { /*TODO*/ }) { Text("Other Button") } }
        }
        Text(
            text = convertNumberOfJobs(count),
            color = MaterialTheme.colorScheme.tertiary,
            style = MaterialTheme.typography.labelSmall,
            modifier = modifier.padding(start = 16.dp)
        )
    }
}

//// we can preview individual UI also
//@Preview(showBackground = true)
//@Composable
//fun PreviewJobSearchScreen(){
//    JetpackComposeV1Theme {
//        JobSearchScreen(viewModel())
//    }
//}

@Composable
fun JobLists(
    jobs: List<JobModel>,
    jobViewedHistoryList: SnapshotStateList<JobViewedHistoryModel>,
    jobApplicationList: SnapshotStateList<JobApplicationModel>,
    onSetJobViewedHistory: (String) -> Unit,
    onFindJobInFavorite: (String) -> Boolean,
    onSetJobFavorite: (JobModel) -> Unit,
    modifier: Modifier = Modifier
) {
    jobs.forEach { job -> // TODO: check order after add or delete
        JobCard(
            jobViewedHistoryList = jobViewedHistoryList,
            jobApplicationList = jobApplicationList,
            job = job,
            onSetJobViewedHistory = onSetJobViewedHistory,
            onFindJobInFavorite = onFindJobInFavorite,
            onSetJobFavorite = onSetJobFavorite
        )
    }
}

@Composable
fun JobCard(
    jobViewedHistoryList: SnapshotStateList<JobViewedHistoryModel>,
    jobApplicationList: SnapshotStateList<JobApplicationModel>,
    job: JobModel,
    onSetJobViewedHistory: (String) -> Unit,
    onFindJobInFavorite: (String) -> Boolean,
    onSetJobFavorite: (JobModel) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedCard(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onPrimary),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        modifier = modifier.padding(vertical = 4.dp, horizontal = 8.dp)
    ) {
        Column(modifier = modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp)) {
            CardHead(
                jobViewedHistoryList = jobViewedHistoryList,
                jobApplicationList = jobApplicationList,
                job = job
            )
            JobContent(
                job = job,
                onSetJobViewedHistory = onSetJobViewedHistory
            )
            CardFoot(
                onFindJobInFavorite = onFindJobInFavorite,
                onSetJobFavorite = onSetJobFavorite,
                job = job
            )
        }
    }
}

@Composable
fun JobContent(
    job: JobModel,
    onSetJobViewedHistory: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(start = 8.dp)
            .fillMaxWidth()
            .clickable {
                onSetJobViewedHistory(job.id)
                //TODO add jump to detail page later, i will pass this job to you

            }
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
        Spacer(modifier = modifier.height(4.dp))
        Row {
            Surface(
                shape = MaterialTheme.shapes.small,
                color = MaterialTheme.colorScheme.surfaceVariant,
                shadowElevation = 1.dp
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
                color = MaterialTheme.colorScheme.surfaceVariant,
                shadowElevation = 1.dp
            ) {
                Text(
                    text = convertSalary(job.salary_min, job.salary_max),
                    color = MaterialTheme.colorScheme.primary,
                    fontStyle = FontStyle.Italic,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = modifier.padding(all = 4.dp)
                )
            }
        }
        Spacer(modifier = modifier.height(8.dp))
        Text(
            text = convertDateTime(job.created),
            color = MaterialTheme.colorScheme.tertiary,
            style = MaterialTheme.typography.labelSmall
        )
    }
}

@Composable
fun CardHead(
    jobViewedHistoryList: SnapshotStateList<JobViewedHistoryModel>,
    jobApplicationList: SnapshotStateList<JobApplicationModel>,
    job: JobModel,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth()
    ) {
        Row(modifier = modifier.weight(0.5f)) {
            if(checkIfNew(job.created)) {
                Icon(
                    painter = painterResource(id = R.drawable.outline_fiber_new_24),
                    contentDescription = "New",
                    tint = MaterialTheme.colorScheme.error,
                    modifier = modifier.padding(start = 8.dp)
                )
            }
        }
        Row(modifier = modifier.weight(0.5f), horizontalArrangement = Arrangement.End) {
            if(jobApplicationList.any { it.id == job.id } ) {
                Text(
                    text = "Applied",
                    color = MaterialTheme.colorScheme.tertiary,
                    style = MaterialTheme.typography.labelSmall
                )
            } else if(jobViewedHistoryList.any { it.id == job.id } ) {
                Text(
                    text = "Viewed",
                    color = MaterialTheme.colorScheme.tertiary,
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
    }
}

@Composable
fun CardFoot(
    job: JobModel,
    onFindJobInFavorite: (String) -> Boolean,
    onSetJobFavorite: (JobModel) -> Unit,
    modifier: Modifier = Modifier
) {
    val outlineIcon = R.drawable.outline_bookmark_border_24
    val fillIcon = R.drawable.baseline_bookmark_24
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier.fillMaxWidth()
    ) {
        IconButton(onClick = { onSetJobFavorite(job) }) {
            Icon(
                painter = painterResource(id = if (onFindJobInFavorite(job.id)) fillIcon else outlineIcon),
                contentDescription = "Saved",
                tint = MaterialTheme.colorScheme.outline
            )
        }
        IconButton(onClick = { /*TODO share the job*/ }) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_share_24),
                contentDescription = "Share",
                tint = MaterialTheme.colorScheme.outline
            )
        }
    }
}