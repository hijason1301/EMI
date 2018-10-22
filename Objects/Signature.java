
package Objects;

import java.util.ArrayList;

/**
 *
 * @author Owner
 */
public class Signature {
    ArrayList<Note> notes;
    String song;

    public Signature(ArrayList<Note> notes, String song) {
        this.notes = notes;
        this.song = song;
    }

    public ArrayList<Note> getNotes() {
        return notes;
    }

    public void setNotes(ArrayList<Note> notes) {
        this.notes = notes;
    }

    public String getSong() {
        return song;
    }

    public void setSong(String song) {
        this.song = song;
    }
    
    
}
