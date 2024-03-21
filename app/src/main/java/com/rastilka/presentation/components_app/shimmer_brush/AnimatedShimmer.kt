package com.rastilka.presentation.components_app.shimmer_brush

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun animatedShimmer(): Brush {
    val shimmerColors = listOf(
        Color(0xFF696969).copy(alpha = 0.6f),
        Color(0xFF696969).copy(alpha = 0.2f),
        Color(0xFF696969).copy(alpha = 0.6f)
    )

    val transition = rememberInfiniteTransition(label = "")
    val transitionAnim = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1000,
                easing = FastOutSlowInEasing
            )
        ),
        label = ""
    )

    return Brush.linearGradient(
        colors = shimmerColors,
        start = Offset.Zero,
        end = Offset(x = transitionAnim.value, y = transitionAnim.value)
    )
}

@Composable
fun ShimmerCardFriend(brush: Brush) {
    ElevatedCard(
        elevation = CardDefaults.elevatedCardElevation(10.dp),
        modifier = Modifier
            .padding(10.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(10.dp)
        ) {
            Row(
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Spacer(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(100))
                        .background(brush = brush)
                )
                Spacer(
                    modifier = Modifier
                        .padding(start = 10.dp, top = 10.dp, end = 20.dp)
                        .height(20.dp)
                        .fillMaxWidth()
                        .background(
                            brush = brush,
                            shape = RoundedCornerShape(100.dp)
                        )


                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 10.dp)
            ) {
                Spacer(
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            brush = brush,
                            shape = RoundedCornerShape(100.dp)
                        )
                )
                Spacer(
                    modifier = Modifier
                        .padding(start = 5.dp, end = 5.dp)
                        .height(50.dp)
                        .width(70.dp)
                        .background(brush = brush)
                )
                Spacer(
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            brush = brush,
                            shape = RoundedCornerShape(100.dp)
                        )
                )
                Spacer(modifier = Modifier.weight(1f))
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(
                        modifier = Modifier
                            .padding(bottom = 5.dp)
                            .width(80.dp)
                            .height(20.dp)
                            .background(brush = brush, shape = RoundedCornerShape(100.dp))
                    )
                    Spacer(
                        modifier = Modifier
                            .width(50.dp)
                            .height(20.dp)
                            .background(brush = brush, shape = RoundedCornerShape(100.dp))
                    )
                }
            }
        }
    }
}

@Composable
fun ShimmerLoadShoppingCartSum(brush: Brush) {
    Column(
        modifier = Modifier.padding(10.dp)
    ) {
        Spacer(
            modifier = Modifier
                .width(100.dp)
                .height(8.dp)
                .background(brush = brush, shape = RoundedCornerShape(10.dp))
        )
        Spacer(modifier = Modifier.height(5.dp))
        Spacer(
            modifier = Modifier
                .width(50.dp)
                .height(14.dp)
                .background(brush = brush, shape = RoundedCornerShape(10.dp))
        )
    }
}

@Composable
fun ShimmerLoadShoppingCartProductCard(brush: Brush) {
    Card(
        modifier = Modifier.padding(start = 8.dp, end = 8.dp, bottom = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
        ) {
            Spacer(
                modifier = Modifier
                    .padding(start = 10.dp)
                    .height(80.dp)
                    .width(80.dp)
                    .background(brush = brush, shape = MaterialTheme.shapes.small)
            )
            Column {
                Spacer(
                    modifier = Modifier
                        .padding(start = 10.dp, end = 10.dp, top = 5.dp)
                        .height(16.dp)
                        .fillMaxWidth()
                        .background(brush = brush, shape = RoundedCornerShape(10.dp))
                )
                Spacer(
                    modifier = Modifier
                        .padding(start = 10.dp, end = 10.dp, top = 5.dp)
                        .height(16.dp)
                        .fillMaxWidth()
                        .background(brush = brush, shape = RoundedCornerShape(10.dp))
                )
                Spacer(
                    modifier = Modifier
                        .padding(start = 10.dp, end = 10.dp, top = 5.dp)
                        .width(100.dp)
                        .height(24.dp)
                        .background(brush = brush, shape = RoundedCornerShape(10.dp))
                )
            }
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(20.dp, Alignment.End),
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 10.dp, start = 10.dp, bottom = 5.dp)
        ) {
            Spacer(
                modifier = Modifier
                    .padding(top = 5.dp)
                    .size(25.dp)
                    .background(brush = brush, shape = RoundedCornerShape(5.dp))
            )
            Spacer(
                modifier = Modifier
                    .padding(start = 10.dp, end = 10.dp, top = 5.dp)
                    .height(25.dp)
                    .width(50.dp)
                    .background(brush = brush, shape = RoundedCornerShape(10.dp))
            )
        }
    }
}

