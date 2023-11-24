package edu.northeastern.jetpackcomposev1.ui.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import edu.northeastern.jetpackcomposev1.models.job.JobModel
import edu.northeastern.jetpackcomposev1.viewmodels.JobViewModel

@Composable
fun JobDetailScreen(job: JobModel, modifier: Modifier = Modifier) {
    Text("Will jump to job details page")
}