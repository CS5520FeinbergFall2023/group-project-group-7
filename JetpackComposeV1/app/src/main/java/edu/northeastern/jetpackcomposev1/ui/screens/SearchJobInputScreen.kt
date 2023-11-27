package edu.northeastern.jetpackcomposev1.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import edu.northeastern.jetpackcomposev1.models.job.JobSearchHistoryModel
import edu.northeastern.jetpackcomposev1.ui.theme.JetpackComposeV1Theme
import edu.northeastern.jetpackcomposev1.viewmodels.JobViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun SearchJobInputScreen(
    jobViewModel: JobViewModel,
    onNavigateToJobSearch: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier
        .fillMaxSize()
        .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        SearchJobInput(jobViewModel = jobViewModel)
        SearchJobButton(
            onButtonClicked = { jobViewModel.getJobFromAPI() },
            onSetJobSearchHistory = { jobViewModel.setJobSearchHistoryToDB() },
            onNavigateToJobSearch = { onNavigateToJobSearch() }
        )
        SearchJobHistory(jobSearchHistoryList = jobViewModel.jobSearchHistoryList)
    }
}

//@Preview(showBackground = true)
//@Composable
//fun PreviewSearchJobInput() {
//    JetpackComposeV1Theme {
//        SearchJobInput()
//    }
//}

@Composable
fun SearchJobInput(
    jobViewModel: JobViewModel,
    modifier: Modifier = Modifier
) {
    Column {
        OutlinedTextField(
            modifier = modifier.padding(vertical = 4.dp).fillMaxWidth(),
            value = jobViewModel.search.what,
            onValueChange = { jobViewModel.search.what = it },
            label = { Text("Search") },
            leadingIcon = { Icon(imageVector = Icons.Outlined.Search, contentDescription = "Search") },
            supportingText = { Text("Multiple terms may be space separated") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            singleLine = false
        )
        Row(modifier = modifier.padding(vertical = 4.dp)) {
            OutlinedTextField(
                modifier = modifier.weight(0.7f),
                value = jobViewModel.search.where,
                onValueChange = { jobViewModel.search.where = it },
                label = { Text("Location") },
                leadingIcon = { Icon(imageVector = Icons.Outlined.Place, contentDescription = "Location") },
                supportingText = { Text("Place names, postal codes, etc") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                singleLine = true
            )
            OutlinedTextField(
                modifier = modifier.padding(start = 4.dp).weight(0.3f),
                value = jobViewModel.search.distance.toString(),
                onValueChange = { jobViewModel.search.distance = if(it.isNotEmpty()) it.toInt() else 0 },
                label = { Text("Distance") },
                suffix = { Text("km") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )
        }
    }
}

@Composable
fun SearchJobHistory(
    jobSearchHistoryList: SnapshotStateList<JobSearchHistoryModel>,
    modifier: Modifier = Modifier
) {
    LazyColumn {
        item { Text("Recent search") }
        items(
            items = jobSearchHistoryList,
            key = { item -> item.id }
        ) {item ->
            Text("${item.what} - ${item.where} - ${item.distance}km")
        }
    }
}

@Composable
fun SearchJobButton(
    onButtonClicked: () -> Unit,
    onSetJobSearchHistory: () -> Unit,
    onNavigateToJobSearch: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        modifier = modifier.padding(vertical = 4.dp).fillMaxWidth(),
        onClick = {
            onButtonClicked()
            onSetJobSearchHistory()
            onNavigateToJobSearch()
        }
    ) {
        Text("Search")
    }
}
