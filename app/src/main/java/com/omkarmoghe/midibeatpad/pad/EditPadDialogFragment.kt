package com.omkarmoghe.midibeatpad.pad

import android.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.content.DialogInterface
import android.os.Bundle
import com.omkarmoghe.midibeatpad.R

class EditPadDialogFragment: DialogFragment() {
    val TAG = "EditPadDialog"

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

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)

        builder.setTitle(R.string.editPad)
        builder.setView(R.layout.edit_pad_dialog_fragment)
        builder.setPositiveButton(R.string.save, { _: DialogInterface, _: Int -> /* TODO: update pad & SharedPreferences */})
        builder.setNegativeButton(R.string.cancel, { _: DialogInterface, _: Int -> })

        return builder.create()
    }
}
