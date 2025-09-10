package com.example.wsspostboard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.wsspostboard.posts.ui.AppNav
import com.example.wsspostboard.posts.viewmodel.PostViewModel
import com.example.wsspostboard.ui.theme.WSSPostBoardTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContent {
                Surface(color = MaterialTheme.colorScheme.background) {
                    val vm: PostViewModel = viewModel()
                    AppNav(viewModel = vm)
                }
            }
        }
}

