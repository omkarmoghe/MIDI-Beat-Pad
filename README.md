# MIDI Beat Pad
DAW agnostic MIDI beat pad for Android M+

# Roadmap
All the shit I need to do.

## MIDI Library
Collection of constants, useful extensions, and wrappers for Android's MIDI package. 
- [x] Channel constants
- [ ] Message constants
- [x] Note constants
- [x] Octave constants
- [ ] MidiDevice extensions

## MIDI Interface
Zero-lag sending and receiving of MIDI data.
- [ ] Multithreaded event scheduler for sending notes

## User Interface
Virtual beat pad & keyboard
- [x] Pad
- [ ] Keyboard
- [ ] Lights & color
- [ ] Velocity control
- [ ] Note selector

# Building & Installing
**MIDI on Android requires a minimum SDK version of `23` == `M` == `6.0`.**

As of 4/16/18, there is no APK available for download. To run install the app, clone the repo and build the project in Android Studio. 

This project was developed on my Nexus 6P and tested with GarageBand, Logic Pro X, and FL Studio 12. In theory (hah), it should work with any DAW that supports MIDI interfaces. YMMV.