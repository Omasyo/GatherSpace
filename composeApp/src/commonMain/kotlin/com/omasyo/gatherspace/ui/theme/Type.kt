package com.omasyo.gatherspace.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import gatherspace.composeapp.generated.resources.*
import gatherspace.composeapp.generated.resources.AtkinsonHyperlegible_Bold
import gatherspace.composeapp.generated.resources.AtkinsonHyperlegible_Regular
import gatherspace.composeapp.generated.resources.Carlito_Bold
import gatherspace.composeapp.generated.resources.Res
import org.jetbrains.compose.resources.Font


val atkinsonHyperLegibleFontFamily
    @Composable get() = FontFamily(
        Font(Res.font.AtkinsonHyperlegible_Bold, FontWeight.Bold),
        Font(Res.font.AtkinsonHyperlegible_Regular, FontWeight.Normal),
    )

val carlitoFontFamily
    @Composable get() = FontFamily(
        Font(Res.font.Carlito_Bold, FontWeight.Bold),
        Font(Res.font.Carlito_Regular, FontWeight.Normal),
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
            bodyLarge = bodyLarge.copy(fontFamily = carlitoFontFamily),
            bodyMedium = bodyMedium.copy(fontFamily = carlitoFontFamily),
            bodySmall = bodySmall.copy(fontFamily = carlitoFontFamily),
            labelLarge = labelLarge.copy(
                fontFamily = carlitoFontFamily,
                fontWeight = FontWeight.Bold
            ),
            labelMedium = labelMedium.copy(fontFamily = carlitoFontFamily),
            labelSmall = labelSmall.copy(fontFamily = carlitoFontFamily),
        )
    }