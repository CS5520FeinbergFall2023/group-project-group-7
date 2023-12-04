
package edu.northeastern.jetpackcomposev1.ui.screens

import androidx.compose.material3.Text
import android.content.Intent
import android.net.Uri

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

import androidx.compose.ui.unit.dp

import androidx.compose.ui.text.font.FontWeight

import androidx.compose.material3.Surface


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import edu.northeastern.jetpackcomposev1.models.job.JobModel
import edu.northeastern.jetpackcomposev1.utility.convertDateTime
import edu.northeastern.jetpackcomposev1.utility.convertSalary
import edu.northeastern.jetpackcomposev1.viewmodels.JobViewModel

@Composable
fun JobDetailScreen(
    listName: String,
    index: Int,
    jobViewModel: JobViewModel,
    onNavigateToApply: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier.padding(horizontal = 8.dp)) {
        item {
            if (listName == "search") {
                JobDetailContent(job = jobViewModel.response.results[index], modifier, onNavigateToApply)
            }
            else if (listName == "favorite") {
                JobDetailContent(job = jobViewModel.jobFavoriteList[index].job, modifier, onNavigateToApply)
            }
            else if (listName == "application") {
                /*TODO: when user click the job from the application screen*/
            }
        }
    }
}

@Composable
fun JobDetailContent(
    job: JobModel,
    modifier: Modifier = Modifier,
    onNavigateToApply: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)

    ) {
        Text(
            text = job.title,
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleMedium,
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
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 8.dp)
    ) {
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
        Surface(
            modifier = modifier.padding(start = 4.dp),
            shape = MaterialTheme.shapes.small,
            color = MaterialTheme.colorScheme.surfaceVariant
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
    Text(
        text = "Job Description:",
        color = MaterialTheme.colorScheme.secondary,
        style = MaterialTheme.typography.bodyLarge,
        fontWeight = FontWeight.Bold,
        modifier = modifier.padding(16.dp)
    )

    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            // .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = job.description,
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.bodyMedium,
            modifier = modifier.padding(16.dp)
        )
    }
    Spacer(modifier = modifier.height(8.dp))
    Text(
        text = convertDateTime(job.created),
        color = MaterialTheme.colorScheme.tertiary,
        style = MaterialTheme.typography.labelSmall
    )

    // button section here
    JobDetailButton(job = job, modifier, onNavigateToApply)
}

@Composable
fun JobDetailButton(
    job: JobModel,
    modifier: Modifier = Modifier,
    onNavigateToApply: () -> Unit
) {
    val context = LocalContext.current
    val applyJobIntent = Intent(Intent.ACTION_VIEW, Uri.parse(job.redirect_url))
    val sendIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, job.redirect_url)
        type = "text/plain"
    }
    val shareJobIntent = Intent.createChooser(sendIntent, null)
    Column {
        Button(onClick = { /*TODO leave this blank for now*/ }) {
            Text("Save")
        }
        Button(onClick = { context.startActivity(applyJobIntent) }) {
            Text("Apply")
        }
        Button(onClick = { context.startActivity(shareJobIntent) }) {
            Text("Share")
        }
        Button(onClick =  onNavigateToApply ) {
            Text("Add application")
        }
    }
}


/*@Preview
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

    JobDetailContent(job = sampleJob)
}*/


