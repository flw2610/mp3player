package studiplayer.audio;

import java.util.List;
import java.util.LinkedList;
import java.io.File;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class PlayList implements Iterable<AudioFile> {
	private List<AudioFile> list = new LinkedList<>();
	private ControllablePlayListIterator playListIterator = new ControllablePlayListIterator(null);
	private String search;
	private AudioFile current = null;
	private SortCriterion sortCriterion;

	public PlayList() {
		sortCriterion = SortCriterion.DEFAULT;
	}

	public PlayList(String pathname) {
		this();
		loadFromM3U(pathname);
	}

	public List<AudioFile> getList() {
		return list;
	}

	public void add(AudioFile file) {
		list.add(file);
		resetIterator();
	}

	public void remove(AudioFile file) {
		list.remove(file);
		resetIterator();
	}

	public ControllablePlayListIterator iterator() {

		return new ControllablePlayListIterator(list, search, sortCriterion);

	}

	public void resetIterator() {
		playListIterator = iterator();
		if (playListIterator.hasNext())
			current = playListIterator.next();

	}

	// Return number of files in playlist
	public int size() {
		return list.size();
	}

	// Return currently selected AudioFile
	public AudioFile currentAudioFile() {
		return current;
	}

	// Move to next song; loops back if at the end or invalid
	public void nextSong() {
		if (playListIterator.hasNext())
			current = playListIterator.next();
		else {
			resetIterator();
		}

	}

	public AudioFile jumpToAudioFile(AudioFile audioFile) {
		
		current= playListIterator.jumpToAudioFile(audioFile);
		return current;
	}

	// Save playlist as M3U file
	public void saveAsM3U(String pathname) {
		try (FileWriter writer = new FileWriter(pathname)) {
			for (AudioFile file : list) {
				writer.write(file.getPathname());
				writer.write(System.lineSeparator());
			}
		} catch (IOException e) {
			throw new RuntimeException("not able to save M3U (pathname: " + pathname + ")", e);
		}
	}

	// Load playlist from M3U file
	public void loadFromM3U(String pathname) {
		list.clear();
		try (Scanner scanner = new Scanner(new File(pathname))) {
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine().trim();

				if (line.isEmpty() || line.startsWith("#")) {
					continue;
				}
				try {
					add(AudioFileFactory.createAudioFile(line));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		} catch (FileNotFoundException e) {
			System.out.println("Failed to load m3U file");
			// throw new RuntimeException("Failed to load M3U file: " + pathname, e);
		}
	}

	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
		resetIterator();
	}

	public SortCriterion getSortCriterion() {
		return sortCriterion;
	}

	public void setSortCriterion(SortCriterion sortCriterion) {
		this.sortCriterion = sortCriterion;
		resetIterator();
	}
	
	@Override
	public String toString() {
	    return list.toString();
	}

}
