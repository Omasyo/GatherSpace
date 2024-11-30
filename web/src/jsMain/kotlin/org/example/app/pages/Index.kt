package org.example.app.pages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import com.varabyte.kobweb.compose.css.height
import com.varabyte.kobweb.compose.css.width
import com.varabyte.kobweb.compose.foundation.layout.*
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.silk.components.icons.mdi.IconStyle
import com.varabyte.kobweb.silk.components.icons.mdi.MdiAdd
import com.varabyte.kobweb.silk.theme.SilkTheme
import com.varabyte.kobweb.silk.theme.colors.palette.border
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.borderRadius
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.*


@Page
@Composable
fun HomePage(
) {
    Column(Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .padding(leftRight = 16.px, topBottom = 8.px)
                .heightIn(48.px)
                .fillMaxWidth()
                .borderBottom(
                    width = 1.px,
                    style = LineStyle.Solid,
                    color = SilkTheme.palette.border
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Img(src = "image/GatherSpace.svg", attrs = {
                height(32)
            })
            Spacer()
            A(
                href = "create-room",
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text("Create Room")
                    MdiAdd()
                }
            }
            A(
                href = "profile",
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text("Username")
                    Img(src = null ?: "image/user_placeholder.svg", attrs = {
                        height(40)
                        width(40)
                        style {
                            borderRadius(8.px)
                        }
                    })
                }
            }
        }
        Row(
            Modifier.weight(1)
        ) {
            Box(
                Modifier
                    .fillMaxHeight()
                    .width(400.px)
                    .borderRight(
                        width = 1.px,
                        style = LineStyle.Solid,
                        color = SilkTheme.palette.border
                    )
            ) {
            }
            val splitText = derivedStateOf { text.split('\n') }
            Box(Modifier.weight(1f).fillMaxHeight()) {
                Span {
                    splitText.value.forEach {
                        P {
                            Text(it)
                        }
                    }
                }
            }
        }
    }
}

val text =
    "It is a Kotlin class returned from a classloader*. At this point, I am assuming something is rotten in the way I am loading the class. (One of my big gripes about Kotlin is the lack of classloader that accepts native Kotlin semantics.)\n\n" +
            "A URLClassLoader based on the standard parent classloader (which searches a “fat jar” containing the classes in my app plus the standard Kotlin runtime plus the Kotlin reflection runtime), plus an additional directory where the class being loaded resides in."
