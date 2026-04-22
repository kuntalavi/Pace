package com.rk.pace.auth.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rk.pace.presentation.components.ButtonVariant
import com.rk.pace.presentation.components.PaceButton

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun WelcomeScreenPreview() {
    WelcomeScreen(
        onSignUpClick = {},
        onSignInClick = {}
    )
}


@Composable
fun WelcomeScreen(
    onSignUpClick: () -> Unit,
    onSignInClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        PaceButton(
            modifier = Modifier.fillMaxWidth(.8f),
            onClick = onSignUpClick,
            text = "JOIN NOW",
            variant = ButtonVariant.Filled
        )

        Spacer(
            modifier = Modifier.height(15.dp)
        )

        PaceButton(
            modifier = Modifier.fillMaxWidth(.8f),
            onClick = onSignInClick,
            text = "LOG IN",
            variant = ButtonVariant.Tonal
        )

        Spacer(
            modifier = Modifier.height(20.dp)
        )
    }
}
