package com.omasyo.gatherspace

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.omasyo.gatherspace.network.message.Tospi
import com.omasyo.gatherspace.ui.theme.GatherSpaceTheme

val js = Tospi()

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        js.jsConnect()

        enableEdgeToEdge()
        setContent {
            GatherSpaceTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    var message by remember { mutableStateOf("") }
                    Column {
                        TextField(message, onValueChange = { message = it })
                        Button(onClick = { js.jsSendMessage(message) }) { Text("Submit") }
                    }
                }
            }
        }
    }
}