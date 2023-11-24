package edu.northeastern.jetpackcomposev1.viewmodels

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage
import edu.northeastern.jetpackcomposev1.models.resume.ResumeModel

class ResumeViewModel: ViewModel() {
    val auth: FirebaseAuth = Firebase.auth
    val database: FirebaseDatabase = Firebase.database
    val storage: FirebaseStorage = Firebase.storage

    var resumeList: SnapshotStateList<ResumeModel> = mutableStateListOf()

    fun getResumeFromStorage(resumeId: String, filePath: String) {
        /*TODO: get .pdf from the google storage for preview*/
    }
    fun setResumeToStorage(pdfUri: Uri) {
        /*TODO: read .pdf from phone, save to google storage*/
        val fileName: String = ""
        val filePath: String = "users/${auth.currentUser?.uid}/resumes/$fileName"
        val storageRef = storage.reference.child(filePath)
        /*TODO: convert file data and upload*/
        //val uploadTask = storageRef.putBytes(pdfData)
    }

    fun getResumeFromDB() {
        val myRef = database.getReference("users/${auth.currentUser?.uid}/resumes")
        myRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snapshot in dataSnapshot.children) {
                    val resumeModel = snapshot.getValue(ResumeModel::class.java)
                    if (resumeModel != null) {
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
    fun setResumeToDB(resume: ResumeModel) {
        // TODO: you might need to redesign this function to fit your design
        resumeList.add(resume)
        database.getReference("users/${auth.currentUser?.uid}/resumes").setValue(resumeList.toList())
    }
}