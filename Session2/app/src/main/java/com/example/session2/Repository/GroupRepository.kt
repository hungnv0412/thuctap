package com.example.session2.Repository

import com.example.session2.data.group.Group
import com.example.session2.data.group.GroupDao
import com.example.session2.network.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GroupRepository @Inject constructor(
    private val apiService: ApiService,
    private val groupDao: GroupDao
) {
    suspend fun refreshGroups(): List<Group> {
        return withContext(Dispatchers.IO) {
            val cachedGroups = groupDao.getAllGroups()
            try {
                val response = apiService.getGroups()
                if (response.isSuccessful) {
                    val groups = response.body() ?: emptyList()
                    groupDao.insertAll(groups)
                    groups
                } else {
                    println("GroupRepository: API error ${response.code()}")
                    cachedGroups
                }
            } catch (e: Exception) {
                println("GroupRepository: Network error - ${e.message}")
                cachedGroups
            }
        }
    }

    suspend fun addGroup(group: Group) {
        withContext(Dispatchers.IO) {
            try {
                val response = apiService.createGroup(group)
                if (response.isSuccessful) {
                    refreshGroups()
                } else {
                    println("GroupRepository: Failed to create group - ${response.code()}")
                }
            } catch (e: Exception) {
                println("GroupRepository: Error creating group - ${e.message}")
            }
        }
    }

    suspend fun getGroupById(id: Int): Group? {
        return withContext(Dispatchers.IO) {
            try {
                groupDao.getGroupById(id)
            } catch (e: Exception) {
                println("GroupRepository: Error getGroupById - ${e.message}")
                null
            }
        }
    }

    suspend fun deleteGroup(id: Int) {
        withContext(Dispatchers.IO) {
            try {
                val response = apiService.deleteGroup(id)
                if (response.isSuccessful) {
                    refreshGroups()
                } else {
                    println("GroupRepository: Failed to delete group - ${response.code()}")
                }
            } catch (e: Exception) {
                println("GroupRepository: Error deleting group - ${e.message}")
            }
        }
    }
}
