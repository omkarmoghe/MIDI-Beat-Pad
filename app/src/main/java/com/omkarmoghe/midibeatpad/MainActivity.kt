package com.omkarmoghe.midibeatpad

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.media.midi.MidiDevice
import android.media.midi.MidiDeviceInfo
import android.media.midi.MidiManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_main.*
import com.omkarmoghe.midibeatpad.midi.*

class MainActivity : Activity() {
    val TAG: String = "MainActivity"

    var allDevices: MutableSet<MidiDeviceInfo> = mutableSetOf()
    var selectedDevice: MidiDevice? = null
    var midiManager: MidiManager? = null

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
        pad1.setOnClickListener {
            if (selectedDevice != null) {
                Log.d(TAG, "Button 1 pressed")
                val portInfos = selectedDevice?.ports
                val inputPortInfo = portInfos?.find { port -> port.type == MidiDeviceInfo.PortInfo.TYPE_INPUT }

                if (inputPortInfo != null) {
                    val inputPort = selectedDevice?.openInputPort(inputPortInfo.portNumber)
                    val midiBytes = byteArrayOf(
                            getMidiMessage(Message.NOTE_ON, Channel.THREE).toByte(),
                            getMidiNote(Note.C).toByte(),
                            127.toByte(),
                            getMidiMessage(Message.NOTE_OFF, Channel.THREE).toByte(),
                            getMidiNote(Note.C).toByte(),
                            0.toByte()
                    )
                    inputPort?.send(midiBytes, 0, midiBytes.size)

                }
            } else {
                Log.d(TAG, "No selected device")
            }
        }
    }

    private val devicesSpinnerListener = object: AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) {
            selectedDevice = null
        }

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            val deviceName = parent?.getItemAtPosition(position) as String
            val selectedDeviceInfo = allDevices.find { device -> device.properties.getString(MidiDeviceInfo.PROPERTY_NAME) == deviceName }
            midiManager?.openDevice(selectedDeviceInfo!!,
                    { device -> selectedDevice = device },
                    null)
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
