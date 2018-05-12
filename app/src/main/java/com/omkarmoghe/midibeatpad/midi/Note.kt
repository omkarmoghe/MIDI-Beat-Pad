package com.omkarmoghe.midibeatpad.midi

/**
 * Valid MIDI notes
 */
enum class Note(val id: Int, val humanString: String = id.toString()) {
    C(0, "C"),
    C_SHARP(1, "C#"),
    D(2, "D"),
    D_SHARP(3, "D#"),
    E(4, "E"),
    F(5, "F"),
    F_SHARP(6, "F#"),
    G(7, "G"),
    G_SHARP(8, "G#"),
    A(9, "A"),
    A_SHARP(10, "A#"),
    B(11, "B"),
}
