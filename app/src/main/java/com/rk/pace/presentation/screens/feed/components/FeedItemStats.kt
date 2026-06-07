package com.rk.pace.presentation.screens.feed.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.withStyle
import com.rk.pace.presentation.theme.scheme
import com.rk.pace.presentation.theme.space
import com.rk.pace.presentation.theme.tvpo
import com.rk.pace.presentation.ut.FormatUt.formatDistance
import com.rk.pace.presentation.ut.FormatUt.formatDuration
import com.rk.pace.presentation.ut.FormatUt.formatPace

@Composable
fun FeedItemStats(
    distance: Float,
    speed: Float,
    duration: Long
) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {

        PaceFeedStat(
            modifier = Modifier.weight(1f),
            title = "DISTANCE",
            value = formatDistance(
                distance
            )
        )

        PaceFeedStat(
            modifier = Modifier.weight(1f),
            title = "PACE",
            value = formatPace(
                speed
            )
        )

        PaceFeedStat(
            modifier = Modifier.weight(1f),
            title = "DURATION",
            value = formatDuration(
                duration
            )
        )

    }
}

@Composable
private fun PaceFeedStat(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    unit: String = ""
) {

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start
    ) {

        Text(
            text = title,
            style = tvpo.bodySmall,
            color = scheme.onSurfaceVariant,
        )

        Row(
            verticalAlignment = Alignment.Bottom
        ) {

            Text(
                modifier = Modifier.alignByBaseline(),
                text = value,
                style = tvpo.titleLarge,
                color = scheme.onSurface
            )

            Spacer(
                modifier = Modifier.width(
                    space.xSmall
                )
            )

            Text(
                modifier = Modifier.alignByBaseline(),
                text = buildAnnotatedString {
                    if (unit.startsWith("/")) {
                        withStyle(
                            style = SpanStyle(
                                baselineShift = BaselineShift(0.08f)
                            )
                        ) {
                            append("/")
                        }
                        append(
                            unit.drop(1)
                        )
                    } else append(unit)
                },
                style = tvpo.bodySmall,
                color = scheme.onSurfaceVariant
            )

        }

    }

}