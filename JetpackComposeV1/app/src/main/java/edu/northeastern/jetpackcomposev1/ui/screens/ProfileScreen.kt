package edu.northeastern.jetpackcomposev1.ui.screens

import android.content.Intent
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Divider
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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
        LazyColumn(modifier = modifier.padding(horizontal = 8.dp)) {
            item {
                ProfileSection(profile = userViewModel.user.profile)
                PostLists(
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

@Composable
fun ProfileSection(
    profile: ProfileModel,
    modifier: Modifier = Modifier
) {
    OutlinedCard(modifier = modifier
        .padding(top = 8.dp, bottom = 4.dp)
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
fun PostLists(
    postList: SnapshotStateList<PostModel>,
    onGetPostProfileFromDB: (Int) -> Unit,
    onFindUserInLikes: (Int) -> Boolean,
    onSetPostLikeToDB: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    postList.forEachIndexed { index, post ->
        onGetPostProfileFromDB(index)
        PostCard(
            index = index,
            post = post,
            onFindUserInLikes = onFindUserInLikes,
            onSetPostLikeToDB = onSetPostLikeToDB
        )
    }
    Spacer(modifier = modifier.height(4.dp))
}

@Composable
fun PostCard(
    index: Int,
    post: PostModel,
    onFindUserInLikes: (Int) -> Boolean,
    onSetPostLikeToDB: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedCard(modifier = modifier.padding(vertical = 4.dp)) {
        Column(modifier = modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                AsyncImage(
                    model = post.avatar_filePath,
                    contentDescription = "Avatar",
                    placeholder = painterResource(R.drawable.ic_launcher_foreground),
                    error = painterResource(R.drawable.ic_launcher_foreground),
                    contentScale = ContentScale.Crop,
                    modifier = modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .border(1.dp, MaterialTheme.colorScheme.outline, CircleShape)
                )
                Column(modifier = modifier.padding(start = 8.dp)) {
                    Text(
                        text = post.user_name,
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleSmall
                    )
                    Text(
                        text = convertToDate(post.time),
                        color = MaterialTheme.colorScheme.secondary,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
            Spacer(modifier = modifier.padding(vertical = 4.dp))
            Divider()
            Spacer(modifier = modifier.padding(vertical = 4.dp))
            Text(post.text)
            PostCardFoot(
                index = index,
                post = post,
                onFindUserInLikes = onFindUserInLikes,
                onSetPostLikeToDB = onSetPostLikeToDB
            )
        }
    }
}

@Composable
fun PostCardFoot(
    index: Int,
    post: PostModel,
    onFindUserInLikes: (Int) -> Boolean,
    onSetPostLikeToDB: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val outlineIcon = Icons.Outlined.FavoriteBorder
    val fillIcon = Icons.Outlined.Favorite
    val context = LocalContext.current
    val sendIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, post.text)
        type = "text/plain"
    }
    val sharePostIntent = Intent.createChooser(sendIntent, null)
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier.fillMaxWidth()
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { onSetPostLikeToDB(index) }) {
                Icon(
                    imageVector = if (onFindUserInLikes(index)) fillIcon else outlineIcon,
                    contentDescription = "Like",
                    tint = if (onFindUserInLikes(index)) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.outline
                )
            }
            Text(text = if(post.likes.isNotEmpty()) post.likes.size.toString() else "")
        }
        IconButton(onClick = { context.startActivity(sharePostIntent) }) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_share_24),
                contentDescription = "Share",
                tint = MaterialTheme.colorScheme.outline
            )
        }
    }
}