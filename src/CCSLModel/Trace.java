package CCSLModel;

import java.util.ArrayList;

/**
 * Created by huming on 2019/6/24.
 */
public class Trace {
    int id;
    int count;
    public ArrayList<Clock> inputClocks;

    public Trace(){
        id = 0;
        count = 0;
        inputClocks = new ArrayList<>();

    }
    public Trace(int id,int count, ArrayList<Clock> inputClocks){
        this.id = id;
        this.count = count;
        this.inputClocks = inputClocks;
    }

    @Override
    public String toString() {
        String clockStr = "";
        for (int  i = 0 ; i < inputClocks.size();i++){
            clockStr += "\n";
            clockStr+= "\t" + inputClocks.get(i).toString();
        }
        return "Trace:[id:"+id + "\t" + "count:"+count + "]" + clockStr;
    }
    public String toSkString(){
        String clockStr = "[";
        for (int  i = 0 ; i < inputClocks.size();i++){
            if(i!= 0){
                clockStr += ",";
            }

            clockStr+= inputClocks.get(i).toSkString();
        }
        clockStr += "]";
        return clockStr;
    }
}
