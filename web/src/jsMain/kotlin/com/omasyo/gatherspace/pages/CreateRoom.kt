package com.omasyo.gatherspace.pages

import androidx.compose.runtime.*
import com.omasyo.gatherspace.TextFieldState
import com.omasyo.gatherspace.components.layouts.HomeLayout
import com.omasyo.gatherspace.components.sections.Header
import com.omasyo.gatherspace.components.sections.SideBar
import com.omasyo.gatherspace.components.widgets.ImageCapture
import com.omasyo.gatherspace.components.widgets.ImageChooser
import com.omasyo.gatherspace.components.widgets.TextField
import com.omasyo.gatherspace.createroom.CreateRoomEvent
import com.omasyo.gatherspace.createroom.CreateRoomState
import com.omasyo.gatherspace.createroom.CreateRoomViewModelImpl
import com.omasyo.gatherspace.styles.lightDark
import com.omasyo.gatherspace.theme.surfaceVariantDark
import com.omasyo.gatherspace.theme.surfaceVariantLight
import com.omasyo.gatherspace.viewmodels.domainComponent
import com.varabyte.kobweb.browser.file.readBytes
import com.varabyte.kobweb.compose.css.*
import com.varabyte.kobweb.core.Page
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.launch
import kotlinx.io.Buffer
import kotlinx.io.readByteArray
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.css.AlignItems
import org.jetbrains.compose.web.css.AlignSelf.Companion.Center
import org.jetbrains.compose.web.dom.*
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.asList
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
        width(280.px)
        height(280.px)

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
    onEventReceived: (CreateRoomEvent) -> Unit,
    state: CreateRoomState
) {
    Style(CreateRoomStyle)
    val scope = rememberCoroutineScope()
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
                                Text("Choose Picture")
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
                                Text("Open Camera")
                            }
                        }
                    }
                }

                Button(attrs = {
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
            }

            is CreateRoomEvent.Error -> {
            }

            is CreateRoomEvent.Success -> {
                window.location.href = "/rooms/${event.id}"
            }

            CreateRoomEvent.None -> Unit
        }

        onEventReceived(state.event)
    }
}