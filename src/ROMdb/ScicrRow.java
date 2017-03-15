package ROMdb;

import javafx.beans.property.SimpleStringProperty;

/**
 * Created by Anthony Orio on 3/14/2017.
 */
public class ScicrRow {

    private SimpleStringProperty type;
    private SimpleStringProperty number;
    private SimpleStringProperty title;
    private SimpleStringProperty build;
    private SimpleStringProperty baseline;

    public ScicrRow(String type, String number, String title, String build, String baseline) {
        this.type       = new SimpleStringProperty(type);
        this.number     = new SimpleStringProperty(number);
        this.title      = new SimpleStringProperty(title);
        this.build      = new SimpleStringProperty(build);
        this.baseline   = new SimpleStringProperty(baseline);
    }

    public String getBaseline() {
        return baseline.get();
    }
    public String getNumber() {
        return number.get();
    }
    public String getType() {
        return type.get();
    }
    public String getTitle() {
        return title.get();
    }
    public String getBuild() {
        return build.get();
    }

    public SimpleStringProperty typeProperty() {
        return type;
    }
    public SimpleStringProperty numberProperty() {
        return number;
    }
    public SimpleStringProperty titleProperty() {
        return title;
    }
    public SimpleStringProperty buildProperty() {
        return build;
    }
    public SimpleStringProperty baselineProperty() {
        return baseline;
    }
}
