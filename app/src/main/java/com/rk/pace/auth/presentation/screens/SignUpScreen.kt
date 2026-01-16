package com.rk.pace.auth.presentation.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.rk.pace.auth.presentation.AuthUIState
import com.rk.pace.auth.presentation.AuthViewModel
import com.rk.pace.presentation.components.CustomButton
import com.rk.pace.presentation.components.CustomInputBox

@Composable
fun SignUpScreen(
    viewModel: AuthViewModel = hiltViewModel(),
    onSignUpSuccess: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var cPassword by remember { mutableStateOf("") }

    val snackbarHostState = remember { SnackbarHostState() }
    val authState by viewModel.authState.collectAsState()

    LaunchedEffect(key1 = Unit) {
        viewModel.resetState()
    }

    LaunchedEffect(key1 = authState) {
        if (authState is AuthUIState.Success) {
            onSignUpSuccess()
        }
        if (authState is AuthUIState.Error) {
            snackbarHostState.showSnackbar((authState as AuthUIState.Error).message)
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
            CustomInputBox(
                value = name,
                onValueChange = { v ->
                    name = v
                },
                placeholder = "Enter Full Name",
//                icon = "Name"
            )
            Spacer(modifier = Modifier.height(24.dp))
            CustomInputBox(
                value = username,
                onValueChange = { v ->
                    username = v
                },
                placeholder = "Enter Username",
//                icon = "Name"
            )
            Spacer(modifier = Modifier.height(32.dp))
            CustomInputBox(
                value = email,
                onValueChange = { v ->
                    email = v
                },
                placeholder = "your@em.com",
//                icon = "Em",
//                keyboardType = KeyboardType.Email
            )
            Spacer(modifier = Modifier.height(24.dp))
            CustomInputBox(
                value = password,
                onValueChange = { v ->
                    password = v
                },
                placeholder = "password",
//                icon = "Password",
                isPassword = true
            )
            Spacer(modifier = Modifier.height(24.dp))
            CustomInputBox(
                value = cPassword,
                onValueChange = { v ->
                    cPassword = v
                },
                placeholder = "confirm password",
//                icon = "Confirm Password",
                isPassword = true
            )
            Spacer(modifier = Modifier.height(32.dp))
            CustomButton(
                text = "Sign Up",
                onClick = {
                    if (password == cPassword) {
                        viewModel.signUp(name, username, email, password)
                    }
                }
            )
        }
    }
}