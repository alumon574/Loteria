package com.example.loteria

import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
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
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.request.ImageRequest
import com.example.loteria.ui.theme.LoteriaTheme
import java.nio.file.Files.size
import android.util.Size as Size1

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
                   Navigation()

                }
            }
        }
    }
}

@Composable
fun Aplicacion(navController: NavHostController) {

    //titulo
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()){
        Text(text = "Lotería Mondongo",Modifier.padding(bottom = 300.dp), style = MaterialTheme.typography.body2)
        //fin titulo

        //numero random y boton
        var visible by remember {
            mutableStateOf(false)
        }
        Button(onClick = { visible = !visible }, modifier = Modifier.padding(end = 175.dp)) { Text("Show/\nHide") }
        AnimatedVisibility(visible = visible, enter = fadeIn(initialAlpha = 0.4f),
            exit = fadeOut(animationSpec = tween(durationMillis = 250))) {
            Text(text = secretNumber, style = TextStyle(fontWeight = FontWeight.Bold), textAlign = TextAlign.Center,
                fontSize = 28.sp)
        }
        //fin numero random y boton

        //cuadro de texto
        var text by remember { mutableStateOf(TextFieldValue("")) }
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
    numString=rellenaCifras(numString)
    return numString
}

fun rellenaCifras(numero:String):String{
    var numero=numero
    while (numero.length<5){
        numero= "0$numero"
    }
    return numero
}

@Composable
fun Acierto(){
    val context = LocalContext.current
    Box(contentAlignment = Alignment.Center,
    modifier = Modifier.fillMaxSize()){
        Text(text = "Has acertado", fontSize = 24.sp)
        Text(text = "Has ganado 5000 forintos hungaros", fontSize = 20.sp, modifier = Modifier.padding(top=75.dp))
        val musica = MediaPlayer.create(context,R.raw.acierto)
        musica.start()
        Image(painter = painterResource(id = R.drawable.david), contentDescription = null,
            modifier = Modifier.padding(bottom = 275.dp), contentScale = ContentScale.Fit)

    }
}

@Composable
fun Fallo(){
    val context = LocalContext.current
    val musicaFallo:MediaPlayer = MediaPlayer.create(context,R.raw.fallo)
    Box(contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()) {
        Text(text = "Has fallado", fontSize = 24.sp)
        Text(text = "El numero ganador es $secretNumber", fontSize = 20.sp, modifier = Modifier.padding(top = 75.dp))
        musicaFallo.start()
        Image(painter = painterResource(id = R.drawable.cabezafallo), contentDescription = null, modifier = Modifier.padding(top = 125.dp))
    }
}

@Composable
fun Navigation(){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "main") {
        composable("main") {
            Aplicacion(navController)
        }
        composable("Acierto"){
            Acierto()
        }
        composable("Fallo"){
            Fallo()
        }
    }
}
