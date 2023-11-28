package edu.northeastern.jetpackcomposev1.ui.sheets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import edu.northeastern.jetpackcomposev1.models.job.JobSearchHistoryModel
import edu.northeastern.jetpackcomposev1.viewmodels.JobViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchJobSheet(
    jobViewModel: JobViewModel,
    onCloseSheet: () -> Unit,
    modifier: Modifier = Modifier
) {
    val sheetState = rememberModalBottomSheetState()
    ModalBottomSheet(
        onDismissRequest = { onCloseSheet() },
        sheetState = sheetState,
    ) {
        // Sheet content
        LazyColumn(modifier = modifier
            .padding(horizontal = 8.dp).fillMaxHeight(0.95f)
        ) {
            item {
                SearchJobInput(jobViewModel = jobViewModel)
                SearchJobButton(
                    onSearchButtonClicked = { jobViewModel.getJobFromAPI() },
                    onSetJobSearchHistory = { jobViewModel.setJobSearchHistoryToDB(true) },
                    onCloseSheet = { onCloseSheet() }
                )
                SearchJobHistory(
                    jobViewModel = jobViewModel,
                    onSearchButtonClicked = { jobViewModel.getJobFromAPI() },
                    onSetJobSearchHistory = { jobViewModel.setJobSearchHistoryToDB(true) },
                    onDeleteJobSearchHistory = { index -> jobViewModel.setJobSearchHistoryToDB(false, index) },
                    onCloseSheet = { onCloseSheet() }
                )
                Spacer(modifier = modifier.height(64.dp))
            }
        }
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
            modifier = modifier.fillMaxWidth(),
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
                modifier = modifier
                    .padding(start = 4.dp)
                    .weight(0.3f),
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
fun SearchJobButton(
    onSearchButtonClicked: () -> Unit,
    onSetJobSearchHistory: () -> Unit,
    onCloseSheet: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        modifier = modifier
            .padding(vertical = 4.dp)
            .fillMaxWidth(),
        onClick = {
            onSearchButtonClicked()
            onSetJobSearchHistory()
            onCloseSheet()
        }
    ) {
        Text("Search")
    }
}

@Composable
fun SearchJobHistory(
    jobViewModel: JobViewModel,
    onSearchButtonClicked: () -> Unit,
    onSetJobSearchHistory: () -> Unit,
    onDeleteJobSearchHistory: (Int) -> Unit,
    onCloseSheet: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column {
        jobViewModel.jobSearchHistoryList.forEachIndexed { index, item ->
            ListItem(
                headlineContent = { Text(item.what.ifEmpty { "Any job" }) },
                supportingContent = { Text("${item.where.ifEmpty { "Any place" }} - ${item.distance} km") },
                trailingContent = {
                    IconButton(onClick = { onDeleteJobSearchHistory(index) }) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Delete"
                        )
                    }
                },
                modifier = modifier.clickable {
                    jobViewModel.search.what = item.what
                    jobViewModel.search.where = item.where
                    jobViewModel.search.distance = item.distance
                    onSearchButtonClicked()
                    onSetJobSearchHistory()
                    onCloseSheet()
                }
            )
            Divider()
        }
    }
}
