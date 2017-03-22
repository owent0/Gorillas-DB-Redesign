package ROMdb;

import javafx.beans.property.SimpleStringProperty;

/**
 * Created by Anthony Orio on 3/14/2017.
 */
public class ScicrRow {

    private int id;
    private SimpleStringProperty type;
    private SimpleStringProperty number;
    private SimpleStringProperty title;
    private SimpleStringProperty build;
    private SimpleStringProperty baseline;

    /**
     * Default constructor.
     * @param type the type.
     * @param number the number.
     * @param title the title.
     * @param build the build.
     * @param baseline the baseline.
     */
    public ScicrRow( String type, String number, String title, String build, String baseline) {
        this.type       = new SimpleStringProperty(type);
        this.number     = new SimpleStringProperty(number);
        this.title      = new SimpleStringProperty(title);
        this.build      = new SimpleStringProperty(build);
        this.baseline   = new SimpleStringProperty(baseline);
    }

    /**
     * Setter for the id.
     * @param id the id.
     */
    public void setID(int id) {
        this.id         = id;
    }

    /**
     * The setter for the type.
     * @param type the type.
     */
    public void setType(String type) {
        this.type.set(type);
    }

    /**
     * The setter for the number.
     * @param number the number.
     */
    public void setNumber(String number) {
        this.number.set(number);
    }

    /**
     * The setter for the title.
     * @param title the title.
     */
    public void setTitle(String title) {
        this.title.set(title);
    }

    /**
     * The setter for the build.
     * @param build the build.
     */
    public void setBuild(String build) {
        this.build.set(build);
    }

    /**
     * The setter for the baseline.
     * @param baseline the baseline.
     */
    public void setBaseline(String baseline) {
        this.baseline.set(baseline);
    }

    /**
     * The getter for the id.
     * @return the id.
     */
    public int getId()  { return this.id; }

    /**
     * The getter for the baseline.
     * @return the baseline.
     */
    public String getBaseline() {
        return baseline.get();
    }

    /**
     * the getter for the number.
     * @return the number.
     */
    public String getNumber() {
        return number.get();
    }

    /**
     * The getter for the type.
     * @return the type.
     */
    public String getType() {
        return type.get();
    }

    /**
     * The getter for the title.
     * @return the title.
     */
    public String getTitle() {
        return title.get();
    }

    /**
     * The getter for the build.
     * @return the build.
     */
    public String getBuild() {
        return build.get();
    }

    /**
     * The getter for the type property.
     * @return the type property.
     */
    public SimpleStringProperty typeProperty() {
        return type;
    }

    /**
     * The getter for the number property.
     * @return the number property.
     */
    public SimpleStringProperty numberProperty() {
        return number;
    }

    /**
     * The getter for the title property.
     * @return the title property.
     */
    public SimpleStringProperty titleProperty() {
        return title;
    }

    /**
     * The getter for the build property.
     * @return the build property.
     */
    public SimpleStringProperty buildProperty() {
        return build;
    }

    /**
     * The getter for the baseline property.
     * @return the baseline property.
     */
    public SimpleStringProperty baselineProperty() {
        return baseline;
    }
}
