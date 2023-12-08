package edu.northeastern.jetpackcomposev1.ui.sheets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
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
import edu.northeastern.jetpackcomposev1.models.search.CountryCode
import edu.northeastern.jetpackcomposev1.models.search.ContractTimeCode
import edu.northeastern.jetpackcomposev1.models.search.ContractTypeCode
import edu.northeastern.jetpackcomposev1.models.search.SalaryMinCode
import edu.northeastern.jetpackcomposev1.models.search.SortByCode
import edu.northeastern.jetpackcomposev1.viewmodels.JobViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterJobSheet(
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
            .padding(horizontal = 8.dp)
            .fillMaxHeight(0.95f)
        ) {
            item {
                FilterJobInput(jobViewModel = jobViewModel)
                FilterJobButton(
                    jobViewModel = jobViewModel,
                    onCloseSheet = { onCloseSheet() }
                )
                Spacer(modifier = modifier.height(64.dp))
            }
        }
    }
}

/*@Preview
@Composable
fun PreviewFilterJobSheet() {
    val jobViewModel: JobViewModel = viewModel()
    var showFilterJobSheet by rememberSaveable { mutableStateOf(false) }
    JetpackComposeV1Theme {
        FilterJobSheet(jobViewModel, {showFilterJobSheet = false})
    }
}*/

@Composable
fun FilterJobInput(
    jobViewModel: JobViewModel,
    modifier: Modifier = Modifier
) {
    Column {
        SortBy(jobViewModel)
        Divider()
        Spacer(modifier = modifier.padding(top = 8.dp))
        ResultsPerPage(jobViewModel)
        Divider()
        Spacer(modifier = modifier.padding(top = 8.dp))
        TravelDistance(jobViewModel)
        Divider()
        Spacer(modifier = modifier.padding(top = 8.dp))
        MaxDaysOld(jobViewModel)
        Divider()
        Spacer(modifier = modifier.padding(top = 8.dp))
        SalaryMin(jobViewModel)
        Divider()
        Spacer(modifier = modifier.padding(top = 8.dp))
        ContractTime(jobViewModel)
        Divider()
        Spacer(modifier = modifier.padding(top = 8.dp))
        ContractType(jobViewModel)
        Divider()
        Spacer(modifier = modifier.padding(top = 8.dp))
        Country(jobViewModel)
    }
}

@Composable
fun SortBy(
    jobViewModel: JobViewModel,
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
                    .clickable { jobViewModel.search.sort_by = item.code }
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
fun Country(
    jobViewModel: JobViewModel,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.selectableGroup()) {
        Text(
            text = "Country",
            fontWeight = FontWeight.Bold
        )
        CountryCode.values().forEach { item ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = modifier
                    .fillMaxWidth()
                    .clickable { jobViewModel.search.country = item.code }
            ) {
                RadioButton(
                    selected = jobViewModel.search.country == item.code,
                    onClick = {  },
                )
                Text(item.displayName)
            }
        }
    }
}

@Composable
fun ResultsPerPage(
    jobViewModel: JobViewModel,
    modifier: Modifier = Modifier
) {
    val radioOptions = listOf(10, 20, 40)
    Column(modifier = modifier.selectableGroup()) {
        Text(
            text = "Results per page",
            fontWeight = FontWeight.Bold
        )
        radioOptions.forEach { item ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = modifier
                    .fillMaxWidth()
                    .clickable { jobViewModel.search.results_per_page = item }
            ) {
                RadioButton(
                    selected = jobViewModel.search.results_per_page == item,
                    onClick = {  },
                )
                Text("$item")
            }
        }
    }
}

@Composable
fun TravelDistance(
    jobViewModel: JobViewModel,
    modifier: Modifier = Modifier
) {
    val radioOptions = listOf(5, 10, 20, 40)
    Column(modifier = modifier.selectableGroup()) {
        Text(
            text = "Travel distance",
            fontWeight = FontWeight.Bold
        )
        radioOptions.forEach { item ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = modifier
                    .fillMaxWidth()
                    .clickable { jobViewModel.search.distance = item }
            ) {
                RadioButton(
                    selected = jobViewModel.search.distance == item,
                    onClick = {  },
                )
                Text( "Within $item km")
            }
        }
    }
}

@Composable
fun MaxDaysOld(
    jobViewModel: JobViewModel,
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
                    .clickable { jobViewModel.search.max_days_old = item }
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

@Composable
fun SalaryMin(
    jobViewModel: JobViewModel,
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
                    .clickable { jobViewModel.search.salary_min = item.code }
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
fun ContractTime(
    jobViewModel: JobViewModel,
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
fun ContractType(
    jobViewModel: JobViewModel,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.selectableGroup()) {
        Text(
            text = "Contract type",
            fontWeight = FontWeight.Bold
        )
        ContractTypeCode.values().forEach { item ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = modifier
                    .fillMaxWidth()
                    .clickable {
                        jobViewModel.search.contract = item.contract
                        jobViewModel.search.permanent = item.permanent
                    }
            ) {
                RadioButton(
                    selected = jobViewModel.search.contract == item.contract && jobViewModel.search.permanent == item.permanent,
                    onClick = {  },
                )
                Text(item.displayName)
            }
        }
    }
}

@Composable
fun FilterJobButton(
    jobViewModel: JobViewModel,
    onCloseSheet: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxWidth()
    ) {
        Button(
            modifier = modifier
                .width(250.dp)
                .padding(vertical = 4.dp),
            onClick = {
                jobViewModel.search.page = 1
                jobViewModel.getJobFromAPI()
                onCloseSheet()
            }
        ) {
            Text("Update")
        }
        Button(
            modifier = modifier
                .width(250.dp)
                .padding(vertical = 4.dp),
            onClick = {
                jobViewModel.search.results_per_page = 10
                jobViewModel.search.distance = 5
                jobViewModel.search.max_days_old = 365
                jobViewModel.search.sort_by = SortByCode.RELEVANCE.code
                jobViewModel.search.salary_min = 0
                jobViewModel.search.full_time = false
                jobViewModel.search.part_time = false
                jobViewModel.search.contract = false
                jobViewModel.search.permanent = false
            }
        ) {
            Text("Reset")
        }
    }
}