package com.example.loteria

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.loteria.ui.theme.LoteriaTheme

val secretNumber = randomNumber()

class MainActivity() : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            LoteriaTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                   navigation()

                }
            }
        }
    }
}

@Composable
fun juego(navController: NavHostController) {

    //titulo
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()){
        Text(text = "Lotería Mondongo",Modifier.padding(bottom = 300.dp), style = MaterialTheme.typography.body2)
        //fin titulo

        //numero random y boton
        var visible by remember {
            mutableStateOf(false)
        }
        Button(onClick = { visible = !visible }, modifier = Modifier.padding(end = 175.dp)) { Text("Toggle") }
        AnimatedVisibility(visible = visible, enter = fadeIn(initialAlpha = 0.4f),
            exit = fadeOut(animationSpec = tween(durationMillis = 250))) {
            Text(text = secretNumber, style = TextStyle(fontWeight = FontWeight.Bold), textAlign = TextAlign.Center,
                fontSize = 28.sp)
        }
        //fin numero random y boton

        //cuadro de texto
        var text by remember { mutableStateOf(TextFieldValue("0")) }
        val guess by remember {  mutableStateOf(text.text.toInt()) }
        val maxChar = 5
        OutlinedTextField(
            value = text,
            label = { Text(text = "Escribe un numero") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            onValueChange = {
                if(it.text.length<=maxChar) text = it
            },
            keyboardActions = KeyboardActions(onDone = {if (text.text.toInt() == secretNumber.toInt()){navController.navigate("Acierto") }
            else navController.navigate("Fallo") }),
            singleLine = true,
            modifier = Modifier.padding(top = 110.dp)
        )
        Text(
            text = "${text.text.length} / $maxChar",
            textAlign = TextAlign.End,
            style = MaterialTheme.typography.caption,
            modifier = Modifier.padding(top = 185.dp, start = 240.dp)
        )
    }
    //fin cuadro de texto
    }

fun randomNumber():String{
    fun rand(start: Int, end: Int): Int {
        require(start <= end) { "Illegal Argument" }
        return (Math.random() * (end - start + 1)).toInt() + start
    }
    var numString = rand(0,99999).toString()
    while (numString.length<5){
        numString= "0$numString"
    }
    return numString
}

fun Comparar(secret:Int,guess:Int):Boolean{
    return secret == guess
}

@Composable
fun acierto(){
    Box(contentAlignment = Alignment.Center) {
        Text(text = "has acertado")
    }
}

@Composable
fun fallo(){
    Row() {
        Text(text = "has fallado")
    }
}

@Composable
fun navigation(){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "main") {
        composable("main") {
            juego(navController)
        }
        composable("Acierto"){
            acierto()
        }
        composable("Fallo"){
            fallo()
        }
    }
}

