package ROMdb.Helpers;

import javafx.stage.FileChooser;
import java.io.*;
import java.net.URISyntaxException;

/**
 * Created by Anthony Orio on 3/17/2017.
 */
public class FileHandler {

    /**
     * Default constructor.
     */
    public FileHandler() {  }

    /**
     * Retrieves the path of the file for loading the database.
     * @return the string representation of the file path.
     * @throws IOException If I/O error occurs.
     * @throws URISyntaxException If path cannot be found for jar file.
     */
    public String getFilePath() throws IOException, URISyntaxException {
        String path = "";

        // Retrieve the file to get the path from.
        File file = retrieveFile();

        // Prepare buffer reader to read the file.
        BufferedReader br = new BufferedReader( new FileReader(file) );

        // Read the file/
        path = br.readLine();

        // This should only run if its the first time running
        // or the path file was deleted somehow. Otherwise else
        // should mainly run.
        if( path == null ) {
            BufferedWriter bw = new BufferedWriter( new FileWriter(file) );

            // Use the file chooser to select the file.
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

    /**
     * Writes a path to the file called path.dat.
     * @param path the path to write into the file.
     * @throws IOException If I/O error occurs.
     * @throws URISyntaxException If jar path cannot be found.
     */
    public void writeNewPath(String path) throws IOException, URISyntaxException {

        // This is the path of the jar file. We use this
        // to create the file right next to the jar file
        // even if the jar is moved to a different directory.
        String savePath = FileHandler.class
                        .getProtectionDomain()
                        .getCodeSource()
                        .getLocation()
                        .toURI()
                        .getPath();

        // Get the new file.
        File file = new File(savePath + "path.dat");

        BufferedWriter bw = new BufferedWriter( new FileWriter(file) );

        bw.write(path);
        bw.flush();
        bw.close();
    }

    /**
     * Retrieves the file to look at and creates it if it doesn't exist yet.
     * @return the file that contains the path to the database.
     * @throws URISyntaxException If jar path cannot be found.
     * @throws IOException If I/O error occurs.
     */
    public File retrieveFile() throws URISyntaxException, IOException {

        // This is the path of the jar file. We use this
        // to create the file right next to the jar file
        // even if the jar is moved to a different directory.
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

    /**
     * Uses a file chooser so that user can find the file manually.
     * @return the file that was chosen by the user.
     */
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
