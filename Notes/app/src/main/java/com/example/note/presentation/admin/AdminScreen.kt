package com.example.note.presentation.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.note.data.api.UserWithSubscriptionDto
import org.koin.androidx.compose.koinViewModel

@Composable
fun AdminScreen(controller: NavHostController) {
    val viewModel: AdminViewModel = koinViewModel()
    val state by viewModel.state.collectAsState()
    val actionError by viewModel.actionError.collectAsState()
    val scheme = MaterialTheme.colorScheme

    var userToSetSub by remember { mutableStateOf<UserWithSubscriptionDto?>(null) }
    var userToCancel by remember { mutableStateOf<UserWithSubscriptionDto?>(null) }

    // Диалог: выбор срока подписки
    userToSetSub?.let { user ->
        AlertDialog(
            onDismissRequest = { userToSetSub = null },
            title = { Text("Подписка для ${user.email}") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf(1 to "1 месяц", 3 to "3 месяца", 6 to "6 месяцев", 12 to "1 год").forEach { (months, label) ->
                        OutlinedButton(
                            onClick = {
                                viewModel.setSubscription(user.id, months)
                                userToSetSub = null
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(label)
                        }
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { userToSetSub = null }) { Text("Отмена") }
            }
        )
    }

    // Диалог: подтверждение отмены подписки
    userToCancel?.let { user ->
        AlertDialog(
            onDismissRequest = { userToCancel = null },
            title = { Text("Удалить подписку?") },
            text = { Text("Подписка пользователя ${user.email} будет отменена.") },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.cancelSubscription(user.id)
                        userToCancel = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = scheme.error)
                ) { Text("Удалить") }
            },
            dismissButton = {
                TextButton(onClick = { userToCancel = null }) { Text("Отмена") }
            }
        )
    }

    // Диалог: ошибка действия
    actionError?.let { error ->
        AlertDialog(
            onDismissRequest = { viewModel.clearActionError() },
            title = { Text("Ошибка") },
            text = { Text(error) },
            confirmButton = {
                TextButton(onClick = { viewModel.clearActionError() }) { Text("OK") }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    colors = listOf(scheme.background, scheme.surfaceContainer),
                    start = Offset(0f, 0f),
                    end = Offset(800f, 1600f)
                )
            )
    ) {
        // Топбар
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = scheme.surfaceContainerHigh
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { controller.popBackStack() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                }
                Text(
                    text = "Пользователи",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }

        when (val s = state) {
            is AdminUiState.Loading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            is AdminUiState.Error -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(s.message, color = scheme.error)
                        TextButton(onClick = { viewModel.loadUsers() }) { Text("Повторить") }
                    }
                }
            }

            is AdminUiState.Success -> {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(s.users, key = { it.id }) { user ->
                        UserCard(
                            user = user,
                            onSetSubscription = { userToSetSub = user },
                            onCancelSubscription = { userToCancel = user }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun UserCard(
    user: UserWithSubscriptionDto,
    onSetSubscription: () -> Unit,
    onCancelSubscription: () -> Unit
) {
    val scheme = MaterialTheme.colorScheme

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        colors = CardDefaults.cardColors(containerColor = scheme.surfaceContainerLow)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = user.email,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
                if (user.hasActive && user.subscription?.expiresAt != null) {
                    val expires = user.subscription.expiresAt.take(10) // YYYY-MM-DD
                    Text(
                        text = "До $expires",
                        style = MaterialTheme.typography.bodySmall,
                        color = scheme.primary
                    )
                } else {
                    Text(
                        text = "Нет подписки",
                        style = MaterialTheme.typography.bodySmall,
                        color = scheme.onSurfaceVariant
                    )
                }
            }

            if (user.hasActive) {
                OutlinedButton(
                    onClick = onCancelSubscription,
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = scheme.error)
                ) {
                    Text("Удалить")
                }
            } else {
                Button(onClick = onSetSubscription) {
                    Text("Добавить")
                }
            }
        }
    }
}
