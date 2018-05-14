package com.omkarmoghe.midibeatpad.pad

import android.os.Parcel
import android.os.Parcelable
import com.omkarmoghe.midibeatpad.midi.*

class MidiPad(
        var channel: Channel = Channel.ONE,
        var note: Note = Note.C,
        var octave: Octave = Octave.FOUR,
        var velocity: Int = 127,
        var enabled: Boolean = true
): Parcelable {
    constructor(parcel: Parcel) : this(
            Channel.valueOf(parcel.readString()),
            Note.valueOf(parcel.readString()),
            Octave.valueOf(parcel.readString()),
            parcel.readInt()) {
        enabled = parcel.readByte() != 0.toByte()
    }

    fun toMidiByteArray(message: Message): ByteArray =
            byteArrayOf(
                    getMidiMessage(message, channel).toByte(),
                    getMidiNote(note, octave).toByte(),
                    velocity.toByte()
            )

    override fun toString(): String {
        return "MidiPad: Channel(${channel.name}) Note(${note.name}${octave.multiplier}) Velocity($velocity)"
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(channel.name)
        dest?.writeString(note.name)
        dest?.writeString(octave.name)
        dest?.writeInt(velocity)
        dest?.writeByte(if (enabled) 1.toByte() else 0.toByte())
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<MidiPad> {
        override fun createFromParcel(parcel: Parcel): MidiPad {
            return MidiPad(parcel)
        }

        override fun newArray(size: Int): Array<MidiPad?> {
            return arrayOfNulls(size)
        }
    }
}