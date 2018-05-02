package com.omkarmoghe.midibeatpad.pad

import android.media.midi.MidiInputPort
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.omkarmoghe.midibeatpad.midi.Message
import com.omkarmoghe.midibeatpad.midi.send

class PadTouchListener(var midiPad: MidiPad, var inputPort: MidiInputPort?):
        View.OnTouchListener, View.OnClickListener {
    val TAG: String = "PadTouchListener"

    // Run's first
    override fun onTouch(v: View?, event: MotionEvent?): Boolean = if (midiPad.enabled) {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                v?.isPressed = true
                inputPort?.send(midiPad.toMidiByteArray(Message.NOTE_ON))
                true
            }
            MotionEvent.ACTION_UP -> {
                v?.isPressed = false
                inputPort?.send(midiPad.toMidiByteArray(Message.NOTE_OFF))
                true
            }
            else -> false
        }
    } else false

    // Runs if onTouch returns false or view.performClick() is called
    override fun onClick(v: View?) {
        Log.d(TAG, "$v clicked!")
    }
}