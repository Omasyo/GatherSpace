package com.omasyo.gatherspace.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.omasyo.gatherspace.ui.theme.GatherSpaceTheme
import com.omasyo.gatherspace.ui.theme.darkScheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthScreen(
    modifier: Modifier = Modifier,
    onAuthenticated: () -> Unit,
    onBackTap: () -> Unit,
    state: AuthState,
    content: @Composable () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    GatherSpaceTheme(colorScheme = darkScheme) {
        Scaffold(
            modifier = modifier,
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            snackbarHost = { SnackbarHost(snackbarHostState) },
        ) {
            Box(
                modifier = Modifier.fillMaxSize()
                    .background(
                        brush = Brush.linearGradient(
                            0.0f to MaterialTheme.colorScheme.tertiaryContainer,
                            0.5f to MaterialTheme.colorScheme.primaryContainer,
                            1.0f to MaterialTheme.colorScheme.secondaryContainer
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    IconButton(
                        onClick = onBackTap,
                        Modifier.padding(24f.dp)

                    ) {
                        Icon(Icons.Default.Close, contentDescription = null)
                    }
                    Box(
                        modifier = Modifier.fillMaxWidth().weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        content()
                    }
                }
            }
        }
    }

    LaunchedEffect(state) {
        if (state is AuthState.Success) {
            onAuthenticated()
        }
        when (state) {
            AuthState.Success -> onAuthenticated()
            is AuthState.Error -> {
                snackbarHostState.showSnackbar(state.message)
            }

            else -> Unit
        }
    }
}
