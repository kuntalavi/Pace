package com.rk.pace.presentation.screens.my_profile

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rk.pace.presentation.components.ButtonVariant
import com.rk.pace.presentation.components.PaceButton
import com.rk.pace.presentation.components.PaceTextInput
import com.rk.pace.presentation.components.PaceUserDp
import com.rk.pace.presentation.components.PaceUserDpSize
import com.rk.pace.presentation.theme.Gray
import com.rk.pace.presentation.theme.arrowLeft
import com.rk.pace.presentation.theme.scheme
import com.rk.pace.presentation.ut.ObserveAsEvents

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreenRoot(
    onBack: () -> Unit,
    viewModel: MyProfileViewModel = hiltViewModel()
) {

    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarState = remember { SnackbarHostState() }

    ObserveAsEvents(
        flow = viewModel.events
    ) { event ->

        when (event) {

            MyProfileEvent.OnBack -> {
                onBack()
            }

            MyProfileEvent.ChangesSaved -> {
                onBack()
            }

            is MyProfileEvent.ChangesSaveError -> {
                snackbarState.showSnackbar(
                    message = event.message,
                    duration = SnackbarDuration.Short
                )
            }

            else -> {}

        }

    }

    EditProfileScreen(
        state = state,
        snackbarState = snackbarState,
        onAction = viewModel::onAction
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    state: MyProfileUiState,
    snackbarState: SnackbarHostState,
    onAction: (MyProfileAction) -> Unit
) {

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            if (uri != null) {
                onAction(
                    MyProfileAction.OnPhotoUriChange(
                        photoUri = uri.toString()
                    )
                )
            }
        }
    )

    if (state.load) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else if (state.error != null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = state.error
            )
        }
    } else {

        if (state.user == null) return

        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background
                    ),
                    title = {
                        Text(
                            text = "EDIT PROFILE",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Medium,
                                letterSpacing = 2.sp
                            )
                        )
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                onAction(
                                    MyProfileAction.OnBackClick
                                )
                            }
                        ) {
                            Icon(
                                imageVector = arrowLeft,
                                contentDescription = null
                            )
                        }
                    }
                )
            },
            snackbarHost = {
                SnackbarHost(
                    hostState = snackbarState
                ) { data ->
                    Snackbar(
                        containerColor = scheme.errorContainer,
                        contentColor = scheme.onErrorContainer,
                        snackbarData = data
                    )
                }
            }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                ) {

                    Spacer(modifier = Modifier.height(15.dp))

                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        val imageToShow = state.photoUri ?: state.user.photoURI

                        PaceUserDp(
                            imageUrl = imageToShow,
                            size = PaceUserDpSize.XLarge,
                            onClick = {
                                photoPickerLauncher.launch(
                                    PickVisualMediaRequest(
                                        ActivityResultContracts.PickVisualMedia.ImageOnly
                                    )
                                )
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(15.dp))

                    InputItem(
                        label = "NAME",
                        value = state.user.name,
                        onValueChange = { name ->
                            onAction(
                                MyProfileAction.OnNameChange(
                                    name
                                )
                            )
                        },
                        placeholder = "NAME"
                    )

                    Spacer(modifier = Modifier.height(15.dp))

                    InputItem(
                        label = "USERNAME",
                        value = state.user.username,
                        onValueChange = { username ->
                            onAction(
                                MyProfileAction.OnUsernameChange(
                                    username
                                )
                            )
                        },
                        placeholder = "USERNAME"
                    )

                    Spacer(modifier = Modifier.height(15.dp))

                    InputItem(
                        label = "EMAIL",
                        value = state.user.email,
                        onValueChange = { email ->
                            onAction(
                                MyProfileAction.OnEmailChange(
                                    email
                                )
                            )
                        },
                        placeholder = "EMAIL"
                    )

                    Spacer(modifier = Modifier.height(25.dp))

                    PaceButton(
                        onClick = {
                            onAction(
                                MyProfileAction.OnSaveChangesClick
                            )
                        },
                        enabled = !state.saving,
                        load = state.saving,
                        text = "SAVE CHANGES",
                        variant = ButtonVariant.Filled
                    )
                }
            }
        }
    }

}


@Composable
fun InputItem(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String
) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Medium,
                letterSpacing = 1.sp,
                color = Gray
            )
        )

        Spacer(modifier = Modifier.height(5.dp))

        PaceTextInput(
            modifier = Modifier
                .fillMaxWidth(),
            value = value,
            onValueChange = {
                onValueChange(it)
            },
            placeholder = placeholder
        )
    }
}