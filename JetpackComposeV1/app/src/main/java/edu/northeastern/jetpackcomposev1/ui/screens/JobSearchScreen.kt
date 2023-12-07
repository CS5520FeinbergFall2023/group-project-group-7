package edu.northeastern.jetpackcomposev1.ui.screens

import android.net.Uri
import android.content.Intent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material.icons.outlined.KeyboardArrowLeft
import androidx.compose.material.icons.outlined.KeyboardArrowRight
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import edu.northeastern.jetpackcomposev1.R
import edu.northeastern.jetpackcomposev1.models.job.JobApplicationModel
import edu.northeastern.jetpackcomposev1.models.job.JobModel
import edu.northeastern.jetpackcomposev1.models.job.JobViewedHistoryModel
import edu.northeastern.jetpackcomposev1.ui.sheets.FilterJobSheet
import edu.northeastern.jetpackcomposev1.ui.sheets.SearchJobSheet
import edu.northeastern.jetpackcomposev1.ui.sheets.QuickFilterJobSheet
import edu.northeastern.jetpackcomposev1.viewmodels.JobViewModel
import edu.northeastern.jetpackcomposev1.utility.checkIfNew
import edu.northeastern.jetpackcomposev1.utility.convertDateTime
import edu.northeastern.jetpackcomposev1.utility.convertNumberOfJobs
import edu.northeastern.jetpackcomposev1.utility.convertSalary
import edu.northeastern.jetpackcomposev1.utility.findContractTimeCode
import edu.northeastern.jetpackcomposev1.utility.findSalaryMinCode
import edu.northeastern.jetpackcomposev1.utility.findSortByCode
import edu.northeastern.jetpackcomposev1.viewmodels.ApplicationViewModel

