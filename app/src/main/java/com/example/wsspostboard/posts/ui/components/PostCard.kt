package com.example.wsspostboard.posts.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wsspostboard.R
import com.example.wsspostboard.posts.data.Post
import com.example.wsspostboard.posts.viewmodel.PostViewModel
import com.example.wsspostboard.ui.theme.Blue
import com.example.wsspostboard.ui.theme.montserratFontFamily

/**
 * A composable function that displays a single post in a card view.
 *
 * The card displays the title, content, and upvote/downvote buttons of the post.
 * The [onUpvote] and [onDownvote] functions will be called when the respective
 * buttons are clicked.
 *
 * The [onOpen] function will be called when the card is clicked.
 *
 * @param post the post to be displayed
 * @param onUpvote the function to be called when the upvote button is clicked
 * @param onDownvote the function to be called when the downvote button is clicked
 * @param viewModel the view model for the post
 * @param onOpen the function to be called when the card is clicked
 */
@Composable
internal fun PostCard(
    post: Post,
    onUpvote: () -> Unit,
    onDownvote: () -> Unit,
    viewModel: PostViewModel,
    onOpen: (Long) -> Unit
) {
    ElevatedCard(modifier = Modifier
        .fillMaxWidth()
        .height(180.dp)
        .clickable {
            onOpen(post.id)
        }
        .padding(horizontal = 20.dp, vertical = 16.dp),
        shape = RoundedCornerShape(5.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = Color.White)) {
        Column(Modifier.padding(16.dp)) {
            Text(
                text = post.title,
                fontFamily = montserratFontFamily,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold, fontSize = 13.sp
                )
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = post.content,
                fontFamily = montserratFontFamily,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2, color = Color.Gray,
                fontSize = 13.sp,
                modifier = Modifier.height(40.dp),
                overflow = TextOverflow.Clip  // Clips the text instead of showing ellipsis
            )

            Spacer(Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Upvote count
                Row(
                    modifier = Modifier.fillMaxWidth(0.5f),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Icon(
                            painterResource(id = R.drawable.thumb_up_svgrepo_com),
                            modifier = Modifier
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    onClick = onUpvote,
                                    indication = null,
                                )
                                .size(15.dp),
                            contentDescription = "Upvote",
                            tint = Color.Gray
                        )
                        Spacer(Modifier.width(5.dp))
                        Text(
                            text = post.upvotes.toString(),
                            fontFamily = montserratFontFamily,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 13.sp,
                            color = Color.Gray,
                            modifier = Modifier//.offset(x = (-10).dp)
                        )
                    }
                    Spacer(Modifier.width(35.dp))
                    // Downvote count
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painterResource(id = R.drawable.thumb_down_svgrepo_com),
                            modifier = Modifier.clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                onClick = onDownvote,
                                indication = null,
                            )
                                .size(15.dp),
                            contentDescription = "Downvote",
                            tint = Color.Gray
                        )
                        Spacer(Modifier.width(5.dp))

                        Text(
                            text = post.downvotes.toString(),
                            fontFamily = montserratFontFamily,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.Gray,
                            fontSize = 13.sp,
                            modifier = Modifier
                        )
                    }
                }
                Spacer(Modifier.weight(1f))
                // Read More button
                Button(
                    onClick = { onOpen(post.id) },
                    colors = ButtonDefaults.buttonColors(containerColor = Blue),
                    shape = RoundedCornerShape(17),
                    contentPadding = PaddingValues(
                        horizontal = 8.dp, vertical = 0.dp
                    ), // reduce vertical padding
                    modifier = Modifier
                        .height(28.dp)
                        .width(120.dp) // set smaller height
                ) {
                    Text(
                        "READ MORE",
                        fontFamily = montserratFontFamily,
                        fontWeight = FontWeight.Medium,
                        fontSize = 11.sp
                    )
                }
            }
        }
    }
}