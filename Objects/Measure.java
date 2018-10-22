package Objects;


import java.util.ArrayList;

/**
 *
 * @author Owner
 */
public class Measure {

    ArrayList<Note> measureNotes;
    ArrayList<Integer> matchedMeasures;
    Measure previousMeasure;
    Measure nextMeasure;
    boolean signature;
    String speacWeight;
    int[] pattern;
    int[] pitchValues;
    int[] durationValues;
    boolean used;
    int matchedPatterns;

    public Measure(ArrayList<Note> measureNotes) {
        this.measureNotes = measureNotes;
        this.matchedMeasures = new ArrayList<Integer>();
        this.used = false;
        this.matchedPatterns = 0;
        if (!measureNotes.isEmpty()) {
            this.pitchValues = autoSetPitchValues();
            this.durationValues = autoSetDurationValues();
        }
    }

    public void setMeasureNotes(ArrayList<Note> measureNotes) {
        this.measureNotes = measureNotes;
    }

    public void addNote(Note n) {
        measureNotes.add(n);
    }

    public ArrayList<Note> getMeasureNotes() {
        return measureNotes;
    }

    public ArrayList<Integer> getMatchedMeasures() {
        return matchedMeasures;
    }

    public void setMatchedMeasures(ArrayList<Integer> matchedMeasures) {
        this.matchedMeasures = matchedMeasures;
    }

    public void addMatched(Integer i) {
        matchedMeasures.add(i);
    }

    public int[] getPattern() {
        return pattern;
    }

    public void setPattern(int[] pattern) {
        this.pattern = pattern;
    }

    public String getSpeacWeight() {
        return speacWeight;
    }

    public void setSpeacWeight(String SPEACweight) {
        this.speacWeight = SPEACweight;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }
    
    public int[] getPitchValues() {
        return pitchValues;
    }

    public void setPitchValues(int[] pitchValues) {
        this.pitchValues = pitchValues;
    }

    public int[] autoSetPitchValues() {
        int[] pitchArray;
        if (measureNotes.size() == 1) {
            pitchArray = new int[measureNotes.size()];
        } else {
            pitchArray = new int[measureNotes.size() - 1];
        }
        for (int i = 0; i < measureNotes.size() - 1; i++) {
            pitchArray[i] = measureNotes.get(i).getPitch();
        }
        return pitchArray;
    }

    public int[] getDurationValues() {
        return durationValues;
    }

    public void setDurationValues(int[] durationValues) {
        this.durationValues = durationValues;
    }
    
    public int[] autoSetDurationValues() {
        int[] durationArray;
        if (measureNotes.size() == 1) {
            durationArray = new int[measureNotes.size()];
        } else {
            durationArray = new int[measureNotes.size() - 1];
        }
        for (int i = 0; i < measureNotes.size() - 1; i++) {
            durationArray[i] = (int) measureNotes.get(i).getDuration();
        }
        return durationArray;
    }    

    public int getMatchedPatterns() {
        return matchedPatterns;
    }

    public void setMatchedPatterns(int matchedPatterns) {
        this.matchedPatterns = matchedPatterns;
    }
    
    public void incrementMatchedPatterns(){
        this.matchedPatterns++;
    }

    public Measure getPreviousMeasure() {
        return previousMeasure;
    }

    public void setPreviousMeasure(Measure previousMeasure) {
        this.previousMeasure = previousMeasure;
    }

    public boolean isSignature() {
        return signature;
    }

    public void setSignature(boolean signature) {
        this.signature = signature;
    }

    public Measure getNextMeasure() {
        return nextMeasure;
    }

    public void setNextMeasure(Measure nextMeasure) {
        this.nextMeasure = nextMeasure;
    }
    
    
}
