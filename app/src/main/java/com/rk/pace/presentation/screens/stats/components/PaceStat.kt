package com.rk.pace.presentation.screens.stats.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.rk.pace.presentation.theme.scheme
import com.rk.pace.presentation.theme.space
import com.rk.pace.presentation.theme.tvpo

@Composable
fun PaceStat(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    unit: String = ""
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = title,
            style = tvpo.labelMedium
        )
        Row(
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                text = value,
                style = tvpo.headlineMedium,
                color = scheme.primary
            )
            Spacer(
                modifier = Modifier.width(
                    space.xSmall
                )
            )
            Text(
                text = unit,
                style = tvpo.labelMedium,
                modifier = Modifier
                    .padding(
                        bottom = space.xSmall
                    )
            )
        }
    }
}