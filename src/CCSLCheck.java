import CCSLModel.Clock;
import CCSLModel.Expression;
import CCSLModel.Relation;
import CCSLModel.Trace;
import Parser.ClockParser;
import Parser.ExpressionParser;
import Parser.RelationParser;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by huming on 2019/6/24.
 */
public class CCSLCheck {
    public boolean check(ArrayList<Trace> traces){
        ArrayList<Relation> relations = RelationParser.getInstance().relations;
        ArrayList<ArrayList<Clock>> clockList = new ArrayList<>();
        HashMap<String,Integer> map = new HashMap();
        for (int i = 0; i < ClockParser.getInstance().clockList.size(); i ++){
            map.put(ClockParser.getInstance().clockList.get(i),i);
            ArrayList<Clock> list = new ArrayList<>();
            clockList.add(list);
        }


        for(int i = 0 ; i < traces.size();i++){
            Trace trace = traces.get(i);
            ArrayList<Clock> cl = trace.inputClocks;
            for (int  j = 0 ; j< cl.size();j++){
                int index = map.get(cl.get(j).name);
                ArrayList<Clock> list = clockList.get(index);
                list.add(cl.get(j));
            }

        }
        ArrayList<Expression> expressions = ExpressionParser.getInstance().refreshExpression();

        for (int i = 0 ; i < ExpressionParser.getInstance().expressionList.size(); i ++){
            Expression expression = expressions.get(i);
            map.put(expression.name,i + ClockParser.getInstance().clockList.size());

            ArrayList<Clock> list = new ArrayList<>();
            int leftIndex = 0;
            int rightIndex = 0;
            switch (expression.type){
                case 0:
                    leftIndex = map.get(expression.variables.get(0).name);
                    rightIndex = map.get(expression.variables.get(1).name);
                    list = getUnionClock(clockList.get(leftIndex),clockList.get(rightIndex),expression.name,expression.id);
                    break;
                case 1:
                    leftIndex = map.get(expression.variables.get(0).name);
                    rightIndex = map.get(expression.variables.get(1).name);
                    list = getIntersectionClock(clockList.get(leftIndex),clockList.get(rightIndex),expression.name,expression.id);
                    break;
                case 2:
                    leftIndex = map.get(expression.variables.get(0).name);
                    rightIndex = map.get(expression.variables.get(1).name);
                    list = getSupremumClock(clockList.get(leftIndex),clockList.get(rightIndex),expression.name,expression.id);
                    break;
                case 3:
                    leftIndex = map.get(expression.variables.get(0).name);
                    rightIndex = map.get(expression.variables.get(1).name);
                    list = getInfimumClock(clockList.get(leftIndex),clockList.get(rightIndex),expression.name,expression.id);
                    break;
                default:
            }
            clockList.add(list);
        }


        for (int i= 0 ; i< relations.size(); i ++){
            Relation relation = relations.get(i);

            int leftIndex = map.get(relation.variables.get(0).name);
            int rightIndex = map.get(relation.variables.get(1).name);
            switch (relation.type){
                case 0: {
                    if (!checkCoincidence(clockList.get(leftIndex), clockList.get(rightIndex))) {
                        return false;
                    }
                }break;
                case 1: {
                    if (!checkStrictPrecedence(clockList.get(leftIndex), clockList.get(rightIndex))) {
                        return false;
                    }
                }break;
                case 2: {
                    if (!checkNonStrictPrecedence(clockList.get(leftIndex), clockList.get(rightIndex))) {
                        return false;
                    }
                }break;
                case 3: {
                    if (!checkExclusion(clockList.get(leftIndex), clockList.get(rightIndex))) {
                        return false;
                    }
                }break;
                case 4: {
                    if (!checkSubClock(clockList.get(leftIndex), clockList.get(rightIndex))) {
                        return false;
                    }
                }break;
                default:
                    break;
            }
        }


        return true;
    }

    /*
        * Relation type
        * Relation: Coincidence(==)             0
        * Relation: Strict Precedence(<)        1
        * Relation: Non-strict Precedence(<=)   2
        * Relation: Exclusion(#)                3
        * Relation: Sub-Clock                   4
    */


