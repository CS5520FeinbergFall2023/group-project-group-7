package edu.northeastern.jetpackcomposev1.viewmodels

import android.Manifest
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
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
import edu.northeastern.jetpackcomposev1.R
import edu.northeastern.jetpackcomposev1.models.application.Event
import edu.northeastern.jetpackcomposev1.models.application.TimeLine
import edu.northeastern.jetpackcomposev1.models.job.ApplicationStatus
import edu.northeastern.jetpackcomposev1.models.job.JobApplicationModel
import edu.northeastern.jetpackcomposev1.models.job.JobModel
import edu.northeastern.jetpackcomposev1.models.post.PostModel
import edu.northeastern.jetpackcomposev1.models.resume.ResumeModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale.filter

class ApplicationViewModel: ViewModel() {
    val auth: FirebaseAuth = Firebase.auth
    val database: FirebaseDatabase = Firebase.database
    val storage: FirebaseStorage = Firebase.storage

    var firstLaunch: Boolean by mutableStateOf(true)
    var firstLaunch2: Boolean by mutableStateOf(true)

    var jobApplicationList: SnapshotStateList<JobApplicationModel> = mutableStateListOf()
    var jobRecommendationList: SnapshotStateList<JobModel> = mutableStateListOf()
    var sortedApplicationList: SnapshotStateList<JobApplicationModel> = mutableStateListOf()
    /**********************************************************************************************/
    //Jun's modification
    //adding state for recording the selected application and selected event
    private val _selectedApplication = mutableStateOf<JobApplicationModel?>(null)
    private val _selectedEvent = mutableStateOf<Event?>(null)

    //getter
    val selectedApplication: State<JobApplicationModel?> get() = _selectedApplication

    val selectedEvent: State<Event?> get() = _selectedEvent

    // setter
    fun selectEvent(event: Event) {
        _selectedEvent.value = event
    }

    fun selectApplication(application: JobApplicationModel) {
        _selectedApplication.value = application
    }


    fun getFilteredJobApplicationList(filter: String): List<JobApplicationModel> {
        return when (filter) {
            "Interviewed" -> sortedApplicationList.filter { jobApplication ->
                jobApplication.timeLine.results.any { event ->
                    event.status == "Interviewed"
                }
            }.toList()

            "Offer" -> sortedApplicationList.filter { jobApplication ->
                jobApplication.timeLine.results.any { event ->
                    event.status == "Offer"
                }
            }.toList()

            "Rejected" -> sortedApplicationList.filter { jobApplication ->
                jobApplication.timeLine.results.any { event ->
                    event.status == "Rejected"
                }
            }.toList()

            "Offer Accepted" -> sortedApplicationList.filter { jobApplication ->
                jobApplication.timeLine.results.any { event ->
                    event.status == "Offer Accepted"
                }
            }.toList()

            else -> sortedApplicationList.toList()
        }
    }


    fun sortJobApplicationListOnDate() {
        sortedApplicationList =
            jobApplicationList.sortedByDescending { it.timeLine.results.first().date }
                .toMutableStateList()
    }

