package edu.northeastern.jetpackcomposev1.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import edu.northeastern.jetpackcomposev1.ui.sheets.DetailJobSheet
import edu.northeastern.jetpackcomposev1.ui.sheets.FilterJobSheet
import edu.northeastern.jetpackcomposev1.ui.sheets.SearchJobSheet
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
    modifier: Modifier = Modifier,
    onNavigateToJobDetail: () -> Unit,

    ) {
    if (jobViewModel.running) {
        ShowCircularProgressIndicator()
    }
    else {
        LazyColumn(modifier = modifier.padding(horizontal = 8.dp)) {
            item {
                SearchSection(jobViewModel = jobViewModel)
                JobLists(
                    jobs = jobViewModel.response.results,
                    jobViewedHistoryList = jobViewModel.jobViewedHistoryList,
                    jobApplicationList = applicationViewModel.jobApplicationList,
                    onSetJobViewedHistory = { jobId -> jobViewModel.setJobViewedHistoryToDB(jobId) },
                    onFindJobInFavorite = { jobId -> jobViewModel.findJobInFavoriteList(jobId) },
                    onSetJobFavorite = {job -> jobViewModel.setJobFavoriteToDB(job) },
                    onNavigateToJobDetail = onNavigateToJobDetail,
                    jobViewModel = jobViewModel
                )
            }
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun PreviewSearchSection() {
//    JetpackComposeV1Theme {
//        SearchSection(count = 39, onNavigateToSearchJobInput = { })
//    }
//}

@Composable
fun SearchSection(
    jobViewModel: JobViewModel,
    modifier: Modifier = Modifier
) {
    // sub sheets are here
    var showSearchJobSheet by rememberSaveable { mutableStateOf(false) }
    if (showSearchJobSheet) {
        SearchJobSheet(
            jobViewModel = jobViewModel,
            onCloseSheet = { showSearchJobSheet = false }
        )
    }
    var showFilterJobSheet by rememberSaveable { mutableStateOf(false) }
    if (showFilterJobSheet) {
        FilterJobSheet(
            jobViewModel = jobViewModel,
            onCloseSheet = { showFilterJobSheet = false }
        )
    }
    // content is here
    Column(modifier = modifier.padding(top = 8.dp)) {
        OutlinedCard(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onPrimary),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        ) {
            Column(modifier = modifier
                .fillMaxWidth()
                .padding(all = 16.dp)
                .clickable { showSearchJobSheet = true }
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Outlined.Search,
                        contentDescription = "Search"
                    )
                    Text(
                        text = jobViewModel.search.what.ifEmpty { "Find a job that interests you" },
                        maxLines = 1,
                        modifier = modifier.padding(start = 8.dp)
                    )
                }
                Spacer(modifier = modifier.height(16.dp))
                Divider()
                Spacer(modifier = modifier.height(16.dp))
                Row {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = modifier.weight(0.7f)) {
                        Icon(
                            imageVector = Icons.Outlined.Place,
                            contentDescription = "Location"
                        )
                        Text(
                            text = jobViewModel.search.where.ifEmpty { "Any work location" },
                            maxLines = 1,
                            modifier = modifier.padding(start = 8.dp)
                        )
                    }
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = modifier.weight(0.3f)) {
                        Icon(
                            imageVector = Icons.Outlined.Home,
                            contentDescription = "Location"
                        )
                        Text(
                            text = "${jobViewModel.search.distance} km",
                            maxLines = 1,
                            modifier = modifier.padding(start = 8.dp)
                        )
                    }
                }
            }
        }
        Spacer(modifier = modifier.height(4.dp))
        LazyRow(verticalAlignment = Alignment.CenterVertically) {
            item { IconButton(onClick = { showFilterJobSheet = true }) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_filter_list_24),
                    contentDescription = "Filter",
                    tint = MaterialTheme.colorScheme.outline
                )
            } }
            item { Button(onClick = { /*TODO*/ }) { Text("Other Button") } }
            item { Button(onClick = { /*TODO*/ }) { Text("Other Button") } }
            item { Button(onClick = { /*TODO*/ }) { Text("Other Button") } }
        }
        Spacer(modifier = modifier.height(4.dp))
        Text(
            text = convertNumberOfJobs(jobViewModel.response.count),
            color = MaterialTheme.colorScheme.tertiary,
            style = MaterialTheme.typography.labelSmall,
            modifier = modifier.padding(start = 8.dp)
        )
    }
}

