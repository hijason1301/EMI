package Objects;


import Objects.Trill;
import Objects.Measure;
import Objects.Mordent;
import Objects.Phrase;
import Objects.Note;
import java.util.ArrayList;

/**
 *
 * @author mss60
 */
public class FileData {
    // Total length of song in ticks
    public long tickLength;
    
    // Total length of song in microseconds
    public long microsecondLength;
    
    // Gets either SMPTE timecode or PPQ (Pulses Per Quarter *Note*)
    public float divisionType;
    
    // Ticks per beat
    public int resolution;
    
    // Raw time signature in format aa bb cc dd where
    // aa - time signature numerator
    // bb - time signature denominator where { actual denominator = 2^[bb]}
    // cc - MIDI ticks per metronome click
    // dd - 32nd notes per MIDI quarter note
    public int[] timeSig;
    public ArrayList<ArrayList<Note>> noteArray;
    public ArrayList<ArrayList<Measure>> measureArray;
    public ArrayList<Measure> songAsSingleTrack;
    public ArrayList<ArrayList<Phrase>> phraseArray;    
    public ArrayList<Trill> trillArray;
    public ArrayList<Note> accArray;
    public ArrayList<Mordent> mordentArray; 
    public ArrayList<ArrayList<Note>> clarified;
    public ArrayList<ArrayList<Measure>> measuresBySpeac;
    public ArrayList<Measure> measuresForMatching;
    public ArrayList<Signature> identifiedSignatures;
    public String name;

    public FileData(String name, long tickLength, long microsecondLength, float divisionType, int resolution) {
        this.name = name;
        this.tickLength = tickLength;
        this.microsecondLength = microsecondLength;
        this.divisionType = divisionType;
        this.resolution = resolution;
        noteArray = new ArrayList<ArrayList<Note>>();
        trillArray = new ArrayList<Trill>();
        accArray = new ArrayList<Note>();
        mordentArray = new ArrayList<Mordent>();
        measureArray = new ArrayList<ArrayList<Measure>>();
        phraseArray = new ArrayList<ArrayList<Phrase>>();
        clarified = new ArrayList<ArrayList<Note>>();
        measuresBySpeac = new ArrayList<ArrayList<Measure>>();
        measuresForMatching = new ArrayList<Measure>();
        identifiedSignatures = new ArrayList<Signature>();
        songAsSingleTrack = new ArrayList<Measure>();
    }

    public ArrayList<ArrayList<Note>> getNoteArray() {
        return noteArray;
    }

    public void setNoteArray(ArrayList<ArrayList<Note>> noteArray) {
        this.noteArray = noteArray;
    }

    public long getTickLength() {
        return tickLength;
    }

    public void setTickLength(long tickLength) {
        this.tickLength = tickLength;
    }

    public long getMicrosecondLength() {
        return microsecondLength;
    }

    public void setMicrosecondLength(long microsecondLength) {
        this.microsecondLength = microsecondLength;
    }

    public float getDivisionType() {
        return divisionType;
    }

    public void setDivisionType(float divisionType) {
        this.divisionType = divisionType;
    }

    public int getResolution() {
        return resolution;
    }

    public void setResolution(int resolution) {
        this.resolution = resolution;
    }

    public int[] getTimeSig() {
        return timeSig;
    }

    public void setTimeSig(int[] timeSig) {
        this.timeSig = timeSig;
    }

    public ArrayList<Trill> getTrillArray() {
        return trillArray;
    }

    public void setTrillArray(ArrayList<Trill> trillArray) {
        this.trillArray = trillArray;
    }
    
    public void addTrill(Trill trill){
        trillArray.add(trill);
    }

    public ArrayList<Note> getAccArray() {
        return accArray;
    }

    public void setAccArray(ArrayList<Note> accArray) {
        this.accArray = accArray;
    }    

    public ArrayList<Mordent> getMordentArray() {
        return mordentArray;
    }

    public void setMordentArray(ArrayList<Mordent> mordentArray) {
        this.mordentArray = mordentArray;
    }

    public ArrayList<ArrayList<Measure>> getMeasureArray() {
        return measureArray;
    }

    public void setMeasureArray(ArrayList<ArrayList<Measure>> measureArray) {
        this.measureArray = measureArray;
    }

    public ArrayList<ArrayList<Phrase>> getPhraseArray() {
        return phraseArray;
    }

    public void setPhraseArray(ArrayList<ArrayList<Phrase>> phraseArray) {
        this.phraseArray = phraseArray;
    }

    public ArrayList<ArrayList<Note>> getClarified() {
        return clarified;
    }

    public void setClarified(ArrayList<ArrayList<Note>> clarified) {
        this.clarified = clarified;
    }

    public ArrayList<ArrayList<Measure>> getMeasuresBySpeac() {
        return measuresBySpeac;
    }

    public void setMeasuresBySpeac(ArrayList<ArrayList<Measure>> measuresBySpeac) {
        this.measuresBySpeac = measuresBySpeac;
    }

    public ArrayList<Measure> getMeasuresForMatching() {
        return measuresForMatching;
    }

    public void setMeasuresForMatching(ArrayList<Measure> measuresForMatching) {
        this.measuresForMatching = measuresForMatching;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Signature> getIdentifiedSignatures() {
        return identifiedSignatures;
    }

    public void setIdentifiedSignatures(ArrayList<Signature> identifiedSignatures) {
        this.identifiedSignatures = identifiedSignatures;
    }
    
    public void addSignature(Signature signature){
        this.identifiedSignatures.add(signature);
    }

    public ArrayList<Measure> getSongAsSingleTrack() {
        return songAsSingleTrack;
    }

    public void setSongAsSingleTrack(ArrayList<Measure> songAsSingleTrack) {
        this.songAsSingleTrack = songAsSingleTrack;
    }
                    
}
