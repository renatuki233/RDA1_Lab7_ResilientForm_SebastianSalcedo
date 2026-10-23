package com.example.rda1_lab7_resilientform_sebastiansalcedo.ui

import androidx.compose.runtime.getValue
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.rda1_lab7_resilientform_sebastiansalcedo.data.UserPreferences
import kotlinx.coroutines.launch

class FormViewModel(
    private val stateHandle: SavedStateHandle,
    private val userPrefs: UserPreferences,



) : ViewModel() {

    var showSavedIndicator by mutableStateOf(false)
        private set

    private var indicatorJob: Job? = null

    // Email: sobrevive a rotación / destrucción de Activity
    var email by mutableStateOf(
        stateHandle.get<String>("email_key") ?: ""
    )
        private set

    fun updateEmail(newEmail: String) {
        email = newEmail
        stateHandle["email_key"] = newEmail
    }
    // Estado temporal en memoria: ViewModel
    var notificationsEnabled by mutableStateOf(false)
        private set

    fun updateNotifications(enabled: Boolean) {
        notificationsEnabled = enabled
    }

    // Nombre: leído desde DataStore
    val nameFromDisk =
        userPrefs.userName.asLiveData()

    fun saveName(newName: String) {
        viewModelScope.launch {

            userPrefs.saveName(newName)

            showSavedIndicator = true

            indicatorJob?.cancel()

            indicatorJob = viewModelScope.launch {
                delay(1000)
                showSavedIndicator = false
            }
        }
    }
}