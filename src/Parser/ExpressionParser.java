package Parser;

import CCSLModel.Expression;
import CCSLModel.Relation;
import CCSLModel.Variable;

import java.util.ArrayList;

/**
 * Created by huming on 2019/6/24.
 */
public class ExpressionParser {
    public ArrayList<Expression> expressionList;
    private static ExpressionParser instance = null;
    public static ExpressionParser getInstance(){
        if(instance == null){
            instance = new ExpressionParser();
        }
        return instance;
    }
    private ExpressionParser(){
        expressionList = new ArrayList<>();
    }
    public void addExpression(String name,ArrayList<String> clocks,int type,int addition){
        ArrayList<Variable> variables = new ArrayList<>();
        for (int i = 0 ; i < clocks.size();i++){
            String clock = clocks.get(i);
            int ctype = 0;
            if (clock == null||clock.equals("")){
                ctype = -1;
            }else {
                int id = ClockParser.getInstance().getClockId(clock);
                if (id == -1){
                    ctype = 1;
                }
            }

            Variable variable = new Variable(clock,ctype);
            variables.add(variable);

        }
        Expression expression = new Expression(expressionList.size(),name,variables,type,addition);
        expressionList.add(expression);
    }
    public int getExpressionId(String name){
        for (int  i = 0 ; i < expressionList.size(); i ++){
            String e = expressionList.get(i).name;
            if(e.equals(name)){
                return i;
            }
        }
        return -1;
    }
    public ArrayList<Expression> refreshExpression(){
        ArrayList<Expression> nonorderExpression = new ArrayList<>();

        for (int i= 0 ; i < expressionList.size() ;i ++){
            nonorderExpression.add(expressionList.get(i));
        }

        ArrayList<Expression> orderExpressions = new ArrayList<>();

        while (nonorderExpression.size() > 0){
            for (int j = 0 ; j < nonorderExpression.size();j++){
                Expression expression = nonorderExpression.get(j);
                Boolean isAdd = true;
                for (int k = 0 ; k < expression.variables.size();k++){
                    Variable variable = expression.variables.get(k);
                    if (variable.type == 1){
                        Boolean isGet = false;
                        for (int i= 0 ; i < orderExpressions.size();i++){
                            if(orderExpressions.get(i).name.equals(expression.name)){
                                isGet =true;
                            }
                        }
                        if (!isGet){
                            isAdd = false;
                            break;
                        }
                    }
                }
                if (isAdd){
                    nonorderExpression.remove(j);
                    orderExpressions.add(expression);
                    j--;
                }
            }
        }
        return orderExpressions;
    }

    public ArrayList<ArrayList<Expression>> orderExpressionCombinations(){
        ArrayList<Expression> nonorderExpression = new ArrayList<>();

        for (int i= 0 ; i < expressionList.size() ;i ++){
            nonorderExpression.add(expressionList.get(i));
        }

        ArrayList<Expression> orderExpressions = new ArrayList<>();

        int size = 0;
        do{
            size = nonorderExpression.size();
            for (int j = 0 ; j < nonorderExpression.size();j++){
                Expression expression = nonorderExpression.get(j);
                Boolean isAdd = true;
                for (int k = 0 ; k < expression.variables.size();k++){
                    Variable variable = expression.variables.get(k);
                    if (variable.name.equals("")){
                        isAdd = false;
                        break;
                    }
                    if (variable.type == 1){
                        Boolean isGet = false;
                        for (int i= 0 ; i < orderExpressions.size();i++){
                            if(orderExpressions.get(i).name.equals(variable.name)){
                                isGet =true;
                            }
                        }
                        if (!isGet){
                            isAdd = false;
                            break;
                        }
                    }

                }
                if (isAdd){
                    nonorderExpression.remove(j);
                    orderExpressions.add(expression);
                    j--;
                }
            }
        }while (nonorderExpression.size() != size);
        return orderExpressionTrace(orderExpressions,nonorderExpression);
    }

