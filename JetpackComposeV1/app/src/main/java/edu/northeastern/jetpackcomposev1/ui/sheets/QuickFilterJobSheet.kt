package edu.northeastern.jetpackcomposev1.ui.sheets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import edu.northeastern.jetpackcomposev1.models.search.ContractTimeCode
import edu.northeastern.jetpackcomposev1.models.search.SalaryMinCode
import edu.northeastern.jetpackcomposev1.models.search.SortByCode
import edu.northeastern.jetpackcomposev1.viewmodels.JobViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuickFilterJobSheet(
    index: Int,
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
                if (index == 1) {
                    QuickSortBy(jobViewModel = jobViewModel, onCloseSheet = { onCloseSheet() })
                }
                else if (index == 2) {
                    QuickContractTime(jobViewModel = jobViewModel, onCloseSheet = { onCloseSheet() })
                }
                else if (index == 3) {
                    QuickSalaryMin(jobViewModel = jobViewModel, onCloseSheet = { onCloseSheet() })
                }
                else if (index == 4) {
                    QuickMaxDaysOld(jobViewModel = jobViewModel, onCloseSheet = { onCloseSheet() })
                }
                Spacer(modifier = modifier.height(64.dp))
            }
        }
    }
}

@Composable
fun QuickSortBy(
    jobViewModel: JobViewModel,
    onCloseSheet: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.selectableGroup()) {
        Text(
            text = "Sort by",
            fontWeight = FontWeight.Bold
        )
        SortByCode.values().forEach { item ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = modifier
                    .fillMaxWidth()
                    .clickable {
                        jobViewModel.search.sort_by = item.code
                        jobViewModel.search.page = 1
                        jobViewModel.getJobFromAPI()
                        onCloseSheet()
                    }
            ) {
                RadioButton(
                    selected = jobViewModel.search.sort_by == item.code,
                    onClick = {  },
                )
                Text(item.displayName)
            }
        }
    }
}

@Composable
fun QuickContractTime(
    jobViewModel: JobViewModel,
    onCloseSheet: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.selectableGroup()) {
        Text(
            text = "Contract time",
            fontWeight = FontWeight.Bold
        )
        ContractTimeCode.values().forEach { item ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = modifier
                    .fillMaxWidth()
                    .clickable {
                        jobViewModel.search.full_time = item.fullTime
                        jobViewModel.search.part_time = item.partTime
                        jobViewModel.search.page = 1
                        jobViewModel.getJobFromAPI()
                        onCloseSheet()
                    }
            ) {
                RadioButton(
                    selected = jobViewModel.search.full_time == item.fullTime && jobViewModel.search.part_time == item.partTime,
                    onClick = {  },
                )
                Text(item.displayName)
            }
        }
    }
}

@Composable
fun QuickSalaryMin(
    jobViewModel: JobViewModel,
    onCloseSheet: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.selectableGroup()) {
        Text(
            text = "Minimum pay",
            fontWeight = FontWeight.Bold
        )
        SalaryMinCode.values().forEach { item ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = modifier
                    .fillMaxWidth()
                    .clickable {
                        jobViewModel.search.salary_min = item.code
                        jobViewModel.search.page = 1
                        jobViewModel.getJobFromAPI()
                        onCloseSheet()
                    }
            ) {
                RadioButton(
                    selected = jobViewModel.search.salary_min == item.code,
                    onClick = {  },
                )
                Text(item.displayName)
            }
        }
    }
}

@Composable
fun QuickMaxDaysOld(
    jobViewModel: JobViewModel,
    onCloseSheet: () -> Unit,
    modifier: Modifier = Modifier
) {
    val radioOptions = listOf(7, 30, 90, 180, 365)
    Column(modifier = modifier.selectableGroup()) {
        Text(
            text = "Date posted",
            fontWeight = FontWeight.Bold
        )
        radioOptions.forEach { item ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = modifier
                    .fillMaxWidth()
                    .clickable {
                        jobViewModel.search.max_days_old = item
                        jobViewModel.search.page = 1
                        jobViewModel.getJobFromAPI()
                        onCloseSheet()
                    }
            ) {
                RadioButton(
                    selected = jobViewModel.search.max_days_old == item,
                    onClick = {  },
                )
                Text( "Last $item days")
            }
        }
    }
}