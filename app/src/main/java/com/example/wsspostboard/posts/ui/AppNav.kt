package com.example.wsspostboard.posts.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.wsspostboard.posts.ui.screens.AllPostsScreen
import com.example.wsspostboard.posts.ui.components.CreateUpdatePostContent
import com.example.wsspostboard.posts.ui.screens.PostDetailScreen
import com.example.wsspostboard.posts.utils.Routes
import com.example.wsspostboard.posts.viewmodel.PostViewModel

/* <<<<<<<<<<<<<<  ✨ Windsurf Command ⭐ >>>>>>>>>>>>>>>> */
/**
 * A composable function that implements the navigation of the app.
 *
 * It renders a [NavHost] that manages the navigation between the different
 * screens of the app.
 *
 * The navigation is as follows:
 * - The start destination is the [AllPostsScreen].
 * - From the [AllPostsScreen], the user can navigate to the [CreateUpdatePostContent]
 *   by clicking on the floating action button.
 * - From the [CreateUpdatePostContent], the user can navigate back to the
 *   [AllPostsScreen] by clicking on the "Done" button.
 * - From the [AllPostsScreen], the user can navigate to the [PostDetailScreen]
 *   by clicking on a post.
 * - From the [PostDetailScreen], the user can navigate to the
 *   [CreateUpdatePostContent] by clicking on the "Edit" button, or back to the
 *   [AllPostsScreen] by clicking on the "Back" button.
 * - From the [CreateUpdatePostContent], the user can navigate back to the
 *   [PostDetailScreen] by clicking on the "Cancel" button.
 *
 * @param viewModel The view model for the posts.
 */
/* <<<<<<<<<<  314c5c67-af4e-4b99-a5ab-187d37d6d8e7  >>>>>>>>>>> */
@Composable
fun AppNav(viewModel: PostViewModel) {
    val nav = rememberNavController()

    NavHost(navController = nav, startDestination = Routes.POSTS) {
        composable(Routes.POSTS) {
            AllPostsScreen(
                viewModel = viewModel,
                onOpen = { id ->
                    nav.navigate("post/$id")
                }
            )
        }
        composable(
            route = Routes.DETAIL,
            arguments = listOf(navArgument("id") { type = NavType.LongType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getLong("id") //?: 0L
            id?.let {
                PostDetailScreen(
                    id = it,
                    viewModel = viewModel,
                    onBack = { nav.popBackStack() },
                )
            }
        }
    }
}