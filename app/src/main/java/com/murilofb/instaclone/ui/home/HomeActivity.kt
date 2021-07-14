package com.murilofb.instaclone.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx
import com.murilofb.instaclone.R
import com.murilofb.instaclone.constants.IntentTags
import com.murilofb.instaclone.databinding.ActivityHomeBinding
import com.murilofb.instaclone.firebase.FirebaseAuthH
import com.murilofb.instaclone.ui.home.feed.FeedFragment
import com.murilofb.instaclone.ui.home.newpost.FilterActivity
import com.murilofb.instaclone.ui.home.newpost.NewPostFragment
import com.murilofb.instaclone.ui.home.profile.ProfileFragment
import com.murilofb.instaclone.ui.home.search.SearchFragment
import com.murilofb.instaclone.ui.welcome.WelcomeActivity
import com.theartofdev.edmodo.cropper.CropImage

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.includedToolbar.customToolbar)
        goToFragment(FeedFragment.getInstance)
        initBottomNavigation()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_home_toolbar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menuSignOut -> {
                FirebaseAuthH.signOut()
                startActivity(Intent(applicationContext, WelcomeActivity::class.java))
                finishAffinity()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (resultCode == RESULT_OK && requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            val newPhotoUri = result.uri
/*
            val newPhotoBitmap = MediaStore.Images.Media.getBitmap(contentResolver, newPhotoUri)
            val baos = ByteArrayOutputStream()
            if (newPhotoBitmap == null) {
                Log.i("NewPostActivity", "Null Bitmap")
            }
            if (result.uri == null) {
                Log.i("NewPostActivity", "null Uri")
            }
            newPhotoBitmap.compress(Bitmap.CompressFormat.PNG, 90, baos)
            val byteArr = baos.toByteArray()

 */
            val filterIntent = Intent(this, FilterActivity::class.java)
            filterIntent.putExtra(IntentTags.EXTRA_IMAGE_URI, newPhotoUri.toString())
            startActivity(filterIntent)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun initBottomNavigation() {
        val bottomNav: BottomNavigationViewEx = binding.includedBottomNavigation.bottomNavHome
        bottomNav.enableAnimation(true)
        bottomNav.enableShiftingMode(false)
        bottomNav.enableItemShiftingMode(false)
        bottomNav.setTextVisibility(false)
        //Deixar o primeiro item como marcado na bottom navigation bar
        val bottomNavMenu = bottomNav.menu
        val menuItem = bottomNavMenu.getItem(0)
        menuItem.isChecked = true

        bottomNav.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.menuProfile -> goToFragment(ProfileFragment.getInstance)
                R.id.menuHome -> goToFragment(FeedFragment.getInstance)
                R.id.menuSearch -> goToFragment(SearchFragment.getInstance)
                else -> goToFragment(NewPostFragment.getInstance)
            }
        }
    }

    private fun goToFragment(fragment: Fragment): Boolean {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.homeViewpager, fragment).commit()
        supportFragmentManager.popBackStack()
        return true
    }
}