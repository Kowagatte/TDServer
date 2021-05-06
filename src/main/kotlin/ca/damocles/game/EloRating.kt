package ca.damocles.game

import ca.damocles.storage.Account
import kotlin.math.pow

//TODO clean this up.
class EloRating(val accountOne: Account, val accountTwo: Account){
    val playerOneExpectedOutcome: Float = 1.0f * 1.0f / (1 + 1.0f * (10.0f.pow(1.0f * (accountTwo.rating - accountOne.rating) / 400)))
    val playerTwoExpectedOutcome: Float = 1.0f - playerOneExpectedOutcome
    private val kConstantPlayerOne: Int = getKConstant(accountOne)
    private val kConstantPlayerTwo: Int = getKConstant(accountTwo)

    fun getNewRatings(actualOutcome: Float): Pair<Float, Float>{
        val newRatingOne: Float = accountOne.rating + (kConstantPlayerOne * (actualOutcome - playerOneExpectedOutcome))
        val newRatingTwo: Float = accountTwo.rating + (kConstantPlayerTwo * ((1 - actualOutcome) - playerTwoExpectedOutcome))
        return Pair(newRatingOne, newRatingTwo)
    }

    fun getNewRatings(actualOutcome: GameOutcome) = getNewRatings(actualOutcome.value)

    fun getDiffs(actualOutcome: Float): Pair<Float, Float>{
        val ratingOneDiff: Float = (kConstantPlayerOne * (actualOutcome - playerOneExpectedOutcome))
        val ratingTwoDiff: Float = (kConstantPlayerTwo * ((1 - actualOutcome) - playerTwoExpectedOutcome))
        return Pair(ratingOneDiff, ratingTwoDiff)
    }

    fun getDiffs(actualOutcome: GameOutcome) = getDiffs(actualOutcome.value)

    private fun getKConstant(account: Account): Int{
        return if(account.rating > 2400){
            10
        }else if((account.gameRecords.size >= 30) && (account.rating <= 2400)){
            20
        }else{
            40
        }
    }

}