package edu.northeastern.jetpackcomposev1.viewmodels

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
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
import edu.northeastern.jetpackcomposev1.models.job.JobFavoriteModel
import edu.northeastern.jetpackcomposev1.models.job.JobModel
import edu.northeastern.jetpackcomposev1.models.job.JobSearchHistoryModel
import edu.northeastern.jetpackcomposev1.models.job.JobSearchResultModel
import edu.northeastern.jetpackcomposev1.models.job.JobViewedHistoryModel
import edu.northeastern.jetpackcomposev1.models.resume.ResumeModel

class JobViewModel: ViewModel() {
    val auth: FirebaseAuth = Firebase.auth
    val database: FirebaseDatabase = Firebase.database
    val storage: FirebaseStorage = Firebase.storage


    var response: JobSearchResultModel by mutableStateOf(JobSearchResultModel())
    var jobSearchHistoryList: SnapshotStateList<JobSearchHistoryModel> = mutableStateListOf()
    var jobViewedHistoryList: SnapshotStateList<JobViewedHistoryModel> = mutableStateListOf()
    var jobFavoriteList: SnapshotStateList<JobFavoriteModel> = mutableStateListOf()
    var jobApplicationList: SnapshotStateList<JobApplicationModel> = mutableStateListOf()
    var resumeList: SnapshotStateList<ResumeModel> = mutableStateListOf()

    /**********************************************************************************************/
    fun getJobSearchHistoryFromDB() {
        val myRef = database.getReference("users/${auth.currentUser?.uid}/jobSearchHistory")
        myRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snapshot in dataSnapshot.children) {
                    val jobSearchHistoryModel = snapshot.getValue(JobSearchHistoryModel::class.java)
                    if (jobSearchHistoryModel != null) {
                        jobSearchHistoryList.add(jobSearchHistoryModel)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w("debug", "Failed to read job search history from DB.", error.toException())
            }
        })
    }
    fun setJobSearchHistoryToDB(search: JobSearchHistoryModel) {
        jobSearchHistoryList.add(search)
        database.getReference("users/${auth.currentUser?.uid}/jobSearchHistory").setValue(jobSearchHistoryList.toList())
    }
    /**********************************************************************************************/
    fun getJobViewedHistoryFromDB() {
        val myRef = database.getReference("users/${auth.currentUser?.uid}/jobViewedHistory")
        myRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snapshot in dataSnapshot.children) {
                    val jobViewedHistoryModel = snapshot.getValue(JobViewedHistoryModel::class.java)
                    if (jobViewedHistoryModel != null) {
                        jobViewedHistoryList.add(jobViewedHistoryModel)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w("debug", "Failed to read job viewed history from DB.", error.toException())
            }
        })
    }
    fun setJobViewedHistoryToDB(jobId: String) {
        val jobViewHistory = JobViewedHistoryModel(jobId)
        val index = jobViewedHistoryList.indexOfFirst { it.id == jobId }
        if (index != -1) {
            jobViewedHistoryList[index] = jobViewHistory
        } else {
            jobViewedHistoryList.add(jobViewHistory)
        }
        database.getReference("users/${auth.currentUser?.uid}/jobViewedHistory").setValue(jobViewedHistoryList.toList())
    }
    /**********************************************************************************************/
    fun getJobFavoriteFromDB() {
        val myRef = database.getReference("users/${auth.currentUser?.uid}/jobFavorites")
        myRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snapshot in dataSnapshot.children) {
                    val jobFavoriteModel = snapshot.getValue(JobFavoriteModel::class.java)
                    if (jobFavoriteModel != null) {
                        jobFavoriteList.add(jobFavoriteModel)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w("debug", "Failed to read favorites from DB.", error.toException())
            }
        })
    }
    fun findJobInFavoriteList(jobId: String): Boolean {
        return jobFavoriteList.any { it.id == jobId }
    }
    fun setJobFavoriteToDB(job: JobModel) {
        if (findJobInFavoriteList(job.id)) {
            jobFavoriteList.removeIf {it.id == job.id}
        } else {
            val favoriteJob = JobFavoriteModel(id = job.id, job = job.copy())
            jobFavoriteList.add(favoriteJob)
        }
        database.getReference("users/${auth.currentUser?.uid}/jobFavorites").setValue(jobFavoriteList.toList())
    }
    /**********************************************************************************************/
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
    fun setJobApplicationToDB(job: JobModel) {
        // there is a default resume object which is empty, if you do not want to pass the resume, it will still work, nice!
        val jobApplication = JobApplicationModel(job = job.copy())
        jobApplicationList.add(jobApplication)
        database.getReference("users/${auth.currentUser?.uid}/jobApplications").setValue(jobApplicationList.toList())
    }
    /**********************************************************************************************/

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