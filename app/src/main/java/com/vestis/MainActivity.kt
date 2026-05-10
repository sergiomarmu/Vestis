package com.vestis

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import com.vestis.navigation.AppNavigationBar
import com.vestis.ui.theme.VestisTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VestisTheme {
                VestisApp()
            }
        }
    }
}

@PreviewScreenSizes
@Composable
fun VestisApp() {
    AppNavigationBar()
}