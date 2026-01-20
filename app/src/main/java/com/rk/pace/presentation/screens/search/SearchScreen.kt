package com.rk.pace.presentation.screens.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rk.pace.presentation.screens.search.components.UserItem
import com.rk.pace.theme.back
import com.rk.pace.theme.close

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    viewModel: SearchViewModel = hiltViewModel(),
    goBack: () -> Unit
) {

    val state by viewModel.state.collectAsStateWithLifecycle()
    var expanded by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        topBar = {
            SearchBar(
                inputField = {
                    SearchBarDefaults.InputField(
                        query = state.query,
                        onQueryChange = {
                            viewModel.onQueryChange(it)
                        },
                        onSearch = {},
                        expanded = expanded,
                        onExpandedChange = { expanded = it },
                        placeholder = {
                            if (expanded) {
                                Text(text = "username")
                            } else {
                                Text(text = "Search")
                            }
                        },
                        leadingIcon = {
                            IconButton(
                                onClick = {
                                    if (expanded) {
                                        viewModel.onClearQuery()
                                        expanded = false
                                    } else {
                                        goBack()
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = back,
                                    contentDescription = ""
                                )
                            }
                        },
                        trailingIcon = {
                            if (state.query.isNotEmpty()) {
                                IconButton(
                                    onClick = { viewModel.onClearQuery() }
                                ) {
                                    Icon(
                                        imageVector = close,
                                        contentDescription = ""
                                    )
                                }
                            }
                        }
                    )
                },
                expanded = expanded,
                onExpandedChange = {
                    expanded = it
                    if (!it) {
                        viewModel.onClearQuery()
                    }
                },
                shape = RoundedCornerShape(0.dp)
            ) {
                if (state.isSearching) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else if (state.results.isEmpty() && state.query.isNotEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No Results For ${state.query}",
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(15.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(state.results) { user ->
                            UserItem(
                                user = user,
                                onClick = {

                                }
                            )
                        }
                    }
                }
            }
        }
    ) {
        Box(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Search for friends to track runs together!",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}