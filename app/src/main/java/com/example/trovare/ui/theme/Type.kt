package com.example.trovare.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.Font
import com.example.trovare.R

val JosefinSans = FontFamily(
        Font(R.font.josefinsans_bold, weight = FontWeight.Bold),
        Font(R.font.josefinsans_light, weight = FontWeight.Light)
)

val Inder = FontFamily(
        Font(R.font.inder_regular)
)


// Set of Material typography styles to start with
val Typography = Typography(

        displayMedium = TextStyle(
                fontFamily = JosefinSans,
                fontWeight = FontWeight.Bold,
                fontSize = 34.sp
        ),
        displaySmall = TextStyle(
                fontFamily = JosefinSans,
                fontWeight = FontWeight.Bold,
                fontSize = 26.sp
        ),
        bodyLarge = TextStyle(
                fontFamily = JosefinSans,
                fontWeight = FontWeight.Light,
                fontSize = 26.sp,
        ),
        bodyMedium = TextStyle(
                fontFamily = JosefinSans,
                fontWeight = FontWeight.Light,
                fontSize = 20.sp
        ),
        bodySmall = TextStyle(
                fontFamily = JosefinSans,
                fontWeight = FontWeight.Light,
                fontSize = 20.sp
        ),
        headlineSmall = TextStyle(
                fontFamily = Inder,
                fontWeight = FontWeight.Normal,
                fontSize = 24.sp
        ),
        labelMedium = TextStyle(
                fontFamily = Inder,
                fontWeight = FontWeight.Normal,
                fontSize = 20.sp
        ),
        labelSmall = TextStyle(
                fontFamily = Inder,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp
        ),




        /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)

