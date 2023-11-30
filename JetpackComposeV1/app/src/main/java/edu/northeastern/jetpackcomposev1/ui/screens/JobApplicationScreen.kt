package edu.northeastern.jetpackcomposev1.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

import edu.northeastern.jetpackcomposev1.viewmodels.ApplicationViewModel

@Composable
fun JobApplicationScreen(
    applicationViewModel: ApplicationViewModel,
    modifier: Modifier = Modifier,
    onNavigateToApplicationDetail: () -> Unit,
) {

    val applicationList = applicationViewModel.jobApplicationList.toList()


    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Job Applications applicationList size: ${applicationList.size}",
        )
        Spacer(modifier = Modifier.height(8.dp))
        if (applicationList.isEmpty()) {
            Text(
                text = "No Job Applications",
            )
        } else {
            LazyColumn() {
                applicationList.forEach { application ->
                    item {
                        OutlinedCard(
                            modifier = Modifier
                                .padding(8.dp)
                                .clickable {
                                    applicationViewModel.selectApplication(application)
                                    onNavigateToApplicationDetail()
                                }
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = application.job.title,
                                )
                                Text(
                                    text = application.job.company.display_name,
                                )
                                Text(
                                    text = application.job.location.display_name,
                                )
                                Text(
                                   
                                    text = application.job.description,
                                )
                                Text(text = application.resume.nickName)
                                Text(text = application.timeLine.results[0].status)

                            }
                        }
                    }
                }
            }
        }
    }

}


