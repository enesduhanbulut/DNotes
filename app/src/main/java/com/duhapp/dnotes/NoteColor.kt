package com.duhapp.dnotes

import androidx.annotation.ColorRes

enum class NoteColor(
    @ColorRes val darkColor: Int,
    @ColorRes val lightColor: Int,
    @ColorRes val textColor: Int
) {
    RED(R.color.note_color_red_dark, R.color.note_color_red_light, R.color.white),
    ORANGE(R.color.note_color_orange_dark, R.color.note_color_orange_light, R.color.black),
    YELLOW(R.color.note_color_yellow_dark, R.color.note_color_yellow_light, R.color.black),
    GREEN(R.color.note_color_green_dark, R.color.note_color_green_light, R.color.black),
    BLUE(R.color.note_color_blue_dark, R.color.note_color_blue_light, R.color.white),
    PURPLE(R.color.note_color_purple_dark, R.color.note_color_purple_light, R.color.white),
    BROWN(R.color.note_color_brown_dark, R.color.note_color_brown_light, R.color.white),
    CYAN(R.color.note_color_cyan_dark, R.color.note_color_cyan_light, R.color.black);

    companion object {
        fun fromOrdinal(ordinal: Int): NoteColor {
            return values()[ordinal]
        }
    }


}