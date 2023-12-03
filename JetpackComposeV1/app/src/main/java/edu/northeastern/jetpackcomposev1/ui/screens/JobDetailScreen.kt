
package edu.northeastern.jetpackcomposev1.ui.screens

import android.content.Context
import androidx.compose.material3.Text
import android.os.Bundle
import android.content.Intent
import android.net.Uri

import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction

import androidx.compose.ui.unit.dp

import androidx.compose.ui.text.font.FontWeight

import androidx.compose.material3.Surface
import androidx.compose.material3.Text


import androidx.compose.foundation.lazy.LazyColumn




import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import edu.northeastern.jetpackcomposev1.R
import edu.northeastern.jetpackcomposev1.models.job.JobApplicationModel
import edu.northeastern.jetpackcomposev1.models.job.JobCompanyModel
import edu.northeastern.jetpackcomposev1.models.job.JobFavoriteModel
import edu.northeastern.jetpackcomposev1.models.job.JobLocationModel
import edu.northeastern.jetpackcomposev1.models.job.JobModel
import edu.northeastern.jetpackcomposev1.models.job.JobViewedHistoryModel
import edu.northeastern.jetpackcomposev1.ui.sheets.DetailJobSheet
import edu.northeastern.jetpackcomposev1.ui.sheets.FilterJobSheet
import edu.northeastern.jetpackcomposev1.ui.sheets.SearchJobSheet
import edu.northeastern.jetpackcomposev1.ui.theme.JetpackComposeV1Theme
import edu.northeastern.jetpackcomposev1.utility.checkIfNew
import edu.northeastern.jetpackcomposev1.utility.convertDateTime
import edu.northeastern.jetpackcomposev1.utility.convertNumberOfJobs
import edu.northeastern.jetpackcomposev1.utility.convertSalary
import edu.northeastern.jetpackcomposev1.viewmodels.ApplicationViewModel
import edu.northeastern.jetpackcomposev1.viewmodels.JobViewModel
import java.time.format.TextStyle

// JobDetailScreen.kt
// the index is the index in the job search result list
// job: JobModel = jobViewModel.response.results[index]

@Composable
fun JobDetailScreen(
    index: Int, jobViewModel: JobViewModel, modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier.padding(horizontal = 8.dp)) {
        item {
            JobDetailContent(job = jobViewModel.response.results[index], modifier = modifier.padding(horizontal = 8.dp) )


        }
    }
}




@Composable
fun JobDetailContent(job: JobModel, modifier: Modifier = Modifier) {
    val uri = Uri.parse(job.redirect_url)
    val intent = Intent(Intent.ACTION_VIEW, uri)

    val context = LocalContext.current

    /*var showDetailJobSheet by rememberSaveable { mutableStateOf(false) }
    if (showDetailJobSheet) {
        DetailJobSheet(
            job = job,
            onCloseSheet = { showDetailJobSheet = false }
        )
    }*/


    OutlinedCard(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        border = BorderStroke(1.dp, Color.Black),
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    )  {

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
                .padding( 16.dp)
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

        // Buttons section
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 8.dp)

        ) {
            Button(
                onClick = {
                    context.startActivity(intent)
                },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 4.dp)
            ){
                Text("Apply")
            }
            Button(
                onClick = { /* Implement save functionality */ },
                modifier = modifier
                    .weight(1f)
                    .padding(horizontal = 4.dp)
            ) {
                Text("Save")
            }
            Button(
                onClick = { /* Implement share functionality */ },
                modifier = modifier
                    .weight(1f)
                    .padding(start = 4.dp)
            ) {
                Text("Share")
            }
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