@Composable
fun JobSearchScreen(
    jobViewModel: JobViewModel,
    applicationViewModel: ApplicationViewModel,
    onNavigateToJobDetail: (Int) -> Unit,
    modifier: Modifier = Modifier
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
                )
                TurnPage(jobViewModel = jobViewModel)
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
    var quickFilterIndex by rememberSaveable { mutableIntStateOf(0) }
    var showQuickFilterJobSheet by rememberSaveable { mutableStateOf(false) }
    if (showQuickFilterJobSheet) {
        QuickFilterJobSheet(
            index = quickFilterIndex,
            jobViewModel = jobViewModel,
            onCloseSheet = { showQuickFilterJobSheet = false }
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
                .padding(all = 12.dp)
                .clickable { showSearchJobSheet = true }
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Outlined.Search,
                        contentDescription = "Search"
                    )
                    Text(
                        text = jobViewModel.search.what.ifEmpty { "Any job" },
                        maxLines = 1,
                        modifier = modifier.padding(start = 8.dp)
                    )
                }
                Spacer(modifier = modifier.height(12.dp))
                Divider()
                Spacer(modifier = modifier.height(12.dp))
                Row {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = modifier.weight(0.7f)) {
                        Icon(
                            imageVector = Icons.Outlined.Place,
                            contentDescription = "Location"
                        )
                        Text(
                            text = jobViewModel.search.where.ifEmpty { "Any place" },
                            maxLines = 1,
                            modifier = modifier.padding(start = 8.dp)
                        )
                    }
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = modifier.weight(0.3f)) {
                        Icon(
                            imageVector = Icons.Outlined.Lock,
                            contentDescription = "Distance"
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
            item {
                OutlinedButton(
                    onClick = { showFilterJobSheet = true },
                    shape = MaterialTheme.shapes.medium
                ) {
                    Icon(painter = painterResource(id = R.drawable.baseline_filter_list_24), contentDescription = "Filter")
                }
                OutlinedButton(
                    onClick = {
                        quickFilterIndex = 1
                        showQuickFilterJobSheet = true },
                    shape = MaterialTheme.shapes.medium,
                    modifier = modifier.padding(start = 8.dp)
                ) {
                    val sortByCode = findSortByCode(jobViewModel.search.sort_by)
                    Text("Sort by: ${sortByCode?.displayName}")
                    Icon(imageVector = Icons.Outlined.ArrowDropDown, contentDescription = "Down arrow")
                }
                OutlinedButton(
                    onClick = {
                        quickFilterIndex = 2
                        showQuickFilterJobSheet = true },
                    shape = MaterialTheme.shapes.medium,
                    modifier = modifier.padding(start = 8.dp)
                ) {
                    val contractTimeCode = findContractTimeCode(jobViewModel.search.full_time, jobViewModel.search.part_time)
                    Text("Contract time: ${contractTimeCode?.displayName}")
                    Icon(imageVector = Icons.Outlined.ArrowDropDown, contentDescription = "Down arrow")
                }
                OutlinedButton(
                    onClick = {
                        quickFilterIndex = 3
                        showQuickFilterJobSheet = true },
                    shape = MaterialTheme.shapes.medium,
                    modifier = modifier.padding(start = 8.dp)
                ) {
                    val salaryMinCode = findSalaryMinCode(jobViewModel.search.salary_min)
                    Text("Minimum pay: ${salaryMinCode?.displayName}")
                    Icon(imageVector = Icons.Outlined.ArrowDropDown, contentDescription = "Down arrow")
                }
                OutlinedButton(
                    onClick = { quickFilterIndex = 4
                        showQuickFilterJobSheet = true },
                    shape = MaterialTheme.shapes.medium,
                    modifier = modifier.padding(start = 8.dp)
                ) {
                    Text("Date posted: last ${jobViewModel.search.max_days_old} days")
                    Icon(imageVector = Icons.Outlined.ArrowDropDown, contentDescription = "Down arrow")
                }
            }
        }
        Spacer(modifier = modifier.height(4.dp))
        Text(
            text = convertNumberOfJobs(jobViewModel.response.count),
            color = MaterialTheme.colorScheme.tertiary,
            style = MaterialTheme.typography.labelSmall,
            modifier = modifier.align(Alignment.CenterHorizontally)
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
    onNavigateToJobDetail: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Spacer(modifier = modifier.height(4.dp))
    jobs.forEachIndexed { index, job ->
        JobCard(
            jobViewedHistoryList = jobViewedHistoryList,
            jobApplicationList = jobApplicationList,
            job = job,
            onSetJobViewedHistory = onSetJobViewedHistory,
            onFindJobInFavorite = onFindJobInFavorite,
            onSetJobFavorite = onSetJobFavorite,
            onNavigateToJobDetail = { onNavigateToJobDetail(index) }
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
    onNavigateToJobDetail: () -> Unit,
    modifier: Modifier = Modifier
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
                onNavigateToJobDetail = onNavigateToJobDetail
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
    onNavigateToJobDetail: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                onSetJobViewedHistory(job.id)
                onNavigateToJobDetail()
            }
    ) {
        Text(
            text = job.title,
            //color = MaterialTheme.colorScheme.primary,
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
        Column(modifier = modifier.weight(0.5f)) {
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
        Column(modifier = modifier.weight(0.5f), horizontalAlignment = Alignment.End) {
            if(jobApplicationList.any { it.job.id == job.id } ) {
                Text(
                    text = "Applied",
                    color = MaterialTheme.colorScheme.error,
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
    val context = LocalContext.current
    val applyJobIntent = Intent(Intent.ACTION_VIEW, Uri.parse(job.redirect_url))
    val sendIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, job.redirect_url)
        type = "text/plain"
    }
    val shareJobIntent = Intent.createChooser(sendIntent, null)
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier.fillMaxWidth()
    ) {
        IconButton(onClick = { onSetJobFavorite(job) }) {
            Icon(
                painter = painterResource(id = if (onFindJobInFavorite(job.id)) fillIcon else outlineIcon),
                contentDescription = "Saved",
                tint = if (onFindJobInFavorite(job.id)) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
            )
        }
        IconButton(onClick = { context.startActivity(applyJobIntent) }) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_content_paste_24),
                contentDescription = "Apply",
                tint = MaterialTheme.colorScheme.outline
            )
        }
        IconButton(onClick = { context.startActivity(shareJobIntent) }) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_share_24),
                contentDescription = "Share",
                tint = MaterialTheme.colorScheme.outline
            )
        }
    }
}

@Composable
fun TurnPage(
    jobViewModel: JobViewModel,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = modifier.weight(0.5f)) {
            if (jobViewModel.search.page != 1) {
                TextButton(
                    onClick = {
                        jobViewModel.search.page--
                        jobViewModel.getJobFromAPI()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Outlined.KeyboardArrowLeft,
                        contentDescription = "Left arrow"
                    )
                    Text("Previous")
                }
            }
        }
        Column(modifier = modifier.weight(0.5f), horizontalAlignment = Alignment.End) {
            if (jobViewModel.search.page != (jobViewModel.response.count / jobViewModel.search.results_per_page + 1)) {
                TextButton(
                    onClick = {
                        jobViewModel.search.page++
                        jobViewModel.getJobFromAPI()
                    }
                ) {
                    Text("Next")
                    Icon(
                        imageVector = Icons.Outlined.KeyboardArrowRight,
                        contentDescription = "Right arrow"
                    )
                }
            }
        }
    }
    Spacer(modifier = modifier.height(4.dp))
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