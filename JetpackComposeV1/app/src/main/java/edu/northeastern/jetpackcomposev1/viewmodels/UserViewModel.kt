package edu.northeastern.jetpackcomposev1.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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
import edu.northeastern.jetpackcomposev1.models.UserModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class UserViewModel: ViewModel() {
    val auth: FirebaseAuth = Firebase.auth
    val database: FirebaseDatabase = Firebase.database

    var running: Boolean by mutableStateOf(false)

    var isSignedIn: Boolean by mutableStateOf(auth.currentUser != null)
    var authMessage = ""
    var messageReturned by mutableIntStateOf(0)
    // define the user in the app
    var user: UserModel by mutableStateOf(UserModel())

    fun signUp() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                // Your long-running operation here
                running = true
                auth.createUserWithEmailAndPassword(user.profile.email.trim(), user.profile.password.trim())
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Sign up success, update UI with the signed-in user's information
                            Log.d("debug", "createUserWithEmail: success")
                            user.id = auth.currentUser?.uid!!
                            // add user to the realtime database
                            database.getReference("users/${user.id}").setValue(user)
                            Log.d("debug", "set user to the DB: success")
                            authMessage = ""
                            isSignedIn = true
                        } else {
                            // If sign in fails, display a message to the user.
                            authMessage = task.exception?.message.toString()
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
                auth.signInWithEmailAndPassword(user.profile.email.trim(), user.profile.password.trim())
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Sign in success
                            Log.d("debug", "signInUserWithEmail: success")
                            getCurrentUser()
                        } else {
                            // If sign in fails, display a message to the user.
                            authMessage = task.exception?.message.toString()
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
                            Log.d("debug", "Email sent.")
                            authMessage = "Password reset email sent"
                            messageReturned++
                        } else {
                            authMessage = task.exception?.message.toString()
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
        user = UserModel()
    }

    fun getCurrentUser() {
        user.id = auth.currentUser?.uid.toString()
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
}