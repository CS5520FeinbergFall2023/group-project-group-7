package edu.northeastern.jetpackcomposev1.ui.screens

import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import edu.northeastern.jetpackcomposev1.R
import edu.northeastern.jetpackcomposev1.models.user.ProfileModel
import edu.northeastern.jetpackcomposev1.utility.convertJoinedDate
import edu.northeastern.jetpackcomposev1.viewmodels.UserViewModel

@Composable
fun ProfileScreen(
    userViewModel: UserViewModel,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier.padding(horizontal = 8.dp)) {
        item {
            ProfileSection(profile = userViewModel.user.profile)
        }
    }
}

@Composable
fun ProfileSection(
    profile: ProfileModel,
    modifier: Modifier = Modifier
) {
    OutlinedCard(modifier = modifier
        .padding(vertical = 8.dp)
        .fillMaxWidth()) {
        Column(modifier = modifier.padding(all = 16.dp)) {
            Row(verticalAlignment = Alignment.Bottom) {
                AsyncImage(
                    model = profile.avatar.filePath,
                    contentDescription = "Avatar",
                    placeholder = painterResource(R.drawable.ic_launcher_foreground),
                    error = painterResource(R.drawable.ic_launcher_foreground),
                    contentScale = ContentScale.Crop,
                    modifier = modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .border(1.dp, MaterialTheme.colorScheme.outline, CircleShape)
                )
                Column(modifier = modifier.padding(start = 8.dp)) {
                    Text(
                        text = profile.name,
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(
                        text = convertJoinedDate(profile.date),
                        color = MaterialTheme.colorScheme.secondary,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
            Spacer(modifier = modifier.padding(vertical = 4.dp))
            Divider()
            Spacer(modifier = modifier.padding(vertical = 4.dp))
            Text(profile.bio.ifEmpty { "Say hi to me!" })
        }
    }
}