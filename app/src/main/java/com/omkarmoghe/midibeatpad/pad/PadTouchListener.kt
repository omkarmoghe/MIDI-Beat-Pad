package com.omkarmoghe.midibeatpad.pad

import android.media.midi.MidiInputPort
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.omkarmoghe.midibeatpad.midi.Message
import com.omkarmoghe.midibeatpad.midi.send

class PadTouchListener(private val midiPad: MidiPad, var inputPort: MidiInputPort?): View.OnTouchListener {
    val TAG: String = "PadTouchListener"

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        return when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                Log.d(TAG, "DOWN: $midiPad")
                inputPort?.send(midiPad.toMidiByteArray(Message.NOTE_ON))
                true
            }
            MotionEvent.ACTION_UP -> {
                Log.d(TAG, "UP: $midiPad")
                inputPort?.send(midiPad.toMidiByteArray(Message.NOTE_OFF))
                v?.performClick()
                true
            }
            else -> false
        }
    }
}