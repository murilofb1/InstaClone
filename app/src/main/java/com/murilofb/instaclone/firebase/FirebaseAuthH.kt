package com.murilofb.instaclone.firebase

import com.google.firebase.auth.*
import com.murilofb.instaclone.constants.AuthExceptionsBR
import com.murilofb.instaclone.model.UserModel
import com.murilofb.instaclone.ui.welcome.WelcomeActivity
import com.murilofb.instaclone.ui.welcome.WelcomeViewModel
import java.lang.Exception
import java.util.*

class FirebaseAuthH {
    companion object {
        private val authInstance = FirebaseAuth.getInstance()
        private var currentUser: FirebaseUser? = null

        fun isSomeoneLogged(): Boolean {
            if (currentUser == null) {
                currentUser = authInstance.currentUser
            }
            RealtimeDatabaseHelper.updateCurrentUser(currentUser)
            return currentUser != null
        }

        fun getCurrentUserID(): String? {
            return if (isSomeoneLogged()) FirebaseAuth.getInstance().currentUser!!.uid
            else null
        }

        fun getCurrentUser(): FirebaseUser? {
            isSomeoneLogged()
            return currentUser
        }

        fun signOut() {
            authInstance.signOut()
            currentUser = null
            RealtimeDatabaseHelper.updateCurrentUser(currentUser)
        }

        fun registerUser(user: UserModel, activity: WelcomeActivity) {
            authInstance.createUserWithEmailAndPassword(user.email, user.password)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        currentUser = authInstance.currentUser
                        user.id = currentUser!!.uid
                        RealtimeDatabaseHelper.registerUser(user)
                        RealtimeDatabaseHelper.updateCurrentUser(currentUser)
                        activity.goToHome()
                    } else {
                        var exceptionMessage: String
                        if (Locale.getDefault() == Locale("pt", "BR")) {
                            try {
                                throw it.exception!!
                            } catch (e: FirebaseAuthWeakPasswordException) {
                                exceptionMessage = AuthExceptionsBR.weakPasswordException
                            } catch (e: FirebaseAuthInvalidCredentialsException) {
                                exceptionMessage = AuthExceptionsBR.invalidCredentialException
                            } catch (e: FirebaseAuthUserCollisionException) {
                                exceptionMessage = AuthExceptionsBR.userCollisionException
                            } catch (e: Exception) {
                                exceptionMessage = "The error: ${e.message} has occurred"
                                e.printStackTrace()
                            }
                        } else exceptionMessage = it.exception?.message.toString()
                        WelcomeViewModel.SignUpViewModel.setExceptionMessage(
                            exceptionMessage
                        )
                    }
                    WelcomeViewModel.SignUpViewModel.stopSignupProcess()
                }
        }

        fun loginWithEmailAndPassword(email: String, password: String, activity: WelcomeActivity) {
            authInstance.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                if (it.isSuccessful) {
                    currentUser = authInstance.currentUser
                    RealtimeDatabaseHelper.updateCurrentUser(currentUser)
                    activity.goToHome()
                } else {
                    var exceptionMessage: String
                    if (Locale.getDefault() == Locale("pt", "BR")) {
                        try {
                            throw it.exception!!
                        } catch (e: FirebaseAuthWeakPasswordException) {
                            exceptionMessage = AuthExceptionsBR.weakPasswordException
                        } catch (e: FirebaseAuthInvalidCredentialsException) {
                            exceptionMessage = AuthExceptionsBR.invalidCredentialException
                        } catch (e: FirebaseAuthUserCollisionException) {
                            exceptionMessage = AuthExceptionsBR.userCollisionException
                        } catch (e: Exception) {
                            exceptionMessage = "The error: ${e.message} has occurred"
                            e.printStackTrace()
                        }
                    } else exceptionMessage = it.exception?.message.toString()
                    WelcomeViewModel.LoginViewModel.setExceptionMessage(exceptionMessage)
                }
                WelcomeViewModel.LoginViewModel.stopLoginProcess()
            }
        }
    }
}