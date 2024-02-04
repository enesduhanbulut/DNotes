package com.duhapp.dnotes

import androidx.annotation.ColorRes

enum class NoteColor(
    @ColorRes val darkColor: Int = R.color.note_color_red_dark,
    @ColorRes val lightColor: Int = R.color.note_color_red_light,
    @ColorRes val textColor: Int = R.color.white,
    @ColorRes val darkTextColor: Int = R.color.black
) {
    RED(R.color.note_color_red_dark, R.color.note_color_red_light, R.color.white, R.color.black),
    ORANGE(R.color.note_color_orange_dark, R.color.note_color_orange_light, R.color.black, R.color.black),
    YELLOW(R.color.note_color_yellow_dark, R.color.note_color_yellow_light, R.color.black, R.color.black),
    GREEN(R.color.note_color_green_dark, R.color.note_color_green_light, R.color.black , R.color.black),
    BLUE(R.color.note_color_blue_dark, R.color.note_color_blue_light, R.color.white, R.color.black),
    PURPLE(R.color.note_color_purple_dark, R.color.note_color_purple_light, R.color.white, R.color.black),
    BROWN(R.color.note_color_brown_dark, R.color.note_color_brown_light, R.color.white, R.color.black),
    CYAN(R.color.note_color_cyan_dark, R.color.note_color_cyan_light, R.color.black, R.color.black);

    companion object {
        fun fromOrdinal(ordinal: Int): NoteColor {
            return values()[ordinal]
        }
    }


}