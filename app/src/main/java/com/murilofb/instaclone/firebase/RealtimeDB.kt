package com.murilofb.instaclone.firebase

import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener

open class RealtimeDB {
    
    private val referencesList: MutableList<Query> = ArrayList()
    private val listenersList: MutableList<ValueEventListener> = ArrayList()

    protected fun addConsult(reference: Query, eventListener: ValueEventListener) {
        referencesList.add(reference)
        listenersList.add(eventListener)
    }

    fun removeAllListeners() {
        var i = 0
        while (i < referencesList.size) {
            referencesList[i].removeEventListener(listenersList[i])
            i++
        }
    }
}