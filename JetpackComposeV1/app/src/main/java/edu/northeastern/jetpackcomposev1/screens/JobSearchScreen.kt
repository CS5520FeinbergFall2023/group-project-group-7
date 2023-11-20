package edu.northeastern.jetpackcomposev1.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import edu.northeastern.jetpackcomposev1.ui.theme.JetpackComposeV1Theme

@Composable
fun JobSearchScreen(modifier: Modifier = Modifier) {
//    SearchSection()
    Text("This is the job search screen")
}

@Composable
fun SearchSection(modifier: Modifier = Modifier) {

}

// we can preview individual UI also
@Preview
@Composable
fun PreviewJobSearchScreen(){
    JetpackComposeV1Theme {
        JobSearchScreen()
    }
}

@Composable
fun JobLists(modifier: Modifier = Modifier) {

}

@Composable
fun JobCard(modifier: Modifier = Modifier) {
    JobContent()
}

@Composable
fun JobContent(modifier: Modifier = Modifier) {
    Column {
        Text("This is the job title")
        Text("Company Name Here")
        Text("Las Vegas, NV")
        Text("$10,000 / year")
        Text("2 weeks ago")
    }
}

data class Job(
    val title: String,
    val company: String,
    val location: String,
    val salary: String,
    val date: String
    )
