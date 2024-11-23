package org.example.app.pages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.omasyo.gatherspace.models.response.Room
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.core.Page
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.Text


@Page
@Composable
fun HomePage(
    rooms: List<Room> = roomsFlow.collectAsState(emptyList()).value
) {

    // TODO: Replace the following with your own content
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("THIS PAGE INTENTIONALLY LEFT BLANK")

        Box(
            Modifier
                .padding(topBottom = 5.px, leftRight = 30.px)
                .border(1.px, LineStyle.Solid, Colors.Black)
        ) {
            Text("WELCOME!!")
        }

        Row {
            Box(
                Modifier
                    .borderLeft {
                        style(LineStyle.Solid)
                        width(10.px)
                        color(Color.blue)
                    }.size(20.px)
            )
            Box(Modifier.background(Color.green).size(20.px))
            Box(Modifier.background(Color.blue).size(20.px))
            for (room in rooms) {
                Text(room.name)
            }
        }
    }
}
