package com.rastilka.presentation.components_app.coil_image_view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.rastilka.BuildConfig
import com.rastilka.R
import com.rastilka.presentation.components_app.shimmer_brush.ShimmerLoadImage
import com.rastilka.presentation.components_app.shimmer_brush.animatedShimmer

@Composable
fun ImageLoadCoil(
    model: String,
    modifier: Modifier,
    contentScale: ContentScale = ContentScale.Fit,
    contentDescription: String?
) {
    SubcomposeAsyncImage(
        modifier = modifier,
        model = ImageRequest.Builder(LocalContext.current)
            .data("${BuildConfig.SERVER_URL}${model}")
            .crossfade(true)
            .networkCachePolicy(CachePolicy.ENABLED)
            .diskCachePolicy(CachePolicy.ENABLED)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .build(),
        filterQuality = FilterQuality.High,
        loading = {
            ShimmerLoadImage(brush = animatedShimmer())
        },
        error = {
            Image(
                painter = painterResource(id = R.drawable.logo_rastilka),
                contentDescription = null,
                modifier = Modifier.padding(30.dp)
            )
        },
        contentScale = contentScale,
        contentDescription = contentDescription
    )
}