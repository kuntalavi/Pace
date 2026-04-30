package com.rk.pace.presentation.screens.feed.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rk.pace.presentation.ut.TimestampUt.getDate
import com.rk.pace.domain.model.FeedPost
import com.rk.pace.domain.model.Run
import com.rk.pace.domain.model.User
import com.rk.pace.presentation.components.PaceStatItem
import com.rk.pace.presentation.components.PaceUserDp
import com.rk.pace.presentation.components.PaceUserDpSize
import com.rk.pace.presentation.components.StatItemStyle
import com.rk.pace.presentation.ut.FormatUt.formatDistance
import com.rk.pace.presentation.ut.FormatUt.formatDuration
import com.rk.pace.presentation.ut.FormatUt.formatPace
import com.rk.pace.presentation.theme.like
import com.rk.pace.presentation.theme.unlike

@Composable
fun FeedItem(
    post: FeedPost,
    toggleLike: () -> Unit,
    onLikesClick: () -> Unit,
    onUserClick: () -> Unit,
    onRunClick: () -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onRunClick()
            },
        shape = RectangleShape,
        colors = CardDefaults.cardColors(
            containerColor = colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {

                FeedItemUser(
                    user = post.user,
                    run = post.run,
                    onUserClick = onUserClick
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = post.run.title.ifEmpty { "Run" },
                    style = typography.titleLarge,
                    letterSpacing = 1.sp
                )

                Spacer(modifier = Modifier.height(10.dp))

                FeedItemStats(post.run)

                Spacer(modifier = Modifier.height(10.dp))
            }

            FeedItemMap(post.run.encodedPath)

            Spacer(modifier = Modifier.height(10.dp))

            FeedItemLike(
                likes = post.run.likes,
                isLikedByMe = post.isLikedByMe,
                toggleLike = toggleLike,
                onLikesClick = onLikesClick
            )
        }
    }
}

@Composable
private fun FeedItemStats(run: Run) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        PaceStatItem(
            value = formatDistance(run.distanceMeters),
            label = "DISTANCE",
            unit = "Km",
            style = StatItemStyle.Inline
        )

        PaceStatItem(
            value = formatPace(run.avgSpeedMps),
            label = "PACE",
            unit = "/Km",
            style = StatItemStyle.Inline
        )

        PaceStatItem(
            value = formatDuration(run.durationMilliseconds),
            label = "TIME",
            unit = "",
            style = StatItemStyle.Inline
        )

    }
}

@Composable
private fun FeedItemUser(
    user: User,
    run: Run,
    onUserClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        PaceUserDp(
            imageUrl = user.photoURL,
            size = PaceUserDpSize.Medium,
            onClick = {
                onUserClick()
            }
        )

        Spacer(modifier = Modifier.width(10.dp))

        Column(
            modifier = Modifier
                .clickable {
                    onUserClick()
                }
        ) {

            Text(
                text = user.name.uppercase(),
                textAlign = TextAlign.Center,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                letterSpacing = 2.sp,
                style = LocalTextStyle.current.copy(
                    platformStyle = PlatformTextStyle(includeFontPadding = false),
                    lineHeight = 12.sp
                )
            )

            Text(
                text = getDate(run.timestamp),
                textAlign = TextAlign.Center,
                fontSize = 8.sp,
                letterSpacing = 1.sp,
                style = LocalTextStyle.current.copy(
                    platformStyle = PlatformTextStyle(includeFontPadding = false),
                    lineHeight = 8.sp
                )
            )

        }
    }
}

@Composable
private fun FeedItemLike(
    likes: Int,
    isLikedByMe: Boolean,
    toggleLike: () -> Unit,
    onLikesClick: () -> Unit
) {

    val transition = updateTransition(
        targetState = isLikedByMe
    )
    val heartScale by transition.animateFloat(
        transitionSpec = {
            if (targetState) {
                spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            } else {
                snap()
            }
        }
    ) { state ->
        if (state) 1.3f else 1.0f
    }
    val heartColor by animateColorAsState(
        targetValue = if (isLikedByMe) colorScheme.primary else colorScheme.primary,
        label = ""
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            imageVector = if (isLikedByMe) like else unlike,
            contentDescription = "",
            tint = heartColor,
            modifier = Modifier
                .clickable { toggleLike() }
                .scale(heartScale)
        )

        if (likes > 0) {
            Text(
                text = "$likes",
                letterSpacing = 1.sp,
                modifier = Modifier
                    .clickable { onLikesClick() }
            )
        }
    }
}
