package edu.northeastern.jetpackcomposev1.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import edu.northeastern.jetpackcomposev1.models.job.JobApplicationModel
import edu.northeastern.jetpackcomposev1.models.job.JobFavoriteModel
import edu.northeastern.jetpackcomposev1.models.job.JobModel
import edu.northeastern.jetpackcomposev1.models.job.JobViewedHistoryModel
import edu.northeastern.jetpackcomposev1.viewmodels.ApplicationViewModel
import edu.northeastern.jetpackcomposev1.viewmodels.JobViewModel

@Composable
fun JobFavoriteScreen(
    navController: NavHostController,
    jobViewModel: JobViewModel,
    applicationViewModel: ApplicationViewModel,
    modifier: Modifier = Modifier
) {
    if (jobViewModel.jobFavoriteList.isEmpty()) {
        Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) { Text("Save your first job") }
    }
    else {
        LazyColumn(modifier = modifier.padding(horizontal = 8.dp)) {
            item {
                FavoriteLists(
                    navController = navController,
                    jobFavoriteList = jobViewModel.jobFavoriteList,
                    jobViewedHistoryList = jobViewModel.jobViewedHistoryList,
                    jobApplicationList = applicationViewModel.jobApplicationList,
                    onSetJobViewedHistory = { jobId -> jobViewModel.setJobViewedHistoryToDB(jobId) },
                    onFindJobInFavorite = { jobId -> jobViewModel.findJobInFavoriteList(jobId) },
                    onSetJobFavorite = { job -> jobViewModel.setJobFavoriteToDB(job) }
                )
            }
        }
    }
}

@Composable
fun FavoriteLists(
    navController: NavHostController,
    jobFavoriteList: SnapshotStateList<JobFavoriteModel>,
    jobViewedHistoryList: SnapshotStateList<JobViewedHistoryModel>,
    jobApplicationList: SnapshotStateList<JobApplicationModel>,
    onSetJobViewedHistory: (String) -> Unit,
    onFindJobInFavorite: (String) -> Boolean,
    onSetJobFavorite: (JobModel) -> Unit,
    modifier: Modifier = Modifier
) {
    Spacer(modifier = modifier.height(4.dp))
    jobFavoriteList.forEachIndexed { index, jobFavorite ->
        JobCard(
            navController = navController,
            jobViewedHistoryList = jobViewedHistoryList,
            jobApplicationList = jobApplicationList,
            listName = "favorite",
            index = index,
            job = jobFavorite.job,
            onSetJobViewedHistory = onSetJobViewedHistory,
            onFindJobInFavorite = onFindJobInFavorite,
            onSetJobFavorite = onSetJobFavorite,
        )
    }
    Spacer(modifier = modifier.height(4.dp))
}