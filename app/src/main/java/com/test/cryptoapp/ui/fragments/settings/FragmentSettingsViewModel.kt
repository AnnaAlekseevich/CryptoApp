package com.test.cryptoapp.ui.fragments.settings

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.cryptoapp.db.DatabaseHelper
import com.test.cryptoapp.net.models.User
import kotlinx.coroutines.launch

class FragmentSettingsViewModel(private val dbHelper: DatabaseHelper) : ViewModel() {

    val savingEnabledLiveData = MutableLiveData(false)
    val isUserExistLiveData = MutableLiveData(false)

    private var firstNameUser: String? = null
    private var lastNameUser: String? = null
    private var dateOfBirthUser: String? = null
    private var urlPhotoUser: String? = null
    private var ifUserExist: Boolean = false
    private lateinit var usersDataFromDb: User

    init {
        viewModelScope.launch {
            runCatching {
                var usersFromDb = dbHelper.getUser()

                if (usersFromDb != null) {
                    ifUserExist = true
                    usersDataFromDb = usersFromDb
                    isUserExistLiveData.value = true
                    savingEnabledLiveData.value = true
                }
                if (!ifUserExist) {
                    dbHelper.insertUser(createUser())
                    ifUserExist = true
                    usersDataFromDb = createUser()
                    isUserExistLiveData.value = true
                }
            }.onFailure {
                Log.d("ROOMUSER", "onFailure")
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

    fun getUser(): User {
        return usersDataFromDb
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
                dbHelper.updateUser(createUser())
            }.onFailure {
                Log.d("ROOMUSER", "onFailure onSave()")
            }
        }

    }


    private fun updateSaveButtonState() {
        if (firstNameUser?.isNotEmpty() == true && lastNameUser?.isNotEmpty() == true && dateOfBirthUser?.isNotEmpty() == true && urlPhotoUser?.isNotEmpty() == true) {
            savingEnabledLiveData.value = true
        } else if (firstNameUser?.isNullOrEmpty() == true || lastNameUser?.isNullOrEmpty() == true || dateOfBirthUser?.isNullOrEmpty() == true || urlPhotoUser?.isNullOrEmpty() == true) {
            savingEnabledLiveData.value = false
        }

    }
}