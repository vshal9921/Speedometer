package com.vishal.speedometer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vishal.speedometer.ui.theme.SpeedometerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SpeedometerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainView(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun MainView(modifier: Modifier = Modifier) {

    var initialAngle by remember { mutableStateOf("") }
    var targetAngle by remember { mutableStateOf(0f) }

    val animatedAngle by animateFloatAsState(
        targetValue = targetAngle,
        animationSpec = tween(durationMillis = 500), // Smooth animation over 500ms
        label = "Pointer Animation"
    )

    Column (modifier = modifier.fillMaxSize()
        .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally){

        // with animation
        Box(modifier = Modifier
            .background(Color.White)
            .fillMaxWidth()
            .height(260.dp)
            .aspectRatio(1f)
            .graphicsLayer{
                clip = true
                translationY = 150f
            }
            ,contentAlignment = Alignment.Center
        ){

            Canvas(modifier = Modifier
                .size(200.dp)
                .aspectRatio(1f)){

                val strokeWidth = 60.dp.toPx()

                drawArc(
                    color = Color.Red,
                    startAngle = 180f,
                    sweepAngle = 180f,
                    useCenter = false,
                    style = Stroke(strokeWidth)
                )
            }

            Box (modifier = Modifier
                .height(IntrinsicSize.Max)
                .align(Alignment.Center)
                .padding(bottom = 90.dp)){

                Image(
                    painter = painterResource(id = R.drawable.ic_pointer),
                    contentDescription = "Arrow",
                    modifier = Modifier
                        .size(90.dp)
                        .align(Alignment.TopCenter)
                        .graphicsLayer(
                            rotationZ = animatedAngle - 90f,        // Rotate the arrow to the calculated angle
                            transformOrigin = TransformOrigin(0.5f, 1f)  // Pivot from the bottom-center
                        )
                )
            }
        }

        Row (horizontalArrangement = Arrangement.spacedBy(16.dp)){

            TextField(
                value = initialAngle,
                onValueChange = { newValue ->
                    val sanitizedValue = newValue.filter { it.isDigit() }
                    val floatValue = sanitizedValue.toFloatOrNull()

                    if (sanitizedValue.isEmpty() || (floatValue != null && floatValue in 0f..180f)) {
                        initialAngle = sanitizedValue // Update only valid values
                    }
                },
                textStyle = TextStyle(textAlign = TextAlign.Center),
                modifier = Modifier.width(100.dp),
                maxLines = 1,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFF5F5F5),
                    unfocusedContainerColor = Color(0xFFF5F5F5),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = Color.Blue
                )
            )

            Button(
                onClick = {
                    targetAngle = initialAngle.toFloat()
                }
            ) {
                Text(text = "Update")
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun PreviewMainView() {
    SpeedometerTheme {
        MainView()
    }
}