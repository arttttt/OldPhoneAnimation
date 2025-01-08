package com.arttttt.oldphoneanimation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arttttt.oldphoneanimation.ui.theme.OldPhoneAnimationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OldPhoneAnimationTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        contentAlignment = Alignment.Center,
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            var phoneNumber by remember {
                                mutableStateOf("")
                            }

                            Text(
                                modifier = Modifier
                                    .align(Alignment.Start)
                                    .padding(
                                        horizontal = 16.dp,
                                    ),
                                text = "Phone number: $phoneNumber",
                                style = MaterialTheme.typography.headlineLarge,
                            )

                            OldPhone(
                                modifier = Modifier.padding(innerPadding),
                                onDigitSelected = { digit ->
                                    phoneNumber += digit
                                },
                            )

                            Button(
                                onClick = {
                                    phoneNumber = ""
                                }
                            ) {
                                Text(
                                    text = "Clear phone number",
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}