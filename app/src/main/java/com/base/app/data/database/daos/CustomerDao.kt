package com.base.app.data.database.daos

import androidx.room.Dao
import androidx.room.Query
import com.base.app.data.database.entities.CustomerEntity

@Dao
interface CustomerDao {

    @Query("select * from customer")
    fun getAll(): List<CustomerEntity>


}