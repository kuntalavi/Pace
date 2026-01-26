package com.rk.pace.presentation.screens.my_profile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rk.pace.auth.presentation.AuthViewModel
import com.rk.pace.presentation.components.PaceButton
import com.rk.pace.presentation.components.ProfileImage
import com.rk.pace.presentation.components.ProfileImageSize
import com.rk.pace.theme.edit

@Composable
fun UserScreen(
    authViewModel: AuthViewModel = hiltViewModel(),
    viewModel: MyProfileViewModel = hiltViewModel(),
    onEditClick: () -> Unit,
    onFollowersClick: (userId: String, tab: Int) -> Unit,
    onFollowingClick: (userId: String, tab: Int) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        viewModel.getMyProfile()
    }

    when (val state = state) {
        is MyProfileState.Load -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is MyProfileState.Success -> {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp)
                        .verticalScroll(rememberScrollState())
                ) {

                    ProfileImage(
                        imageUrl = state.user.photoURI,
                        size = ProfileImageSize.XLarge
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = state.user.name.uppercase(),
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp,
                                color = MaterialTheme.colorScheme.primary
                            )
                        )
                        IconButton(
                            onClick = {
                                onEditClick()
                            }
                        ) {
                            Icon(
                                imageVector = edit,
                                contentDescription = ""
                            )
                        }
                    }

                    Text(
                        text = "#${state.user.username}",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Medium,
                            letterSpacing = 1.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.Top,
                    ) {

                        TextButton(
                            onClick = {
                                onFollowersClick(
                                    state.user.userId,
                                    0
                                )
                            },
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Text(
                                text = "${state.user.followers} FOLLOWERS",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp
                                )
                            )
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        TextButton(
                            onClick = {
                                onFollowingClick(
                                    state.user.userId,
                                    1
                                )
                            },
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Text(
                                text = "${state.user.following} FOLLOWING",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp
                                )
                            )
                        }
                    }

                    PaceButton(
                        onClick = {
                            authViewModel.signOut()
                        },
                        text = "LOG OUT"
                    )
                }
            }
        }

        is MyProfileState.Error -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = state.message)

            }
        }
    }

}