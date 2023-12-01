package edu.northeastern.jetpackcomposev1.ui.screens
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import edu.northeastern.jetpackcomposev1.viewmodels.ResumeViewModel
import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.firebase.storage.storageMetadata
import edu.northeastern.jetpackcomposev1.R
import edu.northeastern.jetpackcomposev1.models.resume.ResumeModel
import edu.northeastern.jetpackcomposev1.utility.getCurrentZonedDateTime
import edu.northeastern.jetpackcomposev1.viewmodels.ResumeViewEvent
import java.io.File
import java.time.format.DateTimeFormatter

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ResumesScreen(navController: NavController, viewModel: ResumeViewModel, modifier: Modifier = Modifier) {
    val viewState = viewModel.consumableState().collectAsState()
    val isShowAlert by viewModel.isShowAlert.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        val textState = remember { mutableStateOf(TextFieldValue("")) }

        SearchView(state = textState, placeholder = "Search here")

        val searchedText = textState.value.text
        // header
        Row(
            modifier = Modifier.padding(15.dp, 0.dp), verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Label",
                modifier = Modifier
                    .weight(0.7f)
            )
            Text(
                text = "Last Modified",
                modifier = Modifier
                    .weight(1.1f)
            )
            Text(
                text = "Preview",
                modifier = Modifier
                    .size(24.dp)
                    .weight(0.6f)
            )
            Text(
                text = "Delete",
                modifier = Modifier
                    .size(24.dp)
                    .weight(0.5f)
            )
        }

        Scaffold(
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        viewModel.handleViewEvent(ResumeViewEvent.AddResume)},
                ) {
                    Icon(Icons.Filled.Add,"")
                }
            }
            , content =
            {
                Log.d("check viewState.value", viewState.value.toString())
                if (viewState.value.resumeList.isEmpty()){
                    viewState.value.isLoading = false
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        Text(
                            text = "No resume uploaded",
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(16.dp)
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp)
                    )
                    {
                        if (viewState.value.resumeList != null){
                            itemsIndexed(viewState.value.resumeList.filter {
                                it.nickName.contains(searchedText, ignoreCase = true) and (it.activeStatus == "true")
                            }) { index, item ->
                                ResumeUI(resume = item,
                                    onDeleteClick = {viewModel.handleViewEvent(ResumeViewEvent.DeleteResume(it, index))},
                                    onPreviewClick = {navController.navigate("PdfViewUI/${it.filePath}")}
                                )
                                Divider()
                            }
                        }
                    }
                }
                if (viewState.value.isLoading) {
                    LoadingIndicator()
                }
            }
        )
    }

    if (isShowAlert) {
        Dialog(
            onDismissRequest = { viewModel.setShowAlert(false) },
            content = {
                InputDialog(viewModel,"Upload Resume")
            }
        )
    }
}

@Composable
fun ResumeUI(resume: ResumeModel, onDeleteClick:(resume:ResumeModel) -> Unit, onPreviewClick: (resume: ResumeModel) -> Unit){
    var openDialog by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .padding(2.dp, 10.dp)
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically
        ){
            // file label
            Text(
                text = resume.nickName,
                modifier = Modifier
                    .weight(0.7f)
                    .align(Alignment.CenterVertically))
            // file last modified date
            Text(
                text = resume.time?.take(19)?.replace("T", "\n") ?: "",
                modifier = Modifier
                    .weight(0.9f)
                    .align(Alignment.CenterVertically)
            )
            // preview
            IconButton(
                modifier = Modifier
                    .size(24.dp)
                    .weight(0.6f)
                    .align(Alignment.CenterVertically),
                onClick = {
                    onPreviewClick(resume)
                }
            ){ Icon(
                painterResource(id = R.drawable.view_24),
                contentDescription = "preview resume",
            )
            }
            // delete
            IconButton(
                modifier = Modifier
                    .size(24.dp)
                    .weight(0.5f)
                    .align(Alignment.CenterVertically),
                onClick = {
                    openDialog = true
                }
            ){ Icon(
                painterResource(id = R.drawable.delete_24),
                contentDescription = "delete resume")
            }
        }
    }

    if(openDialog){
        showAlertDialog(
            context = LocalContext.current,
            {
                onDeleteClick(resume)
                openDialog = false
            },
            { openDialog = false },
            "Delete",
            "Are you sure you want to delete this document",
            "OK", // continue
            "Cancel")
    }
}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun PdfViewUI(navController: NavController, mUrl: String) {
    Scaffold(
        content = {
            Column {
                // back to resumes
                IconButton(onClick = {navController.popBackStack()}) {
                    Icons.Filled.ArrowBack
                }

                // WebView to display the PDF
                AndroidView(factory = { context ->
                    WebView(context).apply {
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                        webViewClient = WebViewClient()
                        loadUrl(mUrl)
                    }
                }, update = { webView ->
                    webView.loadUrl(mUrl)
                })
            }
        }
    )
}

