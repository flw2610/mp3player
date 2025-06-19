package studiplayer.audio;

import java.util.Comparator;

public class TitleComparator implements Comparator<AudioFile> {

	public int compare(AudioFile file1, AudioFile file2){
		if(file1 == null || file2 == null) throw new RuntimeException();
		String title1 = file1.getTitle();
		String title2 = file2.getTitle();
		return title1.compareTo(title2);
    }
}