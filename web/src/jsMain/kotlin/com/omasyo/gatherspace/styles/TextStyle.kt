package com.omasyo.gatherspace.styles
//
//import com.omasyo.gatherspace.theme.*
//import com.varabyte.kobweb.compose.css.FontWeight
//import com.varabyte.kobweb.compose.css.TextDecorationLine
//import com.varabyte.kobweb.compose.ui.Modifier
//import com.varabyte.kobweb.compose.ui.modifiers.*
//import com.varabyte.kobweb.silk.style.CssStyle
//import com.varabyte.kobweb.silk.style.selectors.active
//import com.varabyte.kobweb.silk.style.selectors.hover
//import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
//import org.jetbrains.compose.web.css.*
//
//val TextStyle = CssStyle {
//    cssRule("*") {
//        Modifier.font {
//            family("Carlito", "serif")
//        }
//    }
//    cssRule("h1,h2,h3,h4,h5,h6") {
//        Modifier.font {
//            family("Atkinson Hyperlegible", "serif")
//            weight(FontWeight.Bold)
//        }
//    }
//    cssRule("h1") {
//        Modifier.margin {
//            topBottom(8.px)
//        }.fontSize(1.8.cssRem)
//    }
//    cssRule("a:any-link") {
//        Modifier.textDecorationLine(TextDecorationLine.None)
//            .color(if (colorMode.isDark) onSurfaceDark else onSurfaceLight)
//    }
//    cssRule("input") {
//        Modifier.width(100.percent)
//    }
//}
//
//val CustomLinkStyle = CssStyle {
//    hover {
//        Modifier.textDecorationLine(TextDecorationLine.Underline)
//    }
//}
//
//val ClickableStyle = CssStyle {
//    base {
//        Modifier.borderRadius(32.px)
//            .padding(leftRight = 12.px, topBottom = 4.px)
//    }
//    hover {
//        Modifier.background(
//            color = (if (colorMode.isDark) onSurfaceDark else onSurfaceLight).copyf(alpha = 0.2f)
//        )
//    }
//    active {
//        Modifier.background(
//            color = (if (colorMode.isDark) onSurfaceDark else onSurfaceLight).copyf(alpha = 0.4f)
//        )
//    }
//}
//
//val FilledButtonStyle = CssStyle {
//    base {
//        Modifier.borderRadius(32.px)
//            .background(if (colorMode.isDark) primaryContainerDark else primaryContainerLight)
//            .color(if (colorMode.isDark) onPrimaryContainerDark else onPrimaryContainerDark)
//            .border {
//                style(LineStyle.None)
//            }
//            .padding(leftRight = 12.px, topBottom = 8.px)
//    }
//}