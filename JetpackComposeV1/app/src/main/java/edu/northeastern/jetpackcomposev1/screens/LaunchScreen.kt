package edu.northeastern.jetpackcomposev1.screens

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import edu.northeastern.jetpackcomposev1.models.AccountViewModel

@Composable
fun LaunchScreen(
    accountViewModel: AccountViewModel,
    onNavigateToHome: () -> Unit,
    onNavigateToSignIn: () -> Unit
) {
    Log.d("debug", "Signed in: " + accountViewModel.isSignedIn.toString())
    if (accountViewModel.isSignedIn) {
        onNavigateToHome()
    } else {
        onNavigateToSignIn()
    }
    Log.d("debug", "Launch render finished")
}