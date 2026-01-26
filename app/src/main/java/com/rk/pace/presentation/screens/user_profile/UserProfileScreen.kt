package com.rk.pace.presentation.screens.user_profile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rk.pace.presentation.components.PaceButton
import com.rk.pace.presentation.components.ProfileImage
import com.rk.pace.presentation.components.ProfileImageSize
import com.rk.pace.presentation.components.RunSummary
import com.rk.pace.theme.back

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfileScreen(
    viewModel: UserProfileViewModel = hiltViewModel(),
    onRunClick: (userId: String, runId: String) -> Unit,
    onFollowersClick: (userId: String, tab: Int) -> Unit,
    onFollowingClick: (userId: String, tab: Int) -> Unit,
    goBack: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    when (val currentState = state) {
        is UserProfileState.Load -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is UserProfileState.Success -> {
            Scaffold(
                topBar = {
                    CenterAlignedTopAppBar(
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.background
                        ),
                        title = {
                            Text(
                                text = "",
                                style = MaterialTheme.typography.titleLarge
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
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentPadding = PaddingValues(20.dp)
                    ) {
                        item {
                            ProfileImage(
                                imageUrl = currentState.userProfileUiState.user.photoURL, // draw first letter of name if no photo is there
                                size = ProfileImageSize.XLarge
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = currentState.userProfileUiState.user.name.uppercase(),
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            )

                            Text(
                                text = "#${currentState.userProfileUiState.user.username}",
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
                                            currentState.userProfileUiState.user.userId,
                                            0
                                        )
                                    },
                                    contentPadding = PaddingValues(0.dp)
                                ) {
                                    Text(
                                        text = "${currentState.userProfileUiState.user.followers} FOLLOWERS",
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
                                            currentState.userProfileUiState.user.userId,
                                            1
                                        )
                                    },
                                    contentPadding = PaddingValues(0.dp)
                                ) {
                                    Text(
                                        text = "${currentState.userProfileUiState.user.following} FOLLOWING",
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            letterSpacing = 1.sp
                                        )
                                    )
                                }
                            }

                            if (!currentState.userProfileUiState.isCurrentUser) {
                                PaceButton(
                                    onClick = {
                                        if (!currentState.userProfileUiState.isFollowed) {
                                            viewModel.followUser()
                                        } else {
                                            viewModel.unFollowUser()
                                        }
                                    },
                                    text = if (!currentState.userProfileUiState.isFollowed) "FOLLOW" else "FOLLOWING",
                                    enabled = !currentState.userProfileUiState.followJ,
                                    filled = !currentState.userProfileUiState.isFollowed
                                )
                            }

                            Spacer(
                                modifier = Modifier
                                    .height(20.dp)
                            )

                            Text(
                                text = "RUNS",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Medium,
                                    letterSpacing = 1.sp
                                )
                            )
                            Spacer(
                                modifier = Modifier
                                    .height(10.dp)
                            )
                        }
                        items(
                            items = currentState.userProfileUiState.runs,
                            key = { run ->
                                run.runId
                            }
                        ) { run ->
                            RunSummary(
                                run = run,
                                onClick = {
                                    onRunClick(
                                        run.runId,
                                        run.userId
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }

        is UserProfileState.Error -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = currentState.message)
            }
        }
    }
}