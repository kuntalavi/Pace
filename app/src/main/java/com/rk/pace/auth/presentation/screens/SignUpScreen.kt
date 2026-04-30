package com.rk.pace.auth.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.rk.pace.auth.presentation.AuthAction
import com.rk.pace.auth.presentation.AuthUiEvent
import com.rk.pace.auth.presentation.AuthViewModel
import com.rk.pace.presentation.components.ButtonVariant
import com.rk.pace.presentation.components.PaceButton
import com.rk.pace.presentation.components.PaceInputBox
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SignUpScreen(
    viewModel: AuthViewModel = hiltViewModel()
) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    val lifecycleOwner = LocalLifecycleOwner.current
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(
        key1 = viewModel.events,
        key2 = lifecycleOwner.lifecycle
    ) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.events.collectLatest { event ->
                when (event) {
                    is AuthUiEvent.Error -> {
                        snackbarHostState.showSnackbar(
                            message = event.message,
                            duration = SnackbarDuration.Short
                        )
                    }
                }
            }
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { data ->
                Snackbar(
                    containerColor = colorScheme.errorContainer,
                    contentColor = colorScheme.onErrorContainer,
                    snackbarData = data
                )
            }
        }
    ) { p ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(p)
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center
        ) {
            PaceInputBox(
                value = state.name,
                onValueChange = {
                    viewModel.onAction(
                        AuthAction.OnNameChange(it)
                    )
                },
                placeholder = "Full Name",
            )

            Spacer(
                modifier = Modifier
                    .height(20.dp)
            )

            PaceInputBox(
                value = state.username,
                onValueChange = {
                    viewModel.onAction(
                        AuthAction.OnUsernameChange(it)
                    )
                },
                placeholder = "username",
            )

            Spacer(
                modifier = Modifier
                    .height(20.dp)
            )

            PaceInputBox(
                value = state.email,
                onValueChange = {
                    viewModel.onAction(
                        AuthAction.OnEmailChange(it)
                    )
                },
                placeholder = "your@em.com",
            )

            Spacer(
                modifier = Modifier
                    .height(24.dp)
            )

            PaceInputBox(
                value = state.password,
                onValueChange = {
                    viewModel.onAction(
                        AuthAction.OnPasswordChange(it)
                    )
                },
                placeholder = "password",
                isPassword = true
            )

            Spacer(
                modifier = Modifier
                    .height(20.dp)
            )

            PaceInputBox(
                value = state.confirmPassword,
                onValueChange = {
                    viewModel.onAction(
                        AuthAction.OnConfirmPasswordChange(it)
                    )
                },
                placeholder = "confirm password",
                isPassword = true
            )

            Spacer(
                modifier = Modifier
                    .height(20.dp)
            )

            PaceButton(
                modifier = Modifier
                    .fillMaxWidth(.3f),
                text = "Sign Up",
                onClick = {
                    viewModel.onAction(
                        AuthAction.OnSignUpClick
                    )
                },
                load = state.load,
                enabled = !state.load,
                variant = ButtonVariant.Filled
            )
        }
    }
}