package com.example.wsspostboard.posts.viewmodel

import android.app.Application
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.wsspostboard.Graph
import com.example.wsspostboard.posts.data.Post
import com.example.wsspostboard.posts.utils.StringUtils.capitalizeParagraph
import com.example.wsspostboard.posts.utils.StringUtils.capitalizeWords
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PostViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = Graph.repository


    // Validation state for forms
    data class FormState(
        val title: String = "",
        val content: String = "",
        val author: String = "",
        val upvotes: Int = 0,
        val downvotes: Int = 0,
        val id: Long = 0,
        val createdAt: Long = System.currentTimeMillis(),
        val updatedAt: Long = System.currentTimeMillis(),
        val authorError: String? = null,
        val titleError: String? = null,
        val contentError: String? = null,
        val isSaving: Boolean = false
    )


    val posts: StateFlow<List<Post>> = repo.observeAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())


    internal val formFlow = MutableStateFlow(FormState())
    val form: StateFlow<FormState> = formFlow

    val isCreateUpdateVisible = mutableStateOf(false)
    fun createUpdateVisible(value: Boolean) {
        isCreateUpdateVisible.value = value
    }

    fun sanitizeSpaces(s: String) =
        s.trim().replace(Regex("\\s+"), " ")

    fun validateTitle(raw: String): Boolean {
        val v = sanitizeSpaces(raw).replace("\n", " ")
        val hasAlnum = v.count { it.isLetterOrDigit() } >= 2
        val error = when {
            v.isEmpty() -> "Title is required"
            v.length !in 3..50 -> "Title must be 3–80 characters"
            !hasAlnum -> "Add a more descriptive title"
            else -> null
        }
        formFlow.value = formFlow.value.copy(titleError = error)
        return error == null
    }

    fun validateDescription(raw: String): Boolean {
        val v = sanitizeSpaces(raw.replace(Regex("\\s*\n\\s*"), "\n"))
        val error = when {
            v.isEmpty() -> "Description is required"
            v.length !in 10..1000 -> "Description must be 10–1000 characters"
            else -> null
        }
        formFlow.value = formFlow.value.copy(contentError = error)

        return error == null
    }

    private val authorPattern =
        Regex("^\\p{L}+(?:[\\p{L}'-]*\\p{L})?(?:\\s+\\p{L}+(?:[\\p{L}'-]*\\p{L})?)*$")

    fun validateAuthor(raw: String): Boolean {
        val base = sanitizeSpaces(raw)
        val error = when {
            base.isEmpty() -> "Author is required"
            base.length !in 2..50 -> "Author must be 2–50 characters"
            !authorPattern.matches(base) ->
                "Only letters, spaces, apostrophes, and hyphens are allowed"

            else -> null
        }
        // optional: title-case for display
        formFlow.value = formFlow.value.copy(authorError = error)

        return error == null
    }


    fun onTitleChange(v: String) {
        val formatted = capitalizeWords(v.trimStart())
        val sanitizedTitle = sanitizeSpaces(formatted).replace("\n", " ")
        val hasAlnum = sanitizedTitle.count { it.isLetterOrDigit() } >= 2

        // Perform title validation
        formFlow.value = formFlow.value.copy(
            title = formatted,
            titleError = when {
                sanitizedTitle.isEmpty() -> "Title is required"
                sanitizedTitle.length !in 3..50 -> "Title must be between 3 to 50 characters"
                !hasAlnum -> "Add a more descriptive title"
                else -> null
            }
        )
    }

    fun onContentChange(v: String) {
        val formatted = capitalizeParagraph(v.trimStart())
        // Perform content validation while the user is typing
        val sanitizedContent = sanitizeSpaces(formatted).replace("\n", " ")

        // Perform content validation
        formFlow.value = formFlow.value.copy(content = formatted,
            contentError =
                when {
                    sanitizedContent.isEmpty() -> "Description is required"
                    sanitizedContent.length !in 10..1000 -> "Description must be 10–1000 characters"
                    else -> null
                }
        )
    }

    fun onAuthorChange(v: String) {
        val formatted = capitalizeWords(v.trimStart())
        val sanitizedAuthor = sanitizeSpaces(formatted)
        formFlow.value = formFlow.value.copy(author = formatted, authorError =
            when {
                sanitizedAuthor.isEmpty() -> "Author is required"
                sanitizedAuthor.length !in 2..50 -> "Author must be 2–50 characters"
                !authorPattern.matches(sanitizedAuthor) -> "Only letters, spaces, apostrophes, and hyphens are allowed"
                else -> null
            }
        )
    }


    fun create(onDone: () -> Unit) {
        Log.i("PostViewModel", "create: ${formFlow.value.title} ${validateTitle(formFlow.value.title)}")
        if (!validateTitle(formFlow.value.title)) return
        if (!validateDescription(formFlow.value.content)) return
        if (!validateAuthor(formFlow.value.author)) return
        viewModelScope.launch {
            try {
                formFlow.value = formFlow.value.copy(isSaving = true)
                repo.create(formFlow.value)
                formFlow.value = FormState()
                onDone()
            } catch (t: Throwable) {
                t
//                onError(t)
            } finally {
                formFlow.value = formFlow.value.copy(isSaving = false)
            }
        }
    }


    fun update(onDone: () -> Unit) {
        if (!validateTitle(formFlow.value.title)) return
        if (!validateDescription(formFlow.value.content)) return
        if (!validateAuthor(formFlow.value.author)) return
        viewModelScope.launch {
            try {
                formFlow.value = formFlow.value.copy(isSaving = true)
                // Use DAO update via repository but pass entire entity
                repo.update(formFlow.value)
                onDone()
            } catch (t: Throwable) {
                t
            } finally {
                formFlow.value = formFlow.value.copy(isSaving = false)
            }
        }
    }

    fun delete(post: Post, onDone: () -> Unit, onError: (Throwable) -> Unit) {
        viewModelScope.launch {
            try {
                repo.delete(post); onDone()
            } catch (t: Throwable) {
                onError(t)
            }
        }
    }

    fun upvote(id: Long) = viewModelScope.launch { repo.upvote(id) }
    fun downvote(id: Long) = viewModelScope.launch { repo.downvote(id) }
}
