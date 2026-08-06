package com.example.broadcastreceiverapplication


import com.example.broadcastreceiverapplication.ui.sms.randomGoodColor
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test


class RandomGoodColorTest {

    @Test
    fun randomGoodColor_sameSeed_producesSameColor() {
        val seed = "+37112345678".hashCode()

        val colorA = randomGoodColor(seed)
        val colorB = randomGoodColor(seed)

        assertEquals(colorA, colorB)
    }

    @Test
    fun randomGoodColor_differentSeeds_producesDifferentColors() {
        val colorA = randomGoodColor("+37112345678".hashCode())
        val colorB = randomGoodColor("+37187654321".hashCode())

        assertTrue(colorA != colorB)
    }

    @Test
    fun randomGoodColor_anySeed_returnsChannelsWithinValidRange() {
        val color = randomGoodColor(42)

        assertTrue(color.red in 0f..1f)
        assertTrue(color.green in 0f..1f)
        assertTrue(color.blue in 0f..1f)
    }

    @Test
    fun randomGoodColor_anySeed_returnsFullyOpaqueColor() {
        val color = randomGoodColor(123)

        assertEquals(1f, color.alpha, 0.0001f)
    }
}

