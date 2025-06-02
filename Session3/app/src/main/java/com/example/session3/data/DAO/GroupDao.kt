package com.example.session3.data.DAO

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.session3.data.Entity.Group

@Dao
interface GroupDao{
    @Insert(onConflict = OnConflictStrategy.Companion.IGNORE)
    suspend fun createGroup(group: Group)
    @Query("SELECT * FROM `groups` ")
    suspend fun getAllGroup(): List<Group>
    @Delete
    suspend fun deleteGroup(group: Group)
    @Query("SELECT * FROM `groups` WHERE id =:groupId")
    suspend fun getGroupById(groupId: Int): Group?
}