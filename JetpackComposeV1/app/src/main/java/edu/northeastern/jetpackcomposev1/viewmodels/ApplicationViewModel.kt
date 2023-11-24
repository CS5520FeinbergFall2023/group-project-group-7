package edu.northeastern.jetpackcomposev1.viewmodels

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
import edu.northeastern.jetpackcomposev1.models.job.JobApplicationModel
import edu.northeastern.jetpackcomposev1.models.job.JobModel
import edu.northeastern.jetpackcomposev1.models.resume.ResumeModel

class ApplicationViewModel: ViewModel() {
    val auth: FirebaseAuth = Firebase.auth
    val database: FirebaseDatabase = Firebase.database
    val storage: FirebaseStorage = Firebase.storage

    var jobApplicationList: SnapshotStateList<JobApplicationModel> = mutableStateListOf()

    fun getJobApplicationFromDB() {
        val myRef = database.getReference("users/${auth.currentUser?.uid}/jobApplications")
        myRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snapshot in dataSnapshot.children) {
                    val jobApplicationModel = snapshot.getValue(JobApplicationModel::class.java)
                    if (jobApplicationModel != null) {
                        jobApplicationList.add(jobApplicationModel)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w("debug", "Failed to read applications from DB.", error.toException())
            }
        })
    }
    fun setJobApplicationToDB(job: JobModel, resume: ResumeModel) {
        // there is a default resume object which is empty, if you do not want to pass the resume, it will still work, nice!
        val jobApplication = JobApplicationModel(job = job.copy())
        jobApplication.resume = resume
        jobApplicationList.add(jobApplication)
        database.getReference("users/${auth.currentUser?.uid}/jobApplications").setValue(jobApplicationList.toList())
    }
}