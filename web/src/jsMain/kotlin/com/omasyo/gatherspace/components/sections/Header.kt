package com.omasyo.gatherspace.components.sections

import androidx.compose.runtime.Composable
import com.omasyo.gatherspace.components.widgets.Image
import com.omasyo.gatherspace.models.response.UserDetails
import com.omasyo.gatherspace.pages.date
import com.omasyo.gatherspace.styles.LayoutStyle
import com.omasyo.gatherspace.styles.MainStyle
import com.omasyo.gatherspace.styles.lightDark
import com.omasyo.gatherspace.theme.primaryDark
import com.omasyo.gatherspace.theme.primaryLight
import com.varabyte.kobweb.silk.components.icons.mdi.MdiAdd
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*

object HeaderStyle : StyleSheet() {
    init {
        type("header") style {
            display(DisplayStyle.Flex)
            flexDirection(FlexDirection.Row)
            justifyContent(JustifyContent.SpaceBetween)
            alignContent(AlignContent.Center)
            alignItems(AlignItems.Center)
        }
        id("create-room") style {
            display(DisplayStyle.Flex)
            alignItems(AlignItems.Center)
            flexDirection(FlexDirection.Row)
            height(100.percent)
        }
        id("user-profile") style {
            display(DisplayStyle.Flex)
            alignItems(AlignItems.Center)
            flexDirection(FlexDirection.Row)
            marginLeft(8.px)
            marginRight(4.px)
        }
        id("user-image") style {
            marginLeft(8.px)
        }
    }
}

@Composable
fun Header() {
    Header(
        user = null
    )
}

@Composable
private fun Header(
    user: UserDetails?,
) {
    Style(HeaderStyle)
    Header(
        attrs = {
            classes(LayoutStyle.header)
        }
    ) {
        A(
            href = "/",
        ) {
            Img(src = "/image/GatherSpace.svg", attrs = {
                id("logo")
            })
        }
        Div(
            attrs = {
                style {
                    display(DisplayStyle.Flex)
                    flexDirection(FlexDirection.Row)
                }
            }
        ) {
            if (user != null) {
                A(
                    href = "create-room"
                ) {
                    Div(
                        attrs = {
                            id("create-room")
                            classes(MainStyle.clickable)
                        },
                    ) {
                        Div(
                            attrs = {
                                classes(LayoutStyle.hideOnSmall, MainStyle.center)
                            }
                        ) {
                            Text("Create Room")
                        }
                        MdiAdd()
                    }
                }
                A(
                    href = "profile",
                ) {
                    Div(
                        attrs = {
                            id("user-profile")
                            style {
                            }
                            classes(MainStyle.customLink)
                        },
                    ) {
                        Span(
                            attrs = {
                                classes(LayoutStyle.hideOnMid)
                            }
                        ) {
                            Text(user.username)
                        }
                        Image(
                            user.imageUrl,
                            placeholder = "/image/user_placeholder.svg",
                            size = 40,
                            attrs = {
                                id("user-image")
                            }
                        )
                    }
                }
            } else {

                H3(
                    attrs = {
                        classes(MainStyle.customLink)
                    }
                ) {
                    A(
                        href = "/login",
                    ) {
                        Span(
                            attrs = {
                                style {
                                    color(lightDark(primaryLight, primaryDark))
                                    paddingRight(8f.px)
                                }
                            }
                        ) {
                            Text("Login")
                        }

                    }
                    A(
                        href = "/signup",
                    ) {
                        Span {
                            Text("Register")
                        }
                    }
                }
            }
        }
    }
}