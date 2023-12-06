package edu.northeastern.jetpackcomposev1.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import edu.northeastern.jetpackcomposev1.viewmodels.UserViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignInScreen(
    userViewModel: UserViewModel,
    onNavigateToSignUp: () -> Unit,
    onNavigateToForgotPassword: () -> Unit,
    onNavigateToHome: () -> Unit,
    modifier: Modifier = Modifier
) {
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
                OutlinedTextField(
                    modifier = modifier.padding(top = 8.dp),
                    value = userViewModel.password,
                    onValueChange = { userViewModel.password = it.trim() },
                    label = { Text("Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    singleLine = true,
                    supportingText = {
                        if (userViewModel.password.length in 1..5) {
                            Text("At least 6 characters")
                        }
                    }
                )
                Button(
                    modifier = modifier.padding(top = 32.dp),
                    onClick = { userViewModel.signIn() },
                    enabled = userViewModel.user.profile.email != "" && userViewModel.password != ""
                ) {
                    Text("Sign In")
                }
                OutlinedButton(
                    modifier = modifier.padding(top = 8.dp),
                    onClick = onNavigateToSignUp
                ) {
                    Text("Sign Up")
                }
                TextButton(
                    modifier = modifier.padding(top = 8.dp),
                    onClick = onNavigateToForgotPassword
                ) {
                    Text("Forgot Password")
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
    if (userViewModel.isSignedIn) {
        onNavigateToHome()
    }
//    Log.d("debug", "Sign in render finished")
}

@Composable
fun ShowCircularProgressIndicator(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(
            modifier = modifier.size(64.dp),
            color = MaterialTheme.colorScheme.secondary,
        )
    }
}