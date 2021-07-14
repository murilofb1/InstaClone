package com.murilofb.instaclone.helper

import android.renderscript.Sampler
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.google.firebase.database.ValueEventListener

class RemoveFirebaseListenersObserver : DefaultLifecycleObserver {
    var activity: AppCompatActivity
    var listener: ValueEventListener? = null

    /*
    LISTA DE LISTENERS E LISTA DE REFERENCES ATRELADAS AOS LISTENERS PARA PODER REMOVER NO ON DESTROY
     */


    constructor(activity: AppCompatActivity, listener: ValueEventListener) {
        this.activity = activity
        this.listener = listener
    }

    constructor(activity: AppCompatActivity, listeners: Array<ValueEventListener>) {
        this.activity = activity
    }

    override fun onDestroy(owner: LifecycleOwner) {
        if (activity.lifecycle == owner.lifecycle) {
            if (listener != null) {

            }
        }
        super.onDestroy(owner)
    }
}