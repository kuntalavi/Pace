package com.rk.pace.presentation.screens.connections

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rk.pace.domain.model.User
import com.rk.pace.presentation.screens.search.components.UserItem

@Composable
fun FollowingTab(
    following: List<User>,
    isFollowingLoaded: Boolean,
    onUserClick: (userId: String) -> Unit
) {
    if (isFollowingLoaded) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(20.dp)
        ) {
            items(following) { follow ->
                UserItem(
                    user = follow,
                    onClick = {
                        onUserClick(follow.userId)
                    }
                )
            }
        }
    } else {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}