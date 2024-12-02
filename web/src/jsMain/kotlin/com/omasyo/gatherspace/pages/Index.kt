package com.omasyo.gatherspace.pages

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.core.Page
import com.omasyo.gatherspace.components.sections.TopBar
import com.omasyo.gatherspace.components.layouts.HomeLayout
import com.omasyo.gatherspace.components.sections.RoomsView
import com.omasyo.gatherspace.components.widgets.room.RoomContent
import com.omasyo.gatherspace.models.response.Room
import com.omasyo.gatherspace.models.response.RoomDetails
import com.omasyo.gatherspace.models.response.UserDetails
import com.omasyo.gatherspace.styles.MainStyle
import com.omasyo.gatherspace.styles.lightDark
import com.omasyo.gatherspace.theme.onSurfaceVariantDark
import com.omasyo.gatherspace.theme.onSurfaceVariantLight
import com.varabyte.kobweb.compose.css.borderBottom
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import kotlinx.datetime.LocalDateTime
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.A
import org.jetbrains.compose.web.dom.H4
import org.jetbrains.compose.web.dom.Text

val date = LocalDateTime(1, 1, 1, 1, 1)

@Page
@Composable
fun HomePage(
) {
    HomeLayout(
        title = "GatherSpace",
        topBar = {
            TopBar(
                user = UserDetails(
                    id = 6555,
                    username = "Dwight Pugh",
                    imageUrl = null,
                    created = date,
                    modified = date
                )
            )
        },
        sideBar = {

            A(
                href = "/",
                attrs = {

                    classes(MainStyle.customLink)
                }) {
                H4(
                    attrs = {
                        style {
                            textAlign("center")
                            padding(8.px, 0.px)
                            borderBottom {
                                width(1.px)
                                style(LineStyle.Solid)
                                color(lightDark(onSurfaceVariantDark, onSurfaceVariantLight))
                            }
                        }
                    }
                ) {
                    Text("Join A Room")
                }
            }
            RoomsView(
                rooms = List(50) {
                    Room(id = 4030, name = "Lenore Witt", imageUrl = null)
                }
            )
        },
        isDisplayingContent = true,
    ) {
        RoomContent(
            roomDetails = RoomDetails(
                id = 1268,
                name = "Pearl Bender",
                imageUrl = null,
                creator = null,
                isMember = false,
                members = listOf(),
                created = date,
                modified = date
            )
        )
    }
}

val text =
    "It is a Kotlin class returned from a classloader*. At this point, I am assuming something is rotten in the way I am loading the class. (One of my big gripes about Kotlin is the lack of classloader that accepts native Kotlin semantics.)\n\n" +
            "A URLClassLoader based on the standard parent classloader (which searches a “fat jar” containing the classes in my app plus the standard Kotlin runtime plus the Kotlin reflection runtime), plus an additional directory where the class being loaded resides in."
