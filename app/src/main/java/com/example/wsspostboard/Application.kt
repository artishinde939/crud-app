package com.example.wsspostboard
import android.app.Application
import com.example.wsspostboard.posts.data.AppDatabase
import com.example.wsspostboard.posts.data.PostRepository


object Graph {
    lateinit var db: AppDatabase
    lateinit var repository: PostRepository
}


class Application : Application() {
    /**
     * Called when the application is starting, before any other application objects have
     * been created. The application object is created before any activity object, so this
     * is the ideal place to setup the application graph.
     *
     * The graph consists of a [AppDatabase] and a [PostRepository].
     *
     * @see android.app.Application.onCreate
     */
    override fun onCreate() {
        super.onCreate()
        Graph.db = AppDatabase.getInstance(this)
        Graph.repository = PostRepository(Graph.db.postDao())
    }
}