@Composable
fun ShimmerLoadCatalog(brush: Brush) {
    Card(
        modifier = Modifier.padding(10.dp),
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 15.dp)
            ) {
                Spacer(
                    modifier = Modifier
                        .padding(10.dp)
                        .weight(1f)
                        .height(24.dp)
                        .fillMaxWidth()
                        .background(brush = brush, shape = MaterialTheme.shapes.small)
                )
                Spacer(
                    modifier = Modifier
                        .padding(10.dp)
                        .size(25.dp)
                        .background(brush = brush, shape = RoundedCornerShape(5.dp))
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(
                    10.dp,
                    Alignment.CenterHorizontally
                ),
                modifier = Modifier.padding(10.dp)
            ) {
                repeat(3) {
                    ElevatedCard(
                        elevation = CardDefaults.cardElevation(5.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        ),
                        modifier = Modifier
                            .height(150.dp)
                            .width(150.dp)
                    ) {
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Spacer(
                                modifier = Modifier
                                    .padding(top = 10.dp, start = 5.dp, end = 5.dp)
                                    .height(12.dp)
                                    .fillMaxWidth()
                                    .background(brush = brush, shape = MaterialTheme.shapes.small)
                            )
                            Spacer(
                                modifier = Modifier
                                    .padding(top = 2.dp, start = 5.dp, end = 5.dp)
                                    .height(12.dp)
                                    .fillMaxWidth()
                                    .background(brush = brush, shape = MaterialTheme.shapes.small)
                            )
                            Spacer(
                                modifier = Modifier
                                    .padding(top = 2.dp, start = 5.dp, end = 5.dp, bottom = 5.dp)
                                    .fillMaxHeight()
                                    .fillMaxWidth()
                                    .background(brush = brush, shape = MaterialTheme.shapes.small)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ShimmerLoadBanner(brush: Brush) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(240.dp)
            .padding(5.dp)
            .clip(MaterialTheme.shapes.small)
    ) {
        Spacer(
            modifier = Modifier
                .fillMaxSize()
                .background(brush = brush, shape = MaterialTheme.shapes.small)
        )
    }
}

@Composable
fun ShimmerLoadSection(brush: Brush) {
    Card(
        modifier = Modifier
            .height(350.dp)
            .padding(all = 5.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 5.dp, end = 5.dp)
        ) {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(5.dp)
                    .clip(MaterialTheme.shapes.small)
                    .background(brush = brush, shape = MaterialTheme.shapes.small)
            )
            Spacer(
                modifier = Modifier
                    .padding(bottom = 2.dp)
                    .height(24.dp)
                    .fillMaxWidth()
                    .background(brush = brush, shape = RoundedCornerShape(10.dp))
            )
            Spacer(
                modifier = Modifier
                    .padding(bottom = 6.dp)
                    .height(24.dp)
                    .fillMaxWidth()
                    .background(brush = brush, shape = RoundedCornerShape(10.dp))
            )
            Spacer(
                modifier = Modifier
                    .padding(bottom = 2.dp)
                    .height(16.dp)
                    .fillMaxWidth()
                    .background(brush = brush, shape = RoundedCornerShape(10.dp))
            )
            Spacer(
                modifier = Modifier
                    .padding(bottom = 6.dp)
                    .height(16.dp)
                    .fillMaxWidth()
                    .background(brush = brush, shape = RoundedCornerShape(10.dp))
            )
            Spacer(modifier = Modifier.weight(1f))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp)
            ) {
                Spacer(
                    modifier = Modifier
                        .height(25.dp)
                        .width(60.dp)
                        .background(brush = brush, shape = RoundedCornerShape(10.dp))
                )
                Spacer(
                    modifier = Modifier
                        .padding(top = 5.dp)
                        .size(25.dp)
                        .background(brush = brush, shape = RoundedCornerShape(5.dp))
                )
            }
        }
    }
}

@Composable
fun ShimmerLoadProductCard(brush: Brush) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start,
    ) {
        Spacer(
            modifier = Modifier
                .height(300.dp)
                .fillMaxWidth()
                .background(brush = brush)
        )
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .height(24.dp)
                .background(brush = brush, shape = MaterialTheme.shapes.small)
        )
        Card(
            modifier = Modifier.padding(10.dp)
        ) {
            Column {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                ) {
                    Spacer(
                        modifier = Modifier
                            .height(24.dp)
                            .width(60.dp)
                            .background(brush = brush, shape = RoundedCornerShape(10.dp))
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.clip(MaterialTheme.shapes.extraLarge)
                    ) {
                        Spacer(
                            modifier = Modifier
                                .height(40.dp)
                                .width(100.dp)
                                .padding(end = 10.dp)
                                .clip(MaterialTheme.shapes.extraLarge)
                                .background(brush = brush, shape = RoundedCornerShape(10.dp))
                        )
                        Spacer(
                            modifier = Modifier
                                .height(40.dp)
                                .width(150.dp)
                                .clip(MaterialTheme.shapes.extraLarge)
                                .background(brush = brush, shape = RoundedCornerShape(10.dp))
                        )
                    }
                }
            }
        }
        Column {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .height(24.dp)
                    .background(brush = brush, shape = MaterialTheme.shapes.small)
            )
            Card(
                modifier = Modifier.padding(10.dp)
            ) {
                Spacer(
                    modifier = Modifier
                        .height(300.dp)
                        .fillMaxWidth()
                        .padding(10.dp)
                        .background(brush = brush, shape = RoundedCornerShape(10.dp))
                )
            }
        }
    }
}

