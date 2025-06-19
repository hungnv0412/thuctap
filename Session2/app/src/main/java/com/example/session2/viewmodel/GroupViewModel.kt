package com.example.session2.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.session2.Repository.GroupRepository
import com.example.session2.data.group.Group
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroupViewModel @Inject constructor(
    private val groupRepository: GroupRepository
): ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: MutableLiveData<Boolean> get() = _isLoading
    private val _groups = MutableLiveData<List<Group>>()
    val groups: MutableLiveData<List<Group>> get() = _groups

    fun refreshGroups(){
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _groups.value = groupRepository.refreshGroups()
            }catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
    fun createGroup(group: Group) {
        viewModelScope.launch {
            try {
                groupRepository.addGroup(group)
                refreshGroups()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    fun deleteGroup(id: Int) {
        viewModelScope.launch {
            try {
                groupRepository.deleteGroup(id)
                refreshGroups()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}