# speech-to-text-itx

This program transcribes Norwegian speech to text by uploading one or more audio files to Google cloud and then using Google's Speech-to-Text API to get the transcription.

A GUI is included which is meant to visualize the "conversation" happening in the selected audio files. Each audio file is considered one participant in the conversation. The program attempts to keep the correct chronological order in the conversation. The visualization is in style of Facebook's Messenger, using differently colored bubbles to represent different participants in the conversation.

The audio files to be processed should belong together in a way where, for example, one audio file contains the first person's speech in a phone call, and the other file the other person's speech. Though the program works completely fine for more than 2 people/audio files, and also with just 1 person/audio file. The audio files need to be single-channel, uploading audio files with more than one audio channel will crash the program. This is not a limitation by Google, but rather a choice by us to simplify the program.

A JSON writer is also present in this project, as to provide an example for long-term storage of the results from Google.

To run this program, proper Google credentials need to be present, also the proper Google Cloud Java libraries need to be installed. The Google API wikis describe how to set this up.

Sox was used in this program: http://sox.sourceforge.net/
SoX is a cross-platform (Windows, Linux, MacOS X, etc.) command line utility that can convert various formats of computer audio files in to other formats. It can also apply various effects to these sound files. (From their website)

We used Sox to voluntarily place background noise on the audio files, because the Google APIs ignore silence in the uploaded audio files, which leads to completely butchered chronological order in a normal conversation. To use this in Java, we used a Java wrapper for Sox which we found on Github: https://github.com/corballis/sox-wrapper-java

Made for a bachelor's thesis at the University of South-East Norway.
