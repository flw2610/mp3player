package studiplayer.audio;

import studiplayer.basic.WavParamReader;

public class WavFile extends SampledFile{

    public WavFile()throws NotPlayableException {
        super();
        readAndSetDurationFromFile();
    }
    public WavFile(String path)throws NotPlayableException{
        super(path);
        readAndSetDurationFromFile();
    }
    
    public String toString(){
        return super.toString() + " - " + formatDuration().trim();
    }
    public void readAndSetDurationFromFile() throws NotPlayableException {
    	try {
    		 WavParamReader.readParams(getPathname());
        float frameRate = WavParamReader.getFrameRate();
        long numberOfFrames = WavParamReader.getNumberOfFrames();

        this.duration = computeDuration(numberOfFrames,frameRate);
    	}
    	catch(Exception e) {
    		throw new NotPlayableException(getPathname(), e);
    	}
       
    }

    public static long computeDuration(long numberOfFrames,float frameRate) {
        return (long)((numberOfFrames / frameRate) * 1000000L);	
    }

    public long getDuration() {
        return this.duration;
    }

    
}