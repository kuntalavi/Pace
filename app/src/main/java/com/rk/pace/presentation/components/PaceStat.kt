package com.rk.pace.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import com.rk.pace.presentation.theme.space

enum class StatStyle {
    HERO,
    COMPACT,
    POST
}

@Composable
fun PaceStat(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    unit: String? = null,
    style: StatStyle = StatStyle.COMPACT,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start
) {

    val labelStyle: TextStyle
    val valueStyle: TextStyle
    val unitStyle: TextStyle
    val verticalSpace: Dp
    val alignment: Alignment.Horizontal

    when (style) {

        StatStyle.HERO -> {
            labelStyle = typography.titleMedium.copy(
                letterSpacing = 1.sp
            )
            valueStyle = typography.displayLarge
            unitStyle = typography.titleLarge.copy(
                letterSpacing = 1.sp
            )
            verticalSpace = MaterialTheme.space.small
            alignment = Alignment.CenterHorizontally
        }

        StatStyle.COMPACT -> {
            labelStyle = typography.labelMedium.copy(
                letterSpacing = 1.sp
            )
            valueStyle = typography.displaySmall
            unitStyle = typography.labelSmall.copy(
                letterSpacing = 1.sp
            )
            verticalSpace = MaterialTheme.space.xSmall
            alignment = horizontalAlignment
        }

        StatStyle.POST -> {
            labelStyle = typography.bodySmall.copy(
                letterSpacing = 1.sp
            )
            valueStyle = typography.titleLarge
            unitStyle = typography.labelSmall.copy(
                letterSpacing = 1.sp
            )
            verticalSpace = MaterialTheme.space.xSmall
            alignment = horizontalAlignment
        }

    }

    val contentDes = if (unit != null) "$label, $value $unit" else "$label, $value"

    Surface(
        modifier = modifier
            .fillMaxWidth(if (style == StatStyle.HERO) 1f else 0f)
            .semantics(mergeDescendants = true) {
                contentDescription = contentDes
            },
        color = colorScheme.surface
    ) {

        Column(
            modifier = Modifier.padding(
                if (style == StatStyle.COMPACT) MaterialTheme.space.medium else MaterialTheme.space.small
            ),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = alignment
        ) {

            Text(
                text = label,
                color = colorScheme.onSurface,
                style = labelStyle,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.clearAndSetSemantics { }
            )

            Spacer(
                modifier = Modifier.height(
                    verticalSpace
                )
            )

            Row {

                Text(
                    text = value,
                    color = if (style == StatStyle.POST) colorScheme.onSurface else colorScheme.primary,
                    style = valueStyle,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .alignByBaseline()
                        .clearAndSetSemantics { }
                )

                if (unit != null) {

                    Spacer(
                        modifier = Modifier.width(
                            MaterialTheme.space.small
                        )
                    )

                    Text(
                        text = unit,
                        color = colorScheme.onSurfaceVariant,
                        style = unitStyle,
                        modifier = Modifier
                            .alignByBaseline()
                            .clearAndSetSemantics { }
                    )

                }

            }

        }

    }

}