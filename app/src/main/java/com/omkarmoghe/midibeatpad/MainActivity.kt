package com.omkarmoghe.midibeatpad

import android.app.Activity
import android.app.FragmentManager
import android.content.Context
import android.content.pm.PackageManager
import android.media.midi.MidiDevice
import android.media.midi.MidiDeviceInfo
import android.media.midi.MidiInputPort
import android.media.midi.MidiManager
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.LinearLayout
import com.omkarmoghe.midibeatpad.midi.openFirstInputPort
import com.omkarmoghe.midibeatpad.pad.MidiPad
import com.omkarmoghe.midibeatpad.pad.PadTouchListener
import com.omkarmoghe.midibeatpad.settings.SettingsFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : Activity() {
    val tag: String = "MainActivity"

    var midiManager: MidiManager? = null
    var allDevices: MutableSet<MidiDeviceInfo> = mutableSetOf()
    var selectedDevice: MidiDevice? = null
    var selectedInputPort: MidiInputPort? = null

    // onCreate(s)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setActionBar(toolbar)
        actionBar.setDisplayShowTitleEnabled(false)

        if (applicationContext.packageManager.hasSystemFeature(PackageManager.FEATURE_MIDI)) {
            midiManager = applicationContext.getSystemService(Context.MIDI_SERVICE) as MidiManager
            midiManager!!.registerDeviceCallback(midiDeviceCallback, null)
            allDevices.addAll(midiManager!!.devices)

            setUpDevicesSpinner()
            setUpRack()
        } else {
            Log.d(tag, "No MIDI support :(")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean = try {
        menuInflater.inflate(R.menu.main_menu, menu)
        true
    } catch (e: Exception) {
        super.onCreateOptionsMenu(menu)
    }

    // UI

    private fun setUpDevicesSpinner() {
        val options = allDevices.map { device -> device.properties.getCharSequence(MidiDeviceInfo.PROPERTY_NAME) }
        val adapter: ArrayAdapter<CharSequence> = ArrayAdapter(this, R.layout.spinner_item, options)
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
        devicesSpinner.adapter = adapter
        devicesSpinner.onItemSelectedListener = devicesSpinnerListener
    }

    private fun setUpRack() {
        val numRows = 4 // TODO: get this from shared prefs
        val padsPerRow = 3 // TODO: get this from shared prefs

        repeat(numRows, {
            layoutInflater.inflate(R.layout.row, rack, true)
        })

        repeat(rack.childCount, { index ->
            val row: LinearLayout = rack.getChildAt(index) as LinearLayout
            repeat(padsPerRow, {
                val pad: Button = layoutInflater.inflate(R.layout.pad, row, false) as Button
                val padTouchListener = PadTouchListener(MidiPad(), selectedInputPort, fragmentManager)
                pad.setOnTouchListener(padTouchListener)
                pad.setOnClickListener(padTouchListener)
                row.addView(pad)
            })
        })
    }

    // Callbacks

    override fun onNavigateUp(): Boolean {
        actionBar.setDisplayHomeAsUpEnabled(false)
        actionBar.setDisplayShowHomeEnabled(false)
        fragmentManager.popBackStack("rack", FragmentManager.POP_BACK_STACK_INCLUSIVE)
        return super.onNavigateUp()
    }

    override fun onBackPressed() {
        actionBar.setDisplayHomeAsUpEnabled(false)
        actionBar.setDisplayShowHomeEnabled(false)
        super.onBackPressed()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean = when (item?.itemId) {
        R.id.actionAssignPads -> {
            // TODO: set all pads.editing to true
            true
        }
        R.id.actionSettings -> {
            // TODO: send this to another activity as in intent and hook back button for that activity to finish()
            fragmentManager.beginTransaction()
                    .replace(R.id.rack, SettingsFragment(), "settings")
                    .addToBackStack("rack")
                    .commit()
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setDisplayShowHomeEnabled(true)
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private val devicesSpinnerListener = object: AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) {
            selectedDevice = null
            selectedInputPort = null
        }

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            val deviceName = parent?.getItemAtPosition(position) as String
            val selectedDeviceInfo = allDevices.find { device ->
                device.properties.getString(MidiDeviceInfo.PROPERTY_NAME) == deviceName
            }
            midiManager?.openDevice(
                    selectedDeviceInfo!!,
                    { device ->
                        selectedDevice = device
                        selectedInputPort = selectedDevice!!.openFirstInputPort()
                    },
                    null
            )
            Log.d(tag, "Selected device: $selectedDeviceInfo")
        }
    }

    private val midiDeviceCallback = object: MidiManager.DeviceCallback() {
        override fun onDeviceRemoved(device: MidiDeviceInfo?) {
            Log.d(tag, "Device removed: $device")
            if (device != null) {
                allDevices.remove(device)
                runOnUiThread { setUpDevicesSpinner() }
            }
        }

        override fun onDeviceAdded(device: MidiDeviceInfo?) {
            if (device != null) {
                Log.d(tag, "Device added: $device")
                allDevices.add(device)
                runOnUiThread { setUpDevicesSpinner() }
            }
        }
    }
}
