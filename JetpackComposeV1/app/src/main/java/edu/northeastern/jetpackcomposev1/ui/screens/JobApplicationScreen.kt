package edu.northeastern.jetpackcomposev1.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.northeastern.jetpackcomposev1.R
import edu.northeastern.jetpackcomposev1.models.job.JobApplicationModel
import edu.northeastern.jetpackcomposev1.models.job.JobModel
import edu.northeastern.jetpackcomposev1.models.resume.ResumeModel
import edu.northeastern.jetpackcomposev1.utility.convertDateTime
import edu.northeastern.jetpackcomposev1.utility.convertSalary
import edu.northeastern.jetpackcomposev1.viewmodels.ApplicationViewModel


@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun JobApplicationScreen(
    applicationViewModel: ApplicationViewModel,
    modifier: Modifier = Modifier,
    onNavigateToApplicationDetail: () -> Unit,
) {
    applicationViewModel.sortJobApplicationListOnDate()
    val sortedApplicationList = applicationViewModel.sortedApplicationList
    val applicationSearchState = remember { mutableStateOf(TextFieldValue("")) }
    val applicationSearchText = applicationSearchState.value.text

    //filter the application list based on the search text
    val filteredApplicationList = sortedApplicationList.filter {
        if (applicationSearchText.isEmpty()) {
            return@filter true
        }
        val titleContainsSearchText =
            it.job.title.contains(applicationSearchText, ignoreCase = true)
        val companyContainsSearchText =
            it.job.company.display_name.contains(applicationSearchText, ignoreCase = true)
        titleContainsSearchText || companyContainsSearchText
    }

    // filter the application list based on the status
    val rejectedList = applicationViewModel.getFilteredJobApplicationList("Rejected")
    val interviewedList = applicationViewModel.getFilteredJobApplicationList("Interviewed")
    val offerList = applicationViewModel.getFilteredJobApplicationList("Offer")
    val offerAcceptedList = applicationViewModel.getFilteredJobApplicationList("Offer Accepted")
    val rejectedSize = rejectedList.size
    val interviewedSize = interviewedList.size
    val offerSize = offerList.size
    val offerAcceptedSize = offerAcceptedList.size
    val allSize = sortedApplicationList.size
    var applicationSearchChipState = remember { mutableStateOf(("")) }

    var applicationStatusSearchList = remember { mutableStateListOf<JobApplicationModel>() }
    if (applicationSearchChipState.value == "Interviewed") {
        applicationStatusSearchList.clear()
        applicationStatusSearchList.addAll(interviewedList)
    } else if (applicationSearchChipState.value == "Offer") {
        applicationStatusSearchList.clear()
        applicationStatusSearchList.addAll(offerList)
    } else if (applicationSearchChipState.value == "Rejected") {
        applicationStatusSearchList.clear()
        applicationStatusSearchList.addAll(rejectedList)
    } else if (applicationSearchChipState.value == "Offer Accepted") {
        applicationStatusSearchList.clear()
        applicationStatusSearchList.addAll(offerAcceptedList)
    } else {
        applicationStatusSearchList.clear()
        applicationStatusSearchList.addAll(sortedApplicationList)
    }

    val applicationList = applicationViewModel.jobApplicationList.toList()
    if (applicationList.isEmpty()) {
        Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) { Text("No Application Found") }
    } else {
        // launch reminder
        if (applicationViewModel.firstLaunch2 && applicationList.isNotEmpty()) {
            applicationViewModel.setJobReminderToUser(LocalContext.current)
        }
        applicationViewModel.firstLaunch2 = false
        LazyColumn(
            modifier = modifier.padding(4.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                ApplicationSearch(
                    applicationSearchState,
                    rejectedSize,
                    interviewedSize,
                    offerSize,
                    allSize,
                    offerAcceptedSize,
                    applicationSearchChipState,
                )
                //ApplicationStatistic(applications = applicationList)
                ApplicationList(
                    applications = if (applicationSearchChipState.value.isNotEmpty() && applicationSearchText.isEmpty()) {
                        applicationStatusSearchList
                    } else if (applicationSearchText.isNotEmpty() && applicationSearchChipState.value.isEmpty()) {
                        filteredApplicationList
                    } else if (applicationSearchText.isNotEmpty() && applicationSearchChipState.value.isNotEmpty()) {
                        applicationStatusSearchList.filter {
                            val titleContainsSearchText =
                                it.job.title.contains(applicationSearchText, ignoreCase = true)
                            val companyContainsSearchText =
                                it.job.company.display_name.contains(
                                    applicationSearchText,
                                    ignoreCase = true
                                )
                            titleContainsSearchText || companyContainsSearchText
                        }
                    } else {
                        sortedApplicationList
                    },
                    modifier = modifier,
                    onNavigateToApplicationDetail = onNavigateToApplicationDetail,
                    applicationViewModel = applicationViewModel
                )
            }
        }
    }
    AskForNotificationPermission()
}


