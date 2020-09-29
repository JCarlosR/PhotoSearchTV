package com.programacionymas.photosearchtv.io.response

import com.google.gson.annotations.SerializedName
import com.programacionymas.photosearchtv.model.Photo

/*
{
    "photos": {
        "page": "1",
        "pages": 3,
        "perpage": "6",
        "total": 13,
        "photo": [...]
    },
    "stat": "ok"
}
*/

data class GetPhotosResponse (
    val photos: Photos,
    val stat: String
) {
    data class Photos(
        val page: Int,
        val pages: Int,
        @SerializedName("perpage") val perPage: Int,
        val total: Int,
        val photo: ArrayList<Photo>
    )
}

