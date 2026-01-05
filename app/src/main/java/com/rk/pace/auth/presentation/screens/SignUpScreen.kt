package com.rk.pace.auth.presentation.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.rk.pace.auth.presentation.AuthState
import com.rk.pace.auth.presentation.AuthViewModel
import com.rk.pace.auth.presentation.components.BottomSwitchText
import com.rk.pace.auth.presentation.components.CustomSignButton
import com.rk.pace.auth.presentation.components.Header
import com.rk.pace.auth.presentation.components.InputText
import com.rk.pace.auth.presentation.components.SoSignButton

@Composable
fun SignUpScreen(
    viewModel: AuthViewModel = hiltViewModel(),
    goToSignIn: () -> Unit,
    onSignUpSuccess: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var em by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var cPassword by remember { mutableStateOf("") }

    val snackbarHostState = remember { SnackbarHostState() }
    val authState by viewModel.authState.collectAsState()

    LaunchedEffect(key1 = Unit) {
        viewModel.resetState()
    }

    LaunchedEffect(key1 = authState) {
        if (authState is AuthState.Success) {
            onSignUpSuccess()
        }
        if (authState is AuthState.Error) {
            snackbarHostState.showSnackbar((authState as AuthState.Error).message)
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { data ->
                Snackbar(
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.onErrorContainer,
                    snackbarData = data
                )
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.Start
        ) {
            Header(
                text = "Create Your Account",
                i = null
            )
            Spacer(modifier = Modifier.height(32.dp))
            InputText(
                v = name,
                onVChange = { v ->
                    name = v
                },
                l = "Name",
                placeh = "Enter Full Name",
                i = null
            )
            Spacer(modifier = Modifier.height(24.dp))
            InputText(
                v = em,
                onVChange = { v ->
                    em = v
                },
                l = "Em",
                placeh = "your@em.com",
                keyboardType = KeyboardType.Email,
                i = null
            )
            Spacer(modifier = Modifier.height(24.dp))
            InputText(
                v = password,
                onVChange = { v ->
                    password = v
                },
                l = "Password",
                placeh = "password",
                isPassword = true,
                i = null
            )
            Spacer(modifier = Modifier.height(24.dp))
            InputText(
                v = cPassword,
                onVChange = { v ->
                    cPassword = v
                },
                l = "Confirm Password",
                placeh = "confirm password",
                isPassword = true,
                i = null
            )
            Spacer(modifier = Modifier.height(32.dp))
            CustomSignButton(
                text = "Sign Up",
                onC = {
                    if (password == cPassword && password.isNotEmpty()) {
                        viewModel.signUp(name, em, password)
                    } else{
                        viewModel.resetState()
                    }
                },
                isLoading = authState is AuthState.Load
            )
            HorizontalDivider()
            SoSignButton(
                text = "GOOGLE",
                onC = {

                }
            )
            Spacer(modifier = Modifier.weight(1f))
            BottomSwitchText(
                text = "Already have an account?",
                actionText = "Log In",
                onActionC = goToSignIn
            )
        }
    }
}