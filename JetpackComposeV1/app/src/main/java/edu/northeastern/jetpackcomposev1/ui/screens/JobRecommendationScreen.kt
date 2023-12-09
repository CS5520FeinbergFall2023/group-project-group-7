package edu.northeastern.jetpackcomposev1.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import edu.northeastern.jetpackcomposev1.models.job.JobApplicationModel
import edu.northeastern.jetpackcomposev1.models.job.JobModel
import edu.northeastern.jetpackcomposev1.models.job.JobViewedHistoryModel
import edu.northeastern.jetpackcomposev1.viewmodels.ApplicationViewModel
import edu.northeastern.jetpackcomposev1.viewmodels.JobViewModel

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun JobRecommendationScreen(
    jobViewModel: JobViewModel,
    applicationViewModel: ApplicationViewModel,
    onNavigateToJobDetail: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    if (applicationViewModel.jobRecommendationList.isEmpty()) {
        Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) { Text("Check back later") }
    }
    else {
        LazyColumn(modifier = modifier.padding(horizontal = 8.dp)) {
            item {
                RecommendationLists(
                    jobRecommendationList = applicationViewModel.jobRecommendationList,
                    jobViewedHistoryList = jobViewModel.jobViewedHistoryList,
                    jobApplicationList = applicationViewModel.jobApplicationList,
                    onSetJobViewedHistory = { jobId -> jobViewModel.setJobViewedHistoryToDB(jobId) },
                    onFindJobInFavorite = { jobId -> jobViewModel.findJobInFavoriteList(jobId) },
                    onSetJobFavorite = { job -> jobViewModel.setJobFavoriteToDB(job) },
                    onNavigateToJobDetail = onNavigateToJobDetail,
                    onFindJobInApplication = { jobId -> applicationViewModel.findJobInApplicationList(jobId) }
                )
            }
        }
    }
    AskForNotificationPermission()
}

@Composable
fun RecommendationLists(
    jobRecommendationList: SnapshotStateList<JobModel>,
    jobViewedHistoryList: SnapshotStateList<JobViewedHistoryModel>,
    jobApplicationList: SnapshotStateList<JobApplicationModel>,
    onSetJobViewedHistory: (String) -> Unit,
    onFindJobInFavorite: (String) -> Boolean,
    onSetJobFavorite: (JobModel) -> Unit,
    onNavigateToJobDetail: (Int) -> Unit,
    onFindJobInApplication: (String) -> Boolean,
    modifier: Modifier = Modifier
) {
    Spacer(modifier = modifier.height(4.dp))
    jobRecommendationList.forEachIndexed { index, job ->
        if (!onFindJobInApplication(job.id)) {
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
    Spacer(modifier = modifier.height(4.dp))
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun AskForNotificationPermission(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var hasNotificationPermission by rememberSaveable {
        mutableStateOf(ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED)
    }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            hasNotificationPermission = isGranted
        }
    )
    if(!hasNotificationPermission) {
        LaunchedEffect(key1 = null) {
            launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }
}