package com.example.session3.Repository

import com.example.session3.data.DAO.GroupDao
import com.example.session3.data.Entity.Group
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GroupRepository @Inject constructor(
    private val groupDao: GroupDao
) {
    suspend fun addGroup(group: Group){
        withContext(Dispatchers.IO) {
            groupDao.createGroup(group)
        }
    }
    suspend fun getAllGroup(): List<Group>{
        return withContext(Dispatchers.IO) {
            groupDao.getAllGroup()
        }
    }
    suspend fun deleteGroup(group: Group){
        withContext(Dispatchers.IO){
            groupDao.deleteGroup(group)
        }
    }
    suspend fun getGroupById(id : Int): Group?{
        return withContext(Dispatchers.IO){
            groupDao.getGroupById(id)
        }
    }
}