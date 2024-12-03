package com.omasyo.gatherspace.components.sections

import androidx.compose.runtime.Composable
import com.omasyo.gatherspace.models.response.Room
import com.omasyo.gatherspace.styles.MainStyle
import com.omasyo.gatherspace.styles.lightDark
import com.omasyo.gatherspace.theme.onSurfaceVariantDark
import com.omasyo.gatherspace.theme.onSurfaceVariantLight
import com.varabyte.kobweb.compose.css.CSSFloat
import com.varabyte.kobweb.compose.css.borderBottom
import com.varabyte.kobweb.compose.css.float
import com.varabyte.kobweb.compose.css.left
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.A
import org.jetbrains.compose.web.dom.H4
import org.jetbrains.compose.web.dom.Text

@Composable
fun SideBar() {
    RoomsView(
        rooms = List(50) {
            Room(id = 4030, name = "Lenore Witt", imageUrl = null)
        }
    )
}