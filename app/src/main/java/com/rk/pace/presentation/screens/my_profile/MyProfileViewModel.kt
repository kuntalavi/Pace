package com.rk.pace.presentation.screens.my_profile

import android.net.Uri
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rk.pace.data.ut.InternalStorageHelper
import com.rk.pace.domain.model.User
import com.rk.pace.domain.use_case.user.GetMyProfileUseCase
import com.rk.pace.domain.use_case.user.UpdateMyProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class MyProfileViewModel @Inject constructor(
    private val internalStorageHelper: InternalStorageHelper,
    private val getMyProfileUseCase: GetMyProfileUseCase,
    private val updateProfileUseCase: UpdateMyProfileUseCase
) : ViewModel() {

    private val _state: MutableStateFlow<MyProfileState> = MutableStateFlow(MyProfileState.Load)
    val state = _state

    private val _saveEvent = Channel<SaveUserEvent>()
    val saveEvent = _saveEvent.receiveAsFlow()

    init {
        getMyProfile()
    }

    fun onUserDataChange(newUserData: User) {
        _state.update {
            MyProfileState.Success(newUserData)
        }
    }

    fun updateProfile(photoURI: String = "", user: User) {

        // validation

        viewModelScope.launch {

            _saveEvent.send(SaveUserEvent.Savg)

            try {
                var fileURI = user.photoURI
                if (photoURI.isNotEmpty()) {

                    val localPath =
                        internalStorageHelper.saveGalleryImageToInternalStorage(
                            photoURI.toUri(),
                            user.userId
                        )

                    if (localPath != null) {
                        fileURI = Uri.fromFile(File(localPath)).toString()
                    } else {
                        _saveEvent.send(SaveUserEvent.Error(""))
                        return@launch
                    }

                }

                val updatedUser = user.copy(photoURI = fileURI)
                updateProfileUseCase(updatedUser)

                _saveEvent.send(SaveUserEvent.Success)

                _state.update {
                    MyProfileState.Success(updatedUser)
                }
            } catch (e: Exception) {
                _saveEvent.send(SaveUserEvent.Error(e.message ?: "error"))
            }
        }
    }

    fun getMyProfile() {
        viewModelScope.launch {
            if (_state.value !is MyProfileState.Success) {
                _state.update {
                    MyProfileState.Load
                }
            }
            getMyProfileUseCase().fold(
                onSuccess = { user ->
                    _state.update {
                        MyProfileState.Success(user)
                    }
                },
                onFailure = { error ->
                    _state.update {
                        MyProfileState.Error(error.message ?: "error")
                    }
                }
            )
        }
    }
}