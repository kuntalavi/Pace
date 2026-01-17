package com.rk.pace.presentation.screens.my_profile

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rk.pace.auth.presentation.AuthViewModel
import com.rk.pace.presentation.components.CustomButton
import com.rk.pace.presentation.components.ProfileImage
import com.rk.pace.presentation.components.ProfileImageSize
import com.rk.pace.theme.Black

@Composable
fun UserScreen(
    authViewModel: AuthViewModel = hiltViewModel(),
    viewModel: MyProfileViewModel = hiltViewModel(),
//    onSignOut: () -> Unit
) {
    val user by viewModel.user.collectAsStateWithLifecycle()

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            if (uri != null && user is MyProfileState.Success) {
                val currentUser = (user as MyProfileState.Success).user
                viewModel.onImageSelected(uri, currentUser)
            }
        }
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
    ) {
        when (val state = user) {
            is MyProfileState.Load -> {
                CircularProgressIndicator(
                    modifier = Modifier
                        .fillMaxSize()
                        .align(Alignment.Center)
                )
            }

            is MyProfileState.Success -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    Log.d("imageURI", state.user.photoURI ?: "")
                    ProfileImage(
                        imageUrl = state.user.photoURI,
                        modifier = Modifier,
                        size = ProfileImageSize.XLarge,
                        showBorder = true,
                        borderColor = Black,
                        onClick = {
                            photoPickerLauncher.launch(
                                PickVisualMediaRequest(
                                    ActivityResultContracts.PickVisualMedia.ImageOnly
                                )
                            )
                        }
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = state.user.name.uppercase(),
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        letterSpacing = 1.sp,
                        color = Black
                    )
                    Text(
                        text = "@${state.user.username}",
                        fontWeight = FontWeight.Medium,
                        fontSize = 18.sp,
                        letterSpacing = 1.sp
                    )
                    CustomButton(
                        onClick = {
                            authViewModel.signOut()
//                            onSignOut()
                        },
                        text = "SIGN OUT"
                    )
                }
            }

            is MyProfileState.Error -> Text(text = state.message)
        }
    }
}