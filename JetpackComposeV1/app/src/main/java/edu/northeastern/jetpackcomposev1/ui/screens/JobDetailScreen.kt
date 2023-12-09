
package edu.northeastern.jetpackcomposev1.ui.screens

import androidx.compose.material3.Text
import android.content.Intent
import android.net.Uri
import android.widget.Toast

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

import androidx.compose.ui.unit.dp

import androidx.compose.ui.text.font.FontWeight

import androidx.compose.material3.Surface


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialogDefaults.shape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import edu.northeastern.jetpackcomposev1.models.job.JobCompanyModel
import edu.northeastern.jetpackcomposev1.models.job.JobFavoriteModel
import edu.northeastern.jetpackcomposev1.models.job.JobLocationModel
import edu.northeastern.jetpackcomposev1.models.job.JobModel
import edu.northeastern.jetpackcomposev1.ui.sheets.NewPostSheet
import edu.northeastern.jetpackcomposev1.utility.convertDateTime
import edu.northeastern.jetpackcomposev1.utility.convertSalary
import edu.northeastern.jetpackcomposev1.viewmodels.ApplicationViewModel
import edu.northeastern.jetpackcomposev1.viewmodels.JobViewModel
import edu.northeastern.jetpackcomposev1.viewmodels.PostViewModel


@Composable
fun JobDetailScreen(
    listName: String,
    index: Int,
    jobViewModel: JobViewModel,
    applicationViewModel: ApplicationViewModel,
    onNavigateToApply: () -> Unit,
    postViewModel: PostViewModel,
    modifier: Modifier = Modifier
) {
    if (postViewModel.running) {
        ShowCircularProgressIndicator()
    }
    else {
        LazyColumn(modifier = modifier.padding(horizontal = 8.dp)) {
            item {
                if (listName == "search") {
                    val job = jobViewModel.response.results[index]
                    jobViewModel.selectJob(job)
                    JobDetailContent(job = job, applicationViewModel = applicationViewModel, onNavigateToApply = onNavigateToApply, postViewModel = postViewModel)
                }
                else if (listName == "favorite") {
                    val job = jobViewModel.jobFavoriteList[index].job
                    jobViewModel.selectJob(job)
                    JobDetailContent(job = job, applicationViewModel = applicationViewModel, onNavigateToApply = onNavigateToApply, postViewModel = postViewModel)
                }
                else if (listName == "recommendation") {
                    val job = applicationViewModel.jobRecommendationList[index]
                    jobViewModel.selectJob(job)
                    JobDetailContent(job = job, applicationViewModel = applicationViewModel, onNavigateToApply = onNavigateToApply, postViewModel = postViewModel)
                }
            }
        }
    }
}

