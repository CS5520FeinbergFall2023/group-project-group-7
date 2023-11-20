package edu.northeastern.jetpackcomposev1.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import edu.northeastern.jetpackcomposev1.models.AccountViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordScreen(
    accountViewModel: AccountViewModel,
    onNavigateToSignIn: () -> Unit,
    onNavigateToSignUp: () -> Unit,
    modifier: Modifier = Modifier
) {
//    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    Scaffold(snackbarHost = { SnackbarHost(hostState = snackbarHostState) }) {innerPadding ->
        Column(
            modifier = modifier.padding(innerPadding).fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = accountViewModel.email,
                onValueChange = { accountViewModel.email = it },
                label = { Text("Email") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true
            )
            Button(
                modifier = modifier.padding(top = 32.dp),
                onClick = { accountViewModel.forgotPassword() },
                enabled = accountViewModel.email != ""
            ) {
                Text("Forgot Password")
            }
            TextButton(
                modifier = modifier.padding(top = 8.dp),
                onClick = onNavigateToSignIn
            ) {
                Text("Sign In")
            }
            TextButton(
                modifier = modifier.padding(top = 8.dp),
                onClick = onNavigateToSignUp
            ) {
                Text("Sign Up")
            }
        }
    }// scaffold
    LaunchedEffect(key1 = accountViewModel.messageReturned) {
        if (accountViewModel.authMessage.isNotEmpty()) {
            snackbarHostState.showSnackbar(accountViewModel.authMessage)
            accountViewModel.authMessage = ""
        }
    }
//    Log.d("debug", "Forgot password render finished")
}
