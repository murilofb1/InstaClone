package com.murilofb.instaclone.ui.welcome

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import com.murilofb.instaclone.R
import com.murilofb.instaclone.databinding.ActivityAuthenticationBinding
import com.murilofb.instaclone.firebase.FirebaseAuthH
import com.murilofb.instaclone.helper.ToastHelper
import com.murilofb.instaclone.model.UserModel
import com.murilofb.instaclone.ui.home.HomeActivity
import kotlin.properties.Delegates

class WelcomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuthenticationBinding
    private lateinit var welcomeVM: WelcomeViewModel
    private lateinit var loginVM: WelcomeViewModel.LoginViewModel
    private lateinit var signupVM: WelcomeViewModel.SignUpViewModel
    private var isLoginBeingShown by Delegates.notNull<Boolean>()
    private lateinit var toast: ToastHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        if (FirebaseAuthH.isSomeoneLogged()) goToHome()

        super.onCreate(savedInstanceState)

        binding = ActivityAuthenticationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViewModels()
        toast = ToastHelper(this)
        loadActivityManager()
    }

    override fun onBackPressed() {
        if (!isLoginBeingShown) welcomeVM.switchActivities()
        else super.onBackPressed()
    }

    private fun initViewModels() {
        welcomeVM = WelcomeViewModel()
        loginVM = WelcomeViewModel.LoginViewModel()
        signupVM = WelcomeViewModel.SignUpViewModel()
    }

    private fun loadActivityManager() {
        //View Model that controls what screen is being shown to the user
        welcomeVM.isLoginBeingShown().observe(this, {
            if (it) loadLoginActivity()
            else loadSignUpActivity()
            /*
            binding.edtAuthUserName.setText("murilofb")
            binding.edtAuthEmail.setText("murilo.kta.leo@gmail.com")
            binding.edtAuthPassword.setText("123456")
             */
            binding.progressAuth.isVisible = false
            binding.edtAuthUserName.isVisible = !it
            binding.btnOpenSignUp.isVisible = it
            isLoginBeingShown = it
        })
        //Applying the function to switch activities to the button openSignUp
        binding.btnOpenSignUp.setOnClickListener {
            welcomeVM.switchActivities()
        }
    }

    private fun loadLoginActivity() {
        addLoginObservers()
        binding.btnAuthConfirm.text = getString(R.string.login)
        binding.btnAuthConfirm.setOnClickListener {
            loginVM.loginWithCredentials(
                binding.edtAuthEmail.text.toString(),
                binding.edtAuthPassword.text.toString(),
                this
            )
        }
        removeSignUpObservers()
    }

    private fun addLoginObservers() {
        loginVM.isTryingToLogin().observe(this, {
            binding.progressAuth.isVisible = it
        })
        loginVM.getExceptionMessage().observe(this, {
            toast.showToast(it)
        })
    }

    private fun removeLoginObservers() {
        loginVM.isTryingToLogin().removeObservers(this)
        loginVM.getExceptionMessage().removeObservers(this)
    }

    private fun loadSignUpActivity() {
        addSignUpObservers()
        binding.btnAuthConfirm.text = getString(R.string.confirm)
        binding.btnAuthConfirm.setOnClickListener {
            val user = UserModel(
                binding.edtAuthEmail.text.toString(),
                binding.edtAuthPassword.text.toString(),
                binding.edtAuthUserName.text.toString().lowercase()
            )
            signupVM.signupUser(user, this)
        }
        removeLoginObservers()
    }

    private fun removeSignUpObservers() {
        signupVM.isTryingToSignUp().removeObservers(this)
        signupVM.getExceptionMessage().removeObservers(this)
    }

    private fun addSignUpObservers() {
        signupVM.isTryingToSignUp().observe(this, {
            binding.progressAuth.isVisible = it
        })
        signupVM.getExceptionMessage().observe(this, {
            toast.showToast(it)
        })
    }

    //Skip this activity and open HomeActivity
    fun goToHome() {
        startActivity(Intent(applicationContext, HomeActivity::class.java))
        finish()
    }


}