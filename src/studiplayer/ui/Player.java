package studiplayer.ui;

import java.io.File;

import javax.print.DocFlavor.URL;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import studiplayer.audio.AudioFile;
import studiplayer.audio.NotPlayableException;
import studiplayer.audio.PlayList;
import studiplayer.audio.SortCriterion;

public class Player extends Application {
	public static final String DEFAULT_PLAYLIST = "playlists/playList.cert.m3u";
	private static final String PLAYLIST_DIRECTORY = "/Users/flo/Praktikum/mp3player/";
	private static final String INITIAL_PLAY_TIME_LABEL = "00:00";
	private static final String NO_CURRENT_SONG = " - ";
	private PlayList playList;
	private boolean useCertPlayList = false;
	private Button playButton = createButton("play.jpg");
	private Button pauseButton = createButton("pause.jpg");
	private Button stopButton = createButton("stop.jpg");
	private Button nextButton = createButton("next.jpg");
	private Label playListLabel = new Label(PLAYLIST_DIRECTORY + DEFAULT_PLAYLIST);
	private Label currentSongLabel = new Label(NO_CURRENT_SONG);
	private Label playTimeLabel = new Label(INITIAL_PLAY_TIME_LABEL);

	private ChoiceBox<SortCriterion> sortChoiceBox = new ChoiceBox<SortCriterion>();
	private TextField searchTextField = new TextField();
	private Button filterButton = new Button("Anzeigen");
	private Stage stage;
	private SongTable songTable;
	private boolean paused;
	private boolean stopped;
	private PlayerThread playerThread;
	private TimerThread timerThread;

	public Player() {

	}

	@Override
	public void start(Stage stage) throws Exception {
		System.out.println("es wird die starmethode ausgeführt");
		loadPlayList(DEFAULT_PLAYLIST);
		setButtonStates(true, false, false, true);
		this.stage = stage;
		stage.setTitle("APA Player");
		BorderPane mainPane = new BorderPane();
		// top
		sortChoiceBox.getItems().addAll(SortCriterion.values());
		sortChoiceBox.setValue(SortCriterion.DEFAULT); // oder ein anderes gewünschtes Enum

		HBox ersteZeile = new HBox(new Label("Suchtext  "), searchTextField);
		HBox zweiteZeile = new HBox(new Label("Sortierung  "), sortChoiceBox, filterButton);
		VBox elements_top = new VBox(ersteZeile, zweiteZeile);
		TitledPane filter = new TitledPane("Filter", elements_top);

		// centre
		songTable = new SongTable(playList);
		songTable.setRowSelectionHandler(e -> handleSongTableClick());

		// bottom
		GridPane grid = new GridPane();
		grid.add(new Label("Playlist"), 0, 0);
		grid.add(playListLabel, 1, 0);
		grid.add(new Label("Aktuelle..."), 0, 1);
		grid.add(currentSongLabel, 1, 1);
		grid.add(new Label("Abspielz..."), 0, 2);
		grid.add(playTimeLabel, 1, 2);
		// interaktion Buttons
		playButton.setOnAction(e -> playCurrentSong());
		pauseButton.setOnAction(e -> pauseCurrentSong());
		stopButton.setOnAction(e -> stopCurrentSong());
		nextButton.setOnAction(e -> nextCurrentSong());

		HBox buttonLeiste = new HBox(playButton, pauseButton, stopButton, nextButton);
		VBox elements_bottom = new VBox(grid, buttonLeiste);

		// interaktion Buttons
		filterButton.setOnAction(e -> filtere(sortChoiceBox.getValue(), searchTextField.getText()));

		mainPane.setTop(filter);
		mainPane.setCenter(songTable);
		mainPane.setBottom(elements_bottom);
		Scene scene = new Scene(mainPane, 600, 400);
		stage.setScene(scene);
		stage.show();
	}

	private void handleSongTableClick() {
		Song selectedSong = songTable.getSelectionModel().getSelectedItem();
		AudioFile selectedAF = selectedSong.getAudioFile();
		playList.jumpToAudioFile(selectedAF);
		updateSongInfo(playList.currentAudioFile());
		//song jetzt auch abspielen
		stopCurrentSong();
		
		playCurrentSong();
	}

	private void filtere(SortCriterion sortCriterium, String searchField) {
		if (!(playList == null)) {
			playList.setSearch(searchField);
			playList.setSortCriterion(sortCriterium);
			songTable.refresh(playList);
		}
	}

	public static void main(String[] args) {
		launch(args);
	}

