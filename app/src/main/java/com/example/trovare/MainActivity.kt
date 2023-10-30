package com.example.trovare

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.trovare.ui.theme.TrovareTheme


class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            TrovareTheme {
                // A surface container using the 'background' color from the theme
                Trovare()
        }
    }
}
}

@Preview
@Composable
fun previewApp(){
    TrovareTheme {
        Trovare()
    }
}



