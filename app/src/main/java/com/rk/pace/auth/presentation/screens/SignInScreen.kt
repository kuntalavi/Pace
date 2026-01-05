package com.rk.pace.auth.presentation.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.rk.pace.auth.presentation.AuthState
import com.rk.pace.auth.presentation.AuthViewModel
import com.rk.pace.auth.presentation.components.BottomSwitchText
import com.rk.pace.auth.presentation.components.CustomSignButton
import com.rk.pace.auth.presentation.components.Header
import com.rk.pace.auth.presentation.components.InputText
import com.rk.pace.auth.presentation.components.PrimaryGreen
import com.rk.pace.auth.presentation.components.SoSignButton
import com.rk.pace.theme.run

@Composable
fun SignInScreen(
    viewModel: AuthViewModel = hiltViewModel(),
    goToSignUp: () -> Unit,
    onSignInSuccess: () -> Unit
) {
    var em by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val authState by viewModel.authState.collectAsState()

    LaunchedEffect(key1 = Unit) {
        viewModel.resetState()
    }
    LaunchedEffect(key1 = authState) {
        if (authState is AuthState.Success) {
            onSignInSuccess()
        }
    }

    Scaffold(
        snackbarHost = {
            if (authState is AuthState.Error) {
                Snackbar(
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.onErrorContainer
                ) {
                    Text(text = (authState as AuthState.Error).message)
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
            Spacer(modifier = Modifier.height(40.dp))
            Header(
                text = "WELCOME",
                i = run
            )
            Spacer(modifier = Modifier.height(48.dp))
            InputText(
                v = em,
                onVChange = { v ->
                    em = v
                },
                l = "Em Address",
                placeh = "your@em.com",
                i = Icons.Default.Email,
                keyboardType = KeyboardType.Email
            )
            Spacer(modifier = Modifier.height(24.dp))
            InputText(
                v = password,
                onVChange = { v ->
                    password = v
                },
                l = "Password",
                placeh = "password",
                i = Icons.Default.Lock,
                isPassword = true
            )
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                Text(
                    text = "Forgot Password?",
                    color = PrimaryGreen,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(vertical = 16.dp)
                        .clickable { viewModel.forgetPassword(em) }
                )
            }
            CustomSignButton(
                text = "Sign In",
                onC = { viewModel.signIn(em, password) },
                isLoading = authState is AuthState.Load
            )
            HorizontalDivider()
            SoSignButton(
                text = "GOOGLE",
                onC = {} //
            )
            Spacer(modifier = Modifier.weight(1f))
            BottomSwitchText(
                text = "Don't have an account?",
                actionText = "Sign Up",
                onActionC = goToSignUp
            )
        }
    }

}