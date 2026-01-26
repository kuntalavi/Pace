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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
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
    onUserClick: (String) -> Unit,
    goBack: () -> Unit,
) {

    val state by viewModel.state.collectAsStateWithLifecycle()
    var expanded by rememberSaveable { mutableStateOf(false) }

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    Scaffold(
        topBar = {
            SearchBar(
                colors = SearchBarDefaults.colors(
                    dividerColor = MaterialTheme.colorScheme.background
                ),
                inputField = {
                    SearchBarDefaults.InputField(
                        query = state.query,
                        onQueryChange = {
                            viewModel.onQueryChange(it)
                        },
                        onSearch = {
                            keyboardController?.hide()
                        },
                        expanded = expanded,
                        onExpandedChange = { isExpanded ->
                            expanded = isExpanded
                        },
                        placeholder = {
                            if (expanded) {
                                Text(text = "search people by username")
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
                onExpandedChange = { isExpanded ->
                    expanded = isExpanded
                    if (!isExpanded) {
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
                            text = "No users found for ${state.query} username",
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(15.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(
                            items = state.results,
                            key = { user ->
                                user.userId
                            }
                        ) { user ->
                            UserItem(
                                user = user,
                                onClick = {
                                    keyboardController?.hide()
                                    focusManager.clearFocus()
                                    onUserClick(user.userId)
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
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
    }
}