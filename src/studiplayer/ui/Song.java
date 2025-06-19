package studiplayer.ui;

import studiplayer.audio.AudioFile;

public class Song {
	private AudioFile audioFile;
	private String author;
	private String title;
	private String album;
	private String duration;
	
	public Song(AudioFile audioFile, String author, String title, String album, String duration) {
		this.audioFile = audioFile;
		this.author = author;
		this.title = title;
		this.album = album;
		this.duration = duration;
	}
	
	public AudioFile getAudioFile() {
	    return audioFile;
	}

	public String getAuthor() {
	    return author;
	}

	public String getTitle() {
	    return title;
	}

	public String getAlbum() {
	    return album;
	}

	public String getDuration() {
	    return duration;
	}

}
