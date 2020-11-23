package CCSLModel;

import java.util.ArrayList;

/**
 * Created by huming on 2019/6/24.
 */
/*
    * Relation type
    * Relation: Coincidence(==)             0
    * Relation: Strict Precedence(<)        1
    * Relation: Non-strict Precedence(<=)   2
    * Relation: Exclusion(#)                3
    * Relation: Sub-Clock                   4
    * Relation: Alternates With             5
    * Relation: Upto                        6
    * Relation: Concat                      7
    *
    * RelationExtend: Alternates With & Strict Precedence 10
    * RelationExtend: Exclusion & Strict Precedence 11
    * RelationExtend: Sub-Clock & Strict Precedence 12
    * RelationExtend: Irrelevant 13
    */
public class Relation {
    public ArrayList<Variable> variables;
    public int type;

    public Relation(ArrayList<Variable> clocks ,int type){
        this.variables = clocks;
        this.type = type;
    }
    public boolean equals(Relation constraint){
        if (constraint.type != type || variables.size()!=constraint.variables.size()){
            return false;
        }
        for (int  i = 0 ; i < variables.size();i++){
            if(!variables.get(i).equals(constraint.variables.get(i))){
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        String relationStr = "";
        switch (type){
            case 0:
                relationStr = "==";
                break;
            case 1:
                relationStr = "<";
                break;
            case 2:
                relationStr = "<=";
                break;
            case 3:
                relationStr = "#";
                break;
            case 4:
                relationStr = "is sub-clock of";
                break;
            case 5:
                relationStr = "is alternates with";
                break;
            case 6:
                relationStr = "upto";
                break;
            case 7:
                relationStr = "concat";
                break;
            default:
                break;
        }
        return variables.get(0).name + " " + relationStr + " " + variables.get(1).name;
    }
    public static String getSymbol(String name){
        if (name.equals("Coincidence")){
            return "==";
        }else if (name.equals("StrictPrecedence")){
            return "<";
        }else if (name.equals("NonStrictPrecedence")){
            return "<=";
        }else if (name.equals("Exclusion")){
            return "#";
        }else if (name.equals("SubClock")){
            return "⊆";
        }else if (name.equals("Periodic")){
            return "∝";
        }else if (name.equals("DelayFor")){
            return "$";
        }else if (name.equals("AlternatesWith")){
            return "~";
        }else if (name.equals("AlternatesWithAndStrictPrecedence")){
            return "~<";
        }else if (name.equals("ExclusionAndStrictPrecedence")){
            return "#<";
        }else if (name.equals("SubClockAndStrictPrecedence")){
            return "⊆>";
        }else if (name.equals("Irrelevant")){
            return "rrelevant";
        }
        return name;
    }
    public static int extendStart(){
        return 10;
    }
    public static int extendEnd(){
        return 13;
    }
}
