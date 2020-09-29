package com.programacionymas.photosearchtv.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*

/*
{
    "id": "8432423659",
    "owner": "37107167@N07",
    "secret": "dd1b834ec5",
    "server": "8187",
    "farm": 9,
    "title": "Color",
    "ispublic": 1,
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
    @SerializedName("dateupload") var dateUpload: Long = 0
) : Serializable {

    override fun toString(): String {
        return "Photo{" +
            "id=" + id +
            ", title='" + title + '\'' +
            ", author='" + author + '\'' +
        '}'
    }

    fun getUrlSmall(): String {
        return "https://farm${farm}.staticflickr.com/${server}/${id}_${secret}_s.jpg"
    }

    fun getUrlLarge(): String {
        return "https://farm${farm}.staticflickr.com/${server}/${id}_${secret}_b.jpg"
    }

    fun getFormattedDate(): String {
        return SimpleDateFormat("MMM dd yyyy", Locale.ENGLISH).format(dateUpload * 1000L)
    }

    companion object {
        internal const val serialVersionUID = 727566175075960653L
    }
}
