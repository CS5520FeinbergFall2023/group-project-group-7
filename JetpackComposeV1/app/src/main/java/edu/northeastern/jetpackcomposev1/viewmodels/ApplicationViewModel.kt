package edu.northeastern.jetpackcomposev1.viewmodels

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
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
import edu.northeastern.jetpackcomposev1.models.Application.Event
import edu.northeastern.jetpackcomposev1.models.Application.TimeLine
import edu.northeastern.jetpackcomposev1.models.job.JobApplicationModel
import edu.northeastern.jetpackcomposev1.models.job.JobModel
import edu.northeastern.jetpackcomposev1.models.resume.ResumeModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale.filter

class ApplicationViewModel : ViewModel() {
    val auth: FirebaseAuth = Firebase.auth
    val database: FirebaseDatabase = Firebase.database
    val storage: FirebaseStorage = Firebase.storage
    var jobApplicationList: SnapshotStateList<JobApplicationModel> = mutableStateListOf()


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


    /**********************************************************************************************/

    fun getJobApplicationFromDB() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val myRef = database.getReference("users/${auth.currentUser?.uid}/jobApplications")
                myRef.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        for (snapshot in dataSnapshot.children) {
                            val jobApplicationModel =
                                snapshot.getValue(JobApplicationModel::class.java)
                            if (jobApplicationModel != null) {
                                val index =
                                    jobApplicationList.indexOfFirst { it.id == jobApplicationModel.id }
                                if (index == -1) {
                                    jobApplicationList.add(jobApplicationModel)
                                }

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

    fun setJobApplicationToDB(job: JobModel, resume: ResumeModel, timeLine: TimeLine) {
        val newJobApplication = JobApplicationModel(job = job.copy())
        newJobApplication.resume = resume
        newJobApplication.timeLine = timeLine.copy()
        jobApplicationList.add(newJobApplication)
        selectApplication(newJobApplication)
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                database.getReference("users/${auth.currentUser?.uid}/jobApplications")
                    .setValue(jobApplicationList.toList())
            }
        }
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

        var newJobApplication = JobApplicationModel(job = jobApplication.job.copy())
        newJobApplication.resume = jobApplication.resume
        newJobApplication.timeLine = jobApplication.timeLine.copy()

        if (jobApplication != null) {
            val mutableResults = newJobApplication.timeLine.results.toMutableList()
            // add new event to the list and remove the old event
            //empty new event will not be added to the list -> mark as delete old event
            //empty old event will not be removed from the list -> mark as add new event

            if(newEvent.date!="" && newEvent.status!=""){
                mutableResults.add(newEvent)
            } else {
                //do nothing
            }
            if(oldEvent.date!="" && oldEvent.status!=""){
                mutableResults.filter { it.date != oldEvent.date }
            } else {
                //do nothing
            }

            val updatedTimeLine = newJobApplication.timeLine.copy(
                results = mutableResults.sortedBy { it.date },
                count = mutableResults.size // Update count based on the sorted results
            )
            newJobApplication.timeLine = updatedTimeLine
            selectApplication(newJobApplication)
            updateJobApplicationToDB(jobApplication, newJobApplication)
        }
    }

}



