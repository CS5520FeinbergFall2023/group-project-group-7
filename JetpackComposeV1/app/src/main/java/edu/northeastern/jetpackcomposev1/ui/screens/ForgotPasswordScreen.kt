package edu.northeastern.jetpackcomposev1.ui.screens

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import edu.northeastern.jetpackcomposev1.viewmodels.UserViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordScreen(
    userViewModel: UserViewModel,
    onNavigateToSignIn: () -> Unit,
    onNavigateToSignUp: () -> Unit,
    modifier: Modifier = Modifier
) {
//    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    Scaffold(snackbarHost = { SnackbarHost(hostState = snackbarHostState) }) {innerPadding ->
        if (userViewModel.running) {
            ShowCircularProgressIndicator()
        }
        else {
            Column(
                modifier = modifier.padding(innerPadding).fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedTextField(
                    value = userViewModel.user.profile.email,
                    onValueChange = { userViewModel.user.profile.email = it.trim() },
                    label = { Text("Email") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    singleLine = true
                )
                Button(
                    modifier = modifier.padding(top = 32.dp),
                    onClick = { userViewModel.forgotPassword() },
                    enabled = userViewModel.user.profile.email != ""
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
        }
    }// scaffold
    LaunchedEffect(key1 = userViewModel.messageReturned) {
        if (userViewModel.authMessage.isNotEmpty()) {
            snackbarHostState.showSnackbar(userViewModel.authMessage)
            userViewModel.authMessage = ""
        }
    }
//    Log.d("debug", "Forgot password render finished")
}
