package com.rk.pace.presentation.screens.my_profile

import android.widget.Toast
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
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rk.pace.presentation.components.PaceButton
import com.rk.pace.presentation.components.PaceInputBox
import com.rk.pace.presentation.components.UserImage
import com.rk.pace.presentation.components.UserImageSize
import com.rk.pace.theme.Gray
import com.rk.pace.theme.back

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    goBack: () -> Unit,
    viewModel: MyProfileViewModel = hiltViewModel()
) {

    val state by viewModel.state.collectAsStateWithLifecycle()
    var newPhotoURI by rememberSaveable { mutableStateOf("") }

    var savg by remember { mutableStateOf(false) }
    val context = LocalContext.current

    LaunchedEffect(key1 = Unit) {
        viewModel.saveEvent.collect { event ->
            when (event) {
                is SaveUserEvent.Savg -> {
                    savg = true
                }

                is SaveUserEvent.Success -> {
                    savg = false
                    goBack()
                }

                is SaveUserEvent.Error -> {
                    savg = false
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            if (uri != null && state is MyProfileState.Success) {
                newPhotoURI = uri.toString()
            }
        }
    )

    when (val currentState = state) {
        is MyProfileState.Load -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is MyProfileState.Success -> {
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
                                    letterSpacing = 2.sp,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            )
                        },
                        navigationIcon = {
                            IconButton(
                                onClick = {
                                    goBack()
                                }
                            ) {
                                Icon(
                                    imageVector = back,
                                    contentDescription = ""
                                )
                            }
                        }
                    )
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
                            val imageToShow = newPhotoURI.ifEmpty {
                                currentState.user.photoURI
                            }
                            UserImage(
                                imageUrl = imageToShow,
                                size = UserImageSize.XLarge,
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
                            value = currentState.user.name,
                            onValueChange = { newName ->
                                viewModel.onUserDataChange(
                                    currentState.user.copy(name = newName)
                                )
                            },
                            placeholder = "NAME"
                        )

                        Spacer(modifier = Modifier.height(15.dp))

                        InputItem(
                            label = "USERNAME",
                            value = currentState.user.username,
                            onValueChange = { newUsername ->
                                viewModel.onUserDataChange(
                                    currentState.user.copy(username = newUsername)
                                )
                            },
                            placeholder = "USERNAME"
                        )

                        Spacer(modifier = Modifier.height(15.dp))

                        InputItem(
                            label = "EMAIL",
                            value = currentState.user.email,
                            onValueChange = { newEmail ->
                                viewModel.onUserDataChange(
                                    currentState.user.copy(email = newEmail)
                                )
                            },
                            placeholder = "EMAIL"
                        )

                        Spacer(modifier = Modifier.height(15.dp))

                        PaceButton(
                            onClick = {
                                viewModel.updateProfile(
                                    newPhotoURI,
                                    currentState.user
                                )
                            },
                            enabled = !savg,
                            load = savg,
                            text = "SAVE CHANGES"
                        )
                    }
                }
            }
        }

        is MyProfileState.Error -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = currentState.message)
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

        PaceInputBox(
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