@Composable
fun JobDetailContent(
    job: JobModel,
    applicationViewModel: ApplicationViewModel,
    onNavigateToApply: () -> Unit,
    postViewModel: PostViewModel,
    modifier: Modifier = Modifier
) {
    val topPadding: Int = 16
    // title, company, location
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = job.title,
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleLarge,
            modifier = modifier.padding(bottom = 8.dp)
        )
        Text(
            text = job.company.display_name,
            color = MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.bodyMedium,
            modifier = modifier.padding(bottom = 8.dp)
        )
        Text(
            text = job.location.display_name,
            color = MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.bodyMedium,
            modifier = modifier.padding(bottom = 8.dp)
        )
    }
    Divider()
    // full location
    Column(modifier = modifier.padding(top = topPadding.dp)) {
        Text(text = "Location", fontWeight = FontWeight.Bold, modifier = modifier.padding(bottom = 4.dp))
        for (area in job.location.area.reversed()) {
            Text(
                text = area,
                style = MaterialTheme.typography.bodyMedium,
                modifier = modifier.padding(horizontal = 16.dp)
            )
        }
    }
    // contract time, type
    Column(modifier = modifier.padding(top = topPadding.dp)) {
        Text(text = "Job Type", fontWeight = FontWeight.Bold, modifier = modifier.padding(bottom = 4.dp))
        Row(modifier = modifier.padding(horizontal = 16.dp)) {
            Surface(
                shape = MaterialTheme.shapes.small,
                color = MaterialTheme.colorScheme.surfaceVariant
            ) {
                Text(
                    text = job.contract_time.replaceFirstChar { it.uppercase() },
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = modifier.padding(all = 4.dp)
                )
            }
            if (job.contract_type.isNotEmpty()) {
                Surface(
                    shape = MaterialTheme.shapes.small,
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = modifier.padding(start = 4.dp)
                ) {
                    Text(
                        text = job.contract_type.replaceFirstChar { it.uppercase() },
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = modifier.padding(all = 4.dp)
                    )
                }
            }
        }
    }
    // salary
    Column(modifier = modifier.padding(top = topPadding.dp)) {
        Text(
            text = "Salary",
            fontWeight = FontWeight.Bold,
            modifier = modifier.padding(bottom = 4.dp)
        )
        Surface(
            shape = MaterialTheme.shapes.small,
            color = MaterialTheme.colorScheme.surfaceVariant,
            modifier = modifier.padding(horizontal = 16.dp)
        ) {
            Text(
                text = convertSalary(
                    job.salary_is_predicted,
                    job.salary_min,
                    job.salary_max
                ),
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodyMedium,
                fontStyle = FontStyle.Italic,
                modifier = modifier.padding(all = 4.dp)
            )
        }
    }
    // job description
    Column(modifier = modifier.padding(top = topPadding.dp)) {
        Text(
            text = "Description",
            fontWeight = FontWeight.Bold,
            modifier = modifier.padding(bottom = 4.dp)
        )
        Text(
            text = job.description,
            style = MaterialTheme.typography.bodyMedium,
            modifier = modifier.padding(horizontal = 16.dp)
        )
    }
    // category
    Column(modifier = modifier.padding(top = topPadding.dp)) {
        Text(
            text = "Category",
            fontWeight = FontWeight.Bold,
            modifier = modifier.padding(bottom = 4.dp)
        )
        Text(
            text = job.category.label,
            style = MaterialTheme.typography.bodyMedium,
            modifier = modifier.padding(horizontal = 16.dp)
        )
    }
    // date and time
    Column(modifier = modifier.padding(vertical = topPadding.dp)) {
        Text(
            text = "Posted Date",
            fontWeight = FontWeight.Bold,
            modifier = modifier.padding(bottom = 4.dp)
        )
        Text(
            text = convertDateTime(job.created),
            style = MaterialTheme.typography.bodyMedium,
            modifier = modifier.padding(horizontal = 16.dp)
        )
    }
    Divider()

    // button section here
    JobDetailButton(
        job = job,
        applicationViewModel = applicationViewModel,
        onNavigateToApply = onNavigateToApply,
        postViewModel = postViewModel
    )
}


@Composable
fun JobDetailButton(
    job: JobModel,
    applicationViewModel: ApplicationViewModel,
    onNavigateToApply: () -> Unit,
    postViewModel: PostViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val applyJobIntent = Intent(Intent.ACTION_VIEW, Uri.parse(job.redirect_url))
    val sendIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, job.redirect_url)
        type = "text/plain"
    }
    val shareJobIntent = Intent.createChooser(sendIntent, null)

    // sub sheets are here
    var showNewPostSheet by rememberSaveable { mutableStateOf(false) }
    if (showNewPostSheet) {
        NewPostSheet(
            postViewModel = postViewModel,
            onCloseSheet = { showNewPostSheet = false }
        )
    }

    Column (modifier = modifier
        .fillMaxWidth()
        .padding(vertical = 8.dp), // Adjust horizontal padding as needed
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Button(
            modifier = Modifier
                .width(250.dp)
                .padding(vertical = 8.dp),
            onClick = { context.startActivity(applyJobIntent) }
        ) {
            Text("Apply")
        }
        Button(
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
            modifier = Modifier
                .width(250.dp)
                .padding(vertical = 8.dp),
            onClick = { context.startActivity(shareJobIntent) }
        ) {
            Text("Share")
        }
        Button(
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
            modifier = Modifier
                .width(250.dp)
                .padding(vertical = 8.dp),
            onClick = {
                showNewPostSheet = true
                postViewModel.post.text = "Check out this job!\n----------------------------\n${job.title}\n${job.company.display_name}\n${job.location.display_name}"
                postViewModel.post.url = job.redirect_url
            }
        ) {
            Text("Post Job")
        }
        // add local application
        Button(
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
            modifier = Modifier
                .width(250.dp)
                .padding(vertical = 8.dp),
            onClick = onNavigateToApply,
            enabled = !applicationViewModel.jobApplicationList.any { it.job.id == job.id }
        ) {
            Text(if (!applicationViewModel.jobApplicationList.any { it.job.id == job.id }) "Add Application" else "Applied")
        }
    }
}

