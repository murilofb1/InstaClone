package com.murilofb.instaclone.firebase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.murilofb.instaclone.constants.RealtimeConstants

import com.murilofb.instaclone.model.UserModel

class RealtimeDatabaseHelper {

    companion object {

        private fun getCurrentUserReference(): DatabaseReference {
            val currentUserId = currentUser.value!!.id
            return RealtimeConstants.usersRootReference.child(currentUserId)
        }

        fun incrementPostsCount() {
            var postsCount: Int = currentUser.value?.postsCount!!
            postsCount += 1
            getCurrentUserReference().child(RealtimeConstants.USER_POSTS_COUNT_KEY)
                .setValue(postsCount)
        }

        fun getCurrentUserImagesPathReference(): DatabaseReference {
            val currentUserId = currentUser.value!!.id
            return RealtimeConstants.imagesPathReference.child(currentUserId)
        }

        fun registerUser(user: UserModel) {
            RealtimeConstants.usersRootReference.child(user.id).setValue(user)
        }

        //Dinamically change the user data
        internal val currentUser = MutableLiveData<UserModel>()
        private val userValueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                currentUser.value = snapshot.getValue(UserModel::class.java)
            }

            override fun onCancelled(error: DatabaseError) {}
        }

        fun updateCurrentUser(firebaseUser: FirebaseUser?) {
            if (firebaseUser == null) {
                currentUser.value = null
            } else {
                val currentUserId = firebaseUser.uid
                RealtimeConstants.usersRootReference.child(currentUserId)
                    .addValueEventListener(userValueEventListener)
            }
        }

        fun changeProfilePicture(imagePath: String, imageLink: String) {
            getCurrentUserReference()
                .child(RealtimeConstants.PROFILE_PICTURE_LINK_KEY)
                .setValue(imageLink)
            getCurrentUserImagesPathReference()
                .child(RealtimeConstants.PROFILE_PICTURE_PATH_KEY)
                .setValue(imagePath)
        }


        //Pegar o livedata do usuario atual
        fun getCurrentUser(): LiveData<UserModel> = currentUser


        //Atualizar os dados do usuário no firebase
        fun updateUserInfo(user: UserModel) {
            RealtimeConstants.usersRootReference.child(user.id).setValue(user)
        }

        fun removeUserPhoto() {
            RealtimeConstants.usersRootReference
                .child(currentUser.value!!.id)
                .child(RealtimeConstants.PROFILE_PICTURE_LINK_KEY)
                .setValue("")
        }
    }
}