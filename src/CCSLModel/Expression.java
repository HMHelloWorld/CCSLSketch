package CCSLModel;

import java.util.ArrayList;

/**
 * Created by huming on 2019/6/24.
 */
public class Expression {
    public int id;
    public String name;
    public ArrayList<Variable> variables;
    public int addition;

    /**
     * type
     * Expression: Union(+)                    0
     * Expression: Intersection(*)             1
     * Expression: Sup(&)                      2
     * Expression: Infimum(|)                  3
     * Expression: Delay($)                    4
     * Expression: Periodic                    5
     * Expression: DelayFor($ on)              6
     * Expression: SampledOn                   7
     */
    public int type;

    public Expression(int id,String name,ArrayList<Variable> variables,int type,int addition){
        this.id = id;
        this.name = name;
        this.variables = variables;
        this.addition = addition;
        this.type = type;
    }
    public Expression(Expression exp){
        this.variables = new ArrayList<>();
        this.id = exp.id;
        this.name = exp.name;
        for (int i = 0 ; i < exp.variables.size();i++){
            this.variables.add(new Variable(exp.variables.get(i)));
        }
        this.addition = exp.addition;
        this.type = exp.type;
    }
    public boolean equals(Expression e){
        if (e.type != type || (!name.equals(e.name)) || (variables.size()!=e.variables.size())){
            return false;
        }
        for (int  i = 0 ; i < variables.size();i++){
            if(!variables.get(i).equals(e.variables.get(i))){
                return false;
            }
        }
        if(variables.size() == 1 || type == 6){
            if(addition!=e.addition){
                return false;
            }
        }
        return true;
    }
    public boolean clockEquals(Expression e){
        if (variables.size()!=e.variables.size() || (!name.equals(e.name)) ){
            return false;
        }
        for (int  i = 0 ; i < variables.size();i++){
            if(!variables.get(i).equals(e.variables.get(i))){
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        String expressionStr = "";
        switch (type){
            case 0:
                expressionStr = variables.get(0).name + " " + "+" + " " + variables.get(1).name;
                break;
            case 1:
                expressionStr = variables.get(0).name + " " + "*" + " " + variables.get(1).name;
                break;
            case 2:
                expressionStr = variables.get(0).name + " " + "&" + " " + variables.get(1).name;
                break;
            case 3:
                expressionStr = variables.get(0).name + " " + "|" + " " + variables.get(1).name;
                break;
            case 4:
                expressionStr = variables.get(0).name + " " + "$" + " " + addition;
                break;
            case 5:
                expressionStr = variables.get(0).name + " " + "Periodic" + " " + addition;
                break;
            case 6:
                expressionStr = variables.get(0).name + " " + "$" + " " + addition + " on " +  variables.get(1).name;
                break;
            case 7:
                expressionStr = variables.get(0).name + " " + "SampledOn" + " " + variables.get(1).name;;
                break;
            default:
                break;
        }

        return variables.get(0).name + " " + expressionStr + " " + variables.get(1).name;
    }
    public static String getSymbol(String name){
        if (name.equals("Union")){
            return "+";
        }else if (name.equals("Intersection")){
            return "*";
        }else if (name.equals("Supremum")){
            return "&";
        }else if (name.equals("Infimum")){
            return "|";
        }else if (name.equals("Delay")){
            return "$";
        }else if (name.equals("Periodic")){
            return "∝";
        }else if (name.equals("DelayFor")){
            return "$";
        }else if (name.equals("SampledOn")){
            return "SampledOn";
        }
        return "";
    }
}
