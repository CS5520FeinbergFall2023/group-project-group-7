package edu.northeastern.jetpackcomposev1.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel


import edu.northeastern.jetpackcomposev1.models.AccountViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(onNavigateToSignIn: () -> Unit, onNavigateToForgotPassword: () -> Unit, modifier: Modifier = Modifier) {
    val accountViewModel: AccountViewModel = viewModel()
    val scope = rememberCoroutineScope()
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
            OutlinedTextField(
                modifier = modifier.padding(top = 8.dp),
                value = accountViewModel.password,
                onValueChange = { accountViewModel.password = it },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true
            )
            OutlinedTextField(
                modifier = modifier.padding(top = 8.dp),
                value = accountViewModel.name,
                onValueChange = { accountViewModel.name = it },
                label = { Text("Name") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                singleLine = true
            )
            Button(
                modifier = modifier.padding(top = 32.dp),
                onClick = { accountViewModel.signUp() },
                enabled = accountViewModel.email != "" && accountViewModel.password != "" && accountViewModel.name != ""
            ) {
                Text("Sign Up")
            }
            OutlinedButton(
                modifier = modifier.padding(top = 8.dp),
                onClick = onNavigateToSignIn
            ) {
                Text("Sign In")
            }
            TextButton(
                modifier = modifier.padding(top = 8.dp),
                onClick = onNavigateToForgotPassword
            ) {
                Text("Forgot Password")
            }
        }
        LaunchedEffect(key1 = accountViewModel.buttonClickCount) {
            if (accountViewModel.authMessage.isNotEmpty()) {
                scope.launch { snackbarHostState.showSnackbar(accountViewModel.authMessage) }
            }
        }
    }// scaffold
}
