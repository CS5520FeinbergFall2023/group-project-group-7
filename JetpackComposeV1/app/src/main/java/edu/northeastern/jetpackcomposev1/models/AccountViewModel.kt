package edu.northeastern.jetpackcomposev1.models

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class AccountViewModel: ViewModel() {
    val auth: FirebaseAuth = Firebase.auth
    var isSignedIn by mutableStateOf( auth.currentUser != null)
    var userId by mutableStateOf("")
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var name by mutableStateOf("")
    var authMessage = ""
    var messageReturned by mutableIntStateOf(0)

    fun signUp() {
        auth.createUserWithEmailAndPassword(email.trim(), password.trim())
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("debug", "createUserWithEmail: success")
                    authMessage = ""
                    userId = auth.currentUser?.uid.toString()
                    /*TODO: add user to the realtime database*/
                    isSignedIn = true
                } else {
                    // If sign in fails, display a message to the user.
                    authMessage = task.exception?.message.toString()
                    Log.w("debug", authMessage)
                    messageReturned++
                }
            }
    }

    fun signIn() {
        auth.signInWithEmailAndPassword(email.trim(), password.trim())
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success
                    Log.d("debug", "signInUserWithEmail: success")
                    authMessage = ""
                    userId = auth.currentUser?.uid.toString()
                    /*TODO: get user name from realtime database*/
                    isSignedIn = true
                } else {
                    // If sign in fails, display a message to the user.
                    authMessage = task.exception?.message.toString()
                    Log.w("debug", authMessage)
                    messageReturned++
                }
            }
    }

    fun forgotPassword() {
        auth.sendPasswordResetEmail(email.trim())
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
            }
    }

    fun signOut() {
        auth.signOut()
        isSignedIn = false
        userId = ""
        email = ""
        password = ""
        name = ""
        authMessage = ""
        messageReturned = 0
    }

}