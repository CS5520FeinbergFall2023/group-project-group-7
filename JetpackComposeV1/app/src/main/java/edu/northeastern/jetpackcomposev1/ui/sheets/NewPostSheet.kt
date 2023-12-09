package edu.northeastern.jetpackcomposev1.ui.sheets

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import edu.northeastern.jetpackcomposev1.R
import edu.northeastern.jetpackcomposev1.models.job.JobSearchHistoryModel
import edu.northeastern.jetpackcomposev1.models.post.PostImageModel
import edu.northeastern.jetpackcomposev1.models.user.AvatarModel
import edu.northeastern.jetpackcomposev1.viewmodels.PostViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewPostSheet(
    postViewModel: PostViewModel,
    onCloseSheet: () -> Unit,
    modifier: Modifier = Modifier
) {
    val sheetState = rememberModalBottomSheetState()
    ModalBottomSheet(
        onDismissRequest = { onCloseSheet() },
        sheetState = sheetState,
    ) {
        // Sheet content
        LazyColumn(modifier = modifier
            .padding(horizontal = 8.dp)
            .fillMaxHeight(0.95f)
        ) {
            item {
                AddPost(
                    postViewModel = postViewModel,
                    onCloseSheet = onCloseSheet
                )
                Spacer(modifier = modifier.height(64.dp))
            }
        }
    }
}

@Composable
fun AddPost(
    postViewModel: PostViewModel,
    onCloseSheet: () -> Unit,
    modifier: Modifier = Modifier
) {
    val multiPhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(),
        onResult = { uris ->
            postViewModel.post.images = uris.map { uri -> PostImageModel(fileName = uri.lastPathSegment!!, filePath = uri.toString()) }
        }
    )
    Column {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = modifier.fillMaxWidth()
        ) {
            Text("What's new?")
            Text(
                text = if(postViewModel.post.text.isNotEmpty()) "Clear" else "",
                modifier = modifier.clickable {
                    postViewModel.post.text = ""
                    postViewModel.post.url = ""
                    postViewModel.post.images = emptyList()
                }
            )
        }
        OutlinedTextField(
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            value = postViewModel.post.text,
            onValueChange = { postViewModel.post.text = it },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            singleLine = false,
            minLines = 3
        )
        if(postViewModel.post.url.isNotEmpty()) {
            Text(
                text = "Job link added",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.labelMedium,
            )
        }
        LazyRow {
            items(postViewModel.post.images) { image ->
                AsyncImage(
                    model = image.filePath,
                    contentDescription = "Image",
                    placeholder = painterResource(R.drawable.ic_launcher_foreground),
                    error = painterResource(R.drawable.ic_launcher_foreground),
                    contentScale = ContentScale.Fit,
                    modifier = modifier
                        .padding(vertical = 4.dp)
                        .size(150.dp)
                        .clip(MaterialTheme.shapes.small)
                        .border(1.dp, MaterialTheme.colorScheme.outline, MaterialTheme.shapes.small)
                )
                Spacer(modifier = modifier.width(8.dp))
            }
        }
        Row(modifier = modifier.padding(vertical = 4.dp)) {
            Button(
                modifier = modifier.weight(0.5f),
                onClick = {
                    multiPhotoPickerLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                }
            ) {
                Text("Add Photo")
            }
            Spacer(modifier = modifier.width(8.dp))
            Button(
                modifier = modifier.weight(0.5f),
                onClick = {
                    if (postViewModel.post.images.isEmpty()) {
                        postViewModel.setPostToDB()
                    }
                    else {
                        postViewModel.setPostImageToStorage(0)
                    }
                    onCloseSheet()
                }
            ) {
                Text("Post")
            }
        }
    }
}