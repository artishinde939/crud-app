@file:Suppress("DEPRECATION")

package com.example.wsspostboard.posts.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import com.example.wsspostboard.ui.theme.Blue
import com.example.wsspostboard.ui.theme.montserratFontFamily

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun DeletePopup( onDismissRequest: () -> Unit, onDelete: () -> Unit) {
    BasicAlertDialog(
        onDismissRequest = { onDismissRequest() },
        //            properties = TODO()
        modifier = Modifier.fillMaxWidth().background(Color.White,shape = RoundedCornerShape(5.dp)),
        properties = DialogProperties(), content = {
            Column(
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Are you sure?",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold,fontSize = 15.sp),
                    textAlign = TextAlign.Center,
                    fontFamily = montserratFontFamily,
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    text = "Are you sure you want to delete the post",
                    modifier = Modifier.fillMaxWidth(0.6f),
                    style = MaterialTheme.typography.bodyMedium.copy(color = Color.Black,fontSize = 12.sp, lineHeight = 17.sp),
                    textAlign = TextAlign.Center,fontFamily = montserratFontFamily,fontWeight = FontWeight.Medium
                )

                Spacer(Modifier.height(21.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(
                        12.dp,
                        Alignment.CenterHorizontally
                    )
                ) {
                    // YES (outlined blue with check)
                    OutlinedButton(
                        onClick = {
                           onDelete()
                        },
                        border = BorderStroke(1.2.dp, Blue),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 4.dp,
                            pressedElevation = 6.dp
                        ),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = Color.White,
                            contentColor = Blue        // blue text/icon
                        ),shape = RoundedCornerShape(5.dp),
                        modifier = Modifier.height(35.dp).width(120.dp)
                    ) {
                        Icon(Icons.Rounded.Check, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("YES")
                    }

                    // CANCEL (outlined gray with X)
                    OutlinedButton(
                        onClick = { onDismissRequest() },
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = Color.White,
                            contentColor = Color(0xFF9AA1A9)        // gray text/icon
                        ),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 4.dp,
                            pressedElevation = 6.dp
                        ),
                        border = BorderStroke(1.2.dp, Color(0xFF9AA1A9)),
                        shape = RoundedCornerShape(5.dp),
                        modifier = Modifier.height(35.dp).width(150.dp)
                    ) {
                        Icon(Icons.Rounded.Close, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("CANCEL")
                    }
                }
            }
    })
}