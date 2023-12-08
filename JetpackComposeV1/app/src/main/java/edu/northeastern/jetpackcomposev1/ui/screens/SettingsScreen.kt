package edu.northeastern.jetpackcomposev1.ui.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import edu.northeastern.jetpackcomposev1.R
import edu.northeastern.jetpackcomposev1.models.user.AvatarModel
import edu.northeastern.jetpackcomposev1.viewmodels.JobViewModel
import edu.northeastern.jetpackcomposev1.viewmodels.UserViewModel

@Composable
fun SettingsScreen(
    userViewModel: UserViewModel,
    jobViewModel: JobViewModel,
    modifier: Modifier = Modifier
) {
    val snackbarHostState = remember { SnackbarHostState() }
    Scaffold(snackbarHost = { SnackbarHost(hostState = snackbarHostState) }) { innerPadding -> innerPadding
        if (userViewModel.running) {
            ShowCircularProgressIndicator()
        }
        else {
            LazyColumn(modifier = modifier.padding(horizontal = 8.dp)) {
                item {
                    SettingProfileSection(userViewModel)
                    Divider()
                    SettingPasswordSection(userViewModel)
                    Divider()
                    SettingHistorySection(jobViewModel)
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
}

@Composable
fun SettingProfileSection(
    userViewModel: UserViewModel,
    modifier: Modifier = Modifier
) {
    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            if (uri != null) {
                userViewModel.user.profile.avatar = AvatarModel(fileName = uri.lastPathSegment!!, filePath = uri.toString())
                userViewModel.setAvatarToStorage()
            }
        }
    )
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(vertical = 4.dp)
    ) {
        AsyncImage(
            model = userViewModel.user.profile.avatar.filePath,
            contentDescription = "Avatar",
            placeholder = painterResource(R.drawable.ic_launcher_foreground),
            error = painterResource(R.drawable.ic_launcher_foreground),
            contentScale = ContentScale.Crop,
            modifier = modifier
                .padding(vertical = 4.dp)
                .size(100.dp)
                .clip(CircleShape)
                .border(1.dp, MaterialTheme.colorScheme.outline, CircleShape)
                .clickable { singlePhotoPickerLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) }
        )
        OutlinedTextField(
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
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
                .padding(vertical = 4.dp),
            value = userViewModel.user.profile.bio,
            onValueChange = { userViewModel.user.profile.bio = it },
            label = { Text("Bio") },
            leadingIcon = { Icon(imageVector = Icons.Outlined.Person, contentDescription = "Bio") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            singleLine = false
        )
        Button(
            modifier = modifier
                .width(250.dp)
                .padding(vertical = 4.dp),
            onClick = { userViewModel.setProfileToDB() }
        ) {
            Text("Save")
        }
    }
}

@Composable
fun SettingPasswordSection(
    userViewModel: UserViewModel,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(vertical = 4.dp)
    ) {
        OutlinedTextField(
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            value = userViewModel.user.profile.email,
            onValueChange = {},
            readOnly = true,
            label = { Text("Email") },
            leadingIcon = { Icon(imageVector = Icons.Outlined.Email, contentDescription = "Email") },
            singleLine = true
        )
        OutlinedTextField(
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            value = userViewModel.password,
            onValueChange = { userViewModel.password = it.trim() },
            label = { Text("Current password") },
            leadingIcon = { Icon(imageVector = Icons.Outlined.Info, contentDescription = "Password") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            singleLine = true
        )
        OutlinedTextField(
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            value = userViewModel.newPassword,
            onValueChange = { userViewModel.newPassword = it.trim() },
            label = { Text("New password") },
            leadingIcon = { Icon(imageVector = Icons.Outlined.Info, contentDescription = "Password") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            supportingText = { if (userViewModel.newPassword.length in 1..5) { Text("At least 6 characters") } },
            singleLine = true
        )
        Button(
            modifier = modifier
                .width(250.dp)
                .padding(vertical = 4.dp),
            onClick = { userViewModel.setPasswordToAuth() }
        ) {
            Text("Change Password")
        }
    }
}

@Composable
fun SettingHistorySection(
    jobViewModel: JobViewModel,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Search History: ${jobViewModel.jobSearchHistoryList.size}",
            modifier = modifier.padding(vertical = 4.dp).fillMaxWidth()
        )
        Button(
            modifier = modifier
                .width(250.dp)
                .padding(vertical = 4.dp),
            onClick = { jobViewModel.clearJobSearchHistoryToDB() }
        ) {
            Text("Clear Search")
        }
        Text(
            text = "View History: ${jobViewModel.jobViewedHistoryList.size}",
            modifier = modifier.padding(vertical = 4.dp).fillMaxWidth()
        )
        Button(
            modifier = modifier
                .width(250.dp)
                .padding(vertical = 4.dp),
            onClick = { jobViewModel.clearJobViewedHistoryToDB() }
        ) {
            Text("Clear View")
        }
        Text(
            text = "Favorite History: ${jobViewModel.jobFavoriteList.size}",
            modifier = modifier.padding(vertical = 4.dp).fillMaxWidth()
        )
        Button(
            modifier = modifier
                .width(250.dp)
                .padding(vertical = 4.dp),
            onClick = { jobViewModel.clearJobFavoriteToDB() }
        ) {
            Text("Clear Favorite")
        }
    }
}