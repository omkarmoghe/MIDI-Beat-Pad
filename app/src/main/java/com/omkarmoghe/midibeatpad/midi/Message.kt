package com.omkarmoghe.midibeatpad.midi

/**
 * MIDI messages and their numbers
 */
enum class Message(val id: Int) {
    NOTE_ON(0x90),
    NOTE_OFF(0x80),
}
