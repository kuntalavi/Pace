package com.rk.pace.presentation.screens.connections

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rk.pace.theme.back
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConnectionsScreen(
    viewModel: ConnectionsViewModel = hiltViewModel(),
    goBack: () -> Unit,
    onUserClick: (userId: String) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val tabs = listOf(
        "FOLLOWERS",
        "FOLLOWING"
    )

    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(
        initialPage = viewModel.tab
    ) { tabs.size }

    LaunchedEffect(pagerState.currentPage) {
        when (pagerState.currentPage) {
            0 -> viewModel.getFollowers()

            1 -> viewModel.getFollowing()
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "CONNECTIONS"
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
        ) {
            PrimaryTabRow(
                selectedTabIndex = pagerState.currentPage,
                divider = { }
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = pagerState.currentPage == index,
                        onClick = {
                            scope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        },
                        text = {
                            Text(
                                text = title
                            )
                        }
                    )
                }
            }

            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                when (page) {
                    0 -> FollowersTab(
                        followers = state.followers,
                        isFollowersLoaded = state.isFollowersLoaded,
                        onUserClick = { userId ->
                            onUserClick(userId)
                        }
                    )

                    1 -> FollowingTab(
                        following = state.following,
                        isFollowingLoaded = state.isFollowingLoaded,
                        onUserClick = { userId ->
                            onUserClick(userId)
                        }
                    )
                }
            }
        }
    }
}