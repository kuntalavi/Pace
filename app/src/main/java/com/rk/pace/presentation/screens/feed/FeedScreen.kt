package com.rk.pace.presentation.screens.feed

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rk.pace.presentation.screens.feed.components.FeedItem

@Composable
fun FeedScreen(
    viewmodel: FeedViewModel = hiltViewModel(),
    goToRunStatsScreen: (runId: String) -> Unit
) {
    val state by viewmodel.state.collectAsStateWithLifecycle()

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
//                    contentPadding = PaddingValues(20.dp)
                ) {
                    items(state.posts) { post ->
                        FeedItem(
                            post = post,
                            toggleLike = {
                                viewmodel.toggleLike(post)
                            },
                            goToRunStats = goToRunStatsScreen
                        )
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}
