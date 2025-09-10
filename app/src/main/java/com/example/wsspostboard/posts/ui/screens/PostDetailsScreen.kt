@file:Suppress("DEPRECATION")

package com.example.wsspostboard.posts.ui.screens

//import com.example.posts.Graph
//import com.example.posts.data.Post
//import com.example.posts.viewmodel.PostViewModel
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wsspostboard.R
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.wsspostboard.Graph
import com.example.wsspostboard.posts.ui.components.CreateUpdatePostContent
import com.example.wsspostboard.posts.ui.components.DeletePopup
import com.example.wsspostboard.posts.utils.DateUtils.convertTimestampToFormattedDate
import com.example.wsspostboard.posts.utils.StringUtils.capitalizeWords
import com.example.wsspostboard.posts.viewmodel.PostViewModel
import com.example.wsspostboard.ui.theme.Blue
import com.example.wsspostboard.ui.theme.Orange
import com.example.wsspostboard.ui.theme.montserratFontFamily

// ---- Theme colors you can replace with your app theme ----

/**
 * A composable that displays a post with the given id.
 *
 * @param id The id of the post to display.
 * @param viewModel The view model to use for deleting the post.
 * @param onBack Called when the user wants to go back.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostDetailScreen(
    id: Long,
    viewModel: PostViewModel,
    onBack: () -> Unit,
) {
    val postFlow = remember(id) { Graph.repository.observeById(id) }
    val post = postFlow.collectAsStateWithLifecycle(initialValue = null).value
    val state = remember { mutableStateOf(false) }

    var showDelete by remember { mutableStateOf(false) }

    BackHandler {
        if(state.value) {
            state.value = false
        } else {
           onBack()
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "VIEW POST",
                        color = Color.White, fontFamily = montserratFontFamily,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier, textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            state.value = false
                            onBack() },
                        modifier = Modifier
                            .size(38.dp)
                            .padding(start = 13.dp)
                    ) {
                        Icon(
                            painterResource(id = R.drawable.home_svgrepo_com),
                            contentDescription = "Home",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { },
                        modifier = Modifier
                            .size(38.dp)
                            .padding(end = 13.dp)
                    ) {
                        Icon(
                            painterResource(id = R.drawable.home_svgrepo_com),
                            contentDescription = "Home",
                            tint = Color.Transparent
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Blue,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(Color.White)
                .padding(horizontal = 26.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Spacer(modifier = Modifier.height(15.dp))


            // Post Title Label
            SectionLabel("Post Title")

            // Title
            if (post != null) {
                Text(
                    text = post.title, fontFamily = montserratFontFamily,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp,
                        lineHeight = 28.sp
                    ),
                    color = Color.Black,modifier = Modifier.offset(y = (-2).dp)
                )
            }
            Spacer(modifier = Modifier.height(9.dp))
            val content = (post?.content.orEmpty())
                .trimStart()
                .replaceFirstChar { it.uppercaseChar() }

            // Description Label
            SectionLabel("Post Description")
            Spacer(modifier = Modifier.height(1.dp))

            // Description
            Text(
                text = content,
                style = MaterialTheme.typography.bodyMedium.copy(
                    lineHeight = 20.sp
                ), fontSize = 13.sp,
                color = Color.Black,fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Author
            Column(modifier = Modifier) {
                SectionLabel("By Author")
                Spacer(modifier = Modifier.height(1.dp))

                Text(
                    text = capitalizeWords(post?.author.orEmpty()),
                    fontFamily = montserratFontFamily,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    fontSize = 16.sp,
                    color = Color.Black
                )
            }
            Spacer(modifier = Modifier.height(15.dp))

            // Date
            Column(modifier = Modifier) {
                SectionLabel("Date")
                Spacer(modifier = Modifier.height(1.dp))

                Text(
                    fontFamily = montserratFontFamily, text = convertTimestampToFormattedDate(post?.createdAt ?: 0),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ), fontSize = 16.sp,
                    color = Color.Black
                )
            }
            Spacer(modifier = Modifier.height(14.dp))
            // Likes / Dislikes Row
            Row(
                modifier = Modifier
                    .padding(top = 4.dp)
                    .fillMaxWidth(0.45f),
                verticalAlignment = Alignment.CenterVertically,// horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.thumb_up_svgrepo_com),
                    contentDescription = "Dislikes",
                    tint = Color.Black, modifier = Modifier.size(14.dp)
                )
                post?.let {
                    Text(
                        text = " ${it.upvotes}",
                        fontFamily = montserratFontFamily,
                        style = MaterialTheme.typography.bodyMedium,
                        fontSize = 12.sp, fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(end = 30.dp)
                    )
                }

                Icon(
                    painter = painterResource(id = R.drawable.thumb_down_svgrepo_com),
                    contentDescription = "Dislikes",
                    tint = Color.Black, modifier = Modifier.size(14.dp)
                )
                Text(
                    text = " ${post?.downvotes}", fontFamily = montserratFontFamily,
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 12.sp, fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(34.dp))

            // Action Buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween//spacedBy(16.dp)
            ) {
                // EDIT (Blue Outlined with light fill)
                OutlinedButton(
                    onClick = {
                        state.value = true
//                        viewModel.createUpdateVisible(true)
                              },
                    shape = RoundedCornerShape(4.dp),
                    border = ButtonDefaults.outlinedButtonBorder.copy(
                        brush = SolidColor(Blue)
                    ),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color.White,
                        contentColor = Blue
                    ),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 4.dp,
                        pressedElevation = 6.dp
                    ),
                    modifier = Modifier
                        .width(150.dp)//weight(50.dp)
                        .height(39.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Edit,
                        contentDescription = "Edit",
                        modifier = Modifier.padding(end = 8.dp).size(22.dp)
                    )
                    Text(
                        text = "EDIT", fontFamily = montserratFontFamily,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.SemiBold, fontSize = 14.sp
                    )
                }

                // DELETE (Red Outlined with light fill)
                OutlinedButton(
                    onClick = { showDelete = true },
                    shape = RoundedCornerShape(4.dp),
                    border = ButtonDefaults.outlinedButtonBorder.copy(
                        brush = SolidColor(Orange)
                    ),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color.White,
                        contentColor = Orange
                    ),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 4.dp,
                        pressedElevation = 6.dp
                    ),
                    modifier = Modifier
                        .width(150.dp)
                        .height(39.dp),
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.delete_1487_svgrepo_com),
                        contentDescription = "Delete",
                        modifier = Modifier.padding(end = 8.dp).size(18.dp),tint = Orange
                    )
                    Text(
                        text = "DELETE",
                        textAlign = TextAlign.Center,fontFamily = montserratFontFamily,
                        fontWeight = FontWeight.SemiBold, fontSize = 14.sp
                    )
                }
            }
        }
    }

//    Notes
    if (showDelete && post != null) {
        DeletePopup(onDismissRequest = { showDelete = false },onDelete = {
            viewModel.delete(
                post,
                onDone = { onBack() },
                onError = { showDelete = false }
            )
        })
        }
    if (state.value) {
        CreateUpdatePostContent(
            viewModel = viewModel,
            onDone = {
                onBack()
                viewModel.createUpdateVisible(false)
                     },
            id = id,
        )
    }
}


@Composable
private fun SectionLabel(text: String) {
    Text(
        text = text, fontFamily = montserratFontFamily,
        style = MaterialTheme.typography.labelLarge.copy(
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.2.sp
        ), fontSize = 10.sp,
        color = Color.Black//Color(0xFF3C3C3C)
    )
}