package studiplayer.audio;

import java.util.Comparator;

public class DurationComparator implements Comparator<AudioFile> {

	public int compare(AudioFile file1, AudioFile file2) {
		if(file1 == null || file2 == null) throw new RuntimeException();
		
        long duration1 =0;
        long duration2 =0;
        
        if(file1 instanceof TaggedFile){
            duration1 = ((TaggedFile)file1).getDuration();
        }else if(file1 instanceof WavFile){
            duration1 = ((WavFile)file1).getDuration();
        }
        if(file2 instanceof TaggedFile){
            duration2 = ((TaggedFile)file2).getDuration();
        }else if(file2 instanceof WavFile){
            duration2 = ((WavFile)file2).getDuration();
        } 
        return (int) (duration1 - duration2);
    }

}