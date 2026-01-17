package com.rk.pace.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Preview(showBackground = false, showSystemUi = true)
@Composable
fun CustomInputBoxPreview() {
    CustomInputBox(
        modifier = Modifier
            .size(400.dp)
            .fillMaxWidth(),
        value = "",
        onValueChange = {

        },
        placeholder = "PASSWORD",
        isPassword = false
    )
}


@Composable
fun CustomInputBox(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
//    icon: String,
    placeholder: String,
//    icon: ImageVector?,
    isPassword: Boolean = false,
//    keyboardType: KeyboardType = KeyboardType.Text,
) {

    var isPasswordVisible by remember { mutableStateOf(false) }

    fun togglePasswordVisibility() {
        isPasswordVisible = !isPasswordVisible
    }

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                text = placeholder,
                letterSpacing = 1.sp
            )
        },
        modifier = modifier,
        shape = RoundedCornerShape(0.dp),
        trailingIcon = if (isPassword) {
            {
                IconButton(onClick = { togglePasswordVisibility() }) {
                    Icon(
                        imageVector = if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = "",
                    )
                }
            }
        } else null,
        singleLine = true
    )

//    Column(
//        modifier = modifier
//    ) {
////        Text(
////            text = icon,
////            fontWeight = FontWeight.Medium,
////            color = TextDark,
////            modifier = Modifier.padding(bottom = 8.dp)
////        )
//        OutlinedTextField(
//            value = value,
//            onValueChange = onValueChange,
//            placeholder = { Text(placeholder, color = TextGray) },
//            modifier = Modifier.fillMaxWidth(),
//            shape = RoundedCornerShape(12.dp),
//            colors = OutlinedTextFieldDefaults.colors(
//                focusedContainerColor = White,
//                unfocusedContainerColor = White,
//                unfocusedBorderColor = BorderGray,
//                focusedBorderColor = PrimaryGreen,
//                focusedLabelColor = TextDark,
//                unfocusedLabelColor = TextGray,
//                cursorColor = PrimaryGreen
//            ),
//            leadingIcon = i?.let {
//                { Icon(imageVector = it, contentDescription = "", tint = TextGray) }
//            },
//            trailingIcon = if (isPassword) {
//                {
//                    IconButton(onClick = { isPasswordV = !isPasswordV }) {
//                        Icon(
//                            imageVector = if (isPasswordV) Icons.Default.Visibility else Icons.Default.VisibilityOff,
//                            contentDescription = if (isPasswordV) "Hide Password" else "Show Password",
//                            tint = TextGray
//                        )
//                    }
//                }
//            } else null,
//            visualTransformation = if (isPassword && !isPasswordV) PasswordVisualTransformation() else VisualTransformation.None,
//            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
//            singleLine = true
//        )
//    }
}