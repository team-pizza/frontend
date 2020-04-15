package Objects;

import Objects.Group;
import java.util.Arrays;
import Objects.Account;
import Objects.Database;

public class HeatmapExample {

    private Group group;
    private Account account;
    private Database db;
    private int[] availability;
    private float[] heatmapData;
    private int arraySize = 100;
    private int groupSize;

    public HeatmapExample(Group request, Database data){
        group = request;
        db = data;
        availability = new int[arraySize];
        Arrays.fill(availability, 0);

        heatmapData = new float[arraySize];
        Arrays.fill(heatmapData, 0);
        groupSize = 0;
    }

    public boolean[] isOccupied(Account person){
        boolean[] data = new boolean[arraySize];
        //Loops through each time segment in the heatmap's calendar and determines whether the user is busy at that time
        //TODO: Create an array that holds a series of booleans for each time segment
        //TODO: Change function type and return object
        return data;
    }

    public int[] calculateAvailability(boolean[] cal){
        groupSize++;

        for(int i=0; i<cal.length; i++){
            if(cal[i]){
                availability[i]++;
            }
        }

        return availability;
    }

    public float[] calculatePercentages(float[] available){

        for(int i = 0; i<available.length; i++){
            heatmapData[i] = (available[i] / groupSize);
        }

        return heatmapData;
    }

    //Not sure if it should be here, but there needs to be a function to determine color of blocks based on percentages returned in heatmapData array


}
