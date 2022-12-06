package ca.damocles.game

import ca.damocles.storage.Account
import org.junit.Test

import org.junit.Assert.*
import java.util.*

class EloRatingTest {

    @Test
    fun getRatingChangePossibilities() {
        val accountOne = Account(UUID.randomUUID(), "", "", "", 1000f)
        val accountTwo = Account(UUID.randomUUID(), "", "", "", 1000f)
        val eloRating = EloRating(accountOne, accountTwo)
        assertEquals(eloRating.getRatingChangePossibilities(0).third, 20.0f)
    }

    @Test
    fun expectedOutcomes() {
        val accountOne = Account(UUID.randomUUID(), "", "", "", 1000f)
        val accountTwo = Account(UUID.randomUUID(), "", "", "", 1000f)
        val eloRating = EloRating(accountOne, accountTwo)
        val expected = eloRating.expectedOutcomes(accountOne.rating, accountTwo.rating)
        assertEquals(expected.first, 0.5f)
    }

    @Test
    fun processMatch(){
        val accountOne = Account(UUID.randomUUID(), "", "", "", 1000f)
        val accountTwo = Account(UUID.randomUUID(), "", "", "", 1000f)
        val eloRating = EloRating(accountOne, accountTwo)
        assertEquals(eloRating.processMatch(1.0f).first, 1020.0f)
        assertEquals(eloRating.processMatch(GameOutcome.PLAYER_ONE).first, 1020.0f)
    }

}