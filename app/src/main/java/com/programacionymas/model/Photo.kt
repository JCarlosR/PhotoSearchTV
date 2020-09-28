package com.programacionymas.model

import java.io.Serializable

/**
 * Movie class represents video entity with title, description, image thumbs and video url.
 */
data class Photo(
        var id: Long = 0,
        var title: String? = null,
        var author: String? = null,
        var backgroundImageUrl: String? = null,
        var cardImageUrl: String? = null
) : Serializable {

    override fun toString(): String {
        return "Movie{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", backgroundImageUrl='" + backgroundImageUrl + '\'' +
                ", cardImageUrl='" + cardImageUrl + '\'' +
                '}'
    }

    companion object {
        internal const val serialVersionUID = 727566175075960653L
    }
}
