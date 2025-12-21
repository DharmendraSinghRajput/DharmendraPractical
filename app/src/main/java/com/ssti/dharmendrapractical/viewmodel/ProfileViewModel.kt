package com.ssti.dharmendrapractical.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssti.dharmendrapractical.data.local.ProfileEntity
import com.ssti.dharmendrapractical.data.repository.UserRepository
import com.ssti.dharmendrapractical.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {

    private val _loginState = MutableStateFlow<Resource<ProfileEntity>>(Resource.Loading())
    val loginState: StateFlow<Resource<ProfileEntity>> = _loginState


    private val _registerStatus = MutableStateFlow<Resource<Long>>(Resource.Idle())
    val registerStatus: StateFlow<Resource<Long>> = _registerStatus


    private val _getProfilesStatus = MutableStateFlow<Resource<ProfileEntity>>(Resource.Idle())
    val getProfilesStatus: StateFlow<Resource<ProfileEntity>> = _getProfilesStatus

    private val _usersList = MutableStateFlow<List<ProfileEntity>>(emptyList())
    val usersList: StateFlow<List<ProfileEntity>> = _usersList

    fun getUsers() {
        viewModelScope.launch {
            repository.getUsers().collect { users ->
                _usersList.value = users
            }
        }
    }

    fun deleteUser(id: Int) {
        viewModelScope.launch {
            try {
                repository.deleteUser(id)
                // Refresh the list after deletion
                getUsers()
            } catch (e: Exception) {
                // Handle error if needed
            }
        }
    }

    fun updateUser(id: Int, name: String, email: String, password: String) {
        viewModelScope.launch {
            try {
                repository.updateUser(id, name, email, password)
                // Refresh the list after update
                getUsers()
            } catch (e: Exception) {
                // Handle error if needed
            }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _loginState.value = Resource.Loading()
            try {
                val user = repository.login(email, password)
                if (user != null) {
                    _loginState.value = Resource.Success(user)
                } else {
                    _loginState.value = Resource.Error("Invalid email or password")
                }
            } catch (e: Exception) {
                _loginState.value = Resource.Error(e.message ?: "Unknown error")
            }
        }
    }




    fun registerUser(context: Context, user: ProfileEntity) {
        viewModelScope.launch {
            _registerStatus.value = Resource.Loading()

            try {
                val id = repository.registerUser(user)
                _registerStatus.value = Resource.Success(id)
            } catch (e: Exception) {
                _registerStatus.value =
                    Resource.Error(e.localizedMessage ?: "Registration failed")
            }
        }
    }



}
