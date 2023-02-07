package com.dapadz.issueprogressview

import android.app.Application

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        AndroidUtils.checkDisplaySize(applicationContext)
    }

}