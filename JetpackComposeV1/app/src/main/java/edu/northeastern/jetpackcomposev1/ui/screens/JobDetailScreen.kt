package edu.northeastern.jetpackcomposev1.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import edu.northeastern.jetpackcomposev1.models.job.JobModel
import edu.northeastern.jetpackcomposev1.viewmodels.ApplicationViewModel
import edu.northeastern.jetpackcomposev1.viewmodels.JobViewModel

@Composable
fun JobDetailScreen(
    jobViewModel: JobViewModel,
    onNavigateToApply: () -> Unit,
    onNavigateToApplicationUpdate: () -> Unit,)
{
    //get the selected job from jobViewModel
    //pass the selected job to apply screen and application update screen
    val job =  jobViewModel.selectedJob.value
    if(job !=null){
        Log.d("JobDetailScreen", "JobDetailScreen: get a job ${job.title}")
        Column(modifier = Modifier.fillMaxSize()
            .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center) {
            Text(text = "Job Detail")

            Text(text = job.title)
            Text(text = job.contract_time)
            Text(text = job.location.display_name)
            Text(text = job.created)
            Text(text = job.company.display_name)
            Text(text = job.salary_min.toString())
            Text(text = job.salary_max.toString())

            Text(text = job.description,
                modifier = Modifier
                    .padding(top = 16.dp)
                    .verticalScroll(rememberScrollState()),
                textAlign = TextAlign.Justify)
        }
        Row(){
            Button(
                onClick = onNavigateToApply,
                modifier = Modifier.padding(16.dp)
            ) {
                Text(text = "Apply")
            }
            IconButton(
                onClick = {},
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(Icons.Default.FavoriteBorder, contentDescription = null)

            }
            Button(
                onClick = {
                    onNavigateToApplicationUpdate()},
                modifier = Modifier.padding(16.dp)
            ) {
                Text(text = "Record Application")
            }

        }
    }

}