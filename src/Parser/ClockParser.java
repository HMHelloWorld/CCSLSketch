package Parser;

import java.util.ArrayList;

/**
 * Created by huming on 2019/6/24.
 */
public class ClockParser {public ArrayList<String> clockList;
    private static ClockParser instance = null;
    public static ClockParser getInstance(){
        if(instance == null){
            instance = new ClockParser();
        }
        return instance;
    }
    private ClockParser(){
        clockList = new ArrayList<>();
    }
    public int getAddClockId(String clock){
        for (int  i = 0 ; i < clockList.size(); i ++){
            if(clockList.get(i).equals(clock)){
                return i;
            }
        }
        clockList.add(clock);
        return clockList.size()-1;
    }
    public int getClockId(String clock){
        for (int  i = 0 ; i < clockList.size(); i ++){
            String c = clockList.get(i);
            if(c.equals(clock)){
                return i;
            }
        }
        return -1;
    }
}
