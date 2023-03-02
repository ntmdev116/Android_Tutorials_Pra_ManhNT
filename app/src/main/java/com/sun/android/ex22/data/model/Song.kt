package com.sun.android.ex22.data.model

import android.net.Uri

data class Song(
    val id: Int,
    val title: String,
    val artist: String,
    val duration: Long,
    val path: String,
    val artworkUri: Uri
)

