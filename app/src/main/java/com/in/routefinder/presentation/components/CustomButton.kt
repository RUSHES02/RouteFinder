package com.`in`.routefinder.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.`in`.routefinder.ui.theme.RouteFinderTheme
import com.`in`.routefinder.ui.theme.colorPrimary
import com.`in`.routefinder.ui.theme.colorTransparent
import com.`in`.routefinder.ui.theme.colorWhite

/**
 * A custom composable button with customizable properties.
 *
 * @param modifier The modifier to apply to the button.
 * @param text The text to display on the button.
 * @param icon An optional icon to display with the button at the start.
 * @param onClick The action to perform when the button is clicked.
 * @param buttonType The type of button to display. Defaults to [ButtonType.PRIMARY].
 * @param disabled A boolean indicating whether the button is disabled. Defaults to `false`.
 */
@Composable
fun CustomButton(
    modifier: Modifier = Modifier,
    text: String,
    icon: Painter? = null,
    onClick: () -> Unit,
    buttonType: ButtonType = ButtonType.PRIMARY,
    disabled: Boolean = false
) {
    val containerColor = when (buttonType) {
        ButtonType.PRIMARY -> colorWhite
        ButtonType.SECONDARY -> colorTransparent
    }

    val contentColor = when (buttonType) {
        ButtonType.PRIMARY -> colorPrimary
        ButtonType.SECONDARY -> colorPrimary
    }

    val borderColor = when (buttonType) {
        ButtonType.PRIMARY -> if (disabled) colorWhite.copy(alpha = 0.5f) else colorWhite
        ButtonType.SECONDARY -> if (disabled) colorPrimary.copy(alpha = 0.5f) else colorPrimary
    }

    Button(
		onClick = onClick,
		modifier = modifier,
		shape = RoundedCornerShape(50),
		colors = ButtonDefaults.buttonColors(
			containerColor = containerColor,
			contentColor = contentColor,
            disabledContainerColor = containerColor.copy(alpha = 0.5f),
            disabledContentColor = contentColor.copy(alpha = 0.7f)
		),
		border = BorderStroke(1.dp, borderColor),
		enabled = !disabled,
	) {
		Row (
			horizontalArrangement = Arrangement.spacedBy(8.dp),
			verticalAlignment = Alignment.CenterVertically
		) {
			// Display the icon if provided
			if (icon != null) {
				Icon(
					modifier = Modifier
						.size(13.dp),
                    painter = icon,
					contentDescription = null,
					tint = contentColor
				)
			}
			// Display the text
			Text(
				text = text,
				style = MaterialTheme.typography.labelLarge
			)
		}
	}
}

@Preview(showBackground = true, showSystemUi = false)
@Composable
private fun CustomButtonPreview() {
    RouteFinderTheme {
        CustomButton(
            text = "Button",
            onClick = {},
            buttonType = ButtonType.PRIMARY,
            disabled = false
        )
    }
}

enum class ButtonType {
	PRIMARY,
	SECONDARY
}