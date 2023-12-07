package edu.northeastern.jetpackcomposev1.ui.sheets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
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
    Column {
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
        Button(
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            onClick = {
                postViewModel.setPostToDB()
                onCloseSheet()
            }
        ) {
            Text("Post")
        }
    }
}