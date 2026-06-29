package org.example.gcode

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {
        CodeConverterScreen()
    }
}

@Composable
fun CodeConverterScreen() {
    var inputValue by remember { mutableStateOf("") }
    var resultValue by remember { mutableStateOf("") }

    // ئەنیمەیشنی لەرینەوەی ناونیشانەکە بە شێوازی هێواش
    val infiniteTransition = rememberInfiniteTransition(label = "shake")
    val shakeOffset by infiniteTransition.animateFloat(
        initialValue = -1.5f,
        targetValue = 1.5f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 600, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "shakeOffset"
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "DEV By: GAILAN ABDULLA",
                    color = Color.Red,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(80.dp))

            // ناونیشانی ئەپەکە لەگەڵ سێبەر و ئەنیمەیشنەکەی
            Text(
                text = "GALIAWA CODE",
                style = TextStyle(
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF10042E),
                    shadow = Shadow(
                        color = Color.Black.copy(alpha = 0.3f),
                        offset = Offset(4f, 4f),
                        blurRadius = 8f
                    )
                ),
                modifier = Modifier
                    .padding(bottom = 60.dp)
                    .offset(x = shakeOffset.dp)
            )

            Text(
                text = "کۆد بنوسە",
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.DarkGray
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                textAlign = TextAlign.Center
            )

            OutlinedTextField(
                value = inputValue,
                onValueChange = { input ->
                    inputValue = input
                    val upperInput = input.uppercase().trim()
                    
                    val n = upperInput.toLongOrNull()
                    if (n != null && n > 0) {
                        resultValue = convertNumberToStringCode(n)
                    } else {
                        val resultNum = convertStringCodeToNumber(upperInput)
                        if (resultNum != null) {
                            resultValue = resultNum.toString()
                        } else {
                            resultValue = ""
                        }
                    }
                },
                textStyle = TextStyle(
                    fontWeight = FontWeight.Bold, 
                    fontSize = 20.sp, 
                    textAlign = TextAlign.Center
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = MaterialTheme.shapes.medium
            )

            Spacer(modifier = Modifier.height(32.dp))

            // بۆکسی نیشاندانی ئەنجامەکە (Output Box)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp),
                contentAlignment = Alignment.Center
            ) {
                OutlinedTextField(
                    value = resultValue,
                    onValueChange = {},
                    readOnly = true,
                    textStyle = TextStyle(
                        fontWeight = FontWeight.Black, 
                        fontSize = 80.sp, 
                        color = Color(0xFF10042E),
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier.fillMaxSize(),
                    singleLine = true,
                    shape = MaterialTheme.shapes.medium,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF10042E),
                        unfocusedBorderColor = Color.Gray,
                    )
                )
            }
        }
    }
}

// لۆژیکی گۆڕینی ژمارە بۆ دەق
fun convertNumberToStringCode(n: Long): String {
    if (n <= 0) return ""
    val group = (n - 1) / 26 + 1
    val charIndex = ((n - 1) % 26).toInt()
    val char = ('A'.codeUnitAt(0) + charIndex).toChar()
    return "$group$char"
}

// لۆژیکی گۆڕینی دەق بۆ ژمارە - چارەسەرکراو
fun convertStringCodeToNumber(code: String): Long? {
    if (code.isEmpty()) return null
    
    // دوایین کاراکتەر لیتر دەبێت (A-Z)
    val lastChar = code.last()
    if (lastChar !in 'A'..'Z') return null
    
    // بقیی بەشی دەبێت ژمارە (digits)
    val numberPart = code.dropLast(1)
    if (numberPart.isEmpty() || !numberPart.all { it.isDigit() }) return null
    
    val group = numberPart.toLongOrNull() ?: return null
    if (group <= 0) return null
    
    val charValue = (lastChar - 'A' + 1).toLong()
    return (group - 1) * 26 + charValue
}
