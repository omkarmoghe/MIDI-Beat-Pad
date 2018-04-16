package com.omkarmoghe.midibeatpad.midi

/**
 * Based on Middle C == C4 == 60
 *
 * Multiplier used with Note to calculate corresponding MIDI note number
 */
enum class Octave(val multiplier: Int) {
    NEGATIVE_ONE(0),
    ZERO(1),
    ONE(2),
    TWO(3),
    THREE(4),
    FOUR(5),
    FIVE(6),
    SIX(7),
    SEVEN(8),
    EIGHT(9),
    NINE(10),
}