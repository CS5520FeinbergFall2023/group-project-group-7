package edu.northeastern.jetpackcomposev1.viewmodels

import android.annotation.SuppressLint
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
import edu.northeastern.jetpackcomposev1.models.job.JobFavoriteModel
import edu.northeastern.jetpackcomposev1.models.resume.ResumeModel
import edu.northeastern.jetpackcomposev1.utility.getCurrentZonedDateTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.util.UUID
import kotlin.math.log

class ResumeViewModel: ViewModel() {
    private val _isUpdateDialog = MutableStateFlow(false)
    val isUpdateDialog: StateFlow<Boolean> = _isUpdateDialog.asStateFlow()
    fun setsUpdateDialog(value: Boolean) {
        _isUpdateDialog.value = value
    }

    private val _isShowUploadAlert = MutableStateFlow(false)
    val isShowUploadAlert: StateFlow<Boolean> = _isShowUploadAlert.asStateFlow()
    fun setShowUploadAlert(value: Boolean) {
        _isShowUploadAlert.value = value
    }

    private var uploadProgress : Float = 0f

    val auth: FirebaseAuth = Firebase.auth
    val database: FirebaseDatabase = Firebase.database
    val storage: FirebaseStorage = Firebase.storage

    var newResume = ResumeModel()
    var updateResume = ResumeModel()
    var resumeList: SnapshotStateList<ResumeModel> = mutableStateListOf()

    val bouquetViewModel: BouquetViewModel = BouquetViewModel()

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
        val timestamp = System.currentTimeMillis()
        val filePath = "users/${auth.currentUser?.uid}/resumes/${displayName}_${timestamp}"
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
                if (resumeList.isEmpty()) {
                    val myRef = database.getReference("users/${auth.currentUser?.uid}/resumes")
                    myRef.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            for (snapshot in dataSnapshot.children) {
                                val resumeModel = snapshot.getValue(ResumeModel::class.java)
                                if (resumeModel != null && resumeModel.activeStatus == "true") {
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
    }

    fun handleViewEvent(viewEvent: ResumeViewEvent){
        when(viewEvent){
            is ResumeViewEvent.DeleteResume -> {
                val currentState = uiState.value
                var resReference = FirebaseDatabase.getInstance().getReference("users/${auth.currentUser?.uid}/resumes")
                resReference.orderByChild("nickName").equalTo(viewEvent.resume.nickName).addListenerForSingleValueEvent(
                    object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            for (snapshot in dataSnapshot.children) {
                                val resumeModel = snapshot.getValue(ResumeModel::class.java)
                                if (resumeModel != null) {
                                    snapshot.ref.child("activeStatus").setValue("false")
                                    val targetIndex = currentState.resumeList.indexOfFirst { it == viewEvent.resume }
                                    if (targetIndex != -1) {
                                        // find
                                        currentState.resumeList[targetIndex].activeStatus = "false"
                                        val updatedResumeList = currentState.resumeList.filterNot { it == viewEvent.resume }
                                        uiState.value = currentState.copy(resumeList = updatedResumeList)
                                    } else {
                                        Log.w("error in change activeStatus", "Not find target resume")
                                    }
                                }
                            }
                        }
                        override fun onCancelled(error: DatabaseError) {
                        }
                    }
                )
            }

            is ResumeViewEvent.AddResume -> {
                val currentState = uiState.value
                setShowUploadAlert(true)
                uiState.value = uiState.value.copy(resumeList = resumeList)
            }

            is ResumeViewEvent.UpdateResume -> {
                Log.d("input resume check update", updateResume.nickName + " " + updateResume.fileName + " " + updateResume.filePath + " " + updateResume.time)
                val currentState = uiState.value
                val targetIndex = currentState.resumeList.indexOfFirst { it == viewEvent.resume }
                var resReference = FirebaseDatabase.getInstance().getReference("users/${auth.currentUser?.uid}/resumes")
                resReference.orderByChild("filePath").equalTo(updateResume.filePath).addListenerForSingleValueEvent(
                    object : ValueEventListener {
                        @SuppressLint("SuspiciousIndentation")
                        override fun onDataChange(snapshot: DataSnapshot) {
                            for (snapshot in snapshot.children) {
                                val resumeModel = snapshot.getValue(ResumeModel::class.java)
                                    if (resumeModel != null && resumeModel.activeStatus == "true") {
                                        val curTime = getCurrentZonedDateTime()
                                        snapshot.ref.child("nickName").setValue(viewEvent.resume.nickName)
                                        snapshot.ref.child("time").setValue(curTime)
                                        if (targetIndex != -1) {
                                            // find
                                            currentState.resumeList[targetIndex].nickName = updateResume.nickName
                                            currentState.resumeList[targetIndex].time = curTime
                                            uiState.value = currentState.copy(resumeList = currentState.resumeList.toList())
                                        } else {
                                            Log.w("error in change activeStatus", "Not find target resume")
                                        }
                                    }
                            }
                        }
                        override fun onCancelled(error: DatabaseError) {
                        }
                    }
                )
            }
        }
    }

    fun replicatedLabelCheck() : Boolean{
        for (resume in resumeList) {
            if (resume.nickName == newResume.nickName && resume.activeStatus == "true") {
                return true
            }
        }
        return false
    }
}

data class ResumeListViewState(var isLoading:Boolean = true, val resumeList:List<ResumeModel> = emptyList())
sealed class ResumeViewEvent{
    object AddResume : ResumeViewEvent()
    data class DeleteResume(val resume : ResumeModel): ResumeViewEvent()
    data class UpdateResume(val resume : ResumeModel) : ResumeViewEvent()
}