    fun deleteApplicationFromDB(application: JobApplicationModel) {
        sortedApplicationList.remove(application)
        jobApplicationList.remove(application)
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                database.getReference("users/${auth.currentUser?.uid}/jobApplications")
                    .setValue(jobApplicationList.toList())
            }
        }
    }


    /**********************************************************************************************/
    fun getJobApplicationFromDB() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                // Your long-running operation here
                if (jobApplicationList.isEmpty()) {
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
            }
        }
    }

    fun setJobApplicationToDB(job: JobModel, resume: ResumeModel, timeLine: TimeLine) {
        val newJobApplication = JobApplicationModel(job = job.copy())
        newJobApplication.resume = resume
        newJobApplication.timeLine = timeLine.copy()
        newJobApplication.status = timeLine.results.first().status
        jobApplicationList.add(newJobApplication)
        selectApplication(newJobApplication)
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                database.getReference("users/${auth.currentUser?.uid}/jobApplications")
                    .setValue(jobApplicationList.toList())
            }
        }
        // add this job to recommendation list
        setJobRecommendationToDB(job = job)
    }

    private fun updateJobApplicationToDB(
        oldJobApplication: JobApplicationModel,
        newJobApplication: JobApplicationModel
    ) {
        jobApplicationList.remove(oldJobApplication)
        jobApplicationList.add(newJobApplication)
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                database.getReference("users/${auth.currentUser?.uid}/jobApplications")
                    .setValue(jobApplicationList.toList())
            }

        }
    }

    fun updateEventToDB(jobApplication: JobApplicationModel, oldEvent: Event, newEvent: Event) {
        val newJobApplication = JobApplicationModel(job = jobApplication.job.copy())
        newJobApplication.resume = jobApplication.resume
        newJobApplication.timeLine = jobApplication.timeLine.copy()
        val mutableResults = newJobApplication.timeLine.results.toMutableList()
        if (newEvent.date.isNotBlank() && newEvent.status.isNotBlank()) {
            mutableResults.add(0, newEvent)
        }
        if (oldEvent.date.isNotBlank() && oldEvent.status.isNotBlank()) {
            if (mutableResults.size >= 2) {
                mutableResults.removeIf { it.date == oldEvent.date && it.status == oldEvent.status }
            }
        }
        val updatedTimeLine = newJobApplication.timeLine.copy(
            results = if (newEvent.date != jobApplication.timeLine.results.first().date) {
                mutableResults.sortedByDescending { it.date }
            } else {
                mutableResults
            },
            count = mutableResults.size
        )

        newJobApplication.timeLine = updatedTimeLine
        newJobApplication.status = updatedTimeLine.results.first().status
        selectApplication(newJobApplication)
        updateJobApplicationToDB(jobApplication, newJobApplication)
    }

    /**********************************************************************************************/
    fun getJobRecommendationFromDB(context: Context) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                // Your long-running operation here
                if (jobRecommendationList.isEmpty()) {
                    val myRef = database.getReference("recommendations")
                    myRef.addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            jobRecommendationList.clear()
                            for (snapshot in dataSnapshot.children) {
                                val JobModel = snapshot.getValue(JobModel::class.java)
                                if (JobModel != null) {
                                    jobRecommendationList.add(JobModel)
                                }
                            }
                            // push notification to user
                            if (!firstLaunch &&
                                (jobApplicationList.isEmpty() || jobRecommendationList[0].id != jobApplicationList.last().job.id)) {
                                setJobNotificationToUser(context)
                            }
                            firstLaunch = false
                        }

                        override fun onCancelled(error: DatabaseError) {
                            // Failed to read value
                            Log.w("debug", "Failed to read job recommendation from DB.", error.toException())
                        }
                    })
                }
            }
        }
    }
    fun setJobRecommendationToDB(job: JobModel) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                // Your long-running operation here
                val index = jobRecommendationList.indexOfFirst { it.id == job.id }
                if (index != -1) {
                    jobRecommendationList.removeAt(index)
                }
                jobRecommendationList.add(0, job.copy())
                database.getReference("recommendations").setValue(jobRecommendationList.toList())
            }
        }
    }
    fun findJobInApplicationList(jobId: String): Boolean {
        return jobApplicationList.any { it.job.id == jobId }
    }
    /**********************************************************************************************/
    fun setJobNotificationToUser(context: Context) {
        val notification = NotificationCompat.Builder(context, "channel_recommendation")
            .setSmallIcon(R.drawable.job_track_pro_logo)
            .setContentTitle("New Job Recommendation")
            .setContentText("${jobRecommendationList[0].title}\n${jobRecommendationList[0].company.display_name}")
            .build()
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1, notification)
    }
    fun calculateNumberOfPendingApplication(): Int {
        var count = 0
        for (application in jobApplicationList) {
            if(application.status == ApplicationStatus.APPLIED.displayName || application.status == ApplicationStatus.INTERVIEWED.displayName) {
                count++
            }
        }
        return count
    }
    fun setJobReminderToUser(context: Context) {
        val count = calculateNumberOfPendingApplication()
        val notification = NotificationCompat.Builder(context, "channel_reminder")
            .setSmallIcon(R.drawable.job_track_pro_logo)
            .setContentTitle("Application Reminder")
            .setContentText(
                if(count == 0) {
                    "Good job!\nNo pending applications"
                }
                else if(count == 1) {
                    "You have 1 pending application"
                } else {
                    "You have ${count} pending applications"
                }
            )
            .build()
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(3, notification)
    }
}