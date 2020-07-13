package ca.damocles.utilities

import kotlin.math.pow

class EloRating(private val ratingOne: Float, private val ratingTwo: Float){
    val playerOneExpectedOutcome: Float = 1.0f * 1.0f / (1 + 1.0f * (10.0f.pow(1.0f * (ratingTwo - ratingOne) / 400)))
    val playerTwoExpectedOutcome: Float = 1.0f - playerOneExpectedOutcome
    private val kconstant: Int = 30

    fun getNewRatings(actualOutcome: Int): Pair<Float, Float>{
        val newRatingOne: Float = ratingOne + (kconstant * (actualOutcome - playerOneExpectedOutcome))
        val newRatingTwo: Float = ratingTwo + (kconstant * ((1 - actualOutcome) - playerTwoExpectedOutcome))
        return Pair(newRatingOne, newRatingTwo)
    }
}