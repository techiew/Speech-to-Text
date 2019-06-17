# speech-to-text-itx

This program transcribes Norwegian speech to text by uploading one or more audio files to Google cloud and then using Google's Speech-to-Text API to get the transcription.

A GUI is included which is meant to visualize the "conversation" happening in the selected audio files. Each audio file is considered one participant in the conversation. The program attempts to keep the correct chronological order in the conversation. The visualization is in style of Facebook's Messenger, using differently colored bubbles to represent different participants in the conversation.

The audio files to be processed should belong together in a way where, for example, one audio file contains the first person's speech in a phone call, and the other file the other person's speech. Though the program works completely fine for more than 2 people/audio files, and also with just 1 person/audio file. The audio files need to be single-channel, uploading audio files with more than one audio channel will crash the program. This is not a limitation by Google, but rather a choice by us to simplify the program.

A JSON writer is also present in this project, as to provide an example for long-term storage of the results from Google.
