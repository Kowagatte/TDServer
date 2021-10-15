package ca.damocles.game

import ca.damocles.storage.Account
import java.util.*
import kotlin.math.pow

/**
 * Class EloRating
 *
 * A class used to manipulate and calculate ELO;
 * Used to calculate expected outcomes, elo changes, updates
 * and k-constants.
 */
class EloRating(private val accountOne: Account, private val accountTwo: Account){

    private val expectedOutcomes: Pair<Float, Float> = expectedOutcomes(accountOne.rating, accountTwo.rating)
    private val kConstantPlayerOne: Int = getKConstant(accountOne)
    private val kConstantPlayerTwo: Int = getKConstant(accountTwo)

    /**
     * Get's three possible rating change outcomes for the given player.
     * @param player: a byte representing which player to get the differences for, 0 for player one, 1 for player two.
     * @return: A Triple, each value representing a possible difference in rating;
     * first represents the difference if the player wins,
     * second represents the difference if the player ties,
     * third represents the difference if the player loses.
     */
    fun getRatingChangePossibilities(player: Byte): Triple<Float, Float, Float>{
        /**
         * Get the difference in rating if a theoreticalOutcome were to occur.
         * @param theoreticalOutcome: the theoretical outcome for which a difference
         * in rating will be calculated.
         * @return: the rating difference that would happen if the theoretical outcome
         * were to occur.
         */
        fun getDiff(theoreticalOutcome: Float): Float{
            return if(player == 1.toByte())
                (kConstantPlayerOne * (theoreticalOutcome - expectedOutcomes.first))
            else
                (kConstantPlayerTwo * (theoreticalOutcome - expectedOutcomes.second))
        }

        return Triple(
            getDiff(GameOutcome.PLAYER_ONE.value),
            getDiff(GameOutcome.TIE.value),
            getDiff(GameOutcome.PLAYER_TWO.value)
        )
    }

    /**
     * Gets the difference in ratings for player one and player two if the given outcome were to happen
     * @param outcome: the outcome of the match
     * @return: a pair of floats where the first value is player one's rating change, and the second value
     * is player two's rating change.
     */
    fun getDiffs(outcome: Float): Pair<Float, Float> = Pair(
            (kConstantPlayerOne * (outcome - expectedOutcomes.first)),
            (kConstantPlayerTwo * (outcome - expectedOutcomes.second))
        )

    /**
     * Gets the k-constant for a specific account.
     *
     * A k-constant is the factor in which a rating changes in result of winning or
     * losing a match. This helps to keep fluctuation in higher ratings lower and
     * to have players with fewer games have a more robust rating to attempt to place
     * them at their appropriate rating faster.
     *
     * @param account: the account being assigned a k-constant.
     * @return: An integer representing the k-constant their rating will change in
     * factor of.
     */
    private fun getKConstant(account: Account): Int{
        return if(account.rating > 2400){
            10
        }else if((account.gameRecords.size >= 30) && (account.rating <= 2400)){
            20
        }else{
            40
        }
    }

    /**
     * Calculates the expected outcome of a given match between two opponents.
     * @param playerOne: the rating of the first player.
     * @param playerTwo: the rating of the second player.
     * @return: A pair of Floats, The first value is a float representing the chance that the first
     * player wins. The second value is a float representing the chance that the second player wins.
     */
    fun expectedOutcomes(playerOne: Float, playerTwo: Float): Pair<Float, Float>{
        /**
         * Does the calculation for the outcome of a match between two rated players.
         * @param player: the players rating.
         * @param opponent: the opponents rating.
         * @return: the expected outcome for the player as a Float.
         */
        fun outcomeCalculation(player: Float, opponent: Float): Float =
            1.0f / (1 + 1.0f * (10.0f.pow(1.0f * (opponent - player) / 400)))

        return Pair(outcomeCalculation(playerOne, playerTwo), outcomeCalculation(playerTwo, playerOne))
    }

    /**
     * Calculates the new rating based on a matches outcome.
     * @param rating: the rating before the match.
     * @param kConstant: the k-constant of the player.
     * @param matchOutcome: the actual outcome of the match.
     * @param expectedOutcome: the expected outcome of the match, calculated through a method like #expectedOutcomes
     * @return: a float representing the updated rating after the matches outcome. Note: this is not the difference,
     * it is the replacement rating.
     */
    private fun calculateNewRating(rating: Float, kConstant: Int, matchOutcome: Float, expectedOutcome: Float): Float{
        return rating + (kConstant * (matchOutcome - expectedOutcome))
    }

    /**
     * Processes the matches outcome and gets the newly updated ratings.
     * @param matchOutcome: The outcome of the match, as a float, 1 representing player one has won,
     * 0 representing player two has won.
     * @return: A pair of floats, the first being the new rating of player one, and
     * the second being the new rating of player two.
     */
    fun processMatch(matchOutcome: Float): Pair<Float, Float>{
        return Pair(calculateNewRating(accountOne.rating, kConstantPlayerOne, matchOutcome, expectedOutcomes.first),
        calculateNewRating(accountTwo.rating, kConstantPlayerTwo, 1-matchOutcome, expectedOutcomes.second))
    }

    /**
     * Processes the matches outcome and gets the newly updated ratings.
     * @param matchOutcome: the outcome of the match, as a GameOutcome
     * @return: A pair of floats, the first being the new rating of player one, and
     * the second being the new rating of player two.
     */
    fun processMatch(matchOutcome: GameOutcome): Pair<Float, Float> = processMatch(matchOutcome.value)

}

/**
 * Test driver to see the expected change in ratings
 */
fun main(){
    val ratingOne = 2000f
    val ratingTwo = 1500f

    val accountOne = Account(UUID.randomUUID(), "", "Bob", "", ratingOne)
    val accountTwo = Account(UUID.randomUUID(), "", "Tom", "", ratingTwo)
    val eloRating = EloRating(accountOne, accountTwo)

    println("${accountOne.username} | Rating: ${accountOne.rating}")
    println("${accountTwo.username} | Rating: ${accountTwo.rating}")

    val expected = eloRating.expectedOutcomes(accountOne.rating, accountTwo.rating)
    println("Chance ${accountOne.username} wins: ${expected.first}")
    println("Chance ${accountTwo.username} wins: ${expected.second}")
    val diffsOne = eloRating.getRatingChangePossibilities(1)
    println("${accountOne.username} diffs | Win: ${diffsOne.first}, Tie: ${diffsOne.second}, Lose: ${diffsOne.third}")

    val diffsTwo = eloRating.getRatingChangePossibilities(2)
    println("${accountTwo.username} diffs | Win: ${diffsTwo.first}, Tie: ${diffsTwo.second}, Lose: ${diffsTwo.third}")

    println("${eloRating.processMatch(1.0f).first} | ${eloRating.processMatch(1.0f).second}")
}