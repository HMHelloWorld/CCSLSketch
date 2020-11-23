package Parser;

import CCSLModel.Expression;
import CCSLModel.Relation;
import CCSLModel.Variable;

import java.util.ArrayList;

/**
 * Created by huming on 2019/6/24.
 */
public class RelationParser {
    public ArrayList<Relation> relations;
    public ArrayList<Expression>expressions;
    private static RelationParser instance = null;
    public static RelationParser getInstance(){
        if(instance == null){
            instance = new RelationParser();
        }
        return instance;
    }
    private RelationParser(){
        relations = new ArrayList<>();
        expressions = new ArrayList<>();
    }

    public void addRelation(ArrayList<String> clocks,int type){
        ClockParser clockParser = ClockParser.getInstance();
        ArrayList<Variable> variables = new ArrayList<>();
        for (int i = 0 ; i < clocks.size();i++){
            int id = clockParser.getClockId(clocks.get(i));
            if(id == -1){
                id = ExpressionParser.getInstance().getExpressionId(clocks.get(i));
                if(id == -1){
                    Variable variable = new Variable(clocks.get(i),-1);
                    variables.add(variable);
                }else {
                    Variable variable = new Variable(clocks.get(i),1);
                    variables.add(variable);
                }
            }else{
                Variable variable = new Variable(clocks.get(i),0);
                variables.add(variable);
            }
        }
        Relation relation = new Relation(variables,type);
        relations.add(relation);
    }


    public static int getPenalty(int relationId){
        switch (relationId){
            case 0:
                //"==";
                return 0;
            case 1:
                //"<";
                return 3;
            case 2:
                //"<=";
                return 4;
            case 3:
                //"#";
                return 2;
            case 4:
                //"is sub-clock of";
                return 2;
            case 5:
                //"alternates with";
                return 1;
            case 10:
                //"Alternates With & Strict Precedence";
                return 0;
            case 11:
                //"Exclusion & Strict Precedence";
                return 1;
            case 12:
                //"Subclock & Strict Precedence";
                return 0;
            case 13:
                //"Subclock & Strict Precedence";
                return 10;
            default:
                return 10;
        }
    }
}
