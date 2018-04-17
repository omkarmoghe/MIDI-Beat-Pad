package com.omkarmoghe.midibeatpad.midi

import android.media.midi.MidiInputPort

// Supporting nullable receiver types because the selectedInputPort is not guaranteed to exist
fun MidiInputPort?.send(midiData: ByteArray) {
    this?.send(midiData, 0, midiData.size)
}