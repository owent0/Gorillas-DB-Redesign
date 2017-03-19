package ROMdb;

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

    public File retrieveFile() throws URISyntaxException, IOException {
        String path = FileHandler.class
                        .getProtectionDomain()
                        .getCodeSource()
                        .getLocation()          // oh dear
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

        return selectedFile;
    }
}
