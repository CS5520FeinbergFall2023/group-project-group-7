package edu.northeastern.jetpackcomposev1.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import edu.northeastern.jetpackcomposev1.viewmodels.ApplicationViewModel
import edu.northeastern.jetpackcomposev1.viewmodels.JobViewModel

@Composable
fun JobApplicationScreen(applicationViewModel: ApplicationViewModel, modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier.padding(vertical = 4.dp)) {
        item{ Text("Here is the application page") }
    }
}