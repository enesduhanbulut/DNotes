package com.duhapp.dnotes

import androidx.annotation.ColorRes

enum class NoteColor(
    @ColorRes val darkColor: Int,
    @ColorRes val lightColor: Int,
) {
    RED(R.color.note_color_red_dark, R.color.note_color_red_light),
    ORANGE(R.color.note_color_orange_dark, R.color.note_color_orange_light),
    YELLOW(R.color.note_color_yellow_dark, R.color.note_color_yellow_light),
    GREEN(R.color.note_color_green_dark, R.color.note_color_green_light),
    BLUE(R.color.note_color_blue_dark, R.color.note_color_blue_light),
    PURPLE(R.color.note_color_purple_dark, R.color.note_color_purple_light),
    BROWN(R.color.note_color_brown_dark, R.color.note_color_brown_light),
    CYAN(R.color.note_color_cyan_dark, R.color.note_color_cyan_light);

    companion object {
        fun fromOrdinal(ordinal: Int): NoteColor {
            return values()[ordinal]
        }
    }


}