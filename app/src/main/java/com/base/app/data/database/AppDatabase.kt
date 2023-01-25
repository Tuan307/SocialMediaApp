package com.base.app.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.base.app.data.database.daos.CustomerDao
import com.base.app.data.database.entities.CustomerEntity

@Database(entities = [CustomerEntity::class],version = 1)
abstract class AppDatabase : RoomDatabase(){
    abstract fun customerDao(): CustomerDao
}