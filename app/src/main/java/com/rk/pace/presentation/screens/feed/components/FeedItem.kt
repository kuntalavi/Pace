package com.rk.pace.presentation.screens.feed.components

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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rk.pace.common.extension.formatDistance
import com.rk.pace.common.extension.formatTime
import com.rk.pace.common.ut.PaceUt.getPace
import com.rk.pace.domain.model.FeedPost
import com.rk.pace.presentation.components.ProfileImage
import com.rk.pace.presentation.components.ProfileImageSize
import com.rk.pace.presentation.components.StatItem
import com.rk.pace.theme.like
import com.rk.pace.theme.unlike

@Composable
fun FeedItem(
    post: FeedPost,
//    goToRunStats: (runId: String) -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth(),
//            .height(400.dp)
//            .clickable { goToRunStats(run.runId) },
        shape = RoundedCornerShape(0.dp),
//        elevation = CardDefaults.cardElevation(
//            defaultElevation = 8.dp
//        ),
//        colors = CardDefaults.cardColors(
//            containerColor = MaterialTheme.colorScheme.surface
//        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                ProfileImage(
                    imageUrl = post.user.photoURL,
                    size = ProfileImageSize.Medium
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = post.user.name,
                    textAlign = TextAlign.Center,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 1.sp
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = post.run.title,
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
                    title = "Distance",
                    value = post.run.distanceMeters.formatDistance()
                )
                StatItem(
                    title = "Pace",
                    value = getPace(post.run.avgSpeedMps)
                )
                StatItem(
                    title = "Time",
                    value = post.run.durationMilliseconds.formatTime()
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            FeedItemMap(
                path = post.run.encodedPath
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                if (post.isLikedByMe) {
                    IconButton(
                        onClick = {}
                    ) {
                        Icon(imageVector = like, contentDescription = "", tint = Color.Red)
                    }
                } else {
                    IconButton(
                        onClick = {}
                    ) {
                        Icon(imageVector = unlike, contentDescription = "")
                    }
                }
            }
        }
    }
}
