package studiplayer.audio;

import java.util.Comparator;

public class AlbumComparator implements Comparator<AudioFile> {

	public int compare(AudioFile file1, AudioFile file2) {

		if (file1 == null || file2 == null)
			throw new RuntimeException();
		if (file1 instanceof TaggedFile && !(file2 instanceof TaggedFile))
			return 1;
		if (!(file1 instanceof TaggedFile) && file2 instanceof TaggedFile)
			return -1;
		if (!(file1 instanceof TaggedFile) && !(file2 instanceof TaggedFile))
			return 0;
		
		String album1 = ((TaggedFile) file1).getAlbum();
		String album2 = ((TaggedFile) file2).getAlbum();
		
		if (album1 == null && album2 == null) return 0;
        if (album1 == null) return -1;
        if (album2 == null) return 1;
        
		return album1.compareTo(album2);
	}

}