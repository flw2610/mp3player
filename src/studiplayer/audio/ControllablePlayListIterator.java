package studiplayer.audio;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class ControllablePlayListIterator implements Iterator<AudioFile> {
	private List<AudioFile> playlist;
	private int index = 0;

	public ControllablePlayListIterator(List<AudioFile> playlist) {
		this.playlist = playlist;
	}

	public ControllablePlayListIterator(List<AudioFile> playlist, String search, SortCriterion sortCriterion) {
	    if ((search == null) || (search.equals(""))) {
	        this.playlist = new ArrayList<>(playlist); // Direkt kopieren
	    } else {
	        this.playlist = new ArrayList<>();
	        search = search.toLowerCase();
	        for (AudioFile a : playlist) {
	            if (a != null) {
	                if (a.getAuthor().toLowerCase().contains(search) || a.getTitle().toLowerCase().contains(search)) {
	                    this.playlist.add(a);
	                } else if (a instanceof TaggedFile) {
	                    TaggedFile tf = (TaggedFile) a;
	                    if (tf.getAlbum() != null && tf.getAlbum().toLowerCase().contains(search)) {
	                        this.playlist.add(a);
	                    }
	                }
	            }
	        }
	    }

	    // Sortieren je nach SortCriterion
	    switch (sortCriterion) {
	        case AUTHOR:
	            Collections.sort(this.playlist, new AuthorComparator());
	            break;
	        case TITLE:
	            Collections.sort(this.playlist, new TitleComparator());
	            break;
	        case ALBUM:
	            Collections.sort(this.playlist, new AlbumComparator());
	            break;
	        case DURATION:
	            Collections.sort(this.playlist, new DurationComparator());
	            break;
	        case DEFAULT:
	        default:
	            // keine Sortierung
	            break;
	    }
	}

	@Override
	public boolean hasNext() {
		if(playlist == null)return false;
		return (index < playlist.size());
	}

	@Override
	public AudioFile next() {
		return playlist.get(index++);
	}
	

	public AudioFile jumpToAudioFile(AudioFile file) {
		for (index = 0; index < playlist.size(); index++) {
			if (playlist.get(index).equals(file)) {
				index++;
				return file;
			}
		}
		return null;
	}

}