@Composable
fun ApplicationSearch(
    jobSearchState: MutableState<TextFieldValue>,
    rejectedSize: Int,
    interviewedSize: Int,
    offerSize: Int,
    allSize: Int,
    offerAcceptedSize: Int,
    applicationSearchChipState: MutableState<String>,
) {
    SearchInputView(searchState = jobSearchState, placeholder = "Search Job Title or Company")
    LazyRow(
        modifier = Modifier.padding(start = 8.dp, top = 16.dp, bottom = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            SearchChip(
                text = "All",
                size = allSize,
                applicationSearchChipState = applicationSearchChipState
            )
            Spacer(modifier = Modifier.width(36.dp))
            SearchChip(
                text = "Interviewed",
                size = interviewedSize,
                applicationSearchChipState = applicationSearchChipState
            )
            Spacer(modifier = Modifier.width(36.dp))
            SearchChip(
                text = "Offer",
                size = offerSize,
                applicationSearchChipState = applicationSearchChipState
            )
            Spacer(modifier = Modifier.width(36.dp))
            SearchChip(
                text = "Rejected",
                size = rejectedSize,
                applicationSearchChipState = applicationSearchChipState
            )
            Spacer(modifier = Modifier.width(36.dp))
            SearchChip(
                text = "Offer Accepted",
                size = offerAcceptedSize,
                applicationSearchChipState = applicationSearchChipState
            )
            Spacer(modifier = Modifier.width(36.dp))
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchChip(text: String, size: Int, applicationSearchChipState: MutableState<String>) {
    BadgedBox(badge = {
        Badge(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(x = 0.dp, y = 8.dp),
            contentColor = MaterialTheme.colorScheme.surface,
            containerColor = MaterialTheme.colorScheme.primary,
        ) {
            Text(
                text = size.toString(),
                style = TextStyle(fontSize = 12.sp),
                modifier = Modifier.padding(2.dp)
            )
        }
    })
    {
        Surface(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .clickable { applicationSearchChipState.value = text },
            color = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.primary
        ) {
            Text(
                text = text,
                style = TextStyle(fontSize = 14.sp),
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}


@Composable
fun SearchInputView(searchState: MutableState<TextFieldValue>, placeholder: String) {
    OutlinedCard(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        OutlinedTextField(
            value = searchState.value,
            onValueChange = { value
                ->
                searchState.value = value
                true
            },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .border(
                    0.dp, Color.Transparent, RoundedCornerShape(8.dp)
                ),
            singleLine = true,
            placeholder = {
                Text(text = placeholder)
            },
            leadingIcon = {
                Image(
                    painter = painterResource(id = R.drawable.ic_search_24),
                    contentDescription = "Search Job Title or Company",
                    modifier = Modifier.size(18.dp)
                )
            },
        )
    }
}

@Composable
fun ApplicationList(
    applications: List<JobApplicationModel>,
    modifier: Modifier,
    onNavigateToApplicationDetail: () -> Unit,
    applicationViewModel: ApplicationViewModel
) {
    applications.forEach { application ->

        ApplicationCard(
            application = application,
            modifier = modifier,
            onNavigateToApplicationDetail = onNavigateToApplicationDetail,
            applicationViewModel = applicationViewModel
        )
        Spacer(modifier = modifier.height(4.dp))
    }
}

@Composable
fun ApplicationCard(
    application: JobApplicationModel,
    modifier: Modifier,
    onNavigateToApplicationDetail: () -> Unit,
    applicationViewModel: ApplicationViewModel
) {
    OutlinedCard(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onPrimary),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
    ) {
        Column(modifier = modifier.padding(start = 8.dp, end = 16.dp, top = 8.dp)) {
            ApplicationContent(
                application = application,
                modifier = Modifier.padding(8.dp),
                onNavigateToApplicationDetail = onNavigateToApplicationDetail,
                applicationViewModel = applicationViewModel
            )
        }

    }
}


@Composable
fun ApplicationContent(
    application: JobApplicationModel,
    modifier: Modifier,
    onNavigateToApplicationDetail: () -> Unit,
    applicationViewModel: ApplicationViewModel
) {
    val latestEvent = application.timeLine.results[0]
    val job = application.job
    var isDialogVisible by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .padding(8.dp)
            .clickable {
                applicationViewModel.selectApplication(application)
                onNavigateToApplicationDetail()
            },
    ) {
        ApplicationJobInfo(modifier = modifier, job = job)
        Divider()
        // Resume Info
        Box(
            modifier = modifier
                .align(Alignment.End)
                .fillMaxWidth(),
            contentAlignment = Alignment.CenterEnd
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Left side content
                Column(
                    modifier = Modifier
                        .weight(4f),
                    horizontalAlignment = Alignment.Start
                ) {
                    ApplicationResumeInfo(modifier = modifier, resume = application.resume)
                    // Timeline Info
                    Text(
                        text = "Last Update: ${latestEvent.date}",
                        color = MaterialTheme.colorScheme.secondary,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "Current Status:  ${application.status}",
                        color = MaterialTheme.colorScheme.secondary,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                // Right side content (FloatingActionButton)
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .height(IntrinsicSize.Max)
                        .padding(start = 16.dp),
                    horizontalAlignment = Alignment.End,
                ) {

                    SmallFloatingActionButton(
                        onClick = {
                            isDialogVisible = true

                        }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.delete_24),
                            contentDescription = "Delete Application",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }
    }

    if (isDialogVisible) {
        AlertDialog(
            onDismissRequest = { isDialogVisible = false },
            title = { Text(text = "Delete Application") },
            text = { Text(text = "Are you sure you want to delete this application?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        applicationViewModel.deleteApplicationFromDB(application)
                        isDialogVisible = false
                    },
                ) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        isDialogVisible = false
                    }
                ) {
                    Text("Dismiss")
                }
            }
        )
    }
}


@Composable
fun ApplicationJobInfo(modifier: Modifier, job: JobModel) {
    Column(modifier = Modifier.padding(bottom = 4.dp)) {
        Text(
            text = job.title,
            //color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = job.company.display_name,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.bodyMedium,
        )
        Text(
            text = job.location.display_name,
            color = MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.bodyMedium
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Surface(
                shape = MaterialTheme.shapes.small,
                color = MaterialTheme.colorScheme.surfaceVariant,
            ) {
                Text(
                    text = job.contract_time.replaceFirstChar { it.uppercase() },
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = modifier.padding(all = 2.dp)
                )
            }
            Surface(
                modifier = modifier.padding(start = 4.dp),
                shape = MaterialTheme.shapes.small,
                color = MaterialTheme.colorScheme.surfaceVariant
            ) {
                Text(
                    text = convertSalary(job.salary_is_predicted, job.salary_min, job.salary_max),
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyMedium,
                    fontStyle = FontStyle.Italic,
                    modifier = modifier.padding(all = 2.dp)
                )
            }
        }
        Text(
            text = convertDateTime(job.created),
            color = MaterialTheme.colorScheme.tertiary,
            style = MaterialTheme.typography.labelSmall
        )
    }
    Spacer(modifier = Modifier.height(2.dp))
}

@Composable
fun ApplicationResumeInfo(modifier: Modifier, resume: ResumeModel) {
    Text(
        text = "Resume:  ${resume.nickName}",
        color = MaterialTheme.colorScheme.secondary,
        style = MaterialTheme.typography.bodyMedium
    )
}