package com.example.androidlabs.presentation.screens.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil.compose.AsyncImage
import com.example.androidlabs.domain.model.GithubRepo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepoDetailsScreen(
    repo: GithubRepo,
    onFavoriteClick: () -> Unit,
    isFavorite: Boolean,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = repo.name) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Назад"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onFavoriteClick) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Default.Star else Icons.Outlined.StarBorder,
                            contentDescription = "Избранное",
                            tint = if (isFavorite) Color(0xFFFFC107) else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            val (avatar, langBadge, title, owner, statsRow, divider, description, updatedText) = createRefs()

            AsyncImage(
                model = repo.ownerAvatarUrl,
                contentDescription = "владелец репозитория",
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .constrainAs(avatar) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                    }
            )

            if (repo.language != null) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.constrainAs(langBadge) {
                        top.linkTo(avatar.bottom)
                        bottom.linkTo(avatar.bottom)
                        start.linkTo(avatar.start)
                        end.linkTo(avatar.end)
                    }
                ) {
                    Text(
                        text = repo.language,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
            }

            Text(
                text = repo.name,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.constrainAs(title) {
                    top.linkTo(avatar.top, margin = 8.dp)
                    start.linkTo(avatar.end, margin = 16.dp)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                }
            )

            Text(
                text = "Владелец: ${repo.ownerLogin}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.constrainAs(owner) {
                    top.linkTo(title.bottom, margin = 4.dp)
                    start.linkTo(title.start)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                }
            )

            val headerBottomBarrier = createBottomBarrier(langBadge, owner)

            Row(
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                modifier = Modifier.constrainAs(statsRow) {
                    top.linkTo(headerBottomBarrier, margin = 24.dp)
                    start.linkTo(parent.start)
                }
            ) {
                Text(text = "⭐ ${repo.stars} звезд", style = MaterialTheme.typography.titleMedium)
                Text(text = "🍴 ${repo.forks} форков", style = MaterialTheme.typography.titleMedium)
            }

            HorizontalDivider(
                modifier = Modifier.constrainAs(divider) {
                    top.linkTo(statsRow.bottom, margin = 16.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
            )

            Text(
                text = repo.description ?: "Описание отсутствует",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.constrainAs(description) {
                    top.linkTo(divider.bottom, margin = 16.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                }
            )

            Text(
                text = "Обновлено: ${repo.updatedAt}",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.outline,
                modifier = Modifier.constrainAs(updatedText) {
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                }
            )
        }
    }
}