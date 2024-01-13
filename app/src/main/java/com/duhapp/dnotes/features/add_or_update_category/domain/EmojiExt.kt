package com.duhapp.dnotes.features.add_or_update_category.domain

import com.vanniktech.emoji.Emoji
import com.vanniktech.emoji.google.GoogleEmojiProvider

val emojiProvider = GoogleEmojiProvider()
val emojiUnicodes =
    emojiProvider.categories.flatMap { category -> category.emojis.map { it.unicode } }

fun Emoji.isEmoji(): Boolean {
    return emojiUnicodes.contains(this.unicode)
}

fun String.isEmoji(): Boolean {
    return emojiUnicodes.contains(this)
}