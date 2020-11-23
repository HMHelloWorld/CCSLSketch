package Parser;

import CCSLModel.Expression;
import CCSLModel.NagetiveCCSL;
import CCSLModel.Relation;

import java.util.ArrayList;

/**
 * Created by huming on 2019/6/24.
 */
public class NagetiveCCSLParser {
    public ArrayList<NagetiveCCSL> nagetiveCCSLs;
    private static NagetiveCCSLParser instance = null;
    private NagetiveCCSLParser(){
        nagetiveCCSLs = new ArrayList<>();
    }
    public static NagetiveCCSLParser getInstance(){
        if (instance == null){
            instance = new NagetiveCCSLParser();
        }
        return instance;
    }
    public void addNagetiveCCSL(ArrayList<Expression>expressions, ArrayList<Relation>relations){

//        ArrayList<Expression> expList = ExpressionParser.getInstance().expressionList;
//        for (int i = 0 ; i < expressions.size();i++){
//            Expression expression = expressions.get(i);
//            for (int j = 0 ; j < expList.size();j++){
//                Expression exp = expList.get(j);
//                if (exp.equals(expression)){
//                    expressions.remove(i);
//                    i--;
//                    break;
//                }
//            }
//        }

        ArrayList<Relation> relationList = RelationParser.getInstance().relations;
        for (int i = 0 ; i < relations.size();i++){
            Relation relation = relations.get(i);
            for (int j = 0 ; j < relationList.size();j++){
                Relation rel = relationList.get(j);
                if (rel.equals(relation)){
                    relations.remove(i);
                    i--;
                    break;
                }
            }
        }

        NagetiveCCSL nagetiveCCSL = new NagetiveCCSL(expressions,relations);
        nagetiveCCSLs.add(nagetiveCCSL);
    }
    public void addRelationAssert(Relation relation,String assertStr){
        for (int i = 0; i < nagetiveCCSLs.size();i++){
            NagetiveCCSL ccsl = nagetiveCCSLs.get(i);
            ccsl.addRelationAssert(relation,assertStr);

        }

    }
    public void addExpressionAssert(Expression expression,String assertStr){
        for (int i = 0; i < nagetiveCCSLs.size();i++){
            NagetiveCCSL ccsl = nagetiveCCSLs.get(i);
            ccsl.addExpressionAssert(expression,assertStr);

        }

    }
    public void addExpressionCombAssert(ArrayList<Expression> comb,String assertStr){
        for (int i = 0; i < nagetiveCCSLs.size();i++){
            NagetiveCCSL ccsl = nagetiveCCSLs.get(i);
            ccsl.addExpressionCombAssert(comb,assertStr);

        }

    }
    public ArrayList<String> getNagetiveCCSLAssert(){
        ArrayList<String> asserts = new ArrayList<>();
        for (int  i = 0 ; i < nagetiveCCSLs.size();i++){
            asserts.add(nagetiveCCSLs.get(i).assertString());
        }
        return asserts;
    }
}