@Composable
fun JobLists(
    jobs: List<JobModel>,
    jobViewedHistoryList: SnapshotStateList<JobViewedHistoryModel>,
    jobApplicationList: SnapshotStateList<JobApplicationModel>,
    onSetJobViewedHistory: (String) -> Unit,
    onFindJobInFavorite: (String) -> Boolean,
    onSetJobFavorite: (JobModel) -> Unit,
    modifier: Modifier = Modifier,
    onNavigateToJobDetail: () -> Unit,
    jobViewModel: JobViewModel
) {
    Spacer(modifier = modifier.height(4.dp))
    jobs.forEach { job ->
        JobCard(
            jobViewedHistoryList = jobViewedHistoryList,
            jobApplicationList = jobApplicationList,
            job = job,
            onSetJobViewedHistory = onSetJobViewedHistory,
            onFindJobInFavorite = onFindJobInFavorite,
            onSetJobFavorite = onSetJobFavorite,
            onNavigateToJobDetail = onNavigateToJobDetail,
            jobViewModel = jobViewModel
        )
    }
    Spacer(modifier = modifier.height(4.dp))
}

@Composable
fun JobCard(
    jobViewedHistoryList: SnapshotStateList<JobViewedHistoryModel>,
    jobApplicationList: SnapshotStateList<JobApplicationModel>,
    job: JobModel,
    onSetJobViewedHistory: (String) -> Unit,
    onFindJobInFavorite: (String) -> Boolean,
    onSetJobFavorite: (JobModel) -> Unit,
    modifier: Modifier = Modifier,
    onNavigateToJobDetail: () -> Unit,
    jobViewModel: JobViewModel
) {
    OutlinedCard(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onPrimary),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        modifier = modifier.padding(vertical = 4.dp)

    ) {
        Column(modifier = modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp)) {
            CardHead(
                jobViewedHistoryList = jobViewedHistoryList,
                jobApplicationList = jobApplicationList,
                job = job
            )
            JobContent(
                job = job,
                onSetJobViewedHistory = onSetJobViewedHistory,
                onNavigateToJobDetail = onNavigateToJobDetail,
                jobViewModel = jobViewModel
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
    modifier: Modifier = Modifier,
    onNavigateToJobDetail: () -> Unit,
    jobViewModel: JobViewModel
) {
    var showDetailJobSheet by rememberSaveable { mutableStateOf(false) }
    if (showDetailJobSheet) {
        DetailJobSheet(
            job = job,
            onCloseSheet = { showDetailJobSheet = false }
        )
    }
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                onSetJobViewedHistory(job.id)
                //TOdo: jump to detail page here
                showDetailJobSheet = true
                jobViewModel.selectJob(job)
                onNavigateToJobDetail()
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
                color = MaterialTheme.colorScheme.surfaceVariant
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
                Surface(
                    shape = MaterialTheme.shapes.small,
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.error),
                    modifier = modifier.padding(bottom = 4.dp)
                ) {
                    Text(
                        text = "New",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.labelSmall,
                        modifier = modifier.padding(all = 4.dp)
                    )
                }
            }
        }
        Row(modifier = modifier.weight(0.5f), horizontalArrangement = Arrangement.End) {
            if(jobApplicationList.any { it.id == job.id } ) {
                Text(
                    text = "Applied",
                    color = MaterialTheme.colorScheme.tertiary,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = modifier.padding(vertical = 6.dp)
                )
            } else if(jobViewedHistoryList.any { it.id == job.id } ) {
                Text(
                    text = "Viewed",
                    color = MaterialTheme.colorScheme.tertiary,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = modifier.padding(vertical = 6.dp)
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
        IconButton(onClick = { /*TODO apply the job*/ }) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_content_paste_24),
                contentDescription = "Apply",
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

//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun SearchBottomSheet(onShowSheet: (Boolean) -> Unit) {
//    val sheetState = rememberModalBottomSheetState()
//    ModalBottomSheet(
//        onDismissRequest = { onShowSheet(false) },
//        sheetState = sheetState,
//    ) {
//        // Sheet content
//        Column(modifier = Modifier.fillMaxHeight().padding(bottom = 24.dp)) {
//            Text("Hide bottom sheet")
//            Text("Hide bottom sheet")
//            Text("Hide bottom sheet")
//            Text("Hide bottom sheet")
//            Text("Hide bottom sheet")
//        }
//    }
//}