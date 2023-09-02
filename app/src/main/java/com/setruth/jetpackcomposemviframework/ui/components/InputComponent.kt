package com.setruth.jetpackcomposemviframework.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RemoveRedEye
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material.icons.rounded.VisibilityOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.setruth.jetpackcomposemviframework.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SingleInput(
    inputContent: String = "",
    modifier: Modifier = Modifier,
    startIcon: ImageVector? = null,
    inputTip: String = "",
    enabled: Boolean = true,
    activeContentHideTag: Boolean = false,
    defaultContentHide: Boolean = true,
    inputChange: (value: String) -> Unit = {},
) {
    var inputErr by remember {
        mutableStateOf(false)
    }
    var showContent by remember {
        mutableStateOf(defaultContentHide)
    }
    val visualTransformation =
        if (activeContentHideTag && showContent)
            PasswordVisualTransformation()
        else
            VisualTransformation.None
    OutlinedTextField(
        textStyle = MaterialTheme.typography.labelLarge,
        value = inputContent,
        onValueChange = {
            inputErr = (it == "")
            inputChange(it)
        },
        shape = RoundedCornerShape(10.dp),
        modifier = modifier,
        isError = inputErr,
        singleLine = true,
        maxLines = 1,
        enabled = enabled,
        leadingIcon = startIcon?.let {
            {
                Icon(
                    imageVector = startIcon,
                    contentDescription = null
                )
            }
        },
        label = {
            Text(text = inputTip, style = MaterialTheme.typography.labelSmall)
        },
        trailingIcon = {
            if (activeContentHideTag) {
                val icon=if (showContent) Icons.Rounded.Visibility else Icons.Rounded.VisibilityOff
                IconButton(onClick = { showContent=!showContent }) {
                    Icon(
                        imageVector = icon,
                        contentDescription = "隐藏显示内容图标",
                    )
                }
            }
        },
        visualTransformation = visualTransformation,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
        )
    )
}