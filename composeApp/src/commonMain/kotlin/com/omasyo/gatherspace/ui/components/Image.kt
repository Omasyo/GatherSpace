package com.omasyo.gatherspace.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import gatherspace.composeapp.generated.resources.Res
import gatherspace.composeapp.generated.resources.user_placeholder
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun Image(
    imageUrl: String,
    placeholder: DrawableResource,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Crop,
) {
    AsyncImage(
        model = ImageRequest.Builder(LocalPlatformContext.current)
            .data(imageUrl)
            .crossfade(true).build(),
        contentDescription = contentDescription,
        modifier = modifier,
        placeholder = painterResource(placeholder),
        error = painterResource(placeholder),
        fallback = painterResource(placeholder),
        alignment = alignment,
        contentScale = contentScale,
    )
}

@Composable
fun Image(
    image: DrawableResource,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Crop,
) {
    androidx.compose.foundation.Image(
        painter = painterResource(image),
        contentDescription = contentDescription,
        modifier = modifier,
        alignment = alignment,
        contentScale = contentScale,
    )
}