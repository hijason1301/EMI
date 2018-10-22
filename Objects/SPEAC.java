package Objects;

/**
 *
 * @author mss60
 */
public class SPEAC {
    
    int[] weight;
    String name;

    public SPEAC(int[] weight, String name) {
        this.weight = weight;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int[] getWeight() {
        return weight;
    }

    public void setWeight(int[] weight) {
        this.weight = weight;
    }
    
    
}
