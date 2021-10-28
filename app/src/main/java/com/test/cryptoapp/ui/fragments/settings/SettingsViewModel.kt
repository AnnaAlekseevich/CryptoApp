package com.test.cryptoapp.ui.fragments.settings

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.cryptoapp.data.repository.user.UserRepository
import com.test.cryptoapp.domain.models.UiState
import com.test.cryptoapp.domain.models.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SettingsViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _savingEnabledDataState = MutableStateFlow(false)
    val savingEnabledDataState: StateFlow<Boolean>
        get() = _savingEnabledDataState
    private val _saveDataState = MutableStateFlow(false)
    val saveDataState: StateFlow<Boolean>
        get() = _saveDataState

    private val _myUiState = MutableStateFlow<UiState<User>>(UiState.Loading)
    val myUiState: StateFlow<UiState<User>>
        get() = _myUiState


    private var firstNameUser: String? = null
    private var lastNameUser: String? = null
    private var dateOfBirthUser: String? = null
    private var urlPhotoUser: String? = null

    init {
        viewModelScope.launch {
            runCatching {
                _myUiState.value = UiState.Loading
                var user = userRepository.getUser()

                if (user != null) {
                    _savingEnabledDataState.value = true
                } else {
                    _savingEnabledDataState.value = false
                    user = createUser()
                    userRepository.updateUser(user)
                }
                _myUiState.value = UiState.Success(user)
            }.onFailure {
                Log.d("ROOMUSER", "onFailure")
                _myUiState.value = UiState.Error(it.toString())
            }
        }

        updateSaveButtonState()
    }

    fun setFirstName(firstName: String?) {
        firstNameUser = firstName
        updateSaveButtonState()
    }

    fun setLastName(lastName: String?) {
        lastNameUser = lastName
        updateSaveButtonState()
    }

    fun setDateOfBirth(dateOfBirth: String?) {
        dateOfBirthUser = dateOfBirth
        updateSaveButtonState()
    }

    fun setPhotoUser(authorPhotoUrl: String?) {
        urlPhotoUser = authorPhotoUrl
        updateSaveButtonState()
    }

    private fun createUser(): User {
        return User(
            1,
            firstNameUser,
            lastNameUser,
            dateOfBirthUser,
            urlPhotoUser
        )
    }

    fun onSave() {
        viewModelScope.launch {
            runCatching {
                userRepository.updateUser(createUser())
                _saveDataState.value = true
            }.onFailure {
                Log.d("ROOMUSER", "onFailure onSave()")
            }
        }
    }

    private fun updateSaveButtonState() {
        if (firstNameUser?.isNotEmpty() == true && lastNameUser?.isNotEmpty() == true) {
            _savingEnabledDataState.value = true
        } else if (firstNameUser?.isNullOrEmpty() == true || lastNameUser?.isNullOrEmpty() == true) {
            _savingEnabledDataState.value = false
        }
    }
}