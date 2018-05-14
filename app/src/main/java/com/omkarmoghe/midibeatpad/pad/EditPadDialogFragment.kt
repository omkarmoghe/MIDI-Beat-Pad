package com.omkarmoghe.midibeatpad.pad

import android.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.omkarmoghe.midibeatpad.R
import com.omkarmoghe.midibeatpad.midi.Channel
import com.omkarmoghe.midibeatpad.midi.Note
import com.omkarmoghe.midibeatpad.midi.Octave
import kotlinx.android.synthetic.main.edit_pad_dialog_fragment.view.*

class EditPadDialogFragment: DialogFragment() {
    val logTag = "EditPadDialog"

    var listener: EditPadListener? = null

    companion object {
        private const val midiPadKey = "midiPadKey"

        fun newInstance(midiPad: MidiPad): EditPadDialogFragment {
            val args = Bundle()
            args.putParcelable(midiPadKey, midiPad)

            val fragment = EditPadDialogFragment()
            fragment.arguments = args

            return fragment
        }
    }

    fun withListener(listener: EditPadListener): EditPadDialogFragment {
        this.listener = listener
        return this
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        val view = activity.layoutInflater.inflate(R.layout.edit_pad_dialog_fragment, null)
        val midiPad = arguments.getParcelable(midiPadKey) as MidiPad

        // Set up view
        view.enablePadSwitch.isChecked = midiPad.enabled
        view.enablePadSwitch.setOnCheckedChangeListener({ _, isChecked ->  toggleControls(view, isChecked)})
        setUpSpinner(
                view.channelSpinner,
                Channel.values().map { channel -> channel.humanString },
                midiPad.channel.ordinal
        )
        setUpSpinner(
                view.noteSpinner,
                Note.values().map { note -> note.humanString },
                midiPad.note.ordinal
        )
        setUpSpinner(
                view.octaveSpinner,
                Octave.values().map { octave -> octave.humanString },
                midiPad.octave.ordinal

        )
        view.velocitySeekBar.progress = midiPad.velocity

        // Build dialog
        builder.setView(view)
        builder.setNegativeButton(R.string.cancel, { _: DialogInterface, _: Int -> listener?.onCancel() })
        builder.setPositiveButton(R.string.save, { _: DialogInterface, _: Int ->
            val newMidiPad = MidiPad(
                    channel = Channel.values()[view.channelSpinner.selectedItemPosition],
                    note = Note.values()[view.noteSpinner.selectedItemPosition],
                    octave = Octave.values()[view.octaveSpinner.selectedItemPosition],
                    velocity = view.velocitySeekBar.progress,
                    enabled = view.enablePadSwitch.isChecked
            )
            listener?.onSave(newMidiPad)
            // TODO: update shared prefs
        })

        return builder.create()
    }

    private fun toggleControls(view: View, enable: Boolean) {
        view.channelSpinner.isEnabled = enable
        view.noteSpinner.isEnabled = enable
        view.octaveSpinner.isEnabled = enable
        view.velocitySeekBar.isEnabled = enable
    }

    private fun setUpSpinner(spinner: Spinner, options: List<CharSequence>, default: Int = 0) {
        android.R.layout.simple_spinner_item
        val adapter: ArrayAdapter<CharSequence> = ArrayAdapter(context, R.layout.spinner_item, options)
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
        spinner.adapter = adapter
        spinner.setSelection(default)
    }

    interface EditPadListener {
        fun onSave(newMidiPad: MidiPad)
        fun onCancel()
    }
}
