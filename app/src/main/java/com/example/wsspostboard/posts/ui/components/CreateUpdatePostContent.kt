package com.example.wsspostboard.posts.ui.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.wsspostboard.R
import com.example.wsspostboard.posts.viewmodel.PostViewModel
import com.example.wsspostboard.posts.viewmodel.PostViewModel.FormState
import com.example.wsspostboard.ui.theme.Blue
import com.example.wsspostboard.ui.theme.montserratFontFamily


/**
 * A composable function that renders a screen for creating or editing a post.
 *
 * This screen has a top bar with a title and a home button. The main content
 * is a scrollable column that contains a title field, a description field,
 * an author field, and a submit button. The fields are validated in basic
 * ways (e.g., not blank).
 *
 * @param viewModel The view model for the posts.
 * @param onDone A function to be called when the user is done with the screen.
 * @param id The ID of the post to edit, or null if this is a new post.
 * @return A composable [Scaffold] that renders the screen.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateUpdatePostContent(
    viewModel: PostViewModel,
    onDone: () -> Unit,
    id: Long? = null,
) {
    LocalFocusManager.current
    val posts = viewModel.posts.collectAsStateWithLifecycle().value
    val post = posts.find { it.id == id }//postFlow?.collectAsStateWithLifecycle(initialValue = null)?.value
    val form = viewModel.form.collectAsState().value
    Log.i("AllPostsScreen", "AllPostsScreen: $post")

    LaunchedEffect(id) {
        viewModel.formFlow.value = post?.let { FormState(it.title, it.content, it.author,
            it.upvotes, it.downvotes,
            it.id, it.createdAt, it.updatedAt) }
            ?: FormState()
        Log.i("CreateUpdatePostScreen", "CreateUpdatePostScreen:$id $post $form")
    }
    Scaffold(
        topBar = {
            Box(
                Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = 20.dp,
                        spotColor = Color.Black.copy(alpha = 0.95f),
                        ambientColor = Color.Black.copy(alpha = 0.95f)
                    )
                    .background(Blue)
            ) {
                TopAppBar(
                    title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (id == null) "CREATE POST" else "EDIT POST",
                            color = Color.White, fontFamily = montserratFontFamily,
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier, textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Medium,
                            fontSize = 16.sp
                        )
                    }
                }, navigationIcon = {
                    IconButton(
                        onClick = { onDone() },
                        modifier = Modifier
                            .size(38.dp)
                            .padding(start = 11.dp)
                    ) {
                        Icon(
                            painterResource(id = R.drawable.home_svgrepo_com),
                            contentDescription = "Home",
                            tint = Color.White
                        )
                    }
                }, actions = {
                    IconButton(
                        onClick = {  },
                        modifier = Modifier
                            .size(38.dp)
                            .padding(end = 11.dp)
                    ) {
                        Icon(
                            painterResource(id = R.drawable.home_svgrepo_com),
                            contentDescription = "Home",
                            tint = Color.Transparent
                        )
                    }
                }, colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Blue,
                    navigationIconContentColor = Color.White,
                    titleContentColor = Color.White
                ), modifier = Modifier.fillMaxWidth()//.padding(horizontal = 6.dp)
                )
            }

        }) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(innerPadding)
                .padding(horizontal = 28.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                "Post Title",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 48.dp),
                textAlign = TextAlign.Start,fontFamily = montserratFontFamily
            )
            OutlinedTextField(
                value = form.title,
                onValueChange = { newTitle ->
                        // Update the title
                        viewModel.onTitleChange(newTitle)
                },
                label = {  },
                isError = form.titleError != null,
                supportingText = {
                    form.titleError?.let {
                        Text(it, color = MaterialTheme.colorScheme.error,modifier = Modifier.offset(x = (-16).dp),
                        fontSize = 12.sp,fontFamily = montserratFontFamily)

                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(75.dp)
                    .offset(y = (-8).dp),
                singleLine = true,  // Restrict to one line
                maxLines = 1,       // Prevents the field from expanding to multiple lines
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Blue,
                    unfocusedBorderColor = Color.Black,
                    errorBorderColor = MaterialTheme.colorScheme.error,
                    disabledBorderColor = Color.LightGray,
                    cursorColor = Blue,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                )
            )

            Text(
                "Description",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = (-5).dp),
                textAlign = TextAlign.Start,
                fontFamily = montserratFontFamily,
            )

            OutlinedTextField(
                value = form.content,
                onValueChange = viewModel::onContentChange,
                label = { },
                isError = form.contentError != null,
                supportingText = {
                    form.contentError?.let {
                        Text(
                            it, color = MaterialTheme.colorScheme.error,modifier = Modifier.offset(x = (-16).dp),
                                    fontSize = 12.sp,fontFamily = montserratFontFamily

                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = (-12).dp)
                    .heightIn(min =150.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Blue,//MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = Color.Black,
                    errorBorderColor = MaterialTheme.colorScheme.error,
                    disabledBorderColor = Color.LightGray,
                    cursorColor = Blue,//MaterialTheme.colorScheme.primary,
                    focusedTextColor = Color.Black,//MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = Color.Black//MaterialTheme.colorScheme.onSurface
                )
            )
            Text(
                "Author",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,fontFamily = montserratFontFamily,
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = (-10).dp),
                textAlign = TextAlign.Start
            )
            OutlinedTextField(
                value = form.author,
                onValueChange = viewModel::onAuthorChange,
                label = { },
                isError = form.authorError != null,
                supportingText = {
                    form.authorError?.let {
                        Text(
                            it, color = MaterialTheme.colorScheme.error,modifier = Modifier.offset(x = (-16).dp),
                            fontSize = 12.sp,fontFamily = montserratFontFamily
                        )
                    }
                },
                singleLine = true,  // Restrict to one line
                maxLines = 1,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 80.dp)
                    .offset(y = (-18).dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Blue,//MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = Color.Black,
                    errorBorderColor = MaterialTheme.colorScheme.error,
                    disabledBorderColor = Color.LightGray,
                    cursorColor = Blue,//MaterialTheme.colorScheme.primary,
                    focusedTextColor = Color.Black,//MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = Color.Black//MaterialTheme.colorScheme.onSurface
                )
            )

            // Submit
            Button(
                onClick = {

//                    if (form.title.isNotBlank() && form.content.isNotBlank() && form.author.isNotBlank()) {
                        if (id == null) {
                            viewModel.create(
                                onDone = onDone)
                        } else {
                            post?.let {
                                viewModel.update(
                                    onDone = onDone)
                            }
//                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Blue)
            ) {
                Text(if (id == null) "SUBMIT" else "SAVE CHANGES", fontFamily = montserratFontFamily, fontSize = 14.sp)
            }
        }
    }
}