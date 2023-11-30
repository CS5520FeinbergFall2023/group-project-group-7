package edu.northeastern.jetpackcomposev1.application

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import edu.northeastern.jetpackcomposev1.viewmodels.ApplicationViewModel


@Composable
fun ApplicationDetailScreen(
    viewModel: ApplicationViewModel,
    onNavigateToUpdate: () -> Unit
) {
    val selectedApplication by viewModel.selectedApplication
    var application = selectedApplication

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(text = "Application Detail")
        //Text(text = "Application timeline ${application?.timeLine?.results.toString()}")

        if (application != null) {
            Text(text = application.job.title)
            Text(text = application.job.company.display_name)
            Text(text = application.job.location.display_name)
            Text(
                text = application.job.description,
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
            )
            Text(text = application.resume.nickName)
            FloatingActionButton(
                onClick = onNavigateToUpdate
            )
            {
                Icon(Icons.Filled.Add, "Extended floating action button.")
            }
            TimelineComp(
                application.timeLine,
                onDeleteClicked = { event ->
                    viewModel.selectEvent(event)
                    //viewModel.deleteEvent(application, event)
                },
                onEditClicked = { event ->
                    viewModel.selectEvent(event)
                    onNavigateToUpdate
                },
            )

        }
    }
}





