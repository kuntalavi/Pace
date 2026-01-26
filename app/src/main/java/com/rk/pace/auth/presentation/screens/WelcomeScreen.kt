package com.rk.pace.auth.presentation.screens

import androidx.compose.foundation.layout.Box
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
import com.rk.pace.presentation.components.PaceButton

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun WelcomeScreenPreview() {
    WelcomeScreen(
        goToSignUp = {},
        goToSignIn = {}
    )
}


@Composable
fun WelcomeScreen(
    goToSignUp: () -> Unit,
    goToSignIn: () -> Unit
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        contentAlignment = Alignment.Center
    ) {
        Column {
            PaceButton(
                modifier = Modifier.fillMaxWidth(.8f),
                onClick = {
                    goToSignUp()
                },
                text = "CREATE AN ACCOUNT",
                filled = true
            )

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            PaceButton(
                modifier = Modifier.fillMaxWidth(.8f),
                onClick = {
                    goToSignIn()
                },
                text = "LOG IN"
            )
        }
    }
}
