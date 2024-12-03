package com.omasyo.gatherspace.pages

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.core.Page
import com.omasyo.gatherspace.components.sections.Header
import com.omasyo.gatherspace.components.layouts.HomeLayout
import com.omasyo.gatherspace.components.sections.RoomsGrid
import com.omasyo.gatherspace.components.sections.SideBar
import com.omasyo.gatherspace.models.response.Room
import kotlinx.datetime.LocalDateTime

val date = LocalDateTime(1, 1, 1, 1, 1)

@Page
@Composable
fun HomePage(
) {
    HomeLayout(
        title = "GatherSpace",
        topBar = {
            Header()
        },
        sideBar = {
            SideBar()
        },
        isDisplayingContent = false,
    ) {
        RoomsGrid(
            rooms = List(54) {
                Room(id = 2737, name = "Kim Merritt", imageUrl = null)
            }
        )
    }
}

val text =
    "It is a Kotlin class returned from a classloader*. At this point, I am assuming something is rotten in the way I am loading the class. (One of my big gripes about Kotlin is the lack of classloader that accepts native Kotlin semantics.)\n\n" +
            "A URLClassLoader based on the standard parent classloader (which searches a “fat jar” containing the classes in my app plus the standard Kotlin runtime plus the Kotlin reflection runtime), plus an additional directory where the class being loaded resides in."
