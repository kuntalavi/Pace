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
import androidx.compose.material3.Text
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
fun SignInScreen(
    viewModel: AuthViewModel = hiltViewModel(),
    onSignInSuccess: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val authState by viewModel.authState.collectAsState()

    LaunchedEffect(key1 = Unit) {
        viewModel.resetState()
    }
    LaunchedEffect(key1 = authState) {
        if (authState is AuthUIState.Success) {
            onSignInSuccess()
        }
    }

    Scaffold(
        snackbarHost = {
            if (authState is AuthUIState.Error) {
                Snackbar(
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.onErrorContainer
                ) {
                    Text(text = (authState as AuthUIState.Error).message)
                }
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(48.dp))
            CustomInputBox(
                value = email,
                onValueChange = { v ->
                    email = v
                },
                placeholder = "your@em.com",
//                icon = "Em Address",
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
//            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
//                Text(
//                    text = "Forgot Password?",
//                    color = PrimaryGreen,
//                    fontWeight = FontWeight.Bold,
//                    modifier = Modifier
//                        .padding(vertical = 16.dp)
////                        .clickable { viewModel.resetPassword() }
//                )
//            }
            CustomButton(
                text = "Sign In",
                onClick = {
                    viewModel.signIn(email, password)
                }
            )
        }
    }
}