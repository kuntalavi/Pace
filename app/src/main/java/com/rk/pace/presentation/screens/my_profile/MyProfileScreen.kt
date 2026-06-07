package com.rk.pace.presentation.screens.my_profile

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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rk.pace.presentation.components.ButtonVariant
import com.rk.pace.presentation.components.PaceButton
import com.rk.pace.presentation.components.PaceUserDp
import com.rk.pace.presentation.components.PaceUserDpSize
import com.rk.pace.presentation.components.RunSummary
import com.rk.pace.presentation.theme.edit
import com.rk.pace.presentation.theme.scheme
import com.rk.pace.presentation.theme.space
import com.rk.pace.presentation.theme.tvpo
import com.rk.pace.presentation.ut.ObserveAsEvents

@Composable
fun UserScreenRoot(
    viewModel: MyProfileViewModel = hiltViewModel(),
    onEditClick: () -> Unit,
    onFollowersClick: (userId: String, tab: Int) -> Unit,
    onFollowingClick: (userId: String, tab: Int) -> Unit,
    onRunClick: (userId: String, runId: String) -> Unit
) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(
        flow = viewModel.events
    ) { event ->

        when (event) {

            MyProfileEvent.OnEditClick -> onEditClick()

            is MyProfileEvent.OnFollowersClick -> {
                onFollowersClick(
                    event.userId,
                    event.tab
                )
            }

            is MyProfileEvent.OnFollowingClick -> {
                onFollowingClick(
                    event.userId,
                    event.tab
                )
            }

            is MyProfileEvent.OnRunClick -> {
                onRunClick(
                    event.userId,
                    event.runId
                )
            }

            else -> {}

        }

    }

    UserScreen(
        state = state,
        onAction = viewModel::onAction
    )

}

@Composable
fun UserScreen(
    state: MyProfileUiState,
    onAction: (MyProfileAction) -> Unit
) {

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
                text = state.error,
                color = scheme.error
            )
        }
    } else {

        if (state.user == null) return

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        space.large
                    )
            ) {
                item {

                    PaceUserDp(
                        imageUrl = state.user.photoURI,
                        size = PaceUserDpSize.XLarge
                    )

                    Spacer(
                        modifier = Modifier.height(
                            space.small
                        )
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = state.user.name.uppercase(),
                            style = tvpo.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                        )
                        IconButton(
                            onClick = {
                                onAction(
                                    MyProfileAction.OnEditClick
                                )
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
                        style = tvpo.titleSmall.copy(
                            fontWeight = FontWeight.Medium,
                            letterSpacing = 1.sp,
                        )
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.Top,
                    ) {

                        TextButton(
                            onClick = {
                                MyProfileAction.OnFollowersClick(
                                    state.user.userId,
                                    0
                                )
                            },
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Text(
                                text = "${state.user.followers} Followers",
                                style = tvpo.titleMedium,
                                color = scheme.onSurfaceVariant
                            )
                        }

                        Spacer(
                            modifier = Modifier.width(
                                space.small
                            )
                        )

                        TextButton(
                            onClick = {
                                onAction(
                                    MyProfileAction.OnFollowingClick(
                                        state.user.userId,
                                        1
                                    )
                                )
                            },
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Text(
                                text = "${state.user.following} Following",
                                style = tvpo.titleMedium,
                                color = scheme.onSurfaceVariant
                            )
                        }
                    }

                    Text(
                        text = "RUNS",
                        style = tvpo.titleLarge
                    )

                    Spacer(
                        modifier = Modifier.height(
                            space.medium
                        )
                    )

                }

                items(
                    items = state.runs,
                    key = { run ->
                        run.runId
                    }
                ) { run ->
                    RunSummary(
                        run = run,
                        onClick = {
                            onAction(
                                MyProfileAction.OnRunClick(
                                    run.userId,
                                    run.runId
                                )
                            )
                        }
                    )
                }

                item {

                    Spacer(
                        modifier = Modifier.height(
                            space.small
                        )
                    )

                    PaceButton(
                        onClick = {
                            onAction(
                                MyProfileAction.OnSignOutClick
                            )
                        },
                        text = "LOG OUT",
                        variant = ButtonVariant.Tonal
                    )
                }
            }
        }
    }


}