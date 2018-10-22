package Objects;


/**
 *
 * @author Owner
 */
public class Note {

    String name;
    long start;
    long duration;
    int channel;
    int velocity;
    int pitch;

    public Note(long start, long duration, int channel, int velocity, int pitch) {
        this.start = start;
        this.channel = channel;
        this.pitch = pitch;
        this.velocity = velocity;
        this.duration = duration;
    }    
    
    public void setName(String name) {
        this.name = name;
    }  

    public String getName() {
        return name;
    }

    public long getStart() {
        return start;
    }

    public long getDuration() {
        return duration;
    }

    public int getChannel() {
        return channel;
    }

    public int getVelocity() {
        return velocity;
    }

    public int getPitch() {
        return pitch;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public void setPitch(int pitch) {
        this.pitch = pitch;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    public void setVelocity(int velocity) {
        this.velocity = velocity;
    }    
}
