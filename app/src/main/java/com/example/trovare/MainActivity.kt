package com.example.trovare

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.example.trovare.ui.theme.TrovareTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TrovareTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = Color.Red) {
                    FAQS()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FAQS(modifier: Modifier = Modifier) {
    Scaffold(
        topBar = {
            barraSuperior()
        }
    ) { it ->
        LazyColumn(contentPadding = it){
            item { cuerpoFAQS() }
        }
    }


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun barraSuperior(modifier: Modifier = Modifier) {
    CenterAlignedTopAppBar(
        modifier = modifier.fillMaxWidth(),
        title = {
            Surface{
                Row {
                    Text(text = "Trovare")
                }
            }
        }
    )
}

@Composable
fun cuerpoFAQS(modifier: Modifier = Modifier){
    Text(
        modifier = modifier.fillMaxWidth(),
        textAlign = TextAlign.Center,
        text = "FAQs"
    )
    Divider()
}




@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GreetingPreview() {
    TrovareTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = Color.Red) {
            FAQS()
        }

    }
}