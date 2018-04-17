package com.omkarmoghe.midibeatpad.pad

import com.omkarmoghe.midibeatpad.midi.*

class MidiPad(var channel: Channel = Channel.THREE, var note: Note = Note.C, var velocity: Int = 127) {
    fun toMidiByteArray(message: Message): ByteArray =
        byteArrayOf(
            getMidiMessage(message, channel).toByte(),
            getMidiNote(note).toByte(),
            velocity.toByte()
        )

    override fun toString(): String {
        return "MidiPad: Channel(${channel.name}) Note(${note.name}) Velocity($velocity)"
    }
}