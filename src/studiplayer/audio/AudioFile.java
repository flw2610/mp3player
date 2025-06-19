package studiplayer.audio;

import java.io.File;
public abstract class AudioFile {
	private String pathname;
	private String filename;
	protected String author;
	protected String title;

	public AudioFile() throws NotPlayableException {
		
	}

	public AudioFile(String path) throws NotPlayableException {
		parsePathname(path);
		parseFilename(filename);
		tryToReadPath(pathname);
	}

	private void tryToReadPath(String pathname)throws NotPlayableException{
		if(new File(pathname).canRead()==false){
			throw new NotPlayableException(pathname,"Can't read the pathname: "+pathname);
		}
	}

	public void parsePathname(String path) {
		path = path.replace((char)160, (char)32);
		path = path.trim();
		path = path.replace('\\', '/');
		// if path is empty
		if ((path == null) || (path.isEmpty())) {
			pathname = "";
			filename = "";
		}

		// if it dousn't contain '/' it is just the filename
		else if ((path.indexOf("/") < 0)) {
			pathname = path;
			filename = path;
		}
		// normal case
		else {
			// replace more // with single /
			for (int i = 0; i < path.length(); i++) {
				String replace = "/";
				for (int x = 0; x < i; x++)
					replace += "/";
				path = path.replace(replace, "/");
			}
			path = path.replace("//", "/");

			if (isWindows()) {
				path = path.replace("/", "\\");
			}

			else {
				if (path.contains(":")) {
					path = path.replace(":", "");
					path = "/"+path;
				}
				
			}
			
			pathname = path;
			
			if ((path.charAt(path.length() - 1) == '/') || (path.charAt(path.length() - 1) == '\\'))
				filename = "";
			else
				filename = (isWindows()) ? path.substring(path.lastIndexOf("\\") + 1)
						: path.substring(path.lastIndexOf("/") + 1);
				filename = filename.trim();
		}

	}

	public void parseFilename(String filename) {
		if (filename.equals("-")) {
			author = "";
			title = "-";
			return;
		}
		filename = filename.trim();
		if (filename.indexOf(" - ") <= 0) {
			author = "";
			if (filename.lastIndexOf('.') > 0) {
				title = filename.substring(0, filename.lastIndexOf('.'));
			}

			else title = "";
			return;
		} else {
			
			author = filename.substring(0, filename.indexOf(" - ")).trim();
		}
		
		int indexStart = filename.indexOf(" - ") + 1;
		int indexEnde = filename.lastIndexOf('.');
		if (indexStart < 0)
			indexStart = -1;
		title = filename.substring(indexStart + 1, indexEnde);
		title = title.trim();

	}

	public String getPathname() {
		return pathname;
	}

	public String getFilename() {
		return filename;
	}

	public String getAuthor() {
		return author;
	}

	public String getTitle() {
		return title;
	}

	public String toString() {
		if ((getAuthor() == null) || (getAuthor().equals("")))
			return getTitle();
		return getAuthor() + " - " + getTitle();

	}
	
	private boolean isWindows() {
		return System.getProperty("os.name").toLowerCase().indexOf("win") >= 0;
	}

	public abstract void play()throws NotPlayableException;
	public abstract void togglePause();
	public abstract void stop();
	public abstract String formatDuration();
	public abstract String formatPosition();

	
}
//cmd + Shift + F: Auto-format
//cmd +7: aufkommentieren