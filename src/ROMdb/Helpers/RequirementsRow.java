package ROMdb.Helpers;

import javafx.beans.property.SimpleStringProperty;

/**
 * Created by Anthony Orio on 3/28/2017.
 */
public class RequirementsRow {

    private SimpleStringProperty csc;
    private SimpleStringProperty csu;
    private SimpleStringProperty doorsID;
    private SimpleStringProperty paragraph;
    private SimpleStringProperty baseline;
    private SimpleStringProperty scicr;
    private SimpleStringProperty capability;
    private SimpleStringProperty add;
    private SimpleStringProperty change;
    private SimpleStringProperty delete;
    private SimpleStringProperty designWeight;
    private SimpleStringProperty codeWeight;
    private SimpleStringProperty unitTestWeight;
    private SimpleStringProperty integrationWeight;
    private SimpleStringProperty ri;
    private SimpleStringProperty rommer;
    private SimpleStringProperty program;
    private SimpleStringProperty build;

    public RequirementsRow(SimpleStringProperty csc, SimpleStringProperty csu, SimpleStringProperty doorsID, SimpleStringProperty paragraph, SimpleStringProperty baseline, SimpleStringProperty scicr, SimpleStringProperty capability, SimpleStringProperty add, SimpleStringProperty change, SimpleStringProperty delete, SimpleStringProperty designWeight, SimpleStringProperty codeWeight, SimpleStringProperty unitTestWeight, SimpleStringProperty integrationWeight, SimpleStringProperty ri, SimpleStringProperty rommer, SimpleStringProperty program, SimpleStringProperty build) {
        this.csc = csc;
        this.csu = csu;
        this.doorsID = doorsID;
        this.paragraph = paragraph;
        this.baseline = baseline;
        this.scicr = scicr;
        this.capability = capability;
        this.add = add;
        this.change = change;
        this.delete = delete;
        this.designWeight = designWeight;
        this.codeWeight = codeWeight;
        this.unitTestWeight = unitTestWeight;
        this.integrationWeight = integrationWeight;
        this.ri = ri;
        this.rommer = rommer;
        this.program = program;
        this.build = build;
    }

    public String getCsc() {
        return csc.get();
    }

    public SimpleStringProperty cscProperty() {
        return csc;
    }

    public void setCsc(String csc) {
        this.csc.set(csc);
    }

    public String getCsu() {
        return csu.get();
    }

    public SimpleStringProperty csuProperty() {
        return csu;
    }

    public void setCsu(String csu) {
        this.csu.set(csu);
    }

    public String getDoorsID() {
        return doorsID.get();
    }

    public SimpleStringProperty doorsIDProperty() {
        return doorsID;
    }

    public void setDoorsID(String doorsID) {
        this.doorsID.set(doorsID);
    }

    public String getParagraph() {
        return paragraph.get();
    }

    public SimpleStringProperty paragraphProperty() {
        return paragraph;
    }

    public void setParagraph(String paragraph) {
        this.paragraph.set(paragraph);
    }

    public String getBaseline() {
        return baseline.get();
    }

    public SimpleStringProperty baselineProperty() {
        return baseline;
    }

    public void setBaseline(String baseline) {
        this.baseline.set(baseline);
    }

    public String getScicr() {
        return scicr.get();
    }

    public SimpleStringProperty scicrProperty() {
        return scicr;
    }

    public void setScicr(String scicr) {
        this.scicr.set(scicr);
    }

    public String getCapability() {
        return capability.get();
    }

    public SimpleStringProperty capabilityProperty() {
        return capability;
    }

    public void setCapability(String capability) {
        this.capability.set(capability);
    }

    public String getAdd() {
        return add.get();
    }

    public SimpleStringProperty addProperty() {
        return add;
    }

    public void setAdd(String add) {
        this.add.set(add);
    }

    public String getChange() {
        return change.get();
    }

    public SimpleStringProperty changeProperty() {
        return change;
    }

    public void setChange(String change) {
        this.change.set(change);
    }

    public String getDelete() {
        return delete.get();
    }

    public SimpleStringProperty deleteProperty() {
        return delete;
    }

    public void setDelete(String delete) {
        this.delete.set(delete);
    }

    public String getDesignWeight() {
        return designWeight.get();
    }

    public SimpleStringProperty designWeightProperty() {
        return designWeight;
    }

    public void setDesignWeight(String designWeight) {
        this.designWeight.set(designWeight);
    }

    public String getCodeWeight() {
        return codeWeight.get();
    }

    public SimpleStringProperty codeWeightProperty() {
        return codeWeight;
    }

    public void setCodeWeight(String codeWeight) {
        this.codeWeight.set(codeWeight);
    }

    public String getUnitTestWeight() {
        return unitTestWeight.get();
    }

    public SimpleStringProperty unitTestWeightProperty() {
        return unitTestWeight;
    }

    public void setUnitTestWeight(String unitTestWeight) {
        this.unitTestWeight.set(unitTestWeight);
    }

    public String getIntegrationWeight() {
        return integrationWeight.get();
    }

    public SimpleStringProperty integrationWeightProperty() {
        return integrationWeight;
    }

    public void setIntegrationWeight(String integrationWeight) {
        this.integrationWeight.set(integrationWeight);
    }

    public String getRi() {
        return ri.get();
    }

    public SimpleStringProperty riProperty() {
        return ri;
    }

    public void setRi(String ri) {
        this.ri.set(ri);
    }

    public String getRommer() {
        return rommer.get();
    }

    public SimpleStringProperty rommerProperty() {
        return rommer;
    }

    public void setRommer(String rommer) {
        this.rommer.set(rommer);
    }

    public String getProgram() {
        return program.get();
    }

    public SimpleStringProperty programProperty() {
        return program;
    }

    public void setProgram(String program) {
        this.program.set(program);
    }

    public String getBuild() {
        return build.get();
    }

    public SimpleStringProperty buildProperty() {
        return build;
    }

    public void setBuild(String build) {
        this.build.set(build);
    }
}
