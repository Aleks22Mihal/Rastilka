package com.rastilka.presentation.screens.transaction_screen.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rastilka.R
import com.rastilka.common.utilits_support_preview.SupportPreview
import com.rastilka.domain.models.Transaction
import com.rastilka.presentation.components_app.coil_image_view.ImageLoadCoil
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter


@Composable
fun CardTransaction(transaction: Transaction) {


    val actual = OffsetDateTime.parse(transaction.date, DateTimeFormatter.ISO_DATE_TIME)
    val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")
    val formatDateTime = actual.format(formatter)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(10.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ImageLoadCoil(
                model = transaction.recipeTransaction?.picture.toString(),
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(100))
                    .background(Color.White),
                contentDescription = "",
                contentScale = ContentScale.Crop
            )
            Text(
                text = transaction.recipeTransaction?.name.toString(),
                fontWeight = FontWeight.Bold,
            )
        }
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Icon(
                    painter = painterResource(
                        id = if (transaction.transaction > 0) {
                            R.drawable.ic_arrow_back_24
                        } else R.drawable.ic_arrow_forward_24
                    ),
                    contentDescription = null,
                )

            }
            Text(text = transaction.transactionString.toString(), fontWeight = FontWeight.Bold)
            Text(
                text = transaction.comment.toString(),
                fontWeight = FontWeight.Light,
                maxLines = 1,
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.width(150.dp)
            )
            Text(text = formatDateTime, textAlign = TextAlign.Center, fontSize = 14.sp, color = Color.Blue)
        }
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ImageLoadCoil(
                model = transaction.authTransaction?.picture.toString(),
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(100))
                    .background(Color.White),
                contentDescription = "",
                contentScale = ContentScale.Crop
            )
            Text(
                text = transaction.authTransaction?.name.toString(),
                fontWeight = FontWeight.Bold,
            )
        }
    }
    HorizontalDivider()
}

@Preview
@Composable
private fun DemoCardTransaction() {
    CardTransaction(
        transaction = SupportPreview.transaction
    )
}