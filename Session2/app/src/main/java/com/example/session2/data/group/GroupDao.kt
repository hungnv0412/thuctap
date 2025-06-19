package com.example.session2.data.group

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface GroupDao {
    @Insert(onConflict = OnConflictStrategy.Companion.IGNORE)
    suspend fun insertGroup(group: Group)
    @Query("SELECT * FROM `groups`")
    suspend fun getAllGroups(): List<Group>
    @Query("SELECT * FROM `groups` WHERE id = :id")
    suspend fun getGroupById(id: Int): Group?
    @Query("DELETE FROM `groups` WHERE id = :id")
    suspend fun deleteGroup(id: Int)
    @Insert(onConflict = OnConflictStrategy.Companion.IGNORE)
    suspend fun insertAll(group: List<Group>)
    @Query("DELETE FROM  `groups`")
    fun deleteAllGroups()
}