	public void setUseCertPlayList(boolean useCertPlayList) {
		this.useCertPlayList = useCertPlayList;
	}

	public void loadPlayList(String pathname) {
		if ((pathname == null) || (pathname.equals("")))
			pathname = DEFAULT_PLAYLIST;
		if (useCertPlayList&& Platform.isFxApplicationThread()) {
			// per file chooser auswählen
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Öffne eine M3U-Datei");

			File selectedFile = fileChooser.showOpenDialog(stage);

			pathname = selectedFile.getAbsolutePath();

		}
		this.playList = new PlayList(pathname);
	}

	private Button createButton(String iconfile) {
		Button button = null;
		try {
			java.net.URL url = getClass().getResource("/icons/" + iconfile);
			Image icon = new Image(url.toString());
			ImageView imageView = new ImageView(icon);
			imageView.setFitHeight(20);
			imageView.setFitWidth(20);
			button = new Button("", imageView);
			button.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
			button.setStyle("-fx-background-color: #fff;");
		} catch (Exception e) {
			System.out.println("Image " + "icons/" + iconfile + " not found!");
			System.exit(-1);
		}
		return button;
	}

	private void playCurrentSong() {
		updateSongInfo(playList.currentAudioFile());
		setButtonStates(false, true, true, true);
		startThreads(false);
		stopped = false;
		paused = false;
	}

	private void pauseCurrentSong() {
		if (paused) {
			// song weiterspielen
			playerThread.pause();
			terminateThreads(true);
			paused = false;
		} else {
			// song pausieren
			playerThread.pause();
			startThreads(true);
			paused = true;
		}

	}

	private void stopCurrentSong() {
		terminateThreads(false);
		playTimeLabel.setText(INITIAL_PLAY_TIME_LABEL);
		setButtonStates(true, false, false, true);
		stopped = true;
	}

	private void nextCurrentSong() {
		System.out.println("Switching to next AudioFile");
		if(paused) {
			pauseCurrentSong();
		}
		if (!stopped)
			stopCurrentSong();
		
		playList.nextSong();
		updateSongInfo(playList.currentAudioFile());
		playCurrentSong();
	}

	private void setButtonStates(boolean playButtonState, boolean pauseButtonState, boolean stopButtonState,
			boolean nextButtonState) {
		playButton.setDisable(!playButtonState);
		pauseButton.setDisable(!pauseButtonState);
		stopButton.setDisable(!stopButtonState);
		nextButton.setDisable(!nextButtonState);
	}

	private void updateSongInfo(AudioFile af) {
		Platform.runLater(() -> {
			if (af == null) {
				currentSongLabel.setText("no song");
				playTimeLabel.setText(INITIAL_PLAY_TIME_LABEL);
			} else {
				currentSongLabel.setText(af.toString());
				playTimeLabel.setText(af.formatPosition());
			}
		});

	}

	private void startThreads(boolean onlyTimer) {
		if(timerThread !=null) {
			terminateThreads(true);
		}
	    timerThread = new TimerThread();
	    timerThread.start();

	    if (!onlyTimer) {
	        playerThread = new PlayerThread();
	        playerThread.start();
	    }
	}


	private void terminateThreads(boolean onlyTimer) {
			if(timerThread!=null) {
				timerThread.terminate();
				timerThread.interrupt();
				timerThread = null;
			}
			
	
		if (!onlyTimer) {
			if(playerThread!=null) {
				playerThread.terminate();
				playerThread = null;
			}
				
		}
	}

	class PlayerThread extends Thread {
		private boolean stopped = false;
		private AudioFile af = playList.currentAudioFile();

		public void terminate() {
			stopped = true;
			if(af!=null)
			af.stop();
		}
		
		public void pause() {
			if(af!=null)
			af.togglePause();
		}

		public void run() {
			if(af !=null) {
				
			
			while (!stopped) {
				try {
					af.play();
				} catch (NotPlayableException e) {
					e.printStackTrace();
				}
				//nächstes Lied
				if(!stopped) {
					System.out.println("das lied ist fertig gespielt");
					
					nextCurrentSong();
				}
			}
			}
		}
	}

	class TimerThread extends Thread {
		private boolean stopped = false;
		AudioFile af = playList.currentAudioFile();
		public void terminate() {
			stopped = true;
		}

		public void run() {
			if(af !=null) {
				
			
			while (!stopped) {
				updateSongInfo(af);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					System.out.println("Timer Thread wird gestopped");
				}
			}
			}
		}
	}

}
