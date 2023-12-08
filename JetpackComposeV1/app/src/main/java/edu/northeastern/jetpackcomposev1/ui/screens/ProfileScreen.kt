package edu.northeastern.jetpackcomposev1.ui.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
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
import edu.northeastern.jetpackcomposev1.models.post.PostModel
import edu.northeastern.jetpackcomposev1.models.user.ProfileModel
import edu.northeastern.jetpackcomposev1.ui.sheets.NewPostSheet
import edu.northeastern.jetpackcomposev1.utility.convertToDate
import edu.northeastern.jetpackcomposev1.viewmodels.JobViewModel
import edu.northeastern.jetpackcomposev1.viewmodels.PostViewModel
import edu.northeastern.jetpackcomposev1.viewmodels.UserViewModel

@Composable
fun ProfileScreen(
    userViewModel: UserViewModel,
    jobViewModel: JobViewModel,
    postViewModel: PostViewModel,
    onNavigateToSetting: () -> Unit,
    modifier: Modifier = Modifier
) {
    // sub sheets are here
    var showNewPostSheet by rememberSaveable { mutableStateOf(false) }
    if (showNewPostSheet) {
        NewPostSheet(
            postViewModel = postViewModel,
            onCloseSheet = { showNewPostSheet = false }
        )
    }
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showNewPostSheet = true }) {
                Icon(Icons.Outlined.Create, contentDescription = "Post")
            }
        }
    ) { innerPadding -> innerPadding
        if (postViewModel.running) {
            ShowCircularProgressIndicator()
        }
        else {
            LazyColumn(modifier = modifier.padding(horizontal = 8.dp)) {
                item {
                    ProfileSection(
                        profile = userViewModel.user.profile,
                        jobViewModel = jobViewModel,
                        onNavigateToSetting = onNavigateToSetting
                    )
                    PrivatePostLists(
                        userId = userViewModel.user.id,
                        postList = postViewModel.postList,
                        onGetPostProfileFromDB = { index -> postViewModel.getPostProfileFromDB(index) },
                        onFindUserInLikes = { index -> postViewModel.findUserInLikes(index) },
                        onSetPostLikeToDB = { index -> postViewModel.setPostLikeToDB(index) }
                    )
                    Spacer(modifier = modifier.height(68.dp))
                }
            }
        }
    }
}

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
        modifier = modifier.padding(top = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // avatar here
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
        // name and date here
        Text(
            text = profile.name,
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Joined ${convertToDate(profile.date)}",
            color = MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.bodyMedium
        )
        // history here
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = modifier.fillMaxWidth().padding(top = 8.dp)
        ) {
            Text(
                text = "Search ${jobViewModel.jobSearchHistoryList.size}",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "View ${jobViewModel.jobViewedHistoryList.size}",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Favorite ${jobViewModel.jobFavoriteList.size}",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
        }
        // bio here
        OutlinedCard(
            modifier = modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 8.dp)
        ) {
            Column(modifier = modifier.padding(all = 16.dp)) {
                Text(profile.bio.ifEmpty { "Say hi to me!" })
            }
        }
        // last button here
        Button(
            modifier = modifier.width(250.dp),
            onClick = { onNavigateToSetting() }
        ) {
            Text("Edit Profile")
        }
    }
}

@Composable
fun PrivatePostLists(
    userId: String,
    postList: SnapshotStateList<PostModel>,
    onGetPostProfileFromDB: (Int) -> Unit,
    onFindUserInLikes: (Int) -> Boolean,
    onSetPostLikeToDB: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Spacer(modifier = modifier.height(4.dp))
    postList.forEachIndexed { index, post ->
        if (post.user_id == userId) {
            onGetPostProfileFromDB(index)
            PostCard(
                index = index,
                post = post,
                onFindUserInLikes = onFindUserInLikes,
                onSetPostLikeToDB = onSetPostLikeToDB
            )
        }
    }
    Spacer(modifier = modifier.height(4.dp))
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