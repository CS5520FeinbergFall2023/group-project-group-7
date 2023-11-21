package edu.northeastern.jetpackcomposev1.screens

import android.graphics.fonts.FontStyle
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.northeastern.jetpackcomposev1.R

import edu.northeastern.jetpackcomposev1.models.JobViewModel
import edu.northeastern.jetpackcomposev1.ui.theme.JetpackComposeV1Theme
import edu.northeastern.jetpackcomposev1.utility.convertDateTime
import edu.northeastern.jetpackcomposev1.utility.convertSalary

@Composable
fun JobSearchScreen(jobViewModel: JobViewModel, modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier.padding(vertical = 4.dp)) {
        item { SearchSection() }
        item { JobLists(jobViewModel) }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchSection(modifier: Modifier = Modifier) {
    Column(modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp)) {
        OutlinedTextField(
            value = "Not a real search bar",
            onValueChange = {  },
            label = { Text("Search") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            singleLine = true
        )
        OutlinedTextField(
            value = "Not a real search bar",
            onValueChange = {  },
            label = { Text("Location") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            singleLine = true
        )
        IconButton(onClick = { /*TODO*/ }) {
            
        }
    }
}

//// we can preview individual UI also
//@Preview
//@Composable
//fun PreviewJobSearchScreen(){
//    JetpackComposeV1Theme {
//        JobSearchScreen()
//    }
//}

@Composable
fun JobLists(jobViewModel: JobViewModel, modifier: Modifier = Modifier) {
//    LazyColumn(modifier = modifier.padding(vertical = 4.dp)) {
        jobViewModel.response.results.forEach { item ->
            JobCard(
                title = item.title,
                company = item.company.display_name,
                location = item.location.display_name,
                salary_min = item.salary_min,
                salary_max = item.salary_max,
                time = item.created
            )
        }
//    }
}

@Composable
fun JobCard(
    title: String,
    company: String,
    location: String,
    salary_min: Double,
    salary_max: Double,
    time: String,
    modifier: Modifier = Modifier
) {
    OutlinedCard(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onPrimary),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp)
    ) {
        CardHead()
        JobContent(
            title = title,
            company = company,
            location = location,
            salary_min = salary_min,
            salary_max = salary_max,
            time = time
        )
        Divider()
        CardFoot()
    }
}

@Composable
fun JobContent(
    title: String,
    company: String,
    location: String,
    salary_min: Double,
    salary_max: Double,
    time: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier
        .padding(start = 22.dp, end = 22.dp, bottom = 16.dp)
        .clickable { /*TODO*/ }
    ) {
        Text(
            text = title,
            color = Color.Black,
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = company,
            color = MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = location,
            color = MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(4.dp))
        Row {
            Surface(
                shape = MaterialTheme.shapes.small,
                color = MaterialTheme.colorScheme.surfaceVariant,
                shadowElevation = 1.dp
            ) {
                Text(
                    text = convertSalary(salary_min, salary_max),
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(all = 4.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = convertDateTime(time),
            color = MaterialTheme.colorScheme.tertiary,
            style = MaterialTheme.typography.labelSmall
        )
    }
}

@Composable
fun CardHead() {
    Icon(
        painter = painterResource(id = R.drawable.outline_fiber_new_24),
        contentDescription = "Saved",
        tint = MaterialTheme.colorScheme.error,
        modifier = Modifier.padding(start = 20.dp, top = 16.dp)
    )
}

@Composable
fun CardFoot() {
    val outlineIcon = R.drawable.outline_bookmark_border_24
    val fillIcon = R.drawable.baseline_bookmark_24
    var id by rememberSaveable { mutableIntStateOf(outlineIcon) }
    Row (
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, end = 16.dp)

    ) {
        IconButton(onClick = { id = if (id == outlineIcon) fillIcon else outlineIcon }) {
            Icon(
                painter = painterResource(id = id),
                contentDescription = "Saved",
                tint = MaterialTheme.colorScheme.outline
            )
        }
        Text(
            text = "Applied",
            color = MaterialTheme.colorScheme.tertiary,
            style = MaterialTheme.typography.labelSmall
        )
    }
}