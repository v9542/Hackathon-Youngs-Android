package com.dodotdo.youngs.walkietalkie;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.util.Log;

import java.io.IOException;
import java.util.Arrays;

/**
 * Created by KimYebon on 16. 8. 3..
 */
public class Recorder {
    AudioRecord audioRecord;
    AudioTrack audioTrack;

    public static int bufferSize = 4096;

    public Recorder() {

        audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, 8000,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT, bufferSize);

        audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, 8000, AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT, bufferSize, AudioTrack.MODE_STREAM);


        audioTrack.play();
    }

    public void startRecording() {
        audioRecord.startRecording();
    }

    public void stopRecording() {
        audioRecord.stop();
    }


    public byte[] record(short[] buffer) throws IOException {
        audioRecord.read(buffer, 0, bufferSize);

        return shortToBytes(buffer);
    }

    public void playing(byte[] data, int length) {

        audioTrack.write(data, 0, length / 2);

    }

    public void stopPlaying() {

        audioTrack.stop();
        audioTrack.release();
    }



    public byte[] shortToBytes(short[] sData) {
        int shortArrsize = sData.length;
        byte[] bytes = new byte[shortArrsize * 2];

        for (int i = 0; i < shortArrsize; i++) {
            bytes[i * 2] = (byte) (sData[i] & 0xFF);
            bytes[(i * 2) + 1] = (byte) ((sData[i] >> 8)&0xff);
            sData[i] = 0;
        }
        return bytes;
    }
}
