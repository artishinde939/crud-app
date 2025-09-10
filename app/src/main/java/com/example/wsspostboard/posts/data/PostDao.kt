package com.example.wsspostboard.posts.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow


@Dao
interface PostDao {
    @Query("SELECT * FROM posts ORDER BY (upvotes - downvotes) DESC, updatedAt DESC")
    fun observeAll(): Flow<List<Post>>


    @Query("SELECT * FROM posts WHERE id = :id")
    fun observeById(id: Long): Flow<Post?>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(post: Post): Long


    @Update
    suspend fun update(post: Post)


    @Delete
    suspend fun delete(post: Post)


    @Query("UPDATE posts SET upvotes = upvotes + 1, updatedAt = :now WHERE id = :id")
    suspend fun upvote(id: Long, now: Long)


    @Query("UPDATE posts SET downvotes = downvotes + 1, updatedAt = :now WHERE id = :id")
    suspend fun downvote(id: Long, now: Long)
}