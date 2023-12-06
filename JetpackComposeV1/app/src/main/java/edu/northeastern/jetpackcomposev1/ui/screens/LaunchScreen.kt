package edu.northeastern.jetpackcomposev1.ui.screens

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import edu.northeastern.jetpackcomposev1.viewmodels.UserViewModel

@Composable
fun LaunchScreen(
    userViewModel: UserViewModel,
    onNavigateToHome: () -> Unit,
    onNavigateToSignIn: () -> Unit,
    modifier: Modifier = Modifier
) {
    Log.d("debug", "Signed in: " + userViewModel.isSignedIn.toString())
    if (userViewModel.isSignedIn) {
        userViewModel.getCurrentUserFromDB()
        onNavigateToHome()
    } else {
        onNavigateToSignIn()
    }
    Log.d("debug", "Launch render finished")
}