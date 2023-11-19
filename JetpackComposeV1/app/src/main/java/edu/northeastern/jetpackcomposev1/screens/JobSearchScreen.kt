package edu.northeastern.jetpackcomposev1.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import edu.northeastern.jetpackcomposev1.ui.theme.JetpackComposeV1Theme

/**
 * This is a file to store all composable functions related to the job search screen.
 * This is not a class, we put composable / helper functions here for better organization.
 * In the MainActivity.kt, we will call your screen by calling your root composable function.
 * Warp all of other composable functions under the Root composable function.
 * If you want to show one UI, just call the corresponding composable function.
 * You need to put the annotation @Composable to tell the system this function is a composable.
 * Capitalize the function name if this is a composable function.
 * Regular helper functions do not need the annotation.
 * The concept of Activity or Fragment is gone, there is no XML files anymore.
 * You can think of the entire architecture as a huge MainActivity.kt calling different composable
 * functions to render different screen UI.
 * use MaterialTheme.colorScheme.* to get color.
 * use MaterialTheme.typography.* to set text style.
 * Use @Preview to preview each composable. Use "Split" instead of "Code" to see preview(top right).
 */

@Composable
fun RootJobSearch(modifier: Modifier = Modifier) {
    SearchSection()

}

// preview the entire screen
@Preview
@Composable
fun PreviewRootJobSearch(){
    JetpackComposeV1Theme {
        RootJobSearch()
    }
}

@Composable
fun SearchSection(modifier: Modifier = Modifier) {

}

// we can preview individual UI also
@Preview
@Composable
fun PreviewSearchSection(){
    JetpackComposeV1Theme {
        SearchSection()
    }
}

@Composable
fun JobResults(modifier: Modifier = Modifier) {

}

@Composable
fun JobCard(modifier: Modifier = Modifier) {

}

/** ------------------------ helper functions ---------------------- **/

