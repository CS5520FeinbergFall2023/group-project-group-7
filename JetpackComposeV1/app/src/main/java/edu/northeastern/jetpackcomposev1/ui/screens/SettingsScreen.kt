package edu.northeastern.jetpackcomposev1.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import edu.northeastern.jetpackcomposev1.viewmodels.UserViewModel

@Composable
fun SettingsScreen(
    userViewModel: UserViewModel,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier.padding(all = 8.dp)) {
        item {
            EditUserInfo(userViewModel)
        }
    }
}

@Composable
fun EditUserInfo(
    userViewModel: UserViewModel,
    modifier: Modifier = Modifier
) {
    var currentPassword: String by rememberSaveable { mutableStateOf("") }
    var newPassword: String by rememberSaveable { mutableStateOf("") }
    Column {
        OutlinedTextField(
            modifier = modifier.fillMaxWidth(),
            value = userViewModel.user.profile.email,
            onValueChange = {  },
            readOnly = true,
            label = { Text("Email") },
            leadingIcon = { Icon(imageVector = Icons.Outlined.Email, contentDescription = "Email") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            singleLine = true
        )
        OutlinedTextField(
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            value = currentPassword,
            onValueChange = { currentPassword = it.trim() },
            label = { Text("Current password") },
            leadingIcon = { Icon(imageVector = Icons.Outlined.Info, contentDescription = "Password") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            singleLine = true
        )
        OutlinedTextField(
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            value = newPassword,
            onValueChange = { newPassword = it.trim() },
            label = { Text("New password") },
            leadingIcon = { Icon(imageVector = Icons.Outlined.Info, contentDescription = "Password") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            supportingText = { if (newPassword.length in 1..5) { Text("At least 6 characters") } },
            singleLine = true
        )
        Divider()
        OutlinedTextField(
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            value = userViewModel.user.profile.name,
            onValueChange = { userViewModel.user.profile.name = it },
            label = { Text("Name") },
            leadingIcon = { Icon(imageVector = Icons.Outlined.Face, contentDescription = "Name") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            singleLine = true
        )
        OutlinedTextField(
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            value = userViewModel.user.profile.bio,
            onValueChange = { userViewModel.user.profile.bio = it },
            label = { Text("Bio") },
            leadingIcon = { Icon(imageVector = Icons.Outlined.Person, contentDescription = "Bio") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            singleLine = false
        )
    }
}