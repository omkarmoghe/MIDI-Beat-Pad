# MIDI Beat Pad 🎹
DAW agnostic MIDI beat pad for Android M+

The goal of this project is to be a software based replacement for traditinal [MIDI controllers/pads](https://www.google.com/search?q=midi+pad&tbm=isch). This **is not** a synthesiser; it **does not** generate sounds. This application only generates MIDI _output_ and must be connected to something which takes in MIDI _input_ (like most DAWs) to generate actual sound.

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
**MIDI on Android requires a minimum SDK version of `23` == `Android M` == `6.0`.**

As of 4/16/18, there is no APK available for download. To run/install the app, clone the repo and build the project in Android Studio. 

This project was developed for my Nexus 6P and tested with GarageBand, Logic Pro X, and FL Studio 12. In theory (hah), it should work with any DAW that supports MIDI interfaces. YMMV.
