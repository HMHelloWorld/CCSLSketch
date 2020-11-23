package Encoder;

import Parser.TraceParser;

import java.util.ArrayList;

/**
 * Created by huming on 2019/6/24.
 */
public class HarnessEncoder {
    private int maxLength;
    public HarnessEncoder(int maxLength){
        this.maxLength = maxLength;
    }
    public String encodingAssert(int tab){
        String tabStr= getTab(tab);
        String assertStr = "";
        ArrayList<ArrayList<ArrayList<Integer>>>  input = TraceParser.getInstance().getGlobalNumList(maxLength);
        for (int  i = 0 ; i < input.size();i++){
            String clockStr = "";
            String countStr = "";
            ArrayList<ArrayList<Integer>> clockNumList = input.get(i);
            for (int j = 0 ; j < clockNumList.size(); j++){
                ArrayList<Integer>numList = clockNumList.get(j);
                if(j!= 0){
                    clockStr += ",";
                    countStr += ",";
                }
                countStr += numList.size();
                String clockNumStr = "{";
                for (int k = 0 ; k < numList.size(); k ++){
                    if(k!= 0){
                        clockNumStr += ",";
                    }
                    int global = numList.get(k);
                    clockNumStr += global;
                }
                for (int k = numList.size() ; k < maxLength; k ++){
                    if(k!= 0){
                        clockNumStr += ",";
                    }
                    clockNumStr += -k;
                }
                clockNumStr += "}";
                clockStr += clockNumStr;
            }
            countStr += "";

            if(i!=0){
                assertStr += "\n";
            }
            assertStr += tabStr + "assert" + " " + "check(" + countStr + "," + clockStr + ")==1;";
        }
        return assertStr;

    }
    public String encodingHarness(int tab){
        String tabStr = getTab(tab);
        String harnessStr = tabStr + "harness void main(){" + "\n"
                + encodingAssert(tab +1) + "\n"
                + "}";
        return harnessStr;

    }
    public String getTab(int count){
        String tab = "";
        for (int i = 0 ; i < count;i++){
            tab += "    ";
        }
        return tab;
    }
}
