package com.rk.pace.presentation.screens.my_profile

import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rk.pace.auth.domain.use_case.SignOutUseCase
import com.rk.pace.data.ut.InternalStorageHelper
import com.rk.pace.domain.model.User
import com.rk.pace.domain.use_case.run.GetARunsUseCase
import com.rk.pace.domain.use_case.user.ObserveMyProfileUseCase
import com.rk.pace.domain.use_case.user.UpdateMyProfileUseCase
import com.rk.pace.domain.ut.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class MyProfileViewModel @Inject constructor(
    private val internalStorageHelper: InternalStorageHelper,
    private val fetchARunsUseCase: GetARunsUseCase,
    private val observeMyProfileUseCase: ObserveMyProfileUseCase,
    private val updateProfileUseCase: UpdateMyProfileUseCase,
    private val signOutUseCase: SignOutUseCase
) : ViewModel() {

    private val _state: MutableStateFlow<MyProfileUiState> = MutableStateFlow(MyProfileUiState())
    val state = _state

    private val _events = Channel<MyProfileEvent>()
    val events = _events.receiveAsFlow()

    init {
        fetchData()
    }

    fun onAction(action: MyProfileAction) {

        when (action) {

            is MyProfileAction.OnNameChange -> {
                _state.update {
                    it.copy(
                        user = it.user?.copy(
                            name = action.name
                        )
                    )
                }
            }

            is MyProfileAction.OnUsernameChange -> {
                _state.update {
                    it.copy(
                        user = it.user?.copy(
                            username = action.username
                        )
                    )
                }
            }

            is MyProfileAction.OnEmailChange -> {
                _state.update {
                    it.copy(
                        user = it.user?.copy(
                            email = action.email
                        )
                    )
                }
            }

            is MyProfileAction.OnPhotoUriChange -> {
                _state.update {
                    it.copy(
                        photoUri = action.photoUri
                    )
                }
            }

            MyProfileAction.OnEditClick -> {
                viewModelScope.launch {
                    _events.send(
                        MyProfileEvent.OnEditClick
                    )
                }
            }

            is MyProfileAction.OnFollowersClick -> {
                viewModelScope.launch {
                    _events.send(
                        MyProfileEvent.OnFollowersClick(
                            action.userId,
                            action.tab
                        )
                    )
                }
            }

            is MyProfileAction.OnFollowingClick -> {
                viewModelScope.launch {
                    _events.send(
                        MyProfileEvent.OnFollowingClick(
                            action.userId,
                            action.tab
                        )
                    )
                }
            }

            is MyProfileAction.OnRunClick -> {
                viewModelScope.launch {
                    _events.send(
                        MyProfileEvent.OnRunClick(
                            action.userId,
                            action.runId
                        )
                    )
                }
            }

            MyProfileAction.OnSaveChangesClick -> {
                saveChanges(
                    user = state.value.user,
                    photoUri = state.value.photoUri
                )
            }

            MyProfileAction.OnBackClick -> {
                viewModelScope.launch {
                    _events.send(
                        MyProfileEvent.OnBack
                    )
                }
            }

            MyProfileAction.OnSignOutClick -> {
                signOut()
            }

        }

    }

    private fun saveChanges(
        user: User?,
        photoUri: String?
    ) {

        if (user == null) return

        _state.update {
            it.copy(
                saving = true
            )
        }

        viewModelScope.launch {

            try {

                var uri = user.photoURI

                if (photoUri != null) {

                    val localPath =
                        internalStorageHelper.saveGalleryImageToInternalStorage(
                            photoUri.toUri(),
                            user.userId
                        )

                    if (
                        localPath != null
                    ) {
                        uri = Uri.fromFile(File(localPath)).toString()
                    } else {
                        _events.send(
                            MyProfileEvent.ChangesSaveError(
                                ""
                            )
                        )
                        return@launch
                    }

                }

                val new = user.copy(
                    photoURI = uri
                )
                updateProfileUseCase(new)

                _state.update {
                    it.copy(
                        saving = false
                    )
                }

                _events.send(
                    MyProfileEvent.ChangesSaved
                )

            } catch (e: Exception) {

                _events.send(
                    MyProfileEvent.ChangesSaveError(
                        e.message ?: "Unknown Error"
                    )
                )

            }

        }

    }

    private fun fetchData() {

        viewModelScope.launch {

            _state.update {
                it.copy(
                    load = true
                )
            }

            combine(
                observeMyProfileUseCase(),
                fetchARunsUseCase()
            ) { user, runs ->
                Log.d("PaceDebug", "Database Emitted: User=${user?.name}, Runs=${runs.size}")
                Pair(user, runs)
            }.catch { error ->
                Log.e("PaceDebug", "FLOW CRASHED!", error)
                _state.update {
                    it.copy(
                        load = false,
                        error = error.message
                    )
                }
            }.collect { (user, runs) ->
                _state.update {
                    it.copy(
                        load = false,
                        user = user,
                        runs = runs,
                        error = null
                    )
                }
            }
        }

    }

    private fun signOut() {
        viewModelScope.launch {
            when (
                val result = signOutUseCase()
            ) {
                is Result.Success -> {}
                is Result.Error -> {}
            }
        }
    }

}