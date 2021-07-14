package com.murilofb.instaclone.firebase

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.murilofb.instaclone.constants.RealtimeConstants
import com.murilofb.instaclone.model.UserModel

class FollowersDB : RealtimeDB() {

    private fun getCurrentUserFollowersReference(): DatabaseReference {
        return RealtimeConstants.followersRootReference
            .child(FirebaseAuthH.getCurrentUserID()!!)
    }

    private fun getCurrentUserFollowingCountReference(): DatabaseReference {
        return RealtimeConstants.usersRootReference
            .child(FirebaseAuthH.getCurrentUserID()!!)
            .child(RealtimeConstants.USER_FOLLOWING_COUNT_KEY)
    }

    fun followUser(user: UserModel) {
        getCurrentUserFollowersReference().child(user.id).setValue(true)
        //Add Follower to the Selected User
        val followerCount = (user.followersCount) + 1
        RealtimeConstants.usersRootReference
            .child(user.id)
            .child(RealtimeConstants.USER_FOLLOWERS_COUNT_KEY)
            .setValue(followerCount)
        //Add to current user following count
        val followingCount: Int = RealtimeDatabaseHelper.currentUser.value!!.followingCount + 1
        getCurrentUserFollowingCountReference().setValue(followingCount)
    }

    fun unfollowUser(user: UserModel) {
        getCurrentUserFollowersReference().child(user.id).removeValue()
        //Remove follower to the selected user
        val followerCount = (user.followersCount) - 1
        RealtimeConstants.usersRootReference
            .child(user.id)
            .child(RealtimeConstants.USER_FOLLOWERS_COUNT_KEY)
            .setValue(followerCount)
        //Add to current user following count
        val followingCount: Int = RealtimeDatabaseHelper.currentUser.value!!.followingCount - 1
        getCurrentUserFollowingCountReference().setValue(followingCount)
    }

    fun amIFollowing(id: String, listener: ValueEventListener) {
        val reference = getCurrentUserFollowersReference().child(id)
        reference.addValueEventListener(listener)
        addConsult(reference, listener)
    }

    fun loadFollowersList(eventListener: ValueEventListener) {
        val reference = getCurrentUserFollowersReference()
        reference.addValueEventListener(eventListener)
        addConsult(reference, eventListener)
    }

}