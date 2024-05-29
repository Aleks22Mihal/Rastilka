package com.rastilka.presentation.screens.technical_support_screen.components

import android.text.method.LinkMovementMethod
import android.text.util.Linkify
import android.widget.TextView
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.util.LinkifyCompat
import com.rastilka.domain.models.TechnicalSupportMessage

@Composable
fun TechnicalChatBubbleView(
    message: TechnicalSupportMessage,
    userName: String,
) {
    val context = LocalContext.current
    val customLinkifyText = remember { TextView(context) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 5.dp, end = 5.dp, top = 5.dp, bottom = 10.dp),
        horizontalAlignment = when (userName) {
            message.name -> Alignment.End
            else -> Alignment.Start
        },
    ) {
        ElevatedCard(
            elevation = CardDefaults.elevatedCardElevation(5.dp),
            modifier = Modifier
                .widthIn(max = 340.dp)
                .padding(
                    end = when (userName) {
                        message.name -> 3.dp
                        else -> 0.dp
                    },
                    start = when (userName) {
                        message.name -> 0.dp
                        else -> 3.dp
                    },
                ),
            colors = CardDefaults.cardColors(
                containerColor = when (userName) {
                    message.name -> MaterialTheme.colorScheme.secondary
                    else -> Color(0xFFEAE8ED)
                },
            ),
        ) {
            AndroidView(
                modifier = Modifier.padding(8.dp),
                factory = { customLinkifyText }
            ) { textView ->
                textView.text = message.message
                textView.setTextIsSelectable(true)
                LinkifyCompat.addLinks(textView, Linkify.ALL)
                textView.movementMethod = LinkMovementMethod.getInstance()
            }
        }
    }
}