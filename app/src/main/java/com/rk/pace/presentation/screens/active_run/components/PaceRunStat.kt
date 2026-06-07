package com.rk.pace.presentation.screens.active_run.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import com.rk.pace.presentation.theme.scheme
import com.rk.pace.presentation.theme.space
import com.rk.pace.presentation.theme.tvpo

@Composable
fun PaceRunStat(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    unit: String = ""
) {

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = title,
            style = tvpo.titleMedium.copy(
                letterSpacing = 2.sp
            ),
            color = scheme.onSurfaceVariant,
        )

        Spacer(
            modifier = Modifier.height(
                space.small
            )
        )

        Row(
            verticalAlignment = Alignment.Bottom
        ) {

            Text(
                modifier = Modifier.alignByBaseline(),
                text = if (value != "0") value else "-",
                style = tvpo.displayMedium,
                color = scheme.primary
            )

            Spacer(
                modifier = Modifier.width(
                    space.small
                )
            )

            if (value != "0") {
                Text(
                    modifier = Modifier
                        .alignByBaseline()
                        .padding(
                            bottom = space.small
                        ),
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
                    style = tvpo.titleMedium.copy(
                        letterSpacing = 1.sp
                    ),
                    color = scheme.onSurfaceVariant
                )
            } else {
                Text(
                    modifier = Modifier.alignByBaseline(),
                    text = "",
                    style = tvpo.titleMedium,
                    color = scheme.onSurfaceVariant
                )
            }

        }

    }

}