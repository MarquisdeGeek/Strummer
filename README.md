# Strummer
Making a keyboard sound like a (strummed) guitar.

It does this by changing the notes from a keyboard chord into a guitar chord, and playing them back with a delay between them, to simulate the time between successive strings being played.

## Getting it to work

1. Load the software info Processing. I'm using version 2.2.1. Other 2.x should work.
2. Attach your Arduino, with the circuit shown in schematic.png
3. Note the port to which the Arduino is connected. (This will appear in the Processing console output.)
4. Amend the code to use the correct port. The line begins 'arduino = new Arduino'. Change index of arduinoList.
5. Plug in your MIDI controller keyboard
6. Determine an appropriate MIDI input/output device. (These will appear in the Processing console output.)
7. Amend the code to use the correct port. The line begins 'MidiBus myBus = new MidiBus'. Change the names to match the output.

## The two modes

By default the code runs in 'auto-strum' mode. This means that whenever a chord is recognized by the MIDI input, it is played through MIDI with a short (random) time interval between each string. This is set by:

```
bModeAutoStrum = true;
```

If you switch this to:

```
bModeAutoStrum = false;
```

you can use a suitably rigged Arduino to trigger the notes/strings individual, giving much more flexibility to the sound.

The software simply remaps the keyboard chord to a guitar chord, and plays only the notes that the Arduino says to.

There is also a 'bModeTriggerOnDown' mode, which causes the notes to sound when the string is touched. Otherwise, the sound is made when the contact is broken. This is more realistic and akin to a real guitar. In this mode, touching the string will damped then sound.

## Example

There's a short video at  http://youtu.be/f0hhPRaNRZ0

The web page for the project is at http://www.marquisdegeek.com/making_strummer


## Dependancies

The Minibus
Arduino (Firmata)

install both of these from 'Sketch->Import->Add library'

## Changelog

2015-03-29 
Option for 'trigger on note up'
Refactor to use SoundGenerator abstraction
Refactor to allow Guitar class to handle the playback of individual strings


2015-03-27
Initial release


## Future ideas

* Add a UI so patches and options can be changed whilst running
* Fade out note when 'released'
* Stop string retriggers within N ms of the last one
* Add MIDI through for note range: { from, to, use channel, use patch }


