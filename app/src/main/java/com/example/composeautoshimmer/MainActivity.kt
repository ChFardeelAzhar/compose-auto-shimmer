package com.example.composeautoshimmer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.composeautoshimmer.components.ShimmerBox
import com.example.composeautoshimmer.components.ShimmerOverlay
import com.example.composeautoshimmer.components.ShimmerPlaceholder
import com.example.composeautoshimmer.core.ShimmerDefaults
import com.example.composeautoshimmer.ui.theme.ComposeAutoShimmerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val isDark = isSystemInDarkTheme()
            // Provide global shimmer configuration based on theme
            val shimmerConfig = remember(isDark) {
                ShimmerConfig(
                    baseColor = if (isDark) ShimmerDefaults.BaseColorDark else ShimmerDefaults.BaseColor,
                    highlightColor = if (isDark) ShimmerDefaults.HighlightColorDark else ShimmerDefaults.HighlightColor
                )
            }

            ComposeAutoShimmerTheme {
                ShimmerTheme(config = shimmerConfig) {
                    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                        ShimmerDemoScreen(modifier = Modifier.padding(innerPadding))
                    }
                }
            }
        }
    }
}

@Composable
fun ShimmerDemoScreen(modifier: Modifier = Modifier) {
    var isLoading by remember { mutableStateOf(true) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {

        Text(
            text = "ComposeAutoShimmer Demo",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Loading State", style = MaterialTheme.typography.titleMedium)
            Switch(checked = isLoading, onCheckedChange = { isLoading = it })
        }

        HorizontalDivider()


        // Demo 1: ShimmerBox wrapping a User Profile Card
        DemoSection(title = "1. ShimmerBox (Content Wrapper)") {
            ShimmerBox(isLoading = isLoading) {
                UserProfileCard()
            }
        }

        // Demo 2: ShimmerOverlay on an Image/Container
        DemoSection(title = "2. ShimmerOverlay (Additive Effect)") {
            ShimmerOverlay(active = isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.onSurface),
                    contentAlignment = Alignment.Center
                ) {

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        ShimmerOverlay(isLoading) {
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {

                                Badge {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(5.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Star,
                                            contentDescription = null,
                                            modifier = Modifier.size(25.dp),
                                            tint = Color.Yellow
                                        )
                                        Text("Kotlin")
                                    }
                                }
                                Badge {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(5.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Favorite,
                                            contentDescription = null,
                                            modifier = Modifier.size(25.dp),
                                            tint = Color.Red
                                        )
                                        Text("Android")
                                    }
                                }
                                Badge {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(5.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.CheckCircle,
                                            contentDescription = null,
                                            modifier = Modifier.size(25.dp),
                                            tint = Color.Green
                                        )
                                        Text("Compose")
                                    }
                                }
                            }

                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                modifier = Modifier.size(64.dp),
                                tint = Color.Red
                            )
                            Text(
                                "Thi is Compose Auto Shimmer",
                                modifier = Modifier
                                    .padding(2.dp),
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Blue
                            )
                        }


                    }

                }
            }
        }

        // Demo 3: ShimmerPlaceholder (Skeleton Layout)
        DemoSection(title = "3. ShimmerPlaceholder (Skeleton)") {
            if (isLoading) {
                SkeletonLoader()
            } else {
                RealContent()
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun DemoSection(title: String, content: @Composable () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary
        )
        content()
    }
}

@Composable
fun UserProfileCard() {
    // Note: ShimmerBox creates a silhouette of non-transparent pixels.
    // If we want to see individual text/icon shapes, the container should be transparent.
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // This Circle will be detected as a circular shimmer automatically
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = null,
                    modifier = Modifier.size(40.dp),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }

            Spacer(modifier = Modifier.width(20.dp))

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                // These text lines will be detected as shimmering bars automatically
                Text(
                    text = "Fardeel Dev",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Mid Level Android Engineer",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Badge { Text("Kotlin") }
                    Badge { Text("Compose") }
                    Badge { Text("Library") }
                }
            }
        }
    }
}

@Composable
fun SkeletonLoader() {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            ShimmerPlaceholder(width = 48.dp, height = 48.dp, cornerRadius = 24.dp)
            Spacer(modifier = Modifier.width(12.dp))
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                ShimmerPlaceholder(width = 140.dp, height = 16.dp)
                ShimmerPlaceholder(width = 90.dp, height = 12.dp)
            }
        }
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            ShimmerPlaceholder(width = 280.dp, height = 12.dp)
            ShimmerPlaceholder(width = 240.dp, height = 12.dp)
            ShimmerPlaceholder(width = 180.dp, height = 12.dp)
        }
    }
}

@Composable
fun RealContent() {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.tertiaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onTertiaryContainer
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = "Performance Metrics",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "Updated 2 mins ago",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        Text(
            text = "ComposeAutoShimmer delivers smooth 60fps animations by leveraging the native Android graphics pipeline directly within Jetpack Compose's draw phase.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = "It is fully compatible with Material 3 and dynamic color themes.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}