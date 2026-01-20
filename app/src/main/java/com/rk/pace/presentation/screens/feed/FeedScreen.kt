package com.rk.pace.presentation.screens.feed

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rk.pace.presentation.screens.feed.components.FeedItem
import com.rk.pace.presentation.screens.search.components.UserItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedScreen(
    viewmodel: FeedViewModel = hiltViewModel(),
    goToRunStatsScreen: (runId: String) -> Unit
) {
    val state by viewmodel.state.collectAsStateWithLifecycle()

    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()

    var likes by remember { mutableIntStateOf(0) }

    when {
        state.isInitialLoad -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        state.error != null -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "Failed to load state ${state.error}")
                Log.d("index", state.error!!)
            }
        }

        else -> {
            PullToRefreshBox(
                isRefreshing = state.isRefreshing,
                onRefresh = {
                    viewmodel.refreshFeed()
                },
                state = rememberPullToRefreshState()
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(15.dp),
                ) {
                    items(state.posts) { post ->
                        FeedItem(
                            post = post,
                            toggleLike = {
                                viewmodel.toggleLike(post)
                            },
                            onLikesClick = {
                                likes = post.run.likes
                                viewmodel.getLikedByUsers(post.run.likedBy)
                                showBottomSheet = true
                            },
                            goToRunStats = goToRunStatsScreen
                        )
                        HorizontalDivider()
                    }
                }
            }
            if (showBottomSheet) {
                ModalBottomSheet(
                    sheetState = sheetState,
                    onDismissRequest = {
                        viewmodel.clearLikedByUsers()
                        showBottomSheet = false
                    },
                    shape = RoundedCornerShape(0.dp),
                    dragHandle = { }
                ) {
                    if (state.isLikedByUsersLoading) {
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
                                if (likes > 1) {
                                    Text(
                                        text = "$likes LIKES",
                                        letterSpacing = 3.sp
                                    )
                                } else {
                                    Text(
                                        text = "$likes LIKE",
                                        letterSpacing = 3.sp
                                    )
                                }
                            }
                            items(state.likedByUsers) { user ->
                                UserItem(
                                    user = user,
                                    onClick = {

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
