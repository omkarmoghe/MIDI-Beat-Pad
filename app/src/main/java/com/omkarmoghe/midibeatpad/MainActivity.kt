package com.omkarmoghe.midibeatpad

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.media.midi.MidiDevice
import android.media.midi.MidiDeviceInfo
import android.media.midi.MidiInputPort
import android.media.midi.MidiManager
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_main.*
import com.omkarmoghe.midibeatpad.midi.*

class MainActivity : Activity() {
    val TAG: String = "MainActivity"

    var midiManager: MidiManager? = null
    var allDevices: MutableSet<MidiDeviceInfo> = mutableSetOf()
    var selectedDevice: MidiDevice? = null
    var selectedInputPort: MidiInputPort? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (applicationContext.packageManager.hasSystemFeature(PackageManager.FEATURE_MIDI)) {
            midiManager = applicationContext.getSystemService(Context.MIDI_SERVICE) as MidiManager
            midiManager!!.registerDeviceCallback(midiDeviceCallback, null)
            allDevices.addAll(midiManager!!.devices)

            setUpDevicesSpinner()
            setUpTestButton()
        } else {
            Log.d(TAG, "No MIDI support :(")
        }
    }

    fun setUpDevicesSpinner() {
        val options = allDevices.map { device -> device.properties.getCharSequence(MidiDeviceInfo.PROPERTY_NAME) }
        val adapter: ArrayAdapter<CharSequence> = ArrayAdapter(this, android.R.layout.simple_spinner_item, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        devicesSpinner.adapter = adapter
        devicesSpinner.onItemSelectedListener = devicesSpinnerListener
    }

    fun setUpTestButton() {
        val c4On = byteArrayOf(
            getMidiMessage(Message.NOTE_ON, Channel.THREE).toByte(),
            getMidiNote(Note.C).toByte(),
            127.toByte()
        )
        val c4Off = byteArrayOf(
                getMidiMessage(Message.NOTE_OFF, Channel.THREE).toByte(),
                getMidiNote(Note.C).toByte(),
                127.toByte()
        )

        pad1.setOnTouchListener { view, motionEvent ->
            // escape early if no input port available
            if (selectedInputPort == null) return@setOnTouchListener false

            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    Log.d(TAG, "pad down")
                    selectedInputPort!!.send(c4On, 0, c4On.size)
                    return@setOnTouchListener true
                }
                MotionEvent.ACTION_UP -> {
                    Log.d(TAG, "pad up")
                    selectedInputPort!!.send(c4Off, 0, c4Off.size)
                    return@setOnTouchListener true
                }
                else -> return@setOnTouchListener false
            }
        }
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
            Log.d(TAG, "Selected device: $selectedDeviceInfo")
        }
    }

    private val midiDeviceCallback = object: MidiManager.DeviceCallback() {
        override fun onDeviceRemoved(device: MidiDeviceInfo?) {
            if (device != null) {
                Log.d(TAG, "Device removed: $device")
                allDevices.remove(device)
                runOnUiThread { setUpDevicesSpinner() }
            }
        }

        override fun onDeviceAdded(device: MidiDeviceInfo?) {
            if (device != null) {
                Log.d(TAG, "Device added: $device")
                allDevices.add(device)
                runOnUiThread { setUpDevicesSpinner() }
            }
        }
    }
}
