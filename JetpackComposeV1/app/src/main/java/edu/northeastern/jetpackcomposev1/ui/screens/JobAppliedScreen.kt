package edu.northeastern.jetpackcomposev1.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import edu.northeastern.jetpackcomposev1.viewmodels.JobViewModel

@Composable
fun JobAppliedScreen(jobViewModel: JobViewModel, modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier.padding(vertical = 4.dp)) {
        item { JobLists(jobViewModel.response.results) }
    }
}