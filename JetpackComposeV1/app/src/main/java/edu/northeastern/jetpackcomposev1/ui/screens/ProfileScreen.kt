package edu.northeastern.jetpackcomposev1.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import edu.northeastern.jetpackcomposev1.R
import edu.northeastern.jetpackcomposev1.viewmodels.UserViewModel

@Composable
fun ProfileScreen(
    userViewModel: UserViewModel,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier.padding(horizontal = 8.dp)) {
        item {
            UserInfo(
                name = userViewModel.user.profile.name,
                bio = userViewModel.user.profile.bio,
                date = userViewModel.user.profile.date
            )
        }
    }
}

@Composable
fun UserInfo(
    name: String,
    bio: String,
    date: String,
    modifier: Modifier = Modifier
) {
    Column {
        Image(
            painter = painterResource(R.drawable.ic_launcher_foreground),
            contentDescription = "Avatar",
            modifier = Modifier.size(100.dp).clip(CircleShape)
                .border(1.dp, MaterialTheme.colorScheme.primary, CircleShape)
        )
        Text("Name: $name")
        Text("Bio: $bio")
        Text("Joined: $date")
    }
}