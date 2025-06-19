package studiplayer.audio;


public class AudioFileFactory {

    public static AudioFile createAudioFile(String path) throws NotPlayableException {
        int index_of_dot = path.lastIndexOf('.');
        if (index_of_dot < 0) {
            throw new RuntimeException("Unknown suffix for AudioFile: \"" + path + "\"");
        }
        
        String ending = path.substring(index_of_dot + 1).toLowerCase();
        if (ending.equals("wav")) {
            return new WavFile(path);
        } else if (ending.equals("mp3") || ending.equals("ogg")) {
            return new TaggedFile(path);
        } else {
            throw new RuntimeException("Unknown suffix for AudioFile: \"" + path + "\"");
        }

    }	
}
