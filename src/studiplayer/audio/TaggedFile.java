package studiplayer.audio;

import java.util.Map;
import studiplayer.basic.TagReader;
import java.util.List;
import java.util.ArrayList;

public class TaggedFile extends SampledFile{
    private String album;

    public TaggedFile()throws NotPlayableException {
        super();
        readAndStoreTags();
    }
    public TaggedFile(String path)throws NotPlayableException{
        super(path);
        readAndStoreTags();
    }
    public void readAndStoreTags()throws NotPlayableException {
    	try {
    		        Map<String, Object> tagMap = TagReader.readTags(getPathname());

        if (tagMap.containsKey("title")) {
            Object val = tagMap.get("title");
                this.title = ((String) val).trim();
        }

        if (tagMap.containsKey("author")) {
            Object val = tagMap.get("author");
                this.author = ((String) val).trim();
        }

        if (tagMap.containsKey("album")) {
            Object val = tagMap.get("album");
                this.album = ((String) val).trim();
            
        }

        if (tagMap.containsKey("duration")) {
            Object val = tagMap.get("duration");
                this.duration = (long) val;
            
        }
    	}catch(Exception e) {
    		throw new NotPlayableException(getPathname(),e);
    	}

    }
    
    public String getAlbum() {
        return album;
    }
    
    public String toString(){
        if (album != null) {
            return super.toString() + " - " + album + " - " + formatDuration().trim();
        } else {
            return super.toString() + " - " + formatDuration().trim();
        }
    }
}