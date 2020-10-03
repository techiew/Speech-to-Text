# Speech-to-Text

**The transcription in the video is pretty bad, usually when provided with high quality audio files the transcription is pretty good. The result is based on what the Google API gives us.**

[![Youtube Video](https://github.com/techiew/Speech-to-Text/blob/master/thumbnail.png)](https://www.youtube.com/watch?v=DYBjBtlrQes&feature=youtu.be)

### How it works

This program/library transcribes Norwegian speech to text by uploading one or more audio files to Google Cloud and then uses Google's Speech-to-Text API to get the transcription. 

A GUI exists in this program which is meant to visualize the conversation happening in the selected audio files. Each audio file is considered one participant in the conversation. In other words, an audio file should contain a recording of one person speaking, preferably to someone else in the other selected files. The program attempts to keep the correct chronological order in the conversation (and is usually decent at it). The visualization is in style of Facebook's Messenger, using differently colored bubbles to represent different participants in the conversation.

### Audio files
The audio files to be processed should belong together in a way where, for example, one audio file contains the first person's speech in a phone call, and the other file the other person's speech. However the program works completely fine for more than 2 audio files, and also with just 1 audio file. The audio files need to be single-channel, uploading audio files with more than one audio channel will crash the program. This is not a limitation by Google, but a choice by us to simplify development.

A JSON writer is present in this project, as to provide an example for long-term storage of the transcriptions from Google.

### Google credentials
To run this program, proper Google credentials need to be present, also the proper Google Cloud Java libraries need to be installed. The Google API wikis describe how to set this up.

### SoX
SoX was used in this program: http://sox.sourceforge.net/
SoX is a cross-platform (Windows, Linux, MacOS X, etc.) command line utility that can convert various formats of computer audio files into other formats. It can also apply various effects to these sound files. (Description taken from their website)

We used SoX to voluntarily place background noise on the audio files, because the Google APIs ignore silence in the uploaded audio files, which leads to completely butchered chronological order in a normal conversation when one person stays quiet to let the other person speak. To use Sox in Java, we used a Java wrapper for Sox which we found on Github: https://github.com/corballis/sox-wrapper-java

Made for a bachelor's thesis at the University of South-East Norway.
