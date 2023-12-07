package edu.northeastern.jetpackcomposev1.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage
import edu.northeastern.jetpackcomposev1.models.UserModel
import edu.northeastern.jetpackcomposev1.models.user.AvatarModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserViewModel: ViewModel() {
    val auth: FirebaseAuth = Firebase.auth
    val database: FirebaseDatabase = Firebase.database
    val storage: FirebaseStorage = Firebase.storage

    var running: Boolean by mutableStateOf(false)
    var isSignedIn: Boolean by mutableStateOf(auth.currentUser != null)
    var authMessage = ""
    var messageReturned by mutableIntStateOf(0)
    // define the user in the app
    var user: UserModel by mutableStateOf(UserModel())

    var password: String by mutableStateOf("")
    var newPassword: String by mutableStateOf("")
    /**********************************************************************************************/
    fun signUp() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                // Your long-running operation here
                running = true
                auth.createUserWithEmailAndPassword(user.profile.email.trim(), password.trim())
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Sign up success, update UI with the signed-in user's information
                            Log.d("debug", "createUserWithEmail: success")
                            user.id = auth.currentUser?.uid!!
                            // add user to the realtime database
                            database.getReference("users/${user.id}").setValue(user)
                            Log.d("debug", "set user to the DB: success")
                            authMessage = ""
                            password = ""
                            isSignedIn = true
                        } else {
                            // If sign in fails, display a message to the user.
                            authMessage = task.exception?.message!!
                            Log.w("debug", authMessage)
                            messageReturned++
                        }
                        running = false
                    }
            }
        }
    }

    fun signIn() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                // Your long-running operation here
                running = true
                auth.signInWithEmailAndPassword(user.profile.email.trim(), password.trim())
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Sign in success
                            Log.d("debug", "signInUserWithEmail: success")
                            getCurrentUserFromDB()
                        } else {
                            // If sign in fails, display a message to the user.
                            authMessage = task.exception?.message!!
                            Log.w("debug", authMessage)
                            messageReturned++
                            running = false
                        }
                    }
            }
        }
    }

    fun forgotPassword() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                // Your long-running operation here
                running = true
                auth.sendPasswordResetEmail(user.profile.email.trim())
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d("debug", "Email sent")
                            authMessage = "Password reset email sent"
                            messageReturned++
                        } else {
                            authMessage = task.exception?.message!!
                            Log.w("debug", authMessage)
                            messageReturned++
                        }
                        running = false
                    }
            }
        }
    }

    fun signOut() {
        auth.signOut()
        isSignedIn = false
        authMessage = ""
        messageReturned = 0
        password = ""
        newPassword = ""
    }

    fun getCurrentUserFromDB() {
        user.id = auth.currentUser?.uid!!
        val myRef = database.getReference("users/${user.id}")
        myRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val userModel: UserModel? = dataSnapshot.getValue(UserModel::class.java)
                if (userModel != null) {
                    user = userModel
                } else {
                    // rebuild the account in DB
                    myRef.setValue(user)
                    Log.d("debug", "Recover user to the DB: success")
                }
                authMessage = ""
                password = ""
                isSignedIn = true
                running = false
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                authMessage = error.message
                Log.w("debug", authMessage)
                messageReturned++
                running = false
            }
        })
    }
    /**********************************************************************************************/
    fun setAvatarToStorage() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                // Your long-running operation here
                running = true
                val storageRef = storage.getReference("users/${user.id}/avatar/${user.profile.avatar.fileName}")
                val uploadTask = storageRef.putFile(user.profile.avatar.filePath.toUri())
                // Register observers to listen for when the download is done or if it fails
                uploadTask.addOnFailureListener {
                    // Handle unsuccessful uploads
                    Log.d("debug", "Avatar upload unsuccessful")
                    authMessage = "Avatar upload unsuccessful"
                    messageReturned++
                    running = false
                }.addOnSuccessListener {
                    Log.d("debug", "Avatar upload successful")
                    storageRef.downloadUrl.addOnSuccessListener { uri ->
                        // Got the download URL for the image
                        Log.d("debug", "Avatar public URL download successful")
                        user.profile.avatar = AvatarModel(fileName = user.profile.avatar.fileName, filePath = uri.toString())
                        database.getReference("users/${user.id}/profile/avatar").setValue(user.profile.avatar)
                        authMessage = "Avatar saved"
                        messageReturned++
                        running = false
                    }.addOnFailureListener {
                        // Handle any errors
                        Log.d("debug", "Avatar public URL download unsuccessful")
                        authMessage = "Avatar public URL download unsuccessful"
                        messageReturned++
                        running = false
                    }
                }
            }
        }
    }
    fun setProfileToDB() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                // Your long-running operation here
                database.getReference("users/${user.id}/profile").setValue(user.profile)
                authMessage = "Saved"
                messageReturned++
            }
        }
    }
    fun setPasswordToAuth() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                // Your long-running operation here
                running = true
                if (password != "" && newPassword != "") {
                    val credential = EmailAuthProvider.getCredential(user.profile.email, password)
                    auth.currentUser?.reauthenticate(credential)?.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // The password is correct, now you can allow the user to update the password
                            auth.currentUser?.updatePassword(newPassword)!!
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        Log.d("debug", "Password updated successfully")
                                        password = ""
                                        newPassword = ""
                                        authMessage = "Password updated successfully"
                                        messageReturned++
                                        running = false
                                    } else {
                                        Log.d("debug", "Error updating password")
                                        password = ""
                                        newPassword = ""
                                        authMessage = "Error updating password"
                                        messageReturned++
                                        running = false
                                    }
                                }
                        } else {
                            // The password is incorrect
                            Log.d("debug", "Incorrect current password")
                            password = ""
                            newPassword = ""
                            authMessage = "Incorrect current password"
                            messageReturned++
                            running = false
                        }
                    }
                }
                else {
                    Log.d("debug", "Empty password")
                    authMessage = "Empty password"
                    messageReturned++
                    running = false
                }
            }
        }
    }
}