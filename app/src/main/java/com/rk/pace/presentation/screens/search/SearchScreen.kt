package com.rk.pace.presentation.screens.search

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import com.rk.pace.theme.back

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    goBack: () -> Unit
){
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(
                        onClick = { goBack() }
                    ) {
                        Icon(
                            imageVector = back,
                            contentDescription = ""
                        )
                    }
                },
                title = {}
            )
        }
    ) { }
}