@Composable
fun SearchView(state: MutableState<TextFieldValue>, placeholder: String) {
    Row(modifier = Modifier
        .padding(8.dp)
        .fillMaxWidth(),verticalAlignment = Alignment.CenterVertically ){
        Image(painter = painterResource(id = R.drawable.ic_search_24) , contentDescription = "searchIcon")
        TextField(
            value = state.value,
            onValueChange = {value
                -> state.value = value
                true
            }, modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .clip(RoundedCornerShape(15.dp))
                .border(2.dp, color = Color.Gray, RoundedCornerShape(15.dp)),
            placeholder = {
                Text(text = placeholder)
            }
        )
    }
}

@Composable
fun LoadingIndicator(){
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
        CircularProgressIndicator()
    }
}

@Composable
fun showAlertDialog(
    context:Context,
    onConfirmation: () -> Unit,
    onCancel: () -> Unit,
    dialogTitle: String,
    dialogText: String,
    positive: String, // continue
    negative: String // undo
) {
    AlertDialog(
        title = {
            Text(text = dialogTitle)
        },
        text = {
            Text(text = dialogText)
        },
        onDismissRequest = {
            onCancel()
        },
        confirmButton = {
            TextButton(
                onClick = onConfirmation
            ) {
                Text(positive)
            }
        },
        dismissButton = {
            TextButton(
                onClick = onCancel
            ) {
                Text(negative)
            }
        }
    )
}

// input dialog (update, upload)
@Composable
fun InputDialog(
    viewModel: ResumeViewModel,
    text:String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .background(Color.White)
            .border(1.dp, Color.Black)
    ) {
        Text(text = text, fontSize = 26.sp, textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(3.dp))
        viewModel.newResume.nickName = ""
        LabelAndUpload(viewModel)
    }
}

@SuppressLint("Range")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LabelAndUpload(
    viewModel: ResumeViewModel,
) {
    var readyToUpload by remember { mutableStateOf(false) }
    var isNickNameEmpty by remember { mutableStateOf(true) }
    var pdfUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext
    var displayName: String = ""

    val pdfPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            pdfUri = uri
        }
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        val context = LocalContext.current
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = viewModel.newResume.nickName,
            onValueChange = { newValue ->
                viewModel.newResume.nickName = newValue
                isNickNameEmpty = newValue.isEmpty()
            },
            label = { Text("Label the selected file") },
            singleLine = true
        )
        if (pdfUri != null) {
            var cursor: Cursor? = null
            try {
                cursor = context.contentResolver.query(pdfUri!!, null, null, null, null, null)

                if (cursor != null && cursor.moveToFirst()) {
                    displayName =
                        cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                    viewModel.newResume.fileName = displayName

                }
            } finally {
                cursor?.close()
            }

            Text(text = "Selected file: " + displayName)
        }
        Button(modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
            onClick = {
                pdfPicker.launch("application/pdf")
            }
        ) {
            Text("Select a file to upload")
        }

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            onClick = {
                if (isNickNameEmpty) {
                    val toast =
                        Toast.makeText(context, "Please label your file", Toast.LENGTH_SHORT)
                    toast.show()
                } else if (viewModel.replicatedLabelCheck()) {
                    val toast = Toast.makeText(context, "Label exists", Toast.LENGTH_SHORT)
                    toast.show()
                } else if (pdfUri == null) {
                    val toast = Toast.makeText(context, "Please select a file", Toast.LENGTH_SHORT)
                    toast.show()
                }
                else {
                    readyToUpload = true
                }
            },
        ) {
            Text(
                text = "Upload File"
            )
        }
    }

    if (readyToUpload) {
        pdfUri?.let { viewModel.setResumeToStorage(pdfUri!!, displayName) }
        viewModel.setShowAlert(false)
        viewModel.handleViewEvent(ResumeViewEvent.UpdateView)
    }
}


