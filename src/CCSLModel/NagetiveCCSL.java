package CCSLModel;

import java.util.ArrayList;

/**
 * Created by huming on 2019/6/24.
 */
public class NagetiveCCSL {
    public ArrayList<Expression> expressions;
    public ArrayList<Relation> relations;
    public ArrayList<ArrayList<String>> relationAssetList;
    public ArrayList<ArrayList<String>> expressionAssetList;
    public NagetiveCCSL(){
        expressions = new ArrayList<>();
        relations = new ArrayList<>();
    }
    public NagetiveCCSL(ArrayList<Expression>expressions, ArrayList<Relation>relations){
        this.relations = relations;
        this.expressions = expressions;
        relationAssetList = new ArrayList<>();
        expressionAssetList = new ArrayList<>();
        for (int i=0 ; i< relations.size();i++){
            ArrayList<String> strList = new ArrayList<>();
            relationAssetList.add(strList);
        }
        for (int i=0 ; i< expressions.size()+1;i++){
            ArrayList<String> strList = new ArrayList<>();
            expressionAssetList.add(strList);
        }
    }
    public void addRelationAssert(Relation relation,String assertStr){
        for (int i = 0 ; i < relations.size();i++){
            if (relations.get(i).equals(relation)){
                relationAssetList.get(i).add(assertStr);
            }
        }
    }
    public void addExpressionCombAssert(ArrayList<Expression> comb,String assertStr){
        Boolean isComb = true;
        for (int i = 0; i < comb.size();i++){
            Expression exp = comb.get(i);
            boolean isGet = false;
            for (int j = 0 ; j < expressions.size();j++){
                Expression expression = expressions.get(j);
                if (exp.clockEquals(expression)){
                    isGet = true;
                    break;
                }
            }
            if (!isGet){
                isComb = false;
                break;
            }
        }
        if (isComb){
            expressionAssetList.get(0).add(assertStr);
        }

    }
    public void addExpressionAssert(Expression expression,String assertStr){
        for (int i = 0 ; i < expressions.size();i++){
            if (expressions.get(i).equals(expression)){
                expressionAssetList.get(i+1).add(assertStr);
            }
        }
    }
    public String assertString(){
        String assertStr = "";
        for (int i = 0 ; i < relationAssetList.size();i++){
            ArrayList<String> assertList = relationAssetList.get(i);
            String subAssert = "(";
            for (int j = 0; j < assertList.size(); j++){
                if (!subAssert.equals("(")){
                    subAssert+="||";
                }
                subAssert += assertList.get(j);
            }
            if(!subAssert.equals("(")){
                subAssert+=")";
            }
            if ((!assertStr.equals(""))&&(!subAssert.equals("("))){
                assertStr+="&&";
            }
            if(!subAssert.equals("(")){
                assertStr+=subAssert;
            }

        }
        for (int i = 0 ; i < expressionAssetList.size();i++){
            ArrayList<String> assertList = expressionAssetList.get(i);
            String subAssert = "(";
            for (int j = 0; j < assertList.size(); j++){
                if (!subAssert.equals("(")){
                    subAssert+="||";
                }
                subAssert += assertList.get(j);
            }
            if(!subAssert.equals("(")){
                subAssert+=")";
            }
            if ((!assertStr.equals(""))&&(!subAssert.equals("("))){
                assertStr+="&&";
            }
            if(!subAssert.equals("(")){
                assertStr+=subAssert;
            }
        }
        return assertStr;
    }
}
