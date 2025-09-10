package com.example.wsspostboard.posts.ui.screens

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.wsspostboard.R
import com.example.wsspostboard.posts.data.Post
import com.example.wsspostboard.posts.ui.components.CreateUpdatePostContent
import com.example.wsspostboard.posts.ui.components.PostCard
import com.example.wsspostboard.posts.viewmodel.PostViewModel
import com.example.wsspostboard.ui.theme.Blue
import com.example.wsspostboard.ui.theme.montserratFontFamily

private val ScoreComparator =
    compareByDescending<Post> { it.upvotes - it.downvotes }
        .thenByDescending { it.updatedAt }
        .thenByDescending { it.id }
/**
 * A composable function that renders the main screen showing all posts.
 * It contains a top bar with a title and a floating action button to create a new post.
 * The main content of the screen is a list of posts, each of which is a [PostCard].
 * The list is rendered using a [LazyColumn].
 *
 * @param viewModel The view model for the posts.
 * @param onOpen A function to be called when the user wants to open a post.
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllPostsScreen(
    viewModel: PostViewModel,
    onOpen: (Long) -> Unit
) {
    val posts = viewModel.posts.collectAsStateWithLifecycle().value
    val sortedPosts by remember(posts) {
        derivedStateOf {
            posts.sortedWith(ScoreComparator)
        }
    }
    val context = LocalContext.current
    BackHandler {
        if(viewModel.isCreateUpdateVisible.value) {
            viewModel.createUpdateVisible(false)
        } else {
            (context as? Activity)?.finish()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "ALL POSTS",
                            color = Color.White, fontFamily = montserratFontFamily,
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier
                                .fillMaxWidth().offset(x = (-9).dp), textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Medium,
                            fontSize = 16.sp
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1E6CF4), // blue bar
                    titleContentColor = Color.White
                )
            )
        },
        floatingActionButton = {
                FloatingActionButton(
                    onClick = { viewModel.createUpdateVisible(true) },
                    containerColor = Blue,
                    modifier = Modifier
                        .size(60.dp)
                        .shadow(12.dp, RoundedCornerShape(50)), // <- adds visible shadow
                    shape = RoundedCornerShape(50)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.add_plus_svgrepo_com),
                        contentDescription = "Add",
                        tint = Color.White,
                        modifier = Modifier.size(43.dp)
                    )
                }
        }
    ) { inner ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(inner),
            contentPadding = PaddingValues(vertical = 12.dp,horizontal = 12.dp)
        ) {
            items(sortedPosts, key = { it.id }) { post ->
                PostCard(
                    post = post,
                    onUpvote = { viewModel.upvote(post.id) },
                    onDownvote = { viewModel.downvote(post.id) },
                    viewModel = viewModel,onOpen
                )
            }
        }
    }

    if (viewModel.isCreateUpdateVisible.value) {
        CreateUpdatePostContent(
            viewModel = viewModel,
            onDone = { viewModel.createUpdateVisible(false) },
//            id = id,
//            onCancel = { nav.popBackStack() }
        )
    }
}