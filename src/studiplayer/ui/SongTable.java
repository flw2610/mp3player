package studiplayer.ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import studiplayer.audio.AudioFile;
import studiplayer.audio.PlayList;
import studiplayer.audio.TaggedFile;
import studiplayer.audio.WavFile;

public class SongTable extends TableView<Song> {

	public SongTable(PlayList playList) {
		ObservableList<Song> list = convertToSongList(playList);

		TableColumn<Song, String> interpretColumn = new TableColumn<>("Interpret");
		interpretColumn.setCellValueFactory(new PropertyValueFactory<>("author"));

		TableColumn<Song, String> titleColumn = new TableColumn<>("Titel");
		titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));

		TableColumn<Song, String> albumColumn = new TableColumn<>("Album");
		albumColumn.setCellValueFactory(new PropertyValueFactory<>("album"));

		TableColumn<Song, String> durationColumn = new TableColumn<>("Länge");
		durationColumn.setCellValueFactory(new PropertyValueFactory<>("duration"));

		getColumns().addAll(interpretColumn, titleColumn, albumColumn, durationColumn);

		setItems(list);
	}

	public void refresh(PlayList playList) {
		ObservableList<Song> list = convertToSongList(playList);
		setItems(list);
		System.out.println("alles cool");
	}
	public void setRowSelectionHandler(EventHandler<MouseEvent> handler) {
		setOnMouseClicked(handler);
	}

	private ObservableList<Song> convertToSongList(PlayList playlist) {
		// playlist hat audiofiles, welche wir in songs konverten
		ObservableList<Song> songs = FXCollections.observableArrayList();
		if (!(playlist == null)) {

			for (AudioFile element : playlist) {
				String author = "";
				String title = "";
				String album = "";
				String duration = "";
				if (element instanceof WavFile) {
					WavFile wavFile = (WavFile) element;
					author = wavFile.getAuthor();
					title = wavFile.getTitle();
					album = "Single";
					duration = wavFile.formatDuration();
				}
				if (element instanceof TaggedFile) {
					TaggedFile taggedFile = (TaggedFile) element;
					author = taggedFile.getAuthor();
					title = taggedFile.getTitle();
					album = taggedFile.getAlbum();
					duration = taggedFile.formatDuration();
				}
				Song song = new Song(element, author, title, album, duration);
				songs.add(song);
			}
		}
		return songs;
	}


}
