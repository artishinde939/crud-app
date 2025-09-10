package com.example.wsspostboard.posts.utils

import java.util.Locale

object StringUtils {
    fun capitalizeWords(input: String): String =
        input.split(" ").joinToString(" ") { word ->
            word.lowercase().replaceFirstChar { c ->
                if (c.isLowerCase()) c.titlecase() else c.toString()
            }
        }

    fun capitalizeParagraph(input: String): String {
        if (input.isBlank()) return input
        return input.trimStart().replaceFirstChar { ch ->
            if (ch.isLowerCase()) ch.titlecase() else ch.toString()
        }
    }

//    fun String.capitalizeFirstLetterWords(): String {
//        return this.split(" ").joinToString(" ") { replaceFirstChar { if (it.isLowerCase()) it.titlecase(
//            Locale.ROOT
//        ) else it.toString() } }
//    }
}
