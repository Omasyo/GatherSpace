package com.omasyo.gatherspace.components.layouts

import androidx.compose.runtime.Composable
import com.omasyo.gatherspace.styles.HomeLayoutStyle
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
    Style(HomeLayoutStyle)
    BaseLayout(title) {
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
                    classes(HomeLayoutStyle.contentWrapper)
                }
            ) {
                if (showSideBar) {
                    Div(
                        attrs = {
                            classes(HomeLayoutStyle.sidebar)
                            if (isDisplayingContent) {
                                classes(HomeLayoutStyle.hideOnMid)
                            }
                        }
                    ) {
                        sideBar()
                    }
                }
                Div(
                    attrs = {
                        classes(HomeLayoutStyle.content)
                        if (!isDisplayingContent) {
                            classes(HomeLayoutStyle.hideOnMid)
                        }
                    }
                ) {
                    content()
                }
            }
        }
    }
}