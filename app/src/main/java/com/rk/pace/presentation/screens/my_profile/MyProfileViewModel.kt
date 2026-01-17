package com.rk.pace.presentation.screens.my_profile

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rk.pace.data.ut.InternalStorageHelper
import com.rk.pace.domain.model.User
import com.rk.pace.domain.use_case.user.GetMyProfileUseCase
import com.rk.pace.domain.use_case.user.UpdateProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class MyProfileViewModel @Inject constructor(
    private val internalStorageHelper: InternalStorageHelper,
    private val getMyProfileUseCase: GetMyProfileUseCase,
    private val updateProfileUseCase: UpdateProfileUseCase
) : ViewModel() {

    private val _user: MutableStateFlow<MyProfileState> = MutableStateFlow(MyProfileState.Load)
    val user = _user

    init {
        getMyProfile()
    }

    fun onImageSelected(uri: Uri, user: User) {
        viewModelScope.launch {
            val fileName = "${user.userId}/image.png"
            val localPath = internalStorageHelper.saveGalleryImageToInternalStorage(uri, user.userId)

            if (localPath != null) {
                // Convert path to file URI for Room/UI
                val fileUri = Uri.fromFile(File(localPath)).toString()

                // Now update your User state/Room DB with this 'fileUri'
                updateProfile(user.copy(photoURI = fileUri))
            }
        }
    }

    fun updateProfile(user: User) {
        viewModelScope.launch {
            updateProfileUseCase(user)
            getMyProfile()
        }
    }

    private fun getMyProfile() {
        viewModelScope.launch() {
            _user.update {
                MyProfileState.Load
            }
            getMyProfileUseCase().fold(
                onSuccess = { user ->
                    _user.update {
                        MyProfileState.Success(user)
                    }
                    Log.d("imageURI", user.photoURI ?: "")
                },
                onFailure = { error ->
                    _user.update {
                        MyProfileState.Error(error.message ?: "error")
                    }
                }
            )
        }
    }
}