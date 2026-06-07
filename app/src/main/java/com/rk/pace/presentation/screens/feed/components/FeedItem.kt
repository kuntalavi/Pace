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
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rk.pace.domain.model.FeedPost
import com.rk.pace.domain.model.User
import com.rk.pace.presentation.components.PaceUserDp
import com.rk.pace.presentation.components.PaceUserDpSize
import com.rk.pace.presentation.theme.like
import com.rk.pace.presentation.theme.scheme
import com.rk.pace.presentation.theme.space
import com.rk.pace.presentation.theme.tvpo
import com.rk.pace.presentation.theme.unlike
import com.rk.pace.presentation.ut.TimestampUt.getBarDate

@Composable
fun FeedItem(
    post: FeedPost,
    onPostLieIconClick: () -> Unit,
    onPostLiesClick: () -> Unit,
    onUserClick: () -> Unit,
    onRunClick: () -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                vertical = space.medium
            )
            .clickable {
                onRunClick()
            },
        shape = RectangleShape,
        colors = CardDefaults.cardColors(
            containerColor = scheme.background
        )
    ) {

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement
                .spacedBy(
                    space.medium
                )
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = space.large
                    ),
                verticalArrangement = Arrangement
                    .spacedBy(
                        space.small
                    )
            ) {

                FeedItemUser(
                    user = post.user,
                    date = getBarDate(post.run.timestamp),
                    onUserClick = onUserClick
                )

                if (post.run.title != "") {
                    Text(
                        text = post.run.title,
                        style = tvpo.titleLarge,
                    )
                }else {
                    Spacer(
                        modifier = Modifier.height(
                            space.xSmall
                        )
                    )
                }

                FeedItemStats(
                    distance = post.run.distanceMeters,
                    speed = post.run.avgSpeedMps,
                    duration = post.run.durationMilliseconds
                )

            }

            FeedItemMap(
                post.run.encodedPath
            )

            FeedItemLike(
                lies = post.run.likes,
                isLiedByMe = post.isLikedByMe,
                switchLie = onPostLieIconClick,
                onLiesClick = onPostLiesClick
            )

        }

    }

}

@Composable
private fun FeedItemUser(
    user: User,
    date: String,
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

        Spacer(
            modifier = Modifier.width(
                space.small
            )
        )

        Column(
            modifier = Modifier
                .clickable {
                    onUserClick()
                }
        ) {

            Text(
                text = user.name.uppercase(),
                textAlign = TextAlign.Center,
                style = tvpo.titleSmall.copy(
                    letterSpacing = 2.sp
                )
            )

            Text(
                text = date,
                textAlign = TextAlign.Center,
                style = tvpo.labelSmall,
                color = scheme.onSurfaceVariant
            )

        }
    }
}

@Composable
private fun FeedItemLike(
    lies: Int,
    isLiedByMe: Boolean,
    switchLie: () -> Unit,
    onLiesClick: () -> Unit
) {

    val transition = updateTransition(
        targetState = isLiedByMe
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
        targetValue = if (isLiedByMe) colorScheme.primary else colorScheme.primary,
        label = ""
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = space.large),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            imageVector = if (isLiedByMe) like else unlike,
            contentDescription = "",
            tint = heartColor,
            modifier = Modifier
                .clickable { switchLie() }
                .scale(heartScale)
        )

        if (lies > 0) {
            Text(
                text = "$lies",
                letterSpacing = 1.sp,
                modifier = Modifier
                    .clickable { onLiesClick() }
            )
        }
    }
}
