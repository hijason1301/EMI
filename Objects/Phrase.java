package Objects;


import java.util.ArrayList;

/**
 *
 * @author Owner
 */
public class Phrase {

    ArrayList<Note> notes = new ArrayList<Note>();
    ArrayList<Measure> measures = new ArrayList<Measure>();
    String speacWeight;
    int[] pitchValues;

    public Phrase(ArrayList<Measure> phraseMeasures) {
        measures = phraseMeasures;
        for (Measure m : phraseMeasures) {
            for (Note n : m.getMeasureNotes()){
                notes.add(n);
            }
        }
        pitchValues = setPitchValues();
    }

    public String getSpeacWeight() {
        return speacWeight;
    }

    public void setSpeacWeight(String speacWeight) {
        this.speacWeight = speacWeight;
    }

    public ArrayList<Note> getNotes() {
        return notes;
    }

    public void setNotes(ArrayList<Note> notes) {
        this.notes = notes;
    }
    
    public void addNote(Note n){
        notes.add(n);
    }

    public ArrayList<Measure> getMeasures() {
        return measures;
    }

    public void setMeasures(ArrayList<Measure> measures) {
        this.measures = measures;
    }
    
    public void addMeasure(Measure m){
        measures.add(m);
    }       
    
    public int[] getPitchValues() {
        return pitchValues;
    }

    public int[] setPitchValues() {
        int[] pitchArray;
        if (notes.size() == 1) {
            pitchArray = new int[notes.size()];
        } else {
            pitchArray = new int[notes.size() - 1];
        }
        for (int i = 0; i < notes.size() - 1; i++) {
            pitchArray[i] = notes.get(i).getPitch();
        }
        return pitchArray;
    }
}
