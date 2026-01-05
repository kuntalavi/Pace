package com.rk.pace.auth.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.rk.pace.theme.White

@Composable
fun InputText(
    modifier: Modifier = Modifier,
    v: String,
    onVChange: (String) -> Unit,
    l: String,
    placeh: String,
    i: ImageVector?,
    isPassword: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text,
) {

    var isPasswordV by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
    ) {
        Text(
            text = l,
            fontWeight = FontWeight.Medium,
            color = TextDark,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        OutlinedTextField(
            value = v,
            onValueChange = onVChange,
            placeholder = { Text(placeh, color = TextGray) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = White,
                unfocusedContainerColor = White,
                unfocusedBorderColor = BorderGray,
                focusedBorderColor = PrimaryGreen,
                focusedLabelColor = TextDark,
                unfocusedLabelColor = TextGray,
                cursorColor = PrimaryGreen
            ),
            leadingIcon = i?.let {
                { Icon(imageVector = it, contentDescription = "", tint = TextGray) }
            },
            trailingIcon = if (isPassword) {
                {
                    IconButton(onClick = { isPasswordV = !isPasswordV }) {
                        Icon(
                            imageVector = if (isPasswordV) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = if (isPasswordV) "Hide Password" else "Show Password",
                            tint = TextGray
                        )
                    }
                }
            } else null,
            visualTransformation = if (isPassword && !isPasswordV) PasswordVisualTransformation() else VisualTransformation.None,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            singleLine = true
        )
    }
}