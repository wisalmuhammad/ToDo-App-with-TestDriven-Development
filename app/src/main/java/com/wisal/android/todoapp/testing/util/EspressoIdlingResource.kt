package com.wisal.android.todoapp.testing.util

import androidx.test.espresso.idling.CountingIdlingResource

object EspressoIdlingResource {

    private  const val RESOURCE = "GLOBAL"

    @JvmField
    val countingIdlingResource = CountingIdlingResource(RESOURCE)


    fun increment() {
        countingIdlingResource.increment()
    }

    fun decrement() {
        if(!countingIdlingResource.isIdleNow) {
            countingIdlingResource.decrement()
        }
    }



    inline fun <T> wrapEspressoIdlingResource(block: () -> T): T {
        EspressoIdlingResource.increment()
        return try {
            block()
        } finally {
            EspressoIdlingResource.decrement()
        }
    }

}