package Objects;


import java.util.ArrayList;


/**
 *
 * @author Owner
 */
public class Mordent {
    
    ArrayList<Note> mordentNotes;

    public Mordent() {
        mordentNotes = new ArrayList<Note>();
    }

    public ArrayList<Note> getMordentNotes() {
        return mordentNotes;
    }

    public void setMordentNotes(ArrayList<Note> mordentNotes) {
        this.mordentNotes = mordentNotes;
    }
    
    public void addNoteToMordent(Note n){
        mordentNotes.add(n);
    }
    
}
