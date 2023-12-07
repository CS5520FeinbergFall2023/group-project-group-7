package edu.northeastern.jetpackcomposev1.ui.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material3.Divider
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import edu.northeastern.jetpackcomposev1.R
import edu.northeastern.jetpackcomposev1.models.post.PostModel
import edu.northeastern.jetpackcomposev1.models.user.ProfileModel
import edu.northeastern.jetpackcomposev1.ui.sheets.NewPostSheet
import edu.northeastern.jetpackcomposev1.utility.convertToDate
import edu.northeastern.jetpackcomposev1.viewmodels.PostViewModel
import edu.northeastern.jetpackcomposev1.viewmodels.UserViewModel

@Composable
fun ProfileScreen(
    userViewModel: UserViewModel,
    postViewModel: PostViewModel,
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
    ){ innerPadding -> innerPadding
        if (postViewModel.running) {
            ShowCircularProgressIndicator()
        }
        else {
            LazyColumn(modifier = modifier.padding(horizontal = 8.dp)) {
                item {
                    ProfileSection(profile = userViewModel.user.profile)
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
    modifier: Modifier = Modifier
) {
    OutlinedCard(modifier = modifier
        .padding(top = 8.dp)
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
                        text = "Joined ${convertToDate(profile.date)}",
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