    //check relation Coincidence(left == right)
    public boolean checkCoincidence(ArrayList<Clock> leftTrace,ArrayList<Clock> rightTrace){
        if(leftTrace.size()!= rightTrace.size()){
            return false;
        }
        for (int  i = 0 ; i < leftTrace.size(); i ++){
            if(leftTrace.get(i).globalNum!= rightTrace.get(i).globalNum){
                return false;
            }
        }
        return true;

    }

    //check relation Non-Strict Precedence(left <= right)
    public boolean checkNonStrictPrecedence(ArrayList<Clock> leftTrace,ArrayList<Clock> rightTrace){
        if(leftTrace.size() < rightTrace.size()){
            return false;
        }
        for (int  i = 0 ; i < rightTrace.size(); i ++){
            if(leftTrace.get(i).globalNum > rightTrace.get(i).globalNum){
                return false;
            }
        }
        return true;

    }

    //check relation Strict Precedence(left < right)
    public boolean checkStrictPrecedence(ArrayList<Clock> leftTrace,ArrayList<Clock> rightTrace){
        if(leftTrace.size() < rightTrace.size()){
            return false;
        }
        for (int  i = 0 ; i < rightTrace.size(); i ++){
            if(leftTrace.get(i).globalNum >= rightTrace.get(i).globalNum){
                return false;
            }
        }
        return true;

    }

    //check relation Exclusion(left # right)
    public boolean checkExclusion(ArrayList<Clock> leftTrace,ArrayList<Clock> rightTrace){
        int index = 0;
        for (int  i = 0 ; i < rightTrace.size(); i ++){
            for (int  j = index ; j < leftTrace.size(); j ++){
                if(leftTrace.get(i).globalNum == rightTrace.get(j).globalNum){
                    return false;
                }else if(leftTrace.get(i).globalNum > rightTrace.get(j).globalNum){
                    index ++;
                }else {
                    break;
                }
            }
        }
        return true;
    }

    //check relation Sub-clock(left is sub-clock of right)
    public boolean checkSubClock(ArrayList<Clock> leftTrace,ArrayList<Clock> rightTrace){
        if(leftTrace.size() > rightTrace.size()){
            return false;
        }
        int index = 0;
        for (int  i = 0 ; i < leftTrace.size(); i ++){
            for (int  j = index ; j < rightTrace.size(); j ++){

                if(leftTrace.get(i).globalNum > rightTrace.get(j).globalNum){
                    index ++;
                    continue;
                }else if(leftTrace.get(i).globalNum < rightTrace.get(j).globalNum){
                    return false;
                }else {
                    index++;
                    break;
                }


            }

        }
        return true;

    }

    //check relation Alternates With
    public boolean checkAlternatesWith(ArrayList<Clock> leftTrace,ArrayList<Clock> rightTrace){
        int range = leftTrace.size() - rightTrace.size();
        if(range > 1 || range < -1){
            return false;
        }
        if(leftTrace.get(0).globalNum < rightTrace.get(0).globalNum){
            for (int i = 0 ; i < rightTrace.size();i++){
                if(leftTrace.size() == i+1){
                    if(!(leftTrace.get(i).globalNum < rightTrace.get(i).globalNum)){
                        return false;
                    }
                }else if(!(leftTrace.get(i).globalNum < rightTrace.get(i).globalNum && rightTrace.get(i).globalNum < leftTrace.get(i+1).globalNum)){
                    return false;
                }
            }
        }else{
            for (int i = 0 ; i < leftTrace.size();i++){
                if(rightTrace.size() == i+1){
                    if(!(rightTrace.get(i).globalNum < leftTrace.get(i).globalNum)){
                        return false;
                    }
                }else if(!(rightTrace.get(i).globalNum < leftTrace.get(i).globalNum && leftTrace.get(i).globalNum < rightTrace.get(i+1).globalNum)){
                    return false;
                }
            }
        }
        return true;

    }

    public ArrayList<Clock> getIntersectionClock(ArrayList<Clock> leftTrace,ArrayList<Clock> rightTrace,String clockName,int clockId){
        ArrayList<Clock> unionClock  = new ArrayList<>();
        int leftIndex = 0;
        int rightIndex = 0;
        int count = 0;
        while (leftIndex < leftTrace.size() && rightIndex < rightTrace.size()){
            Clock rightClock = rightTrace.get(rightIndex);
            Clock leftClock = leftTrace.get(leftIndex);
            if(leftClock.globalNum == rightClock.globalNum){
                count ++ ;
                Clock clock = new Clock(clockId,clockName,leftClock.globalNum,count);
                unionClock.add(clock);
                rightIndex++;
                leftIndex++;
            }else if(leftClock.globalNum > rightClock.globalNum){
                rightIndex++;
            }else {
                leftIndex++;
            }
        }
        return unionClock;

    }

