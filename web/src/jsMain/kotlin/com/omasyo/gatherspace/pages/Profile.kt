package com.omasyo.gatherspace.pages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.omasyo.gatherspace.components.widgets.Image
import com.omasyo.gatherspace.components.widgets.ImageCapture
import com.omasyo.gatherspace.components.widgets.ImageChooser
import com.omasyo.gatherspace.components.widgets.SessionCard
import com.omasyo.gatherspace.domain.formatDate
import com.omasyo.gatherspace.domain.formatDateTime
import com.omasyo.gatherspace.models.response.UserDetails
import com.omasyo.gatherspace.models.response.UserSession
import com.omasyo.gatherspace.profile.ProfileScreenEvent
import com.omasyo.gatherspace.profile.ProfileScreenState
import com.omasyo.gatherspace.profile.ProfileViewModelImpl
import com.omasyo.gatherspace.styles.lightDark
import com.omasyo.gatherspace.theme.onSurfaceVariantDark
import com.omasyo.gatherspace.theme.onSurfaceVariantLight
import com.omasyo.gatherspace.viewmodels.domainComponent
import com.varabyte.kobweb.browser.file.readBytes
import com.varabyte.kobweb.compose.css.*
import com.varabyte.kobweb.core.Page
import kotlinx.browser.document
import kotlinx.coroutines.launch
import kotlinx.io.Buffer
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.css.AlignSelf.Companion.Center
import org.jetbrains.compose.web.dom.*
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.asList

val profileViewModel = ProfileViewModelImpl(domainComponent.authRepository, domainComponent.userRepository)

private object ProfilePageStyle : StyleSheet() {
    val imagePicker by style {
        display(DisplayStyle.Flex)
        flexDirection(FlexDirection.Column)
        gap(8.px)
    }

    val image by style {
        width(184.px)
        height(184.px)
        marginRight(16.px)
        objectFit(ObjectFit.Cover)
    }

    val imagePickerButtons by style {
        display(DisplayStyle.Flex)
        gap(8.px)
        justifyContent(com.varabyte.kobweb.compose.css.JustifyContent.Center)
    }

    init {
        keyframes {  }
    }
}

@Page
@Composable
fun ProfilePage() {
    ProfilePage(
        userDetails = profileViewModel.userDetails.collectAsState().value,
        sessions = profileViewModel.userSessions.collectAsState().value,
        updateImage = profileViewModel::updateImage,
        onLogoutTap = profileViewModel::logout,
        onSessionLogoutTap = profileViewModel::logoutSession,
        onEventReceived = profileViewModel::onEventReceived,
        state = profileViewModel.state.collectAsState().value,
    )
}

@Composable
fun ProfilePage(
    userDetails: UserDetails?,
    sessions: List<UserSession>,
    updateImage: (Buffer) -> Unit,
    onLogoutTap: () -> Unit,
    onSessionLogoutTap: (UserSession) -> Unit,
    onEventReceived: (ProfileScreenEvent) -> Unit,
    state: ProfileScreenState
) {
    Style(ProfilePageStyle)
    Div(attrs = {
        style {
            width(100.percent)
            height(100.vh)
        }
    }) {
        Div(attrs = {
            style {
                maxWidth(800.px)
                height(100.percent)
                borderLeft(1.px, LineStyle.Solid, lightDark(onSurfaceVariantLight, onSurfaceVariantDark, 0.4f))
                borderRight(1.px, LineStyle.Solid, lightDark(onSurfaceVariantLight, onSurfaceVariantDark, 0.4f))
                property("margin", "auto")
                padding(16.px)
            }
        }) {

            Header(
                attrs = {
                    style {
                        paddingBottom(20.px)
                    }
                }
            ) {
                A(
                    href = "/",
                ) {
                    Img(src = "/image/GatherSpace.svg", attrs = {
                        id("logo")
                    })
                }
            }
            Div(
                attrs = {

                }
            ) {
                Div(
                    attrs = {
                        style {
                            display(DisplayStyle.Flex)
                        }
                    }
                ) {

                    Div(
                        attrs = {
                            classes(ProfilePageStyle.imagePicker)
                        }
                    ) {
                        Image(userDetails?.imageUrl, "/image/user_placeholder.svg", attrs = {

                            classes(ProfilePageStyle.image)
                        })

                        Div(
                            attrs = {
                                classes(ProfilePageStyle.imagePickerButtons)
                            }
                        ) {
                            ImageChooser(onComplete = updateImage) {
                                Button(attrs = {
                                    onClick {
                                        chooseImage()
                                    }
                                }) {
                                    Text("Choose Picture")
                                }
                            }
                            ImageCapture(
                                onComplete = updateImage
                            ) {
                                Button(attrs = {
                                    onClick {
                                        openCamera()
                                    }
                                }) {
                                    Text("Open Camera")
                                }
                            }
                        }
                    }
                    Div {
                        H2 {
                            Text(userDetails?.username ?: "")
                        }
                        P {
                            Text("Created: ${userDetails?.created?.formatDateTime() ?: ""}")
                        }
                    }
                }
            }

            H2(
                attrs = {
                    style {
                        paddingTop(24.px)
                        paddingBottom(16.px)
                    }
                }
            ) {
                Text("Active Sessions")
            }

            for (session in sessions) {
                SessionCard(
                    sessionDetails = session,
                    onLogoutTap = {
                        onSessionLogoutTap(session)
                    },
                )
            }

            Button(attrs = {
                onClick { onLogoutTap() }
                style {
                    marginTop(20.px)
                    width(100.percent)
                }
            }) {
                Text("Logout")
            }
        }
    }


    LaunchedEffect(state) {
//        when (val event = state.event) {
//            ProfileScreenEvent.AuthError -> {
//                snackbarHostState.showSnackbar("Auth error")
//            }
//
//            is ProfileScreenEvent.Error -> {
//                snackbarHostState.showSnackbar(event.message)
//            }
//
//            ProfileScreenEvent.ImageUpdated -> {
//                snackbarHostState.showSnackbar("Image updated")
//            }
//
//            ProfileScreenEvent.Logout -> {
//                onBackTap()
//            }
//
//            is ProfileScreenEvent.SessionLogout -> {
//                snackbarHostState.showSnackbar("Logout of device successful")
//            }
//
//            ProfileScreenEvent.None -> Unit
//        }
        onEventReceived(state.event)
    }
}