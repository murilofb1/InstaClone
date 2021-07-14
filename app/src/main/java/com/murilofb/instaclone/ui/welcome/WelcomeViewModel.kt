package com.murilofb.instaclone.ui.welcome

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.murilofb.instaclone.R
import com.murilofb.instaclone.firebase.FirebaseAuthH
import com.murilofb.instaclone.model.UserModel

class WelcomeViewModel : ViewModel() {

    private val isLoginBeingShown: MutableLiveData<Boolean> = MutableLiveData<Boolean>(true)
    fun isLoginBeingShown(): LiveData<Boolean> {
        return isLoginBeingShown
    }

    fun switchActivities() {
        isLoginBeingShown.value = isLoginBeingShown.value != true
    }


    class LoginViewModel : ViewModel() {
        companion object {
            private val isTryingToLogin: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
            internal val loginExceptionMessage: MutableLiveData<String> = MutableLiveData<String>()
            private var lastException = loginExceptionMessage.value

            fun setExceptionMessage(message: String) {
                loginExceptionMessage.value = message
                lastException = message
            }

            fun startLoginProcess() {
                isTryingToLogin.value = true
            }

            fun stopLoginProcess() {
                isTryingToLogin.value = false
            }
        }

        fun loginWithCredentials(email: String, password: String, activity: AppCompatActivity) {
            if (email.isNotEmpty() && password.isNotEmpty()) {
                startLoginProcess()
                FirebaseAuthH.loginWithEmailAndPassword(
                    email,
                    password,
                    activity as WelcomeActivity
                )
            } else {
                val message: String = when {
                    email.isEmpty() -> activity.applicationContext.getString(R.string.fill_email)
                    password.isEmpty() -> activity.applicationContext.getString(R.string.fill_password)
                    else -> activity.applicationContext.getString(R.string.fill_all)
                }
                setExceptionMessage(message)
            }
        }

        fun isTryingToLogin(): LiveData<Boolean> {
            return isTryingToLogin
        }

        fun getExceptionMessage(): LiveData<String> {
            val returnItem = loginExceptionMessage
            loginExceptionMessage.value = ""
            return returnItem
        }
    }

    open class SignUpViewModel : ViewModel() {
        companion object {
            private val isTryingToSignUp: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
            internal val signUpExceptionMessage: MutableLiveData<String> = MutableLiveData<String>()

            fun setExceptionMessage(exceptionMessage: String) {
                this.signUpExceptionMessage.value = exceptionMessage
            }

            fun startSignupProcess() {
                isTryingToSignUp.value = true
            }

            fun stopSignupProcess() {
                isTryingToSignUp.value = false
            }
        }

        fun getExceptionMessage(): LiveData<String> {
            val returnItem = signUpExceptionMessage
            signUpExceptionMessage.value = ""
            return returnItem
        }

        fun signupUser(user: UserModel, activity: AppCompatActivity) {
            if (user.email.isNotEmpty() && user.password.isNotEmpty() && user.username.isNotEmpty()) {
                startSignupProcess()
                FirebaseAuthH.registerUser(user, activity as WelcomeActivity)
            } else {
                val message: String = when {
                    user.username.isEmpty() -> activity.applicationContext.getString(R.string.fill_username)
                    user.email.isEmpty() -> activity.applicationContext.getString(R.string.fill_email)
                    user.password.isEmpty() -> activity.applicationContext.getString(R.string.fill_password)
                    else -> activity.applicationContext.getString(R.string.fill_all)
                }
                setExceptionMessage(message)
            }
        }

        fun isTryingToSignUp(): LiveData<Boolean> {
            return isTryingToSignUp
        }

    }

}