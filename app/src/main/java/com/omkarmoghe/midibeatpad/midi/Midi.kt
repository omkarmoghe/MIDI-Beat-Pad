package com.omkarmoghe.midibeatpad.midi

fun getMidiMessage(message: Message, channel: Channel): Int = message.id + (channel.id - 1)

fun getMidiNote(note: Note, octave: Octave = Octave.FOUR): Int = note.id + (octave.multiplier * Note.values().size)