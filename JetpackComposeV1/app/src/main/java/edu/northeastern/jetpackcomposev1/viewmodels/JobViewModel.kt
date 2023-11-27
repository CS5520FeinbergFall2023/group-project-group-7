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
import androidx.lifecycle.viewModelScope
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
import edu.northeastern.jetpackcomposev1.models.search.SearchModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class JobViewModel: ViewModel() {
    val auth: FirebaseAuth = Firebase.auth
    val database: FirebaseDatabase = Firebase.database
    val storage: FirebaseStorage = Firebase.storage

    var search: SearchModel by mutableStateOf(SearchModel())
    var response: JobSearchResultModel by mutableStateOf(JobSearchResultModel())
    var jobSearchHistoryList: SnapshotStateList<JobSearchHistoryModel> = mutableStateListOf()
    var jobViewedHistoryList: SnapshotStateList<JobViewedHistoryModel> = mutableStateListOf()
    var jobFavoriteList: SnapshotStateList<JobFavoriteModel> = mutableStateListOf()



    /**********************************************************************************************/
    fun getJobFromAPI() {
        val requestHead = "https://api.adzuna.com/v1/api/jobs"
        val app_id = "?app_id=${search.app_id}"
        val app_key = "&app_key=${search.app_key}"
        val results_per_page = "&results_per_page=${search.results_per_page}"

        var requestURL = "${requestHead}/${search.country}/search/${search.page}" + app_id + app_key + results_per_page
        if (search.what.isNotEmpty()) { requestURL += "&what=${search.what}" }
        if (search.what_and.isNotEmpty()) { requestURL += "&what_end=${search.what_and}" }
        if (search.what_phrase.isNotEmpty()) { requestURL += "&what_phrase=${search.what_phrase}" }
        if (search.what_or.isNotEmpty()) { requestURL += "&what_or=${search.what_or}" }
        if (search.what_exclude.isNotEmpty()) { requestURL += "&what_exclude=${search.what_exclude}" }
        if (search.title_only.isNotEmpty()) { requestURL += "&title_only=${search.title_only}" }
        if (search.where.isNotEmpty()) { requestURL += "&where=${search.where}&distance=${search.distance}" }

    }



    /**********************************************************************************************/
    fun getJobSearchHistoryFromDB() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                // Your long-running operation here
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
        }
    }
    fun setJobSearchHistoryToDB() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                // Your long-running operation here
                val newSearch = JobSearchHistoryModel(country = search.country, what = search.what, where = search.where, distance = search.distance)
                val index = jobSearchHistoryList.indexOfFirst { it.country == newSearch.country && it.what == newSearch.what && it.where == newSearch.where && it.distance == newSearch.distance }
                if (index != -1) {
                    jobSearchHistoryList.removeAt(index)
                }
                jobSearchHistoryList.add(0, newSearch)
                database.getReference("users/${auth.currentUser?.uid}/jobSearchHistory").setValue(jobSearchHistoryList.toList())
            }
        }
    }
    /**********************************************************************************************/
    fun getJobViewedHistoryFromDB() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                // Your long-running operation here
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
        }
    }
    fun setJobViewedHistoryToDB(jobId: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                // Your long-running operation here
                val jobViewHistory = JobViewedHistoryModel(jobId)
                val index = jobViewedHistoryList.indexOfFirst { it.id == jobId }
                if (index != -1) {
                    jobViewedHistoryList[index] = jobViewHistory
                } else {
                    jobViewedHistoryList.add(0, jobViewHistory)
                }
                database.getReference("users/${auth.currentUser?.uid}/jobViewedHistory").setValue(jobViewedHistoryList.toList())
            }
        }
    }
    /**********************************************************************************************/
    fun getJobFavoriteFromDB() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                // Your long-running operation here
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
        }
    }
    fun findJobInFavoriteList(jobId: String): Boolean {
        return jobFavoriteList.any { it.id == jobId }
    }
    fun setJobFavoriteToDB(job: JobModel) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                // Your long-running operation here
                if (findJobInFavoriteList(job.id)) {
                    jobFavoriteList.removeIf {it.id == job.id}
                } else {
                    val favoriteJob = JobFavoriteModel(id = job.id, job = job.copy())
                    jobFavoriteList.add(0, favoriteJob)
                }
                database.getReference("users/${auth.currentUser?.uid}/jobFavorites").setValue(jobFavoriteList.toList())
            }
        }
    }
    /**********************************************************************************************/





}