package rtp;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Control;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

import ss.AudioSS;
import ui.MainWindow;

/** An instance of this class sends a request to a specific
 * server. Once the request is received by the server, this
 * instance receives information (audio only as of Feb.08.2011),
 * spatializes the sound, and plays it to the receiver
 */
public class AudioReceiver extends Thread {
	boolean stopCapture = false;
	
	ByteArrayOutputStream byteArrayOutputStream;

	AudioFormat targetAudioFormat;
	AudioFormat audioFormat;

	TargetDataLine targetDataLine;

	// AudioInputStream audioInputStream;

	BufferedOutputStream out = null;

	BufferedInputStream in = null;

	Socket sock = null;

	SourceDataLine sourceDataLine;
	
	AudioSS audioSS;
	FloatControl gainCtrl;
	FloatControl balCtrl;
	
	String ipAdd = "";
	int ptNum = 0;

	public AudioSS getAudioSS() {
		return this.audioSS;
	}
	
	/** Constructor: creates an AudioReceiver that receives
	 * from IP Address ip through port pt */
	public AudioReceiver(String ip, int pt) {
		this.ipAdd = ip;
		this.ptNum = pt;
		this.audioSS = new AudioSS(0, 0);
	}
	
	public void run() {
		this.captureAudio();
	}

	/** request information from a specific server
	 * and capture the audio that is sent */
	private void captureAudio() {
		try {
			int created = 0;
			while (created < 10) {
				// Creates a stream socket from a specific IP address at port 5000
				try {
					sock = new Socket(this.ipAdd, this.ptNum);
					created = 10;
				}
				catch (IOException E) {
					System.out.println("IO Exception. Try again.");
					Thread.sleep(1000);
					created++;
				}
			}
			System.out.println("New Socket: " + this.ipAdd + ", " +  this.ptNum);
			out = new BufferedOutputStream(sock.getOutputStream());
			in = new BufferedInputStream(sock.getInputStream());

			// get information of the available mixer(s)
			Mixer.Info[] mixerInfo = AudioSystem.getMixerInfo();
			System.out.println("Available mixers:");
			for (int cnt = 0; cnt < mixerInfo.length; cnt++) {
				System.out.println(mixerInfo[cnt].getName());
			}
			targetAudioFormat = getTargetAudioFormat();

			DataLine.Info dataLineInfo = new DataLine.Info(
					TargetDataLine.class, targetAudioFormat);

			Mixer mixer = AudioSystem.getMixer(mixerInfo[3]);

			targetDataLine = (TargetDataLine) mixer.getLine(dataLineInfo);

			targetDataLine.open(targetAudioFormat);
			targetDataLine.start();

			Thread captureThread = new CaptureThread();
			captureThread.start();

			audioFormat = getSourceAudioFormat();
			DataLine.Info dataLineInfo1 = new DataLine.Info(
					SourceDataLine.class, audioFormat);
			sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo1);
			sourceDataLine.open(audioFormat);
			sourceDataLine.start();
			FloatControl gainCtrl = (FloatControl) sourceDataLine.getControl(FloatControl.Type.MASTER_GAIN);
			FloatControl balCtrl = (FloatControl) sourceDataLine.getControl(FloatControl.Type.BALANCE);
			
			Thread playThread = new PlayThread();
			playThread.start();

		} catch (Exception e) {
			System.out.println(e);
			System.exit(0);
		}
	}

	/** */
	class CaptureThread extends Thread {

		byte tempBuffer[] = new byte[10000];

		public void run() {
			byteArrayOutputStream = new ByteArrayOutputStream();
			stopCapture = false;
			try {
				while (!stopCapture) {

					int cnt = targetDataLine.read(tempBuffer, 0,
							tempBuffer.length);

					out.write(tempBuffer);

					if (cnt > 0) {

						byteArrayOutputStream.write(tempBuffer, 0, cnt);

					}
				}
				byteArrayOutputStream.close();
			} catch (Exception e) {
				System.out.println(e);
				System.exit(0);
			}
		}
	}

	/** Audio format of the target source:
	 * 8kHz, signed 16-bit, 1-channel (mono), little-Endian */
	private AudioFormat getTargetAudioFormat() {
		float sampleRate = 8000.0F;

		int sampleSizeInBits = 16;

		int channels = 1;

		boolean signed = true;

		boolean bigEndian = false;

		return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed,
				bigEndian);
	}
	
	/** Audio format of the source sent to the mixer:
	 * 8kHz, signed 16-bit, 2-channel (stereo), little-Endian */
	private AudioFormat getSourceAudioFormat() {
		float sampleRate = 8000.0F;

		int sampleSizeInBits = 16;

		int channels = 2;

		boolean signed = true;

		boolean bigEndian = false;

		return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed,
				bigEndian);
	}

	/** a thread that reads 10000 bytes of data from the input stream
	 * and writes it into a sourceDataLine, which is used as a source for the mixer */
	class PlayThread extends Thread {
		byte tempBuffer[] = new byte[10000];

		public void run() {
			try {
				while (in.read(tempBuffer) != -1) {
					byte[] ssTemp = audioSS.spatialize(MainWindow.curr.num, tempBuffer);
					gainCtrl.setValue(audioSS.getGain(MainWindow.curr.num));
					balCtrl.setValue(audioSS.getBalance(MainWindow.curr.num));
					sourceDataLine.write(ssTemp, 0, ssTemp.length);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
