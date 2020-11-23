package CCSLModel;

import java.util.ArrayList;

/**
 * Created by huming on 2019/6/24.
 */
public class RelationMapItem {
    public String leftClock;
    public int type;
    public String rightClock;
    public ArrayList<Integer> penatlyCountArr;
    public ArrayList<Integer>penatlyValueArr;
    public RelationMapItem(int type,String leftClock,String rightClock){
        this.leftClock = leftClock;
        this.rightClock = rightClock;
        this.type = type;
        penatlyCountArr = new ArrayList<>();
        penatlyValueArr = new ArrayList<>();
    }
    public void addPenaltyCount(int count,int value){
        penatlyCountArr.add(count);
        penatlyValueArr.add(value);
    }
    public boolean checkItem(int type,String leftClock,String rightClock){
        if (this.type==type&& this.leftClock.equals(leftClock)&&this.rightClock.equals(rightClock)){
            return true;
        }else {
            if(this.type == 0 ||this.type == 3 || this.type == 5  ){
                if (this.type==type&& this.leftClock.equals(rightClock)&&this.rightClock.equals(leftClock)) {
                    return true;
                }
            }
            return false;
        }
    }
    @Override
    public String toString(){
        String str = "";
        for (int i = 0 ; i < penatlyCountArr.size();i++){
            str += "&&relation_"+ penatlyCountArr.get(i) + " != " + penatlyValueArr.get(i);
        }
        return str;
    }
}
