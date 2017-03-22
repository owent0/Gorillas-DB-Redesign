package ROMdb;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.FileChooser;

import java.io.*;
import java.net.URISyntaxException;

/**
 * Created by Anthony Orio on 3/17/2017.
 */
public class FileHandler {

    public FileHandler() {  }

    public String getFilePath() throws IOException, URISyntaxException {
        String path = "";

        File file = retrieveFile();
        BufferedReader br = new BufferedReader( new FileReader(file) );
        path = br.readLine();

        if( path == null ) {
            BufferedWriter bw = new BufferedWriter( new FileWriter(file) );

            path = useFileChooser().getPath();
            bw.write(path);
            bw.flush();

            br.close();
            bw.close();

            return path;
        }
        else {
            return path;
        }
    }

    public void writeNewPath(String path) throws IOException, URISyntaxException {
        String savePath = FileHandler.class
                        .getProtectionDomain()
                        .getCodeSource()
                        .getLocation()
                        .toURI()
                        .getPath();

        File file = new File(savePath + "path.dat");

        BufferedWriter bw = new BufferedWriter( new FileWriter(file) );

        bw.write(path);
        bw.flush();
        bw.close();
    }

    public File retrieveFile() throws URISyntaxException, IOException {
        String path = FileHandler.class
                        .getProtectionDomain()
                        .getCodeSource()
                        .getLocation()
                        .toURI()
                        .getPath();

        File file = new File(path + "path.dat");
        file.createNewFile();

        return file;
    }

    public File useFileChooser() {
        // Create a file chooser object to select database.
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Database Resource File");

        // Filters out all other file types.
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Database Files", "*.mdb"));

        // Return the file and then extract its path.
        File selectedFile = fileChooser.showOpenDialog(null);

        return (selectedFile == null) ? null : selectedFile;
    }
}
