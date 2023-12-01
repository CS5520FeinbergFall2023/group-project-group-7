package edu.northeastern.jetpackcomposev1.viewmodels

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.storage
import edu.northeastern.jetpackcomposev1.models.resume.ResumeModel
import edu.northeastern.jetpackcomposev1.ui.screens.InputDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class ResumeViewModel: ViewModel() {
    private val _isShowAlert = MutableStateFlow(false)
    val isShowAlert: StateFlow<Boolean> = _isShowAlert.asStateFlow()
    fun setShowAlert(value: Boolean) {
        _isShowAlert.value = value
    }
    private var uploadProgress : Float = 0f

    val auth: FirebaseAuth = Firebase.auth
    val database: FirebaseDatabase = Firebase.database
    val storage: FirebaseStorage = Firebase.storage

    var newResume = ResumeModel()
    var resumeList: SnapshotStateList<ResumeModel> = mutableStateListOf()

    private val uiState = MutableStateFlow(ResumeListViewState(false, resumeList= resumeList ))
    fun consumableState() = uiState.asStateFlow()

    fun getResumeFromStorage(resumeId: String, filePath: String) {
        /*TODO: get .pdf from the google storage for preview*/
    }

    fun setResumeToDB(resume: ResumeModel) {
        resumeList.add(resume)
        database.getReference("users/${auth.currentUser?.uid}/resumes").setValue(resumeList.toList())
    }

    @Composable
    fun setResumeToStorage(pdfUri:Uri, displayName:String) {
        Log.d("debug uri input", pdfUri.toString())
        val filePath = "users/${auth.currentUser?.uid}/resumes/${displayName}"
        val storageRef = storage.reference.child(filePath)
        val uploadTask = storageRef.putFile(pdfUri)

        uploadTask.addOnProgressListener { snapshot ->
            val progress = (100.0 * snapshot.bytesTransferred / snapshot.totalByteCount).toFloat()
            uploadProgress = progress
        }.addOnFailureListener {
            Log.w("debug", "Fail to upload file")
        }.continueWithTask { task ->
            if (!task.isSuccessful) {
                throw task.exception!!
            }
            storageRef.downloadUrl
        }.addOnCompleteListener { downloadUrlTask ->
            if (downloadUrlTask.isSuccessful) {
                val url = downloadUrlTask.result
                newResume.filePath = url.toString()
                setResumeToDB(newResume)
                newResume = ResumeModel()
            } else {
                Log.w("debug", "Failed to get download URL.")
            }
        }
        LinearProgressIndicator(
            progress = uploadProgress / 100f,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
    }

    fun getResumeFromDB() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val myRef = database.getReference("users/${auth.currentUser?.uid}/resumes")
                myRef.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        for (snapshot in dataSnapshot.children) {
                            val resumeModel = snapshot.getValue(ResumeModel::class.java)
                            if (resumeModel != null && resumeModel.activeStatus) {
                                resumeList.add(resumeModel)
                            }
                        }
                    }
                    override fun onCancelled(error: DatabaseError) {
                        // Failed to read value
                        Log.w("debug", "Failed to read resumes from DB.", error.toException())
                    }
                })
            }
        }
    }

    fun handleViewEvent(viewEvent: ResumeViewEvent){
        when(viewEvent){
            is ResumeViewEvent.DeleteResume -> {
                val currentState = uiState.value
                Log.d("removed index", viewEvent.index.toString())
                Log.d("auth id check", (auth.uid !== null).toString())


//                method 1: fail
//                val resumeRef = database.getReference("users/${auth.currentUser?.uid}/resumes/${viewEvent.index}")
//                val updates = mapOf<String, Any>("activeStatus" to false)
//                resumeRef.updateChildren(updates)

//                method 2: fail
//                database.getReference("users/${auth.currentUser?.uid}/resumes/${viewEvent.index}/activeStatus").update(false)
//                    .addOnCompleteListener { task ->
//                        if (task.isSuccessful) {
//                            Log.d("Firebase", "activeStatus updated successfully")
//                        } else {
//                            val error = task.exception
//                            Log.e("Firebase", "Error updating activeStatus", error)
//                        }
//                    }

//                method 3: processing 
//                similiar to update replace with a new resume model

                database.getReference("users/${auth.currentUser?.uid}/resumes").setValue(resumeList.toList())
                val items = currentState.resumeList.toMutableList().apply {
                    remove(viewEvent.resume)
                }.toList()
                uiState.value = uiState.value.copy(resumeList = items)
            }

            is ResumeViewEvent.AddResume -> {
                val currentState = uiState.value
                setShowAlert(true)
                uiState.value = uiState.value.copy(resumeList = resumeList)
            }
        }
    }

    fun replicatedLabelCheck() : Boolean{
        for (resume in resumeList) {
            if (resume.nickName == newResume.nickName) {
                return true
            }
        }
        return false
    }
}

data class ResumeListViewState(var isLoading:Boolean = true, val resumeList:List<ResumeModel> = emptyList())
sealed class ResumeViewEvent{
    object AddResume : ResumeViewEvent()
    data class DeleteResume(val resume : ResumeModel, val index : Int): ResumeViewEvent()
}
