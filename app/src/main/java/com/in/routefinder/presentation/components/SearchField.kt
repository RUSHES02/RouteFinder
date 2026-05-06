package com.`in`.routefinder.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons.Default
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import com.`in`.routefinder.ui.theme.colorAccent1
import com.`in`.routefinder.ui.theme.colorPrimary
import com.`in`.routefinder.ui.theme.colorPrimary1
import com.`in`.routefinder.ui.theme.colorWhite

@Composable
fun SearchField(
    value: String,
    placeholder: String,
    leadingIcon: ImageVector,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        placeholder = {
            Text(placeholder)
        },
        leadingIcon = {
            Icon(
                imageVector = leadingIcon,
                contentDescription = null
            )
        },
        shape = RoundedCornerShape(25f),
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedContainerColor = colorPrimary,
            focusedContainerColor = colorPrimary,
            unfocusedBorderColor = colorAccent1,
            focusedBorderColor = colorWhite,
            cursorColor = colorPrimary,
            focusedLeadingIconColor = colorWhite,
            unfocusedLeadingIconColor = colorWhite,
            focusedTextColor = colorWhite,
            unfocusedTextColor = colorWhite,
            focusedPlaceholderColor = colorAccent1,
            unfocusedPlaceholderColor = colorAccent1
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun SearchFieldPreview() {
    SearchField(
        value = "",
        placeholder = "Where to?",
        leadingIcon = Default.Search,
        onValueChange = {}
    )
}