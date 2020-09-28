package com.programacionymas.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable
/*
{
    "id": "8432423659",
    "owner": "37107167@N07",
    "secret": "dd1b834ec5",
    "server": "8187",
    "farm": 9,
    "title": "Color",
    "ispublic": 1,
    "isfriend": 0,
    "isfamily": 0,
    "dateupload": "1359655414",
    "ownername": "DaneMakes",
    "is_primary": 1,
    "has_comment": 0
}
*/
data class Photo(
    var id: Long = 0,
    var title: String? = null,
    @SerializedName("ownername") var author: String? = null,

    var server: String? = null,
    var farm: Int? = null,
    var secret: String? = null,
    @SerializedName("dateupload") var dateUpload: String? = null,

    var backgroundImageUrl: String? = null,
    var cardImageUrl: String? = null
) : Serializable {

    override fun toString(): String {
        return "Photo{" +
            "id=" + id +
            ", title='" + title + '\'' +
            ", author='" + author + '\'' +
        '}'
    }

    companion object {
        internal const val serialVersionUID = 727566175075960653L
    }
}
