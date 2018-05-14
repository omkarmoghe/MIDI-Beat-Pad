package com.omkarmoghe.midibeatpad.pad

import android.app.FragmentManager
import android.media.midi.MidiInputPort
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import com.omkarmoghe.midibeatpad.midi.Channel
import com.omkarmoghe.midibeatpad.midi.Message
import com.omkarmoghe.midibeatpad.midi.Note
import com.omkarmoghe.midibeatpad.midi.send

class PadTouchListener(
        var midiPad: MidiPad,
        var inputPort: MidiInputPort?,
        val fragmentManager: FragmentManager
): View.OnTouchListener, View.OnClickListener, EditPadDialogFragment.EditPadListener {
    private val tag: String = "PadTouchListener"

    var editing: Boolean = true

    // Run's first
    override fun onTouch(v: View?, event: MotionEvent?): Boolean = if (midiPad.enabled && !editing) {
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
        if (editing) EditPadDialogFragment.newInstance(midiPad).withListener(this).show(fragmentManager, null)
    }

    // Run when pad is in edit mode
    override fun onSave(newMidiPad: MidiPad) {
        midiPad = newMidiPad
        Log.d(tag, "Edit pad saved")
    }

    override fun onCancel() {
        Log.d(tag, "Edit pad cancelled")
    }
}