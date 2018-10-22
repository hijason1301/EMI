package Objects;


import java.util.ArrayList;


/**
 *
 * @author Owner
 */
public class Trill {
    
    ArrayList<Note> trillNotes;

    public Trill() {
        trillNotes = new ArrayList<Note>();
    }

    public ArrayList<Note> getTrillNotes() {
        return trillNotes;
    }

    public void setTrillNotes(ArrayList<Note> trillNotes) {
        this.trillNotes = trillNotes;
    }
    
    public void addNoteToTrill(Note n){
        trillNotes.add(n);
    }    
    
}