@Composable
fun ShimmerLoadOrderCard(brush: Brush) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                Column {
                    Spacer(
                        modifier = Modifier
                            .width(50.dp)
                            .height(16.dp)
                            .background(brush = brush, shape = MaterialTheme.shapes.small)
                    )
                    Spacer(
                        modifier = Modifier
                            .padding(top = 5.dp)
                            .width(150.dp)
                            .height(16.dp)
                            .background(brush = brush, shape = MaterialTheme.shapes.small)
                    )
                    Spacer(
                        modifier = Modifier
                            .padding(top = 5.dp)
                            .width(150.dp)
                            .height(8.dp)
                            .background(brush = brush, shape = MaterialTheme.shapes.small)
                    )
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                repeat(5) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(
                            5.dp,
                            Alignment.CenterVertically
                        ),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .width(80.dp)
                            .clip(MaterialTheme.shapes.small)
                    ) {
                        Card {
                            Spacer(
                                modifier = Modifier
                                    .width(80.dp)
                                    .height(80.dp)
                                    .background(brush = brush, shape = MaterialTheme.shapes.small)
                            )
                        }
                        Spacer(
                            modifier = Modifier
                                .width(80.dp)
                                .height(8.dp)
                                .background(brush = brush, shape = MaterialTheme.shapes.small)
                        )
                    }
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                Column {
                    Spacer(
                        modifier = Modifier
                            .padding(top = 5.dp)
                            .width(150.dp)
                            .height(8.dp)
                            .background(brush = brush, shape = MaterialTheme.shapes.small)
                    )
                    Spacer(
                        modifier = Modifier
                            .padding(top = 5.dp)
                            .width(100.dp)
                            .height(16.dp)
                            .background(brush = brush, shape = MaterialTheme.shapes.small)
                    )
                }
                Spacer(
                    modifier = Modifier
                        .padding(top = 5.dp)
                        .width(150.dp)
                        .height(16.dp)
                        .background(brush = brush, shape = MaterialTheme.shapes.small)
                )
            }
        }
    }
}

@Composable
fun ShimmerLoadPointCard(brush: Brush) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Column {
                Spacer(
                    modifier = Modifier
                        .height(16.dp)
                        .width(150.dp)
                        .background(brush = brush, shape = RoundedCornerShape(10.dp))
                )
                Spacer(
                    modifier = Modifier
                        .padding(top = 5.dp)
                        .height(12.dp)
                        .width(100.dp)
                        .background(brush = brush, shape = RoundedCornerShape(10.dp))
                )
                Spacer(
                    modifier = Modifier
                        .padding(top = 5.dp)
                        .height(12.dp)
                        .width(100.dp)
                        .background(brush = brush, shape = RoundedCornerShape(10.dp))
                )
                Spacer(
                    modifier = Modifier
                        .padding(top = 5.dp)
                        .height(16.dp)
                        .width(200.dp)
                        .background(brush = brush, shape = RoundedCornerShape(10.dp))
                )
            }
            Spacer(
                modifier = Modifier
                    .height(24.dp)
                    .width(100.dp)
                    .background(brush = brush, shape = RoundedCornerShape(10.dp))
            )
        }
    }
}

@Composable
fun ShimmerLoadSum(brush: Brush) {
    Spacer(
        modifier = Modifier
            .height(18.dp)
            .width(100.dp)
            .background(brush = brush, shape = RoundedCornerShape(10.dp))
    )
}

@Composable
fun ShimmerLoadImage(brush: Brush) {
    Spacer(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = brush)
    )
}

@Preview
@Composable
fun DemoShimmerCardFrind() {
    ShimmerCardFriend(brush = animatedShimmer())
}

@Preview
@Composable
fun DemoShimmerLoadShoppingCartProductCard() {
    ShimmerLoadShoppingCartProductCard(brush = animatedShimmer())
}

@Preview
@Composable
fun DemoShimmerCatalog() {
    ShimmerLoadCatalog(brush = animatedShimmer())
}

@Preview
@Composable
fun DemoShimmerBanner() {
    ShimmerLoadBanner(brush = animatedShimmer())
}

@Preview
@Composable
fun DemoShimmerSection() {
    ShimmerLoadSection(brush = animatedShimmer())
}

@Preview
@Composable
fun DemoShimmerProductCard() {
    ShimmerLoadProductCard(brush = animatedShimmer())
}

@Preview
@Composable
fun DemoShimmerOrder() {
    ShimmerLoadOrderCard(brush = animatedShimmer())
}

@Preview
@Composable
fun DemoShimmerPointCard() {
    ShimmerLoadPointCard(brush = animatedShimmer())
}

@Preview
@Composable
fun DemoShimmerPoint() {
    ShimmerLoadSum(brush = animatedShimmer())
}

