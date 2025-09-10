package com.example.wsspostboard.posts.data


import com.example.wsspostboard.posts.viewmodel.PostViewModel
import kotlinx.coroutines.flow.Flow


class PostRepository(private val dao: PostDao) {
    fun observeAll(): Flow<List<Post>> = dao.observeAll()
    fun observeById(id: Long): Flow<Post?> = dao.observeById(id)


    suspend fun create(value: PostViewModel.FormState) {
        val now = System.currentTimeMillis()
        dao.insert(
            Post(
                title = value.title.trim(),
                content = value.content.trim(),
                createdAt = now,
                updatedAt = now,
                id = value.id,
                author = value.author,
                upvotes = value.upvotes,
                downvotes = value.downvotes
            )
        )
    }


    suspend fun update(value: PostViewModel.FormState) {
// We can't get a value from Flow here; just write directly using a new model.
        val now = System.currentTimeMillis()
        dao.update(
            Post(
                title = value.title.trim(), content = value.content.trim(), createdAt = now, updatedAt = now,
                id = value.id,
                author = value.author,
                upvotes = value.upvotes,
                downvotes = value.downvotes
            )
        )
    }


    suspend fun delete(post: Post) = dao.delete(post)


    suspend fun upvote(id: Long) = dao.upvote(id, System.currentTimeMillis())
    suspend fun downvote(id: Long) = dao.downvote(id, System.currentTimeMillis())
}