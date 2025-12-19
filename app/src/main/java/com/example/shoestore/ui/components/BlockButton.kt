package com.example.shoestore.ui.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shoestore.R

@Composable
fun BlockButton(modifier: Modifier = Modifier, text: String) {
    Button(onClick = {}, shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = colorResource(R.color.block),
            contentColor = colorResource(R.color.black))) {
        Text(text = text, fontSize = 14.sp)
    }
}

@Preview
@Composable
private fun BlockButtonPreview() {
    BlockButton(text = "Текст кнопки")
}