package ROMdb.Helpers;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Created by Anthony Orio on 3/28/2017.
 */
public class RequirementsRow {

    private int id;



    private SimpleStringProperty csc;
    private SimpleStringProperty csu;
    private SimpleStringProperty doorsID;
    private SimpleStringProperty paragraph;
    private SimpleStringProperty baseline;
    private SimpleStringProperty scicr;
    private SimpleStringProperty capability;
    private SimpleDoubleProperty add;
    private SimpleDoubleProperty change;
    private SimpleDoubleProperty delete;
    private SimpleDoubleProperty designWeight;
    private SimpleDoubleProperty codeWeight;
    private SimpleDoubleProperty unitTestWeight;
    private SimpleDoubleProperty integrationWeight;
    private SimpleStringProperty ri;
    private SimpleStringProperty rommer;
    private SimpleStringProperty program;

    public RequirementsRow(String csc,
                           String csu,
                           String doorsID,
                           String paragraph,
                           String baseline,
                           String scicr,
                           String capability,
                           Double add,
                           Double change,
                           Double delete,
                           Double designWeight,
                           Double codeWeight,
                           Double unitTestWeight,
                           Double integrationWeight,
                           String ri,
                           String rommer,
                           String program)
    {

        this.csc = new SimpleStringProperty(csc);
        this.csu =  new SimpleStringProperty(csu);
        this.doorsID =  new SimpleStringProperty(doorsID);
        this.paragraph =  new SimpleStringProperty(paragraph);
        this.baseline =  new SimpleStringProperty(baseline);
        this.scicr =  new SimpleStringProperty(scicr);
        this.capability =  new SimpleStringProperty(capability);
        this.add =  new SimpleDoubleProperty(add);
        this.change =  new SimpleDoubleProperty(change);
        this.delete = new SimpleDoubleProperty(delete);
        this.designWeight =  new SimpleDoubleProperty(designWeight);
        this.codeWeight =  new SimpleDoubleProperty(codeWeight);
        this.unitTestWeight =  new SimpleDoubleProperty(unitTestWeight);
        this.integrationWeight =  new SimpleDoubleProperty(integrationWeight);
        this.ri =  new SimpleStringProperty(ri);
        this.rommer =  new SimpleStringProperty(rommer);
        this.program =  new SimpleStringProperty(program);
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public SimpleStringProperty cscProperty() {return csc;}
    public SimpleStringProperty csuProperty() {return csu;}
    public SimpleStringProperty doorsIDProperty() {return doorsID;}
    public SimpleStringProperty paragraphProperty() {return paragraph;}
    public SimpleStringProperty baselineProperty() {return baseline;}
    public SimpleStringProperty scicrProperty() {return scicr;}
    public SimpleStringProperty capabilityProperty() {return capability;}
    public SimpleStringProperty riProperty() {return ri;}
    public SimpleStringProperty rommerProperty() {return rommer;}
    public SimpleStringProperty programProperty() {return program;}

    public SimpleDoubleProperty changeProperty() {return change;}
    public SimpleDoubleProperty deleteProperty() {return delete;}
    public SimpleDoubleProperty addProperty() {return add;}
    public SimpleDoubleProperty designWeightProperty() {return designWeight;}
    public SimpleDoubleProperty codeWeightProperty() {return codeWeight;}
    public SimpleDoubleProperty unitTestWeightProperty() {return unitTestWeight;}
    public SimpleDoubleProperty integrationWeightProperty() {return integrationWeight;}

    public String getCsc() {return csc.get();}
    public String getCsu() {return csu.get();}
    public String getDoorsID() {return doorsID.get();}
    public String getParagraph() {return paragraph.get();}
    public String getBaseline() {return baseline.get();}
    public String getScicr() {return scicr.get();}
    public String getCapability() {return capability.get();}
    public String getRi() {return ri.get();}
    public String getRommer() {return rommer.get();}
    public String getProgram() {return program.get();}

    public double getAdd() {return add.get();}
    public double getChange() {return change.get();}
    public double getDelete() {return delete.get();}
    public double getDesignWeight() {return designWeight.get();}
    public double getCodeWeight() {return codeWeight.get();}
    public double getUnitTestWeight() {return unitTestWeight.get();}
    public double getIntegrationWeight() {return integrationWeight.get();}

    public void setCsc(String csc) {this.csc.set(csc);}
    public void setCsu(String csu) {this.csu.set(csu);}
    public void setDoorsID(String doorsID) {this.doorsID.set(doorsID);}
    public void setParagraph(String paragraph) {this.paragraph.set(paragraph);}
    public void setBaseline(String baseline) {this.baseline.set(baseline);}
    public void setScicr(String scicr) {this.scicr.set(scicr);}
    public void setCapability(String capability) {this.capability.set(capability);}
    public void setAdd(double add) {this.add.set(add);}
    public void setChange(double change) {this.change.set(change);}
    public void setDelete(double delete) {this.delete.set(delete);}
    public void setDesignWeight(double designWeight) {this.designWeight.set(designWeight);}
    public void setCodeWeight(double codeWeight) {this.codeWeight.set(codeWeight);}
    public void setUnitTestWeight(double unitTestWeight) {this.unitTestWeight.set(unitTestWeight);}
    public void setIntegrationWeight(double integrationWeight) {this.integrationWeight.set(integrationWeight);}
    public void setRi(String ri) {this.ri.set(ri);}
    public void setRommer(String rommer) {this.rommer.set(rommer);}
    public void setProgram(String program) {this.program.set(program);}

    @Override
    public String toString()
    {
        return "" + id;
    }
}
