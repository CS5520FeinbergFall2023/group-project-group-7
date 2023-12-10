package edu.northeastern.jetpackcomposev1.viewmodels

import android.app.NotificationManager
import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.core.app.NotificationCompat
import androidx.core.net.toUri
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
import edu.northeastern.jetpackcomposev1.models.job.JobFavoriteModel
import edu.northeastern.jetpackcomposev1.models.job.JobModel
import edu.northeastern.jetpackcomposev1.models.job.JobViewedHistoryModel
import edu.northeastern.jetpackcomposev1.models.post.PostImageModel
import edu.northeastern.jetpackcomposev1.models.post.PostModel
import edu.northeastern.jetpackcomposev1.models.user.AvatarModel
import edu.northeastern.jetpackcomposev1.models.user.ProfileModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.ZonedDateTime

class PostViewModel: ViewModel() {
    val auth: FirebaseAuth = Firebase.auth
    val database: FirebaseDatabase = Firebase.database
    val storage: FirebaseStorage = Firebase.storage

    var firstLaunch: Boolean by mutableStateOf(true)
    var running: Boolean by mutableStateOf(false)

    var post: PostModel by mutableStateOf(PostModel())
    var postList: SnapshotStateList<PostModel> = mutableStateListOf()
    var postListSize: Int by mutableIntStateOf(0)

    /**********************************************************************************************/
    fun getPostFromDB(context: Context) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                // Your long-running operation here
                if (postList.isEmpty()) {
                    val myRef = database.getReference("posts")
                    myRef.addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            postList.clear()
                            for (snapshot in dataSnapshot.children) {
                                val postModel = snapshot.getValue(PostModel::class.java)
                                if (postModel != null) {
                                    postList.add(postModel)
                                }
                            }
                            // push notification to user
                            if (!firstLaunch && postListSize != postList.size && postList[0].user_id != auth.currentUser?.uid!!) {
                                setPostNotificationToUser(context)
                            }
                            firstLaunch = false
                            postListSize = postList.size
                        }

                        override fun onCancelled(error: DatabaseError) {
                            // Failed to read value
                            Log.w("debug", "Failed to read posts from DB.", error.toException())
                        }
                    })
                }
            }
        }
    }
    fun setPostToDB() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                // Your long-running operation here
                running = true
                post.user_id = auth.currentUser?.uid!!
                post.time = ZonedDateTime.now().toString()
                postList.add(0, post)
                database.getReference("posts").setValue(postList.toList())
                post = PostModel()
                running = false
            }
        }
    }

    /**********************************************************************************************/
    fun getPostProfileFromDB(index: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                // Your long-running operation here
                val myRef = database.getReference("users/${postList[index].user_id}/profile")
                myRef.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val profileModel = dataSnapshot.getValue(ProfileModel::class.java)
                        if (profileModel != null) {
                            postList[index].user_name = profileModel.name
                            postList[index].avatar_filePath = profileModel.avatar.filePath
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        // Failed to read value
                        Log.w("debug", "Failed to read user avatar from DB.", error.toException())
                    }
                })
            }
        }
    }

    /**********************************************************************************************/
    fun findUserInLikes(index: Int): Boolean {
        return postList[index].likes.contains(auth.currentUser?.uid)
    }

    fun setPostLikeToDB(index: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                // Your long-running operation here
                if (findUserInLikes(index)) {
                    postList[index].likes = postList[index].likes.filter { it != auth.currentUser?.uid!! }
                } else {
                    postList[index].likes += auth.currentUser?.uid!!
                }
                database.getReference("posts").setValue(postList.toList())
            }
        }
    }
    /**********************************************************************************************/
    fun setPostImageToStorage(index: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                // Your long-running operation here
                running = true
                val storageRef = storage.getReference("posts/${post.id}/images/${post.images[index].fileName}")
                val uploadTask = storageRef.putFile(post.images[index].filePath.toUri())
                // Register observers to listen for when the download is done or if it fails
                uploadTask.addOnFailureListener {
                    // Handle unsuccessful uploads
                    Log.d("debug", "Post image upload unsuccessful")
                    running = false
                }.addOnSuccessListener {
                    Log.d("debug", "Post image upload successful")
                    storageRef.downloadUrl.addOnSuccessListener { uri ->
                        // Got the download URL for the image
                        Log.d("debug", "Post image public URL download successful")
                        post.images[index].filePath = uri.toString()
                        if ((index + 1) != post.images.size) {
                            setPostImageToStorage(index + 1)
                        }
                        else {
                            setPostToDB()
                        }
                    }.addOnFailureListener {
                        // Handle any errors
                        Log.d("debug", "Post image public URL download unsuccessful")
                        running = false
                    }
                }
            }
        }
    }
    /**********************************************************************************************/
    fun setPostNotificationToUser(context: Context) {
        val notification = NotificationCompat.Builder(context, "channel_post")
            .setSmallIcon(R.drawable.job_track_pro_logo)
            .setContentTitle("Post Update")
            .setContentText("New post discovered!")
            .build()
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(2, notification)
    }

}