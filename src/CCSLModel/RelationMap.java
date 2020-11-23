package CCSLModel;

import java.util.ArrayList;

/**
 * Created by huming on 2019/6/24.
 */
public class RelationMap {
    ArrayList<RelationMapItem> mapItems;
    public RelationMap(){
        mapItems = new ArrayList<>();
    }
    public void addItem(int type,String leftClock,String rightClock,int penaltyCount,int penaltyValue){
        Boolean isAdd = false;
        for (int i = 0 ;i < mapItems.size();i++){
            RelationMapItem item = mapItems.get(i);
            if(item.checkItem(type,leftClock,rightClock)){
                item.addPenaltyCount(penaltyCount,penaltyValue);
                isAdd = true;
                break;
            }
        }
        if (!isAdd){
            RelationMapItem item = new RelationMapItem(type,leftClock,rightClock);
            item.addPenaltyCount(penaltyCount,penaltyValue);
            mapItems.add(item);
        }
    }
    public String getPenaltyStr(int type,String leftClock,String rightClock){
        for (int i = 0 ;i < mapItems.size();i++){
            RelationMapItem item = mapItems.get(i);
            if(item.checkItem(type,leftClock,rightClock)){
                return item.toString();
            }
        }
        return "";
    }
}
