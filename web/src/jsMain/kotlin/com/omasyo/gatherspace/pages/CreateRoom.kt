package com.omasyo.gatherspace.pages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import com.omasyo.gatherspace.TextFieldState
import com.omasyo.gatherspace.components.layouts.HomeLayout
import com.omasyo.gatherspace.components.layouts.showSnackbar
import com.omasyo.gatherspace.components.sections.Header
import com.omasyo.gatherspace.components.sections.SideBar
import com.omasyo.gatherspace.components.widgets.ImageCapture
import com.omasyo.gatherspace.components.widgets.ImageChooser
import com.omasyo.gatherspace.components.widgets.TextField
import com.omasyo.gatherspace.createroom.CreateRoomEvent
import com.omasyo.gatherspace.createroom.CreateRoomState
import com.omasyo.gatherspace.createroom.CreateRoomViewModelImpl
import com.omasyo.gatherspace.styles.MainStyle
import com.omasyo.gatherspace.styles.lightDark
import com.omasyo.gatherspace.theme.surfaceVariantDark
import com.omasyo.gatherspace.theme.surfaceVariantLight
import com.omasyo.gatherspace.viewmodels.domainComponent
import com.varabyte.kobweb.compose.css.*
import com.varabyte.kobweb.core.Page
import kotlinx.browser.window
import kotlinx.io.Buffer
import kotlinx.io.readByteArray
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.css.AlignItems
import org.jetbrains.compose.web.css.AlignSelf.Companion.Center
import org.jetbrains.compose.web.dom.*
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

val createRoomViewModel = CreateRoomViewModelImpl(domainComponent.roomRepository)

object CreateRoomStyle : StyleSheet() {
    val createRoomPage by style {
        justifyContent(com.varabyte.kobweb.compose.css.JustifyContent.Center)
        alignContent(org.jetbrains.compose.web.css.AlignContent.Center)
        alignItems(AlignItems.Center)
        justifyItems(JustifyItems.Center)
    }

    val createRoomForm by style {
        display(DisplayStyle.Flex)
        flexDirection(FlexDirection.Column)
        gap(8.px)

        width(100.percent)
        padding(16.px)
        maxWidth(540.px)
    }

    val imagePicker by style {
        display(DisplayStyle.Flex)
        flexDirection(FlexDirection.Column)
        gap(8.px)
    }

    val image by style {
        backgroundColor(lightDark(surfaceVariantLight, surfaceVariantDark))
        width(264.px)
        height(264.px)

        alignSelf(Center)
    }

    val imagePickerButtons by style {
        display(DisplayStyle.Flex)
        gap(8.px)
        justifyContent(com.varabyte.kobweb.compose.css.JustifyContent.Center)
    }
}

@Page
@Composable
fun CreateRoomPage() {
    CreateRoomPage(
        roomName = createRoomViewModel.nameField,
        onRoomNameChange = createRoomViewModel::changeName,
        description = createRoomViewModel.descriptionField,
        onDescriptionChange = createRoomViewModel::changeDescription,
        image = createRoomViewModel.image,
        setImage = createRoomViewModel::updateImage,
        onSubmit = createRoomViewModel::submit,
        onAuthError = profileViewModel::logout,
        onEventReceived = createRoomViewModel::onEventReceived,
        state = createRoomViewModel.state.collectAsState().value
    )
}

@OptIn(ExperimentalEncodingApi::class)
@Composable
fun CreateRoomPage(
    roomName: TextFieldState,
    onRoomNameChange: (String) -> Unit,
    description: TextFieldState,
    onDescriptionChange: (String) -> Unit,
    image: Buffer?,
    setImage: (Buffer) -> Unit,
    onSubmit: () -> Unit,
    onAuthError: () -> Unit,
    onEventReceived: (CreateRoomEvent) -> Unit,
    state: CreateRoomState
) {
    Style(CreateRoomStyle)
    HomeLayout(
        title = "GatherSpace - Create Room",
        topBar = {
            Header()
        },
        sideBar = {
            SideBar()
        },
        isDisplayingContent = true,
    ) {
        Div(
            attrs = {
                classes(CreateRoomStyle.createRoomPage)
            }
        ) {
            Div(
                attrs = {
                    classes(CreateRoomStyle.createRoomForm)
                }
            ) {
                H1 {
                    Text("Create Room")
                }
                TextField(
                    value = roomName.value,
                    onValueChange = onRoomNameChange,
                    placeholder = "Enter room name",
                )

                TextField(
                    value = description.value,
                    onValueChange = onDescriptionChange,
                    placeholder = "Enter description",
                    singleLine = false,
                    rows = 3
                )

                Div(
                    attrs = {
                        classes(CreateRoomStyle.imagePicker)
                    }
                ) {
                    Div(attrs = {
                        classes(CreateRoomStyle.image)
                    }) {
                        if (image != null) {
                            Img(
                                src = remember(image) {
                                    "data:image/png;base64," + Base64.encode(image.copy().readByteArray())
                                },
                                attrs = {
                                    style {
                                        width(100.percent)
                                        height(100.percent)
                                        objectFit(ObjectFit.Cover)
                                    }
                                })
                        }
                    }

                    Div(
                        attrs = {
                            classes(CreateRoomStyle.imagePickerButtons)
                        }
                    ) {
                        ImageChooser(onComplete = setImage) {
                            Button(attrs = {
                                onClick {
                                    chooseImage()
                                }
                            }) {
                                Text("Upload")
                            }
                        }
                        ImageCapture(
                            onComplete = setImage
                        ) {
                            Button(attrs = {
                                onClick {
                                    openCamera()
                                }
                            }) {
                                Text("Camera")
                            }
                        }
                    }
                }

                Button(attrs = {
                    classes(MainStyle.filledButton)
                    onClick {
                        onSubmit()
                    }
                }) {
                    Text("Submit")
                }
            }
        }
    }



    LaunchedEffect(state) {
        when (val event = state.event) {
            CreateRoomEvent.AuthError -> {
                showSnackbar("Authentication Error")
                onAuthError()
            }

            is CreateRoomEvent.Error -> {
                showSnackbar(event.message)
            }

            is CreateRoomEvent.Success -> {
                window.location.href = "/rooms/${event.id}"
            }

            CreateRoomEvent.None -> Unit
        }

        onEventReceived(state.event)
    }
}