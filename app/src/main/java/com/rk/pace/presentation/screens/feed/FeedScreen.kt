package com.rk.pace.presentation.screens.feed

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.rk.pace.presentation.components.PaceUser
import com.rk.pace.presentation.screens.feed.components.FeedItem
import com.rk.pace.presentation.theme.like
import com.rk.pace.presentation.theme.scheme
import com.rk.pace.presentation.theme.space
import com.rk.pace.presentation.theme.tvpo
import com.rk.pace.presentation.ut.ObserveAsEvents

@Composable
fun FeedScreenRoot(
    viewModel: FeedViewModel = hiltViewModel(),
    reload: Long,
    onRunClick: (userId: String, runId: String) -> Unit,
    onUserClick: (userId: String) -> Unit
) {

    val state by viewModel.state.collectAsStateWithLifecycle()
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(
        key1 = lifecycleOwner
    ) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(
            Lifecycle.State.RESUMED
        ) {
            viewModel.onAction(
                FeedAction.Refresh
            )
        }
    }

    ObserveAsEvents(
        flow = viewModel.events
    ) { event ->
        when (event) {

            is FeedEvent.OnRunClick -> {
                onRunClick(
                    event.userId,
                    event.runId
                )
            }

            is FeedEvent.OnUserClick -> {
                onUserClick(
                    event.userId
                )
            }

            else -> {}

        }
    }

    FeedScreen(
        reload = reload,
        state = state,
        onAction = viewModel::onAction
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedScreen(
    reload: Long,
    state: FeedUiState,
    onAction: (FeedAction) -> Unit
) {

    when {

        state.initialLoad -> {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        scheme.background
                    ),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }

        }

        state.error != null -> {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        scheme.background
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = state.error
                )
            }

        }

        else -> {

            PullToRefreshBox(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        scheme.background
                    ),
                isRefreshing = state.refresh,
                onRefresh = {
                    onAction(
                        FeedAction.Refresh
                    )
                },
                state = rememberPullToRefreshState()
            ) {

                val listState = rememberLazyListState()

                LaunchedEffect(
                    key1 = reload
                ) {
                    if (reload > 0L) {
                        onAction(
                            FeedAction.Refresh
                        )
                        listState.animateScrollToItem(0)
                    }
                }

                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(
                        space.medium
                    )
                ) {
                    items(
                        items = state.posts,
                        key = { post ->
                            post.run.runId
                        }
                    ) { post ->

                        FeedItem(
                            post = post,
                            onPostLieIconClick = {
                                onAction(
                                    FeedAction.OnPostLieIconClick(post)
                                )
                            },
                            onPostLiesClick = {
                                onAction(
                                    FeedAction.OnPostLiesClick(post)
                                )
                            },
                            onUserClick = {
                                onAction(
                                    FeedAction.OnUserClick(
                                        post.user.userId
                                    )
                                )
                            },
                            onRunClick = {
                                onAction(
                                    FeedAction.OnRunClick(
                                        post.user.userId,
                                        post.run.runId
                                    )
                                )
                            }
                        )

                        HorizontalDivider()

                    }
                }

            }

            if (state.showLiesBottomSheet) {

                ModalBottomSheet(
                    sheetState = rememberModalBottomSheetState(),
                    sheetMaxWidth = Dp.Unspecified,
                    onDismissRequest = {
                        onAction(
                            FeedAction.DismissLiesBottomSheet
                        )
                    },
                    shape = RectangleShape,
                    dragHandle = null
                ) {
                    if (state.isLiedByUsersLoad) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    } else {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(15.dp),
                            contentPadding = PaddingValues(15.dp)
                        ) {
                            item {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = like,
                                        contentDescription = null,
                                        tint = scheme.primary
                                    )
                                    Text(
                                        text = "${state.selectedPostLies}",
                                        style = tvpo.titleMedium,
                                        letterSpacing = 1.sp
                                    )
                                }
                            }
                            items(
                                items = state.liedByUsers,
                                key = { user ->
                                    user.userId
                                }
                            ) { user ->
                                PaceUser(
                                    user = user,
                                    onClick = {
                                        onAction(
                                            FeedAction.OnUserClick(
                                                user.userId
                                            )
                                        )
                                    }
                                )
                            }
                        }
                    }
                }

            }

        }

    }

}
