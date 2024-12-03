package com.omasyo.gatherspace.components.layouts

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.omasyo.gatherspace.styles.LayoutStyle
import kotlinx.browser.document
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.Div


@Composable
fun HomeLayout(
    title: String,
    topBar: @Composable () -> Unit,
    sideBar: @Composable () -> Unit,
    isDisplayingContent: Boolean,
    showSideBar: Boolean = true,
    content: @Composable () -> Unit,
) {
    Style(LayoutStyle)
    LaunchedEffect(title) {
        document.title = title
    }
    Div(
        attrs = {
            style {
                width(100.percent)
                height(100.vh)
            }
        }
    ) {
        topBar()
        Div(
            attrs = {
                classes(LayoutStyle.contentWrapper)
            }
        ) {
            if (showSideBar) {
                Div(
                    attrs = {
                        classes(LayoutStyle.sidebar)
                        if (isDisplayingContent) {
                            classes(LayoutStyle.hideOnMid)
                        }
                    }
                ) {
                    sideBar()
                }
            }
            Div(
                attrs = {
                    classes(LayoutStyle.content)
                    if (!isDisplayingContent) {
                        classes(LayoutStyle.hideOnMid)
                    }
                }
            ) {
                content()
            }
        }
    }
}