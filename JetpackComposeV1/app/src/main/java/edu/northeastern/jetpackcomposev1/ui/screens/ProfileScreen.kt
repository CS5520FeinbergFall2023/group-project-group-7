package edu.northeastern.jetpackcomposev1.ui.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import edu.northeastern.jetpackcomposev1.R
import edu.northeastern.jetpackcomposev1.models.user.ProfileModel
import edu.northeastern.jetpackcomposev1.utility.convertJoinedDate
import edu.northeastern.jetpackcomposev1.viewmodels.JobViewModel
import edu.northeastern.jetpackcomposev1.viewmodels.UserViewModel

@Composable
fun ProfileScreen(
    userViewModel: UserViewModel,
    jobViewModel: JobViewModel,
    onNavigateToSetting: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier.padding(horizontal = 8.dp)) {
        item {
            ProfileSection(profile = userViewModel.user.profile,onNavigateToSetting = onNavigateToSetting, jobViewModel = jobViewModel)
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ProfileSection(
    profile: ProfileModel,
    modifier: Modifier = Modifier,
    onNavigateToSetting: () -> Unit,
    jobViewModel: JobViewModel
) {
    val rainbowColorsBrush = remember {
        Brush.sweepGradient(
            listOf(
                Color(0xFF9575CD),
                Color(0xFFBA68C8),
                Color(0xFFE57373),
                Color(0xFFFFB74D),
                Color(0xFFFFF176),
                Color(0xFFAED581),
                Color(0xFF4DD0E1),
                Color(0xFF9575CD)
            )
        )
    }
    Column(
        modifier = modifier
            .padding(all = 16.dp)
            .fillMaxWidth()
            .fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center)
            {
                AsyncImage(
                    model = profile.avatar.filePath,
                    contentDescription = "Avatar",
                    placeholder = painterResource(R.drawable.ic_launcher_foreground),
                    error = painterResource(R.drawable.ic_launcher_foreground),
                    contentScale = ContentScale.Crop,
                    modifier = modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .border(2.dp, rainbowColorsBrush, CircleShape)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.Center) {
                Column(modifier = modifier.padding(start = 8.dp)) {
                    Text(
                        text = profile.name,
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = convertJoinedDate(profile.date),
                        color = MaterialTheme.colorScheme.secondary,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Row(modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween){
                Text(
                    text = "Search history: ${jobViewModel.jobSearchHistoryList.size}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "View history: ${jobViewModel.jobViewedHistoryList.size}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Favorite history: ${jobViewModel.jobFavoriteList.size}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            OutlinedCard(
                modifier = modifier
                    .padding(vertical = 16.dp)
                    .fillMaxWidth()
            ) {
                Spacer(modifier = modifier.padding(vertical = 4.dp))
                Text(profile.bio.ifEmpty { "Say hi to me!" },modifier = modifier.padding(8.dp))
            }

            Button(modifier = modifier.fillMaxWidth(),
                onClick = { onNavigateToSetting() }) {
                Text("Edit Profile")
            }
        }

    }
}

//previous version
// @Composable
//fun ProfileSection(
//    profile: ProfileModel,
//    modifier: Modifier = Modifier,
//    onNavigateToSetting: () -> Unit,
//) {
//    OutlinedCard(
//        modifier = modifier
//            .padding(vertical = 8.dp)
//            .fillMaxWidth()
//    ) {
//        Column(modifier = modifier.padding(all = 16.dp)) {
//            AsyncImage(
//                model = profile.avatar.filePath,
//                contentDescription = "Avatar",
//                placeholder = painterResource(R.drawable.ic_launcher_foreground),
//                error = painterResource(R.drawable.ic_launcher_foreground),
//                contentScale = ContentScale.Crop,
//                modifier = modifier
//                    .size(100.dp)
//                    .clip(CircleShape)
//                    .border(1.dp, MaterialTheme.colorScheme.outline, CircleShape)
//
//            )
//            Row(verticalAlignment = Alignment.Bottom) {
//                Column(modifier = modifier.padding(start = 8.dp)) {
//                    Text(
//                        text = profile.name,
//                        color = MaterialTheme.colorScheme.primary,
//                        style = MaterialTheme.typography.titleLarge
//                    )
//                    Text(
//                        text = convertJoinedDate(profile.date),
//                        color = MaterialTheme.colorScheme.secondary,
//                        style = MaterialTheme.typography.bodyMedium
//                    )
//                }
//            }
//            Spacer(modifier = modifier.padding(vertical = 4.dp))
//            Divider()
//            Spacer(modifier = modifier.padding(vertical = 4.dp))
//            Text(profile.bio.ifEmpty { "Say hi to me!" })
//        }
//    }
//}
