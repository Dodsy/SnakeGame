import java.util.Random;
public class Crum {
    private int location;
    int max_int;
    public Crum(int max_int){
        this.max_int = max_int;
        generate_crum();
    }
    public void generate_crum(){
        Random rand = new Random();
        int randomNum = rand.nextInt((max_int-1)+1);
        location = randomNum;
    }
    public int get_location(){
        return location;
    }

    
}
