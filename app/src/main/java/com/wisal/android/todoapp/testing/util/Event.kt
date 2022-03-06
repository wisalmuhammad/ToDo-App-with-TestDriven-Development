package com.wisal.android.todoapp.testing.util

import androidx.lifecycle.Observer
import java.lang.Exception


open class Event <out T>(private val content: T) {

    var hasBeenHandled = false
    private set

    fun getContentIfNotHandled(): T? {
        return if(hasBeenHandled) null
        else {
            hasBeenHandled = true
            content
        }
    }

    fun peekContent(): T = content

}

class EventObserver<T>(private val onEventUnhandledException: (T) -> Unit): Observer<Event<T>> {
    override fun onChanged(t: Event<T>?) {
        t?.getContentIfNotHandled()?.let {
            onEventUnhandledException(it)
        }
    }
}