/*
@Preview
@Composable
fun JobDetailContentPreview() {
    val sampleJob = JobModel(
        title = "Software Engineer",
        company = JobCompanyModel(display_name = "Example Company"),
        location = JobLocationModel(display_name = "City, Country"),
        description = "We are seeking a Machine Learning Engineer to join a innovative team that builds Generative BI features for QuickSight. You will play a critical role in developing ML systems that analyze large sets of data, extracts insights and make them available to our customers using Natural Language\n" +
                "\n" +
                "Key job responsibilities\n" +
                ". Understand business objectives, product requirements and develop ML algorithms that achieve them.\n" +
                ". Build Prototypes, POC to determine feasibility.\n" +
                ". Run experiments to assess performance and improvements.\n" +
                ". Provide ideas and alternatives to drive a product/feature.\n" +
                ". Define data and feature validation strategies\n" +
                ". Deploy models to production systems and operate them including monitoring and troubleshooting\n" +
                "\n" +
                "We are open to hiring candidates to work out of one of the following locations:\n" +
                "\n" +
                "Seattle, WA, USA\n" +
                "\n" +
                "BASIC QUALIFICATIONS\n" +
                "- 3+ years of non-internship professional software development experience\n" +
                "- 2+ years of non-internship design or architecture (design patterns, reliability and scaling) of new and existing systems experience\n" +
                "- Experience programming with at least one software programming language\n" +
                "- Experience in machine learning, data mining, information retrieval, statistics or natural language processing\n" +
                "\n" +
                "PREFERRED QUALIFICATIONS\n" +
                "- 3+ years of full software development life cycle, including coding standards, code reviews, source control management, build processes, testing, and operations experience\n" +
                "- Bachelor's degree in computer science or equivalent\n" +
                "\n" +
                "Amazon is committed to a diverse and inclusive workplace. Amazon is an equal opportunity employer and does not discriminate on the basis of race, national origin, gender, gender identity, sexual orientation, protected veteran status, disability, age, or other legally protected status. For individuals with disabilities who would like to request an accommodation, please visit https://www.amazon.jobs/en/disability/us.\n" +
                "\n" +
                "Our compensation reflects the cost of labor across several US geographic markets. The base pay for this position ranges from \$115,000/year in our lowest geographic market up to \$223,600/year in our highest geographic market. Pay is based on a number of factors including market location and may vary depending on job-related knowledge, skills, and experience. Amazon is a total compensation company. Dependent on the position offered, equity, sign-on payments, and other forms of compensation may be provided as part of a total compensation package, in addition to a full range of medical, financial, and/or other benefits. For more information, please visit https://www.aboutamazon.com/workplace/employee-benefits. Applicants should apply via our internal or external career site.",
        contract_time = "full_time",
        salary_is_predicted = "yes",
        salary_min = 50000.0,
        salary_max = 80000.0,
        created = "2023-11-27T12:34:56Z"
    )

    JobDetailContent(job = sampleJob, )
}


*/