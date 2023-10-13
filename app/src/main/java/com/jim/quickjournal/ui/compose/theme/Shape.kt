package com.jim.quickjournal.ui.compose.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

val Shapes = Shapes(
    small = RoundedCornerShape(4.dp),
    medium = RoundedCornerShape(bottomStart = 16.dp, topEnd = 16.dp),
    large = RoundedCornerShape(16.dp)
)
