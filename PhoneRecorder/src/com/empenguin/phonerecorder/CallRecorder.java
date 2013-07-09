package com.empenguin.phonerecorder;

import java.io.IOException;
import android.media.MediaRecorder;

public class CallRecorder {
	private MediaRecorder mRecoder= null;

	public void start(String path) throws IllegalStateException, IOException {
		mRecoder = new MediaRecorder();
		mRecoder.reset();
		mRecoder.setAudioSource(MediaRecorder.AudioSource.VOICE_CALL);
		mRecoder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		mRecoder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
		mRecoder.setOutputFile(path);
		mRecoder.prepare();
		mRecoder.start();
	}
	
	public void stop() {
		mRecoder.stop();
		mRecoder.reset();
		mRecoder.release();
	}
}
