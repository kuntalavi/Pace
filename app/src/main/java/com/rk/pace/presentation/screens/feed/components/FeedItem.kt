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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rk.pace.common.extension.formatDistance
import com.rk.pace.common.extension.formatTime
import com.rk.pace.common.ut.PaceUt.getPace
import com.rk.pace.common.ut.TimestampUt.getDate
import com.rk.pace.domain.model.FeedPost
import com.rk.pace.presentation.components.ProfileImage
import com.rk.pace.presentation.components.ProfileImageSize
import com.rk.pace.presentation.components.StatItem
import com.rk.pace.theme.like
import com.rk.pace.theme.unlike

@Composable
fun FeedItem(
    post: FeedPost,
    toggleLike: () -> Unit,
    onLikesClick: () -> Unit,
    onUserClick: () -> Unit,
    onRunClick: () -> Unit
) {

    val transition = updateTransition(
        targetState = post.isLikedByMe
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
        targetValue = if (post.isLikedByMe) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primary,
        label = "Color"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onRunClick()
            },
        shape = RoundedCornerShape(0.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                ProfileImage(
                    imageUrl = post.user.photoURL,
                    size = ProfileImageSize.Medium,
                    onClick = {
                        onUserClick()
                    }
                )

                Spacer(modifier = Modifier.width(10.dp))

                Column(
                    modifier = Modifier.clickable {
                        onUserClick()
                    }
                ) {

                    Text(
                        text = post.user.name.uppercase(),
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
                        text = getDate(post.run.timestamp),
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

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = post.run.title.uppercase().ifEmpty { "RUNNING" },
                fontSize = 24.sp,
                fontWeight = FontWeight.Medium,
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                StatItem(
                    title = "DISTANCE",
                    value = post.run.distanceMeters.formatDistance()
                )

                StatItem(
                    title = "PACE",
                    value = getPace(post.run.avgSpeedMps)
                )

                StatItem(
                    title = "DURATION",
                    value = post.run.durationMilliseconds.formatTime()
                )

            }

            Spacer(modifier = Modifier.height(10.dp))

            FeedItemMap(
                path = post.run.encodedPath
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                IconButton(
                    onClick = { toggleLike() }
                ) {
                    Icon(
                        imageVector = if (post.isLikedByMe) like else unlike,
                        contentDescription = "",
                        tint = heartColor,
                        modifier = Modifier.scale(heartScale)
                    )
                }

                if (post.run.likes > 0) {
                    TextButton(
                        onClick = {
                            onLikesClick()
                        }
                    ) {
                        if (post.run.likes == 1) {
                            Text(
                                text = "${post.run.likes} LIKE",
                                letterSpacing = 3.sp
                            )
                        } else {
                            Text(
                                text = "${post.run.likes} LIKES",
                                letterSpacing = 3.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
