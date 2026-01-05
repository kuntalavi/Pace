package com.rk.pace.auth.presentation.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rk.pace.theme.White

@Composable
fun SoSignButton(
    modifier: Modifier = Modifier,
    text: String,
//    i: ImageVector,
    onC: () -> Unit,
) {
    Button(
        onClick = onC,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .border(1.dp, BorderGray, RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = White,
            contentColor = TextDark
        ),
        elevation = null
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
//            Icon(
//                imageVector = i,
//                contentDescription = "",
//                modifier = Modifier.size(24.dp)
//            )
//            Image(
//                painter = painterResource(R.drawable.google_48),
//                contentDescription = ""
//            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = text,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}