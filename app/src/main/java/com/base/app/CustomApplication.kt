package com.base.app

import android.app.Application
import com.base.app.data.AppDataManager
import com.base.app.data.prefs.AppPreferencesHelper
import com.base.app.data.prefs.PreferencesHelper
import com.base.app.data.realm.AppRealmHelper
import com.base.app.data.realm.RealmHelper
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.HiltAndroidApp
import io.github.inflationx.calligraphy3.CalligraphyConfig
import io.github.inflationx.calligraphy3.CalligraphyInterceptor
import io.github.inflationx.viewpump.ViewPump

@HiltAndroidApp
class CustomApplication : Application() {

    private var mPreferencesHelper: PreferencesHelper? = null
    private var mRealmHelper: RealmHelper? = null

    companion object {
        lateinit var dataManager: AppDataManager
    }

    override fun onCreate() {
        super.onCreate()
        initCalligraphy()
        mPreferencesHelper = AppPreferencesHelper(this)
        mRealmHelper = AppRealmHelper(this)
        dataManager = AppDataManager(this, mPreferencesHelper, mRealmHelper)
    }

    private fun initCalligraphy() {
        ViewPump.init(
            ViewPump.builder()
                .addInterceptor(
                    CalligraphyInterceptor(
                        CalligraphyConfig.Builder()
                            .setDefaultFontPath("fonts/Roboto-Regular.ttf")
                            .setFontAttrId(R.attr.fontPath)
                            .build()
                    )
                )
                .build()
        )
    }
}