    public ArrayList<Clock> getUnionClock(ArrayList<Clock> leftTrace,ArrayList<Clock> rightTrace,String clockName,int clockId){
        ArrayList<Clock> unionClock  = new ArrayList<>();
        int leftIndex = 0;
        int rightIndex = 0;
        int count = 0;
        while (leftIndex < leftTrace.size() || rightIndex < rightTrace.size()){
            if (leftIndex >= leftTrace.size()){
                Clock rightClock = rightTrace.get(rightIndex);
                count ++ ;
                Clock clock = new Clock(clockId,clockName,rightClock.globalNum,count);
                unionClock.add(clock);
                rightIndex++;
            }else if (rightIndex >= rightTrace.size()){
                Clock leftClock = leftTrace.get(leftIndex);
                count ++ ;
                Clock clock = new Clock(clockId,clockName,leftClock.globalNum,count);
                unionClock.add(clock);
                leftIndex++;
            }else {
                Clock rightClock = rightTrace.get(rightIndex);
                Clock leftClock = leftTrace.get(leftIndex);
                if(leftClock.globalNum == rightClock.globalNum){
                    count ++ ;
                    Clock clock = new Clock(clockId,clockName,leftClock.globalNum,count);
                    unionClock.add(clock);
                    rightIndex++;
                    leftIndex++;
                }else if(leftClock.globalNum > rightClock.globalNum){
                    count ++ ;
                    Clock clock = new Clock(clockId,clockName,rightClock.globalNum,count);
                    unionClock.add(clock);
                    rightIndex++;
                }else {
                    count ++ ;
                    Clock clock = new Clock(clockId,clockName,leftClock.globalNum,count);
                    unionClock.add(clock);
                    leftIndex++;
                }
            }

        }
        return unionClock;

    }

    public ArrayList<Clock> getInfimumClock(ArrayList<Clock> leftTrace,ArrayList<Clock> rightTrace,String clockName,int clockId){
        ArrayList<Clock> infClock  = new ArrayList<>();
        int count = 0;
        int shotCount = 0;
        int choose = 0;
        if(leftTrace.size() <= rightTrace.size()){
            count = rightTrace.size();
            shotCount = leftTrace.size();
            choose = 1;
        }else{
            count = leftTrace.size();
            shotCount = rightTrace.size();
        }
        for (int i = 0 ; i < count ; i++){
            if(shotCount>i){
                Clock rightClock = rightTrace.get(i);
                Clock leftClock = leftTrace.get(i);
                if(leftClock.globalNum <= rightClock.globalNum){
                    Clock clock = new Clock(clockId,clockName,leftClock.globalNum,count);
                    infClock.add(clock);
                }else {
                    Clock clock = new Clock(clockId,clockName,rightClock.globalNum,count);
                    infClock.add(clock);
                }
            }
            else{
                if(choose == 1){
                    Clock clock = new Clock(clockId,clockName,rightTrace.get(i).globalNum,count);
                    infClock.add(clock);
                }else {
                    Clock clock = new Clock(clockId,clockName,leftTrace.get(i).globalNum,count);
                    infClock.add(clock);
                }
            }
        }
        return infClock;

    }

    public ArrayList<Clock> getSupremumClock(ArrayList<Clock> leftTrace,ArrayList<Clock> rightTrace,String clockName,int clockId){
        ArrayList<Clock> supClock  = new ArrayList<>();
        int count = 0;
        if(leftTrace.size() <= rightTrace.size()){
            count = leftTrace.size();
        }else{
            count = rightTrace.size();
        }
        for (int i = 0 ; i < count ; i++){
            Clock rightClock = rightTrace.get(i);
            Clock leftClock = leftTrace.get(i);
            if(leftClock.globalNum <= rightClock.globalNum){
                Clock clock = new Clock(clockId,clockName,rightClock.globalNum,count);
                supClock.add(clock);
            }else {
                Clock clock = new Clock(clockId,clockName,leftClock.globalNum,count);
                supClock.add(clock);
            }

        }
        return supClock;

    }
}
