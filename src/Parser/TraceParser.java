package Parser;

import CCSLModel.Clock;
import CCSLModel.Trace;

import java.util.ArrayList;

/**
 * Created by huming on 2019/6/24.
 */
public class TraceParser {
    public ArrayList<ArrayList<Trace>>allTraces;
    static TraceParser instance = null;
    public static TraceParser getInstance(){

        if(instance==null){
            instance = new TraceParser();
        }
        return instance;
    }
    private TraceParser(){
        allTraces = new ArrayList<>();
    }
    public ArrayList<Trace> setTraces(ArrayList<ArrayList<String>> preTraces){

        ArrayList<Integer> clockCount = new ArrayList<>();

        for (int i = 0 ; i < ClockParser.getInstance().clockList.size() ;i++){
            clockCount.add(0);
        }

        ArrayList<Trace> traces = new ArrayList<>();
        for (int i = 0 ; i < preTraces.size(); i ++){

            ArrayList<String> preTrace = preTraces.get(i);
            ArrayList<Clock> clocks = new ArrayList<>();
            for (int j = 0 ; j < preTrace.size();j ++){
                String clockName = preTrace.get(j);
                int  id = ClockParser.getInstance().getAddClockId(clockName);
                if (id == ClockParser.getInstance().clockList.size()-1){

                    clockCount.add(1);
                }else {
                    int count = clockCount.get(id)+1;
                    clockCount.remove(id);
                    clockCount.add(id,count);
                }
                Clock c = new Clock(id,clockName,i+1,clockCount.get(id));
                clocks.add(c);
            }
            Trace trace = new Trace(i,i+1,clocks);
            traces.add(trace);
        }
        allTraces.add(traces);
        return traces;
    }


    public ArrayList<ArrayList<ArrayList<Integer>>> getGlobalNumList(int maxCount){
        ArrayList<ArrayList<ArrayList<Integer>>> globalNumList = new ArrayList<>();
        for (int i = 0 ; i < allTraces.size(); i ++){
            ArrayList<ArrayList<Integer>> clockNumList = new ArrayList<>();
            ArrayList<Trace>traces = allTraces.get(i);
            for (int j = 0 ; j < ClockParser.getInstance().clockList.size();j++){
                ArrayList<Integer> numList = new ArrayList<>();
                clockNumList.add(numList);
            }
            for (int j = 0 ; j < traces.size();j++){
                Trace trace = traces.get(j);
                for (int k = 0 ; k < trace.inputClocks.size();k++){
                    Clock clock = trace.inputClocks.get(k);
                    ArrayList<Integer>numList = clockNumList.get(clock.id);
                    if (clock.globalNum <= maxCount){
                        numList.add(clock.globalNum);
                    }

                }
            }
            globalNumList.add(clockNumList);
        }
        return globalNumList;
    }

    public ArrayList<String> getClockNameList(){
        return ClockParser.getInstance().clockList;
    }


}
