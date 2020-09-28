package com.programacionymas.model

object PhotoList {
    val list: List<Photo> by lazy {
        setupMovies()
    }
    private var count: Long = 0

    private fun setupMovies(): List<Photo> {
        val title = arrayOf(
                "Zeitgeist 2010_ Year in Review",
                "Google Demo Slam_ 20ft Search",
                "Introducing Gmail Blue",
                "Introducing Google Fiber to the Pole",
                "Introducing Google Nose")

        val author = arrayOf(
                "Studio Zero",
                "Studio One",
                "Studio Two",
                "Studio Three",
                "Studio Four"
        )


        val bgImageUrl = arrayOf(
                "http://commondatastorage.googleapis.com/android-tv/Sample%20videos/Zeitgeist/Zeitgeist%202010_%20Year%20in%20Review/bg.jpg",
                "http://commondatastorage.googleapis.com/android-tv/Sample%20videos/Demo%20Slam/Google%20Demo%20Slam_%2020ft%20Search/bg.jpg",
                "http://commondatastorage.googleapis.com/android-tv/Sample%20videos/April%20Fool's%202013/Introducing%20Gmail%20Blue/bg.jpg",
                "http://commondatastorage.googleapis.com/android-tv/Sample%20videos/April%20Fool's%202013/Introducing%20Google%20Fiber%20to%20the%20Pole/bg.jpg",
                "http://commondatastorage.googleapis.com/android-tv/Sample%20videos/April%20Fool's%202013/Introducing%20Google%20Nose/bg.jpg")
        val cardImageUrl = arrayOf(
                "http://commondatastorage.googleapis.com/android-tv/Sample%20videos/Zeitgeist/Zeitgeist%202010_%20Year%20in%20Review/card.jpg",
                "http://commondatastorage.googleapis.com/android-tv/Sample%20videos/Demo%20Slam/Google%20Demo%20Slam_%2020ft%20Search/card.jpg",
                "http://commondatastorage.googleapis.com/android-tv/Sample%20videos/April%20Fool's%202013/Introducing%20Gmail%20Blue/card.jpg",
                "http://commondatastorage.googleapis.com/android-tv/Sample%20videos/April%20Fool's%202013/Introducing%20Google%20Fiber%20to%20the%20Pole/card.jpg",
                "http://commondatastorage.googleapis.com/android-tv/Sample%20videos/April%20Fool's%202013/Introducing%20Google%20Nose/card.jpg")

        val list = title.indices.map {
            buildMovieInfo(
                title[it],
                author[it],
                cardImageUrl[it],
                bgImageUrl[it]
            )
        }

        return list
    }

    private fun buildMovieInfo(
            title: String,
            author: String,
            cardImageUrl: String,
            backgroundImageUrl: String): Photo {
        val movie = Photo()
        movie.id = count++
        movie.title = title
        movie.author = author
        movie.cardImageUrl = cardImageUrl
        movie.backgroundImageUrl = backgroundImageUrl
        return movie
    }
}
