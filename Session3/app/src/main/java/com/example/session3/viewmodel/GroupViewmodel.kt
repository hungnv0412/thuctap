package com.example.session3.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.session3.Repository.GroupRepository
import com.example.session3.data.Entity.Group
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroupViewmodel @Inject constructor(
    private val groupRepository: GroupRepository
): ViewModel() {
    private val _groups = MutableLiveData<List<Group>>()
    val groups: LiveData<List<Group>> get() = _groups
    private val _group = MutableLiveData<Group>()
    val group : LiveData<Group> get()=_group

    fun loadGroups(){
        viewModelScope.launch {
            _groups.value = groupRepository.getAllGroup()
        }
    }
    fun addGroup(group: Group){
        viewModelScope.launch {
            groupRepository.addGroup(group)
            loadGroups()
        }
    }
    fun deleteGroup(group: Group){
        viewModelScope.launch {
            groupRepository.deleteGroup(group)
            loadGroups()
        }
    }
    fun getGroupById(id : Int){
        viewModelScope.launch {
            _group.value=groupRepository.getGroupById(id)
        }
    }
}