    public ArrayList<ArrayList<Expression>> orderExpressionTrace(ArrayList<Expression> orderExps,ArrayList<Expression> nonorderExps){
        ArrayList<ArrayList<Expression>> traceList = new ArrayList<>();
        ArrayList<Expression>orders = copyExpressioList(orderExps);
        ArrayList<Expression>nonorders = copyExpressioList(nonorderExps);
        if( nonorderExps.size() == 0 ){
            traceList.add(orders);
            return traceList;
        }else {
            Expression expression = null;
            int index = 0;
            int expIndex = 0;
            for (int i= 0 ; i < nonorders.size();i++){
                Expression curExp = nonorders.get(i);
                for (int j = 0 ;j < curExp.variables.size();j++){
                    Variable variable = curExp.variables.get(j);
                    if(variable.name.equals("")||variable.name == null){
                        expression = curExp;
                        index = j;
                        expIndex = i;
                        break;
                    }
                }
                if(expression!= null){
                    break;
                }
            }
            if(expression == null){
                return traceList;
            }else {
                ArrayList<String> clocks = ClockParser.getInstance().clockList;
                for (int i = 0; i < clocks.size();i++){
                    int vIndex = index==0?1:0;
                    if( (expression.type==4||expression.type==5)||((!expression.variables.get(vIndex).name.equals(clocks.get(i)))&&isCheckRelation(expression,clocks.get(i),index))){

                        Expression exp = new Expression(expression);
                        exp.variables.remove(index);
                        Variable variable = new Variable(clocks.get(i),0);
                        exp.variables.add(index,variable);
                        ArrayList<Expression> orderList = copyExpressioList(orders);
                        ArrayList<Expression> nonorderList = copyExpressioList(nonorders);
                        nonorderList.remove(expIndex);
                        nonorderList.add(expIndex,exp);
                        int size = 0;
                        do{
                            size = nonorderList.size();
                            for (int j = 0 ; j < nonorderList.size();j++){
                                Expression expr = nonorderList.get(j);
                                Boolean isAdd = true;
                                for (int k = 0 ; k < expr.variables.size();k++){
                                    Variable var = expr.variables.get(k);
                                    Boolean isGet = false;
                                    if (var.name.equals("")){
                                        isAdd = false;
                                        break;
                                    }
                                    if (var.type == 1){

                                        for (int p= 0 ; p < orderList.size();p++){
                                            if(orderList.get(p).name.equals(var.name)){
                                                isGet =true;
                                            }
                                        }
                                        if (!isGet){
                                            isAdd = false;
                                            break;
                                        }
                                    }
                                }
                                if (isAdd){
                                    nonorderList.remove(j);
                                    orderList.add(expr);
                                    j--;
                                }
                            }
                        }while (nonorderList.size() != size);


                        ArrayList<ArrayList<Expression>> result = orderExpressionTrace(orderList,nonorderList);
                        for (int j = 0 ; j < result.size() ; j ++){
                            traceList.add(result.get(j));
                        }
                    }
                }




                for (int i = 0; i < orders.size();i++){

                    int vIndex = index==0?1:0;
                    if( (expression.type==4||expression.type==5||((!expression.variables.get(vIndex).name.equals(orders.get(i).name))&&isCheckRelation(expression,orders.get(i).name,index)))){
                        Expression exp = new Expression(expression);
                        exp.variables.remove(index);
                        Variable variable = new Variable(orders.get(i).name,1);
                        exp.variables.add(index,variable);
                        ArrayList<Expression> orderList = copyExpressioList(orders);
                        ArrayList<Expression> nonorderList = copyExpressioList(nonorders);
                        nonorderList.remove(expIndex);
                        nonorderList.add(expIndex,exp);
                        int size = 0;
                        do{
                            size = nonorderList.size();
                            for (int j = 0 ; j < nonorderList.size();j++){
                                Expression expr = nonorderList.get(j);
                                Boolean isAdd = true;
                                for (int k = 0 ; k < expr.variables.size();k++){
                                    Variable var = expr.variables.get(k);
                                    Boolean isGet = false;
                                    if (var.name.equals("")){
                                        isAdd = false;
                                        break;
                                    }
                                    if (var.type == 1){

                                        for (int p= 0 ; p < orderList.size();p++){
                                            if(orderList.get(p).name.equals(var.name)){
                                                isGet =true;
                                            }
                                        }
                                        if (!isGet){
                                            isAdd = false;
                                            break;
                                        }
                                    }
                                }
                                if (isAdd){
                                    nonorderList.remove(j);
                                    orderList.add(expr);
                                    j--;
                                }
                            }
                        }while (nonorderList.size() != size);


                        ArrayList<ArrayList<Expression>> result = orderExpressionTrace(orderList,nonorderList);
                        for (int j = 0 ; j < result.size() ; j ++){
                            traceList.add(result.get(j));
                        }
                    }
                }

                for (int i = 0; i < nonorders.size();i++){

                    int vIndex = index==0?1:0;
                    Expression nonorderExpression = nonorders.get(i);

                    if(expression.type==4||expression.type==5|| ((!expression.variables.get(vIndex).name.equals(nonorderExpression.name))
                            &&(!nonorderExpression.variables.get(0).name.equals(expression.name))
                            &&(!nonorderExpression.variables.get(1).name.equals(expression.name))
                            &&isCheckRelation(expression,nonorderExpression.name,index))){
                        Expression exp = new Expression(expression);
                        exp.variables.remove(index);
                        Variable variable = new Variable(nonorderExpression.name,1);
                        exp.variables.add(index,variable);
                        ArrayList<Expression> orderList = copyExpressioList(orders);
                        ArrayList<Expression> nonorderList = copyExpressioList(nonorders);
                        nonorderList.remove(expIndex);
                        nonorderList.add(expIndex,exp);
                        int size = 0;
                        do{
                            size = nonorderList.size();
                            for (int j = 0 ; j < nonorderList.size();j++){
                                Expression expr = nonorderList.get(j);
                                Boolean isAdd = true;
                                for (int k = 0 ; k < expr.variables.size();k++){
                                    Variable var = expr.variables.get(k);
                                    Boolean isGet = false;
                                    if (var.name.equals("")){
                                        isAdd = false;
                                        break;
                                    }
                                    if (var.type == 1){

                                        for (int p= 0 ; p < orderList.size();p++){
                                            if(orderList.get(p).name.equals(var.name)){
                                                isGet =true;
                                            }
                                        }
                                        if (!isGet){
                                            isAdd = false;
                                            break;
                                        }
                                    }
                                }
                                if (isAdd){
                                    nonorderList.remove(j);
                                    orderList.add(expr);
                                    j--;
                                }
                            }
                        }while (nonorderList.size() != size);
                        ArrayList<ArrayList<Expression>> result = orderExpressionTrace(orderList,nonorderList);
                        for (int j = 0 ; j < result.size() ; j ++){
                            traceList.add(result.get(j));
                        }
                    }
                }
            }
            return traceList;
        }
    }
    public ArrayList<Expression> copyExpressioList(ArrayList<Expression> list){
        ArrayList<Expression> copyList = new ArrayList<>();
        for (int i = 0 ; i < list.size();i++){
            copyList.add(new Expression(list.get(i)));
        }
        return copyList;
    }

    public boolean isCheckRelation(Expression expression,String addV,int index){
//        int type = expression.type;
        String expressionName = expression.name;
        for (int i = 0 ; i < expression.variables.size();i++){
            if(i != index){
                if(expression.variables.get(i).name.equals(addV)){
                    return false;
                }
            }
        }
        ArrayList<Relation> relations =  RelationParser.getInstance().relations;
        for (int i = 0 ; i < relations.size();i++){
            Relation Relation = relations.get(i);
            String leftName = Relation.variables.get(0).name;
            String rightName = Relation.variables.get(1).name;
            if ((leftName.equals(addV)&&rightName.equals(expressionName))||(rightName.equals(addV)&&leftName.equals(expressionName))){
                return false;
            }
        }
        return true;


    }
    public Expression getExpression(String name){
        for (int  i = 0 ; i < expressionList.size(); i ++){
            String e = expressionList.get(i).name;
            if(e.equals(name)){
                return expressionList.get(i);
            }
        }
        return null;
    }
}
