package studiplayer.audio;

import java.util.Comparator;

public class AuthorComparator implements Comparator<AudioFile> {

	public int compare(AudioFile file1, AudioFile file2){
        if(file1 == null || file2 == null) throw new RuntimeException();
        
        String author1 = file1.getAuthor();
        String author2 = file2.getAuthor();
        
       return author1.compareTo(author2);
    }

}