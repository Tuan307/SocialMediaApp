package com.base.app.di

import java.lang.annotation.Documented
import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class BytePaySite()

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class BytePaySiteWeb()

@Documented
@Qualifier
annotation class ApiMobile{
}

@Documented
@Qualifier
annotation class ApiWeb{
}