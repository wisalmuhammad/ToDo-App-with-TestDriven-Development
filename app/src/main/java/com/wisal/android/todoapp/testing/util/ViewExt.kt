package com.wisal.android.todoapp.testing.util

import android.widget.Toast
import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData

fun Fragment.setUpToast(owner: LifecycleOwner,data: LiveData<Event<Int>>) {
    data.observe(owner, {
        it.getContentIfNotHandled()?.let { id ->
            Toast.makeText(context,id,Toast.LENGTH_SHORT).run {
                show()
            }
        }
    })
}