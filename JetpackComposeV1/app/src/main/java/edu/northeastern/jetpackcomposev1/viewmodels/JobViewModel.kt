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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.storage.storage
import edu.northeastern.jetpackcomposev1.models.job.JobApplicationModel
import edu.northeastern.jetpackcomposev1.models.job.JobFavoriteModel
import edu.northeastern.jetpackcomposev1.models.job.JobSearchHistoryModel
import edu.northeastern.jetpackcomposev1.models.job.JobSearchResultModel
import edu.northeastern.jetpackcomposev1.models.job.JobViewedHistoryModel
import edu.northeastern.jetpackcomposev1.models.resume.ResumeModel
import edu.northeastern.jetpackcomposev1.utility.dummyJson
import edu.northeastern.jetpackcomposev1.utility.fromJson
import edu.northeastern.jetpackcomposev1.utility.toJson
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class JobViewModel: ViewModel() {
    val database = Firebase.database
    val storage = Firebase.storage


    var response: JobSearchResultModel by mutableStateOf(JobSearchResultModel())
    var jobSearchHistoryList: SnapshotStateList<JobSearchHistoryModel> = mutableStateListOf()
    var jobViewedHistoryList: SnapshotStateList<JobViewedHistoryModel> = mutableStateListOf()
    var jobFavoriteList: SnapshotStateList<JobFavoriteModel> = mutableStateListOf()
    var jobApplicationList: SnapshotStateList<JobApplicationModel> = mutableStateListOf()
    var resumeList: SnapshotStateList<ResumeModel> = mutableStateListOf()

    /**********************************************************************************************/
    fun getJobSearchHistoryFromDB(id: Int) {
        val myRef = database.getReference("users/$id/jobSearchHistory")
        myRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val jsonString: String = dataSnapshot.getValue(String::class.java).toString()
                // convert json to object list
                val json = Json { ignoreUnknownKeys = true }
                val tempList: List<JobSearchHistoryModel> = json.decodeFromString(jsonString)
                jobSearchHistoryList = tempList.toMutableStateList()
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w("debug", "Failed to read value.", error.toException())
            }
        })
    }
    fun setJobSearchHistoryToDB(id: Int) {
        val jsonString: String = Json.encodeToString(jobSearchHistoryList.toList())
        val myRef = database.getReference("users/$id/jobSearchHistory")
        myRef.setValue(jsonString)
    }
    /**********************************************************************************************/
    fun getJobViewedHistoryFromDB(id: Int) {
        val myRef = database.getReference("users/$id/jobViewedHistory")
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val jsonString: String = dataSnapshot.getValue(String::class.java).toString()
                // convert json to object list
                val json = Json { ignoreUnknownKeys = true }
                val tempList: List<JobViewedHistoryModel> = json.decodeFromString(jsonString)
                jobViewedHistoryList = tempList.toMutableStateList()
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w("debug", "Failed to read value.", error.toException())
            }
        })
    }
    fun setJobViewedHistoryToDB(id: Int) {
        val jsonString: String = Json.encodeToString(jobViewedHistoryList.toList())
        val myRef = database.getReference("users/$id/jobViewedHistory")
        myRef.setValue(jsonString)
    }
    /**********************************************************************************************/
    fun getJobFavoriteFromDB(id: Int) {
        val myRef = database.getReference("users/$id/jobFavorite")
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val jsonString: String = dataSnapshot.getValue(String::class.java).toString()
                // convert json to object list
                val json = Json { ignoreUnknownKeys = true }
                val tempList: List<JobFavoriteModel> = json.decodeFromString(jsonString)
                jobFavoriteList = tempList.toMutableStateList()
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w("debug", "Failed to read value.", error.toException())
            }
        })
    }
    fun setJobFavoriteToDB(id: Int) {
        val jsonString: String = Json.encodeToString(jobFavoriteList.toList())
        val myRef = database.getReference("users/$id/jobFavorite")
        myRef.setValue(jsonString)
    }
    /**********************************************************************************************/
    fun getJobApplicationFromDB(id: Int) {
        val myRef = database.getReference("users/$id/jobApplication")
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val jsonString: String = dataSnapshot.getValue(String::class.java).toString()
                // convert json to object list
                val json = Json { ignoreUnknownKeys = true }
                val tempList: List<JobApplicationModel> = json.decodeFromString(jsonString)
                jobApplicationList = tempList.toMutableStateList()
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w("debug", "Failed to read value.", error.toException())
            }
        })
    }
    fun setJobApplicationToDB(id: Int) {
        val jsonString: String = Json.encodeToString(jobApplicationList.toList())
        val myRef = database.getReference("users/$id/jobApplication")
        myRef.setValue(jsonString)
    }
    /**********************************************************************************************/

    fun getResumeFromStorage(id: Int, filePath: String) {
        /*TODO: get .pdf from the google storage for preview*/
    }
    fun getResumeFromDB(id: Int) {
        val myRef = database.getReference("users/$id/resumes")
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val jsonString = dataSnapshot.getValue(String::class.java)
                // Do something with the value
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w("debug", "Failed to read value.", error.toException())
            }
        })
    }

    fun setResumeToStorage(id: Int, pdfUri: Uri) {
        /*TODO: read .pdf from phone, save to google storage*/
        val fileName: String = ""
        val filePath: String = "users/$id/resumes/$fileName"
        val storageRef = storage.reference.child(filePath)
        /*TODO: convert file data and upload*/
        //val uploadTask = storageRef.putBytes(pdfData)
    }
    fun setResumeToDB(id: Int) {
        val jsonString: String = Json.encodeToString(resumeList.toList())
        val myRef = database.getReference("users/$id/resumes")
        myRef.setValue(jsonString)
    }


}