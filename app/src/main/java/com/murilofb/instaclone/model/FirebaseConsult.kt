package com.murilofb.instaclone.model

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import java.lang.Exception
import java.util.concurrent.Executor

class FirebaseConsult(
    val activity: AppCompatActivity,
    private val reference: DatabaseReference,
    private val valueEventListener: ValueEventListener
) : DefaultLifecycleObserver, Task<Void>() {
    private var removeOnDestroy = true

    init {
        activity.lifecycle.addObserver(this)
    }

    override fun onDestroy(owner: LifecycleOwner) {
        if (removeOnDestroy) reference.removeEventListener(valueEventListener)
        super.onDestroy(owner)
    }

    fun removeListenerOnDestroy(value: Boolean) {
        this.removeOnDestroy = value
    }

    override fun isComplete(): Boolean {
        TODO("Not yet implemented")
    }

    override fun isSuccessful(): Boolean {
        TODO("Not yet implemented")
    }

    override fun isCanceled(): Boolean {
        TODO("Not yet implemented")
    }

    override fun getResult(): Void? {
        TODO("Not yet implemented")
    }

    override fun <X : Throwable?> getResult(p0: Class<X>): Void? {
        TODO("Not yet implemented")
    }

    override fun getException(): Exception? {
        TODO("Not yet implemented")
    }

    override fun addOnSuccessListener(p0: OnSuccessListener<in Void>): Task<Void> {
        TODO("Not yet implemented")
    }

    override fun addOnSuccessListener(p0: Executor, p1: OnSuccessListener<in Void>): Task<Void> {
        TODO("Not yet implemented")
    }

    override fun addOnSuccessListener(p0: Activity, p1: OnSuccessListener<in Void>): Task<Void> {
        TODO("Not yet implemented")
    }

    override fun addOnFailureListener(p0: OnFailureListener): Task<Void> {
        TODO("Not yet implemented")
    }

    override fun addOnFailureListener(p0: Executor, p1: OnFailureListener): Task<Void> {
        TODO("Not yet implemented")
    }

    override fun addOnFailureListener(p0: Activity, p1: OnFailureListener): Task<Void> {
        TODO("Not yet implemented")
    }
}