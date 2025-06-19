package studiplayer.audio;

import studiplayer.basic.BasicPlayer;

public abstract class SampledFile extends AudioFile {
	protected long duration; // einheit ist Micro sekunden

	public SampledFile()throws NotPlayableException {
	}

	public SampledFile(String path)throws NotPlayableException {
		super(path);
	}

	public void play() throws NotPlayableException {
		try {
			BasicPlayer.play(getPathname());
		} catch (Exception e) {
			throw new NotPlayableException(getPathname(), e);
		}
	}

	public void togglePause() {
		BasicPlayer.togglePause();
	}

	public void stop() {
		BasicPlayer.stop();
	}

	public long getDuration() {
		return duration;
	}
	

	public String formatDuration() {
		long duration = getDuration();
		return timeFormatter(duration);
	}

	public String formatPosition() {
		long position = BasicPlayer.getPosition();
		return timeFormatter(position);
	}

	public static String timeFormatter(long timeInMicroSeconds) {
		long timeInSeconds = timeInMicroSeconds / (long) Math.pow(10, 6);
		long minutes = timeInSeconds / 60;
		long seconds = timeInSeconds % 60;

		if ((timeInMicroSeconds < 0) || (timeInSeconds > 99 * 60 + 59)) {
			throw new RuntimeException("Time is not accentable (to short or long)");
		}

		return String.format("%02d:%02d", minutes, seconds);
	}

}