package com.omasyo.gatherspace.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import gatherspace.composeapp.generated.resources.AtkinsonHyperlegible_Bold
import gatherspace.composeapp.generated.resources.AtkinsonHyperlegible_Regular
import gatherspace.composeapp.generated.resources.Res
import org.jetbrains.compose.resources.Font


val atkinsonHyperLegibleFontFamily
    @Composable get() = FontFamily(
        Font(Res.font.AtkinsonHyperlegible_Bold, FontWeight.Bold),
        Font(Res.font.AtkinsonHyperlegible_Regular, FontWeight.Normal),
    )

val Typography
    @Composable get() = with(Typography()) {
        Typography(
            displayLarge = displayLarge.copy(
                fontFamily = atkinsonHyperLegibleFontFamily,
                fontWeight = FontWeight.Bold
            ),
            displayMedium = displayMedium.copy(
                fontFamily = atkinsonHyperLegibleFontFamily,
                fontWeight = FontWeight.Bold
            ),
            displaySmall = displaySmall.copy(
                fontFamily = atkinsonHyperLegibleFontFamily,
                fontWeight = FontWeight.Bold
            ),
            headlineLarge = headlineLarge.copy(
                fontFamily = atkinsonHyperLegibleFontFamily,
                fontWeight = FontWeight.Bold
            ),
            headlineMedium = headlineMedium.copy(
                fontFamily = atkinsonHyperLegibleFontFamily,
                fontWeight = FontWeight.Bold
            ),
            headlineSmall = headlineSmall.copy(
                fontFamily = atkinsonHyperLegibleFontFamily,
                fontWeight = FontWeight.Bold
            ),
            titleLarge = titleLarge.copy(
                fontFamily = atkinsonHyperLegibleFontFamily,
                fontWeight = FontWeight.Bold
            ),
            titleMedium = titleMedium.copy(
                fontFamily = atkinsonHyperLegibleFontFamily,
                fontWeight = FontWeight.Bold
            ),
            titleSmall = titleSmall.copy(
                fontFamily = atkinsonHyperLegibleFontFamily
            ),
            bodyLarge = bodyLarge.copy(fontFamily = atkinsonHyperLegibleFontFamily),
            bodyMedium = bodyMedium.copy(fontFamily = atkinsonHyperLegibleFontFamily),
            bodySmall = bodySmall.copy(fontFamily = atkinsonHyperLegibleFontFamily),
            labelLarge = labelLarge.copy(fontFamily = atkinsonHyperLegibleFontFamily),
            labelMedium = labelMedium.copy(fontFamily = atkinsonHyperLegibleFontFamily),
            labelSmall = labelSmall.copy(fontFamily = atkinsonHyperLegibleFontFamily),
        )
    }