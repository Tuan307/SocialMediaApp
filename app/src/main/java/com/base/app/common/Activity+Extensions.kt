package com.base.app.common

import android.app.Activity
import com.base.app.CustomApplication

val Activity.customApplication: CustomApplication
get() = application as CustomApplication