package com.amanullah.chromaticsaiassessment.base.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.amanullah.chromaticsaiassessment.R
import com.amanullah.chromaticsaiassessment.data.local.entity.Contact

@Composable
fun ContactItemView(
    contact: Contact,
    trailingIconPainter: Painter = painterResource(id = R.drawable.ic_block),
    trailingIconImageVector: ImageVector? = null,
    clickCallBack: (Contact) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Box(
            modifier = Modifier
                .padding(end = 16.dp)
        ) {
            if (contact.photoUri.isNullOrEmpty()) {
                NameAvatar(
                    modifier = Modifier,
                    name = contact.name
                )
            } else {
                AsyncImage(
                    model = ImageRequest.Builder(context = LocalContext.current)
                        .data(data = contact.photoUri)
                        .crossfade(enable = true)
                        .build(),
                    contentDescription = null,
                    modifier = Modifier
                        .size(size = 48.dp)
                        .clip(CircleShape)
                )
            }
        }

        Column(
            modifier = Modifier
                .weight(weight = 1F)
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = contact.name,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = contact.phoneNumber,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        IconButton(
            onClick = {
                clickCallBack(contact)
            }
        ) {
            if (trailingIconImageVector == null) {
                Icon(
                    painter = trailingIconPainter,
                    contentDescription = null
                )
            } else {
                Icon(
                    imageVector = trailingIconImageVector,
                    contentDescription = null
                )
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    Box(
        modifier = Modifier
            .background(color = Color.White)
    ) {
        ContactItemView(
            contact = Contact(
                phoneNumber = "(338) 612-6802",
                name = "Bonita May",
                photoUri = null,
                isBlocked = false
            ),
            clickCallBack = {}
        )
    }
}