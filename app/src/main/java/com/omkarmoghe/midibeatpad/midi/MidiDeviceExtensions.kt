package com.omkarmoghe.midibeatpad.midi

import android.media.midi.MidiDevice
import android.media.midi.MidiDeviceInfo
import android.media.midi.MidiInputPort
import android.media.midi.MidiOutputPort

val MidiDevice.ports: Array<out MidiDeviceInfo.PortInfo>
    get() = info.ports

fun MidiDevice.findFirstInputPort(): MidiDeviceInfo.PortInfo? = ports.find { port -> port.type == MidiDeviceInfo.PortInfo.TYPE_INPUT }
fun MidiDevice.openFirstInputPort(): MidiInputPort = openInputPort(findFirstInputPort()!!.portNumber)

fun MidiDevice.findFirstOutputPort(): MidiDeviceInfo.PortInfo? = ports.find { port -> port.type == MidiDeviceInfo.PortInfo.TYPE_OUTPUT }
fun MidiDevice.openFirstOutputPort(): MidiOutputPort = openOutputPort(findFirstOutputPort()!!.portNumber)
