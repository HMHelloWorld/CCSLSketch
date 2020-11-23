package Encoder;

import CCSLModel.Expression;
import CCSLModel.Relation;
import CCSLModel.RelationMap;
import CCSLModel.Variable;
import Parser.ClockParser;
import Parser.ExpressionParser;
import Parser.NagetiveCCSLParser;
import Parser.RelationParser;

import java.util.ArrayList;

/**
 * Created by huming on 2019/6/24.
 */
public class CheckFuncEncoder {
    private int relationMax;
    private int maxLength;
    private boolean startRelationExtend;
    private int penaltyCount;
    private RelationMap relationMap;
    private ArrayList<Variable> allVariables;
    public CheckFuncEncoder(int maxLength,ArrayList<Variable> allVariables){
        this.maxLength = maxLength;
        this.allVariables = allVariables;
        relationMax = 5;
        penaltyCount = 0;
        relationMap = new RelationMap();
        startRelationExtend = false;
    }
    public CheckFuncEncoder(int maxLength,ArrayList<Variable> allVariables,boolean isRelationExtend){
        this.maxLength = maxLength;
        this.allVariables = allVariables;
        relationMax = 5;
        penaltyCount = 0;
        relationMap = new RelationMap();
        startRelationExtend = isRelationExtend;
    }
    public String encodingCheckFuncDef(){
        ArrayList<String> clockList = ClockParser.getInstance().clockList;
        String countStr = "";
        String varStr = "";
        String checkStr = "int check(";
        for (int i= 0;i< clockList.size();i++){
            if (i != 0){
                varStr += ",";
                countStr += ",";
            }
            varStr += "int" + "[" + maxLength + "]"+ " clock_"+ clockList.get(i);
            countStr += "int" + " " + clockList.get(i) + "_count";
        }
        checkStr += countStr + "," + varStr ;
        checkStr += ")";
        return checkStr;
    }

    public String encodingCheckFunc(int tab){
        String tabStr = getTab(tab);
        String funcStr = "";
        ArrayList<Relation> relations = RelationParser.getInstance().relations;
        String funcName = encodingCheckFuncDef();
        funcStr += tabStr + funcName + "{\n"
                + encodingExpression(tab+1)
                + encodingClockPenaltyStr(tab+1)
                + encodingRelation(tab+1,relations) + "\n"
                + tabStr + "}";
        return funcStr;
    }

    public String encodingRelation(int tab,ArrayList<Relation> relations){
        String tabStr = getTab(tab);
        String relationStr = "";
        for (int i= 0 ; i < relations.size();i++){
            Relation relation = relations.get(i);
            boolean isNull = false;
            for (int j = 0 ; j < relation.variables.size();j++){
                if(relation.variables.get(j).name.equals("")){
                    isNull = true;
                }
            }
            String func = getFucName(relation.type,relation.variables,tab,i);

            if (relation.type == -1){
                relationStr += func;
            }else if(isNull){
                relationStr += func;
            }else {
                relationStr += tabStr + "if(" + func + "==0){" + "\n"
                        + tabStr + "    " + "return 0;" + "\n"
                        + tabStr + "}" + "\n";
                String penatlyStr = "int penalty_" + penaltyCount + "= 0";
                for (int j = 0 ; j < relation.variables.size();j++){
                    if ( relation.variables.get(j).type==1 ){
                        switch (relation.type){
                            case 0:

                                break;
                            case 1:
                                if (j == 0){
                                    penatlyStr += " + 3 - " + relation.variables.get(j).name+"_func_penalty";
                                }else {
                                    penatlyStr += " + " + relation.variables.get(j).name+"_func_penalty";
                                }
                                break;
                            case 2:

                                if (j == 0){
                                    penatlyStr += " + 3 - " + relation.variables.get(j).name+"_func_penalty";
                                }else {
                                    penatlyStr += " + " + relation.variables.get(j).name+"_func_penalty";
                                }
                                break;
                            case 3:

                                penatlyStr += " + " + relation.variables.get(j).name+"_func_penalty";
                                break;
                            case 4:

                                if (j == 1){
                                    penatlyStr += " + 3 -" + relation.variables.get(j).name+"_func_penalty";
                                }else {
                                    penatlyStr += " +  " + relation.variables.get(j).name+"_func_penalty";
                                }
                                break;
                            case 5:
                                penatlyStr += " + 3 - " + relation.variables.get(j).name+"_func_penalty";
                                break;
                            default:
                                break;
                        }
                    }
                }

                penaltyCount++;
                penatlyStr += ";"+"\n";
                relationStr += tabStr + penatlyStr;
            }

        }
        relationStr += encodingPenaltyStr(tab);
        relationStr += encodingNagetiveCCSL(tab) +"\n";
        String ss = encodingNagetiveCCSL(tab) +"\n";
        relationStr += tabStr + "return 1;";
        return relationStr;
    }

    public String getFucName(int type,ArrayList<Variable>oldClocks,int tab,int index){
        String tabStr = getTab(tab);
        String funcStr = "";
        int relationIndex = index;
        ArrayList<Variable>clocks = new ArrayList<>();
        for (int i = 0 ; i < oldClocks.size();i++){
            Variable variable = new Variable(oldClocks.get(i));
            clocks.add(variable);
        }

        if(type == -1){
            funcStr += tabStr + "int penalty_" + penaltyCount + "= 0;" + "\n";
            funcStr += tabStr + "int relation_" + penaltyCount + "= ??;" + "\n";
            ArrayList<String> funcList = new ArrayList<>();
            ArrayList<Integer> penaltyList = new ArrayList<>();
            ArrayList<ArrayList<Variable>> clocksList = new ArrayList<>();
            for (int  i = 0 ; i < relationMax + 1;i++){

                if(checkRelations(i,clocks,index)){
                    funcList.add(getSubFuncName(i,clocks));
                    penaltyList.add(i);
                    clocksList.add(clocks);

                }
                if (clocks.size() == 2 && i != 0){
                    ArrayList<Variable> changeclock = new ArrayList<>();
                    changeclock.add(clocks.get(1));
                    changeclock.add(clocks.get(0));
                    if(checkRelations(i,clocks,index)){
                        funcList.add(getSubFuncName(i,changeclock));
                        penaltyList.add(i);
                        clocksList.add(changeclock);
                    }
                }

            }
            if (startRelationExtend){
                for (int i = Relation.extendStart();i<=Relation.extendEnd();i++){
                    if(checkRelations(i,clocks,index)){
                        funcList.add(getSubFuncName(i,clocks));
                        penaltyList.add(i);
                        clocksList.add(clocks);

                    }
                    if (clocks.size() == 2 && i != 0){
                        ArrayList<Variable> changeclock = new ArrayList<>();
                        changeclock.add(clocks.get(1));
                        changeclock.add(clocks.get(0));
                        if(checkRelations(i,clocks,index)){
                            funcList.add(getSubFuncName(i,changeclock));
                            penaltyList.add(i);
                            clocksList.add(changeclock);
                        }
                    }
                }
            }


            for(int i = 0 ; i < funcList.size();i++){
                String penaltyStr = relationMap.getPenaltyStr(penaltyList.get(i),clocksList.get(i).get(0).name,clocksList.get(i).get(1).name);
                if (funcList.size() ==1){
                    funcStr += tabStr +"if(" + getSubFuncName(penaltyList.get(i),clocksList.get(i)) + "==0"+ penaltyStr +"){" + "\n"
                            + tabStr + "    " + "return 0;" + "\n"
                            + tabStr + "}" + "\n";
                    break;
                }else {
                    if (i == funcList.size()-1){
                        funcStr += tabStr + "else {"+"\n";
                    }else if(i != 0){
                        funcStr += tabStr + "else if(" +"relation_" + penaltyCount +" == "+ i + penaltyStr +"){"+"\n";
                    }else {
                        funcStr += tabStr + "if(" +"relation_" + penaltyCount +" == "+ i + penaltyStr +"){"+"\n";
                    }

                    funcStr += tabStr + "    " + "penalty_" + penaltyCount + " = " + RelationParser.getPenalty(penaltyList.get(i)) + ";"+"\n";
                    funcStr += tabStr + "    " + "if(" + getSubFuncName(penaltyList.get(i),clocksList.get(i)) + "==0){" + "\n"
                            + tabStr + "        " + "return 0;" + "\n"
                            + tabStr + "    " + "}" + "\n";
                    funcStr += tabStr + "}"+"\n";
                    if(i == funcList.size() - 1){
                        funcStr+= tabStr +"assert relation_"+penaltyCount+ ">=0&&relation_"+penaltyCount + "<"+funcList.size()+";"+"\n";
                    }
                    NagetiveCCSLParser parser = NagetiveCCSLParser.getInstance();
                    Relation assertRelation = new Relation(clocksList.get(i),penaltyList.get(i));
                    String assertStr = "relation_" + penaltyCount + "=="+ i;
                    parser.addRelationAssert(assertRelation,assertStr);
                }
                relationMap.addItem(penaltyList.get(i),clocksList.get(i).get(0).name,clocksList.get(i).get(1).name,penaltyCount,i);

            }
            penaltyCount++;
        }else {
            int nullIndex = -1;
            for (int i= 0 ; i < clocks.size() ; i++){
                if(clocks.get(i).name==null||clocks.get(i).name.equals("")){
                    nullIndex = i;
                    break;
                }
            }
            if(nullIndex == -1){
                funcStr += getSubFuncName(type,clocks);
            }else {

                funcStr += tabStr + "int penalty_" + penaltyCount + "= 0;" + "\n";
                funcStr += tabStr + "int relation_" + penaltyCount + "= ??;" + "\n";
                ArrayList<String> funcList = new ArrayList<>();
                ArrayList<String> leftList = new ArrayList<>();
                ArrayList<String> rightList = new ArrayList<>();
                ArrayList<String> penaltyList = new ArrayList<>();

                ArrayList<Variable> inputClocks = new ArrayList<>();
                ArrayList<Variable> clockNames = allVariables;
                for (int i = 0 ; i < clockNames.size() ; i++ ){
                    Boolean isExist = false;
                    for (int j = 0 ; j < clocks.size();j++){
                        if(j == nullIndex){
                            continue;
                        }
                        if (clockNames.get(i).name.equals(clocks.get(j).name)){
                            isExist = true;
                            break;
                        }
                    }
                    if (!isExist){
                        inputClocks.add(clockNames.get(i));
                    }


                }
//                funcStr += "{|";
                for (int i = 0 ; i < inputClocks.size() ; i++ ){
                    Variable currentClock = inputClocks.get(i);
                    clocks.remove(nullIndex);
                    clocks.add(nullIndex,currentClock);
                    if(checkRelations(type,clocks,index)){
//                        funcStr += getSubFuncName(type,clocks) + "|";
                        funcList.add(getSubFuncName(type,clocks));
                        leftList.add(clocks.get(0).name);
                        rightList.add(clocks.get(1).name);

                        NagetiveCCSLParser parser = NagetiveCCSLParser.getInstance();
                        Relation assertRelation = new Relation(clocks,type);
                        String assertStr = "relation_" + penaltyCount + "=="+ (funcList.size()-1);
                        parser.addRelationAssert(assertRelation,assertStr);

                        String penalty = "";
                        switch (type){
                            case 0:{
                                penalty = "0";
                            }break;
                            case 1:{
                                if (nullIndex == 0){
                                    penalty = "penalty_"+ currentClock.name;

                                    if (currentClock.type == 1){
                                        ArrayList<Expression> expressions = ExpressionParser.getInstance().expressionList;
                                        int t = 0;
                                        for (int j = 0 ; j < expressions.size();j++){
                                            if (expressions.get(j).name.equals(currentClock.name)){
                                                t = expressions.get(j).type;
                                                break;
                                            }
                                        }
                                        if (t == -1){
                                            penalty += "+" + currentClock.name + "_func_penalty";
                                        }
                                    }
                                }else {
                                    penalty = allVariables.size() + " - penalty_"+ currentClock.name;
                                    if (currentClock.type == 1){
                                        ArrayList<Expression> expressions = ExpressionParser.getInstance().expressionList;
                                        int t = 0;
                                        for (int j = 0 ; j < expressions.size();j++){
                                            if (expressions.get(j).name.equals(currentClock.name)){
                                                t = expressions.get(j).type;
                                                break;
                                            }
                                        }
                                        if (t == -1){
                                            penalty += "+ 3 - " + currentClock.name + "_func_penalty";
                                        }

                                    }
                                }
                            }break;
                            case 2:{
                                if (nullIndex == 0){
                                    penalty = "penalty_"+ currentClock.name;
                                    if (currentClock.type == 1){
                                        ArrayList<Expression> expressions = ExpressionParser.getInstance().expressionList;
                                        int t = 0;
                                        for (int j = 0 ; j < expressions.size();j++){
                                            if (expressions.get(j).name.equals(currentClock.name)){
                                                t = expressions.get(j).type;
                                                break;
                                            }
                                        }
                                        if (t == -1){
                                            penalty += "+" + currentClock.name + "_func_penalty";
                                        }

                                    }
                                }else {
                                    penalty = allVariables.size() + " - penalty_"+ currentClock.name;
                                    if (currentClock.type == 1){
                                        ArrayList<Expression> expressions = ExpressionParser.getInstance().expressionList;
                                        int t = 0;
                                        for (int j = 0 ; j < expressions.size();j++){
                                            if (expressions.get(j).name.equals(currentClock.name)){
                                                t = expressions.get(j).type;
                                                break;
                                            }
                                        }
                                        if (t == -1){
                                            penalty += "+ 3 - " + currentClock.name + "_func_penalty";
                                        }
                                    }
                                }
                            }break;
                            case 3:{
                                penalty = allVariables.size() + " - penalty_"+ currentClock.name;
                                if (currentClock.type == 1){
                                    ArrayList<Expression> expressions = ExpressionParser.getInstance().expressionList;
                                    int t = 0;
                                    for (int j = 0 ; j < expressions.size();j++){
                                        if (expressions.get(j).name.equals(currentClock.name)){
                                            t = expressions.get(j).type;
                                            break;
                                        }
                                    }
                                    if (t == -1){
                                        penalty += "+ 3 - " + currentClock.name + "_func_penalty";
                                    }
                                }
                            }break;
                            case 4:{
                                if (nullIndex == 1){
                                    penalty = "penalty_"+ currentClock.name;
                                    if (currentClock.type == 1){
                                        ArrayList<Expression> expressions = ExpressionParser.getInstance().expressionList;
                                        int t = 0;
                                        for (int j = 0 ; j < expressions.size();j++){
                                            if (expressions.get(j).name.equals(currentClock.name)){
                                                t = expressions.get(j).type;
                                                break;
                                            }
                                        }
                                        if (t == -1){
                                            penalty += "+" + currentClock.name + "_func_penalty";
                                        }
                                    }
                                }else {
                                    penalty = allVariables.size() + " - penalty_"+ currentClock.name;
                                    if (currentClock.type == 1){
                                        ArrayList<Expression> expressions = ExpressionParser.getInstance().expressionList;
                                        int t = 0;
                                        for (int j = 0 ; j < expressions.size();j++){
                                            if (expressions.get(j).name.equals(currentClock.name)){
                                                t = expressions.get(j).type;
                                                break;
                                            }
                                        }
                                        if (t == -1){
                                            penalty += "+ 3 - " + currentClock.name + "_func_penalty";
                                        }
                                    }
                                }
                            }break;
                            case 5:{
                                penalty = allVariables.size() + " - penalty_"+ currentClock.name;
                                if (currentClock.type == 1){
                                    ArrayList<Expression> expressions = ExpressionParser.getInstance().expressionList;
                                    int t = 0;
                                    for (int j = 0 ; j < expressions.size();j++){
                                        if (expressions.get(j).name.equals(currentClock.name)){
                                            t = expressions.get(j).type;
                                            break;
                                        }
                                    }
                                    if (t == -1){
                                        penalty += "+ 3 - " + currentClock.name + "_func_penalty";
                                    }
                                }
                            }break;
                            default:
                        }
                        penaltyList.add(penalty);
                    }

                }

                for(int i = 0 ; i < funcList.size();i++){
                    if (funcList.size() ==1){
                        funcStr += tabStr +"if(" + funcList.get(i) + "==0){" + "\n"
                                + tabStr + "    " + "return 0;" + "\n"
                                + tabStr + "}" + "\n";
                        break;
                    }else {
//                        penaltyIndexMap.put(relationIndex,penaltyCount);
//                        ArrayList<Integer> sameList = checkSamerelation(new CSrelation(old_clocks,type),relationIndex);
//                        String sameStr = "";
//                        for (int j =0;j<sameList.size();j++){
//                            int sameIndex = sameList.get(j);
//                            int penaltyIndex = penaltyIndexMap.get(sameIndex);
//                            sameStr += "&&relation_"+ penaltyIndex + " != " + i ;
//                        }
                        String penaltyStr = relationMap.getPenaltyStr(type,leftList.get(i),rightList.get(i));

                        if(i != 0){

                            funcStr += tabStr + "else if(" +"relation_" + penaltyCount +" == "+ i + " " + penaltyStr + "){"+"\n";
                        }else {

                            funcStr += tabStr + "if(" +"relation_" + penaltyCount +" == "+ i + " " + penaltyStr + "){"+"\n";
                        }

                        funcStr += tabStr +"    " + "penalty_" + penaltyCount + " = " + penaltyList.get(i) + ";"+"\n";
                        funcStr += tabStr + "    " + "if(" + funcList.get(i) + "==0){" + "\n"
                                + tabStr + "        " + "return 0;" + "\n"
                                + tabStr + "    " + "}" + "\n";
                        funcStr += tabStr + "}"+"\n";
                        if(i == funcList.size() - 1){
                            funcStr += tabStr + "else {" + "\n"
                                    + tabStr + "    " + "return 0;" + "\n"
                                    + tabStr + "}" + "\n";
                            funcStr+= tabStr +"assert relation_"+penaltyCount+ ">=0&&relation_"+penaltyCount + "<"+funcList.size()+";"+"\n";
                        }
                    }

                    relationMap.addItem(type,leftList.get(i),rightList.get(i),penaltyCount,i);

                }

                penaltyCount++;
            }
        }
        return funcStr;
    }

    public String getSubFuncName(int type,ArrayList<Variable>clocks){
        String funcStr = "";
        switch (type){
            case 0:
                funcStr = "checkCoincidence(";
                break;
            case 1:
                funcStr = "checkStrictPrecedence(";
                break;
            case 2:
                funcStr = "checkNonStrictPrecedence(";
                break;
            case 3:
                funcStr = "checkExclusion(";
                break;
            case 4:
                funcStr = "checkSubClock(";
                break;
            case 5:
                funcStr = "checkAlternatesWith(";
                break;
            case 10:
                funcStr = "checkAlternatesWithAndStrictPrecedence(";
                break;
            case 11:
                funcStr = "checkExclusionAndStrictPrecedence(";
                break;
            case 12:
                funcStr = "checkSubClockAndStrictPrecedence(";
                break;
            case 13:
                funcStr = "checkIrrelevant(";
                break;
            default:
                break;
        }
        String countStr = "";
        String clockStr = "";

        for (int  i = 0 ; i < clocks.size();i++){
            if(i!=0){
                clockStr += ",";
                countStr += ",";
            }

            String titleStr = clocks.get(i).type==0?"clock_":"exp_";
            clockStr += titleStr + clocks.get(i).name;
            countStr += clocks.get(i).name + "_count";
        }
        return funcStr + countStr + "," + clockStr + ")";
    }

    public String encodingExpression(int tab) {
        String tabStr = getTab(tab);
        ExpressionParser parser = ExpressionParser.getInstance();
        ArrayList<ArrayList<Expression>> expressionCombinations = parser.orderExpressionCombinations();
        String traceStr = "";
        for (int i = 0 ; i <parser.expressionList.size();i++){
            Expression expression = parser.expressionList.get(i);
            traceStr += tabStr + "int "+ expression.name + "_count;" +"\n"
                    + tabStr + "int["+maxLength+"] exp_"+ expression.name + ";" +"\n"
                    + tabStr + "int "+expression.name+"_func_penalty = 0;\n";
        }

        traceStr += tabStr +"int exp_choose = ??;" +"\n";

        if(expressionCombinations.size() == 1){
            traceStr += encodingExpressionComb(expressionCombinations.get(0),0,tab) + "\n";
        }else {
            for (int i = 0;i <expressionCombinations.size();i++){
                if(i == expressionCombinations.size() -1){
                    traceStr += tabStr + "else{"+"\n"
                            + encodingExpressionComb(expressionCombinations.get(i),i,tab+1)
                            + tabStr + "}\n"
                            +tabStr +"assert exp_choose>=0&&exp_choose<"+expressionCombinations.size()+";"+"\n";
                }else if(i == 0) {
                    traceStr += tabStr + "if(exp_choose == "+i+"){"+"\n"
                            + encodingExpressionComb(expressionCombinations.get(i),i,tab+1)
                            + tabStr + "}\n";
                }else {
                    traceStr += tabStr + "else if(exp_choose == "+i+"){"+"\n"
                            + encodingExpressionComb(expressionCombinations.get(i),i,tab+1)
                            + tabStr + "}\n";
                }

                NagetiveCCSLParser nagetiveCCSLParser = NagetiveCCSLParser.getInstance();
                String assertStr = "exp_choose == "+i;
                nagetiveCCSLParser.addExpressionCombAssert(expressionCombinations.get(i),assertStr);


            }
        }

        return traceStr;
    }
    public String encodingExpressionComb(ArrayList<Expression> combinations, int combIndex,int tab){
        String tabStr = getTab(tab);
        String traceStr = "";
        for (int i = 0 ; i < combinations.size();i++){

            Expression expression = combinations.get(i);
            int expressionCount = expression.variables.size();
            String varTitle_0 = expression.variables.get(0).type==0?"clock_":"exp_";
            String varTitle_1 = "";
            if(expressionCount == 2){
                varTitle_1 = expression.variables.get(1).type==0?"clock_":"exp_";
            }


            if (expression.type == -1){
                traceStr += tabStr + "int "+expression.name+"_choice = ??;\n"
                        + tabStr + "if("+expression.name+"_choice == 0){\n"
                        + tabStr + "    " + expression.name + "_count = getUnionCount(" + expression.variables.get(0).name + "_count" + "," + expression.variables.get(1).name + "_count"  + ","+ varTitle_0 + expression.variables.get(0).name + "," + varTitle_1 +expression.variables.get(1).name + ");" + "\n"
                        + tabStr + "    " + "exp_" +expression.name + " = getUnion(" + expression.variables.get(0).name + "_count" + "," + expression.variables.get(1).name + "_count"  + "," + varTitle_0 + expression.variables.get(0).name + "," + varTitle_1 +expression.variables.get(1).name +","+ "exp_"+ expression.name + ");" + "\n"
                        + tabStr + "    " + expression.name+"_func_penalty = 0;\n"
                        + tabStr + "}" + "\n"
                        + tabStr + "else if("+expression.name+"_choice == 1){\n"
                        + tabStr + "    " + expression.name + "_count = getIntersectionCount(" + expression.variables.get(0).name + "_count" + "," + expression.variables.get(1).name + "_count"  + ","+ varTitle_0 + expression.variables.get(0).name + "," + varTitle_1 +expression.variables.get(1).name + ");" + "\n"
                        + tabStr + "    " + "exp_" +expression.name + " = getIntersection(" + expression.variables.get(0).name + "_count" + "," + expression.variables.get(1).name + "_count"  + "," + varTitle_0 + expression.variables.get(0).name + "," + varTitle_1 +expression.variables.get(1).name +","+ "exp_"+ expression.name + ");" + "\n"
                        + tabStr + "    " + expression.name+"_func_penalty = 4;\n"
                        + tabStr + "}" + "\n"
                        + tabStr + "else "+"if("+expression.name+"_choice == 2){\n"

                        + tabStr + "    " + expression.name + "_count = getSupremumCount(" + expression.variables.get(0).name + "_count" + "," + expression.variables.get(1).name + "_count"  + ","+ varTitle_0 + expression.variables.get(0).name + "," + varTitle_1 +expression.variables.get(1).name + ");" + "\n"
                        + tabStr + "    " + "exp_" +expression.name + " = getSupremum(" + expression.variables.get(0).name + "_count" + "," + expression.variables.get(1).name + "_count"  + "," + varTitle_0 + expression.variables.get(0).name + "," + varTitle_1 +expression.variables.get(1).name +","+ "exp_"+ expression.name + ");" + "\n"
                        + tabStr + "    " + expression.name+"_func_penalty = 2;\n"
                        + tabStr + "}" + "\n"
                        + tabStr + "else "+"if("+expression.name+"_choice == 3){\n"
                        + tabStr + "    " + expression.name + "_count = getInfimumCount(" + expression.variables.get(0).name + "_count" + "," + expression.variables.get(1).name + "_count"  + ","+ varTitle_0 + expression.variables.get(0).name + "," + varTitle_1 +expression.variables.get(1).name + ");" + "\n"
                        + tabStr + "    " + "exp_" +expression.name + " = getInfimum(" + expression.variables.get(0).name + "_count" + "," + expression.variables.get(1).name + "_count"  + "," + varTitle_0 + expression.variables.get(0).name + "," + varTitle_1 +expression.variables.get(1).name +","+ "exp_"+ expression.name + ");" + "\n"
                        + tabStr + "    " + expression.name+"_func_penalty = 1;\n"
                        + tabStr + "}" + "\n"
                        + tabStr + "else "+"if("+expression.name+"_choice == 4){\n"
                        + tabStr + "    " + expression.name + "_count = getSampledOnCount(" + expression.variables.get(0).name + "_count" + "," + expression.variables.get(1).name + "_count"  + ","+ varTitle_0 + expression.variables.get(0).name + "," + varTitle_1 +expression.variables.get(1).name + ");" + "\n"
                        + tabStr + "    " + "exp_" +expression.name + " = getSampledOn(" + expression.variables.get(0).name + "_count" + "," + expression.variables.get(1).name + "_count"  + "," + varTitle_0 + expression.variables.get(0).name + "," + varTitle_1 +expression.variables.get(1).name +","+ "exp_"+ expression.name + ");" + "\n"
                        + tabStr + "    " + expression.name+"_func_penalty = 3;\n"
                        + tabStr + "}" + "\n"
                        + tabStr +"assert "+expression.name + "_choice>=0&&"+expression.name + "_choice<5;"+"\n";
                if (combIndex == 0){
                    NagetiveCCSLParser parser = NagetiveCCSLParser.getInstance();
                    for (int j = 0 ; j < 5 ; j++){
                        Expression exp;
                        if (j == 4){
                            exp = new Expression(0,expression.name,expression.variables,7,0);
                        }else {
                            exp = new Expression(0,expression.name,expression.variables,j,0);
                        }
                        String assertStr = expression.name + "_choice == "+j;
                        parser.addExpressionAssert(exp,assertStr);
                    }
                }


            }
            else if (expression.type == 0){

                traceStr += tabStr + expression.name+"_func_penalty = 0;\n"
                        + tabStr  + expression.name + "_count = getUnionCount(" + expression.variables.get(0).name + "_count" + "," + expression.variables.get(1).name + "_count"  + ","+ varTitle_0 + expression.variables.get(0).name + "," + varTitle_1 +expression.variables.get(1).name + ");" + "\n"
                        + tabStr + "exp_" +expression.name + " = getUnion(" + expression.variables.get(0).name + "_count" + "," + expression.variables.get(1).name + "_count"  + "," + varTitle_0 + expression.variables.get(0).name + "," + varTitle_1 +expression.variables.get(1).name +","+ "exp_"+ expression.name + ");" + "\n";
            }
            else if (expression.type == 1) {
                traceStr += tabStr + expression.name + "_func_penalty = 0;\n"
                        + tabStr + expression.name + "_count = getIntersectionCount(" + expression.variables.get(0).name + "_count" + "," + expression.variables.get(1).name + "_count" + "," + varTitle_0 + expression.variables.get(0).name + "," + varTitle_1 + expression.variables.get(1).name + ");" + "\n"
                        + tabStr + "exp_" + expression.name + " = getIntersection(" + expression.variables.get(0).name + "_count" + "," + expression.variables.get(1).name + "_count" + "," + varTitle_0 + expression.variables.get(0).name + "," + varTitle_1 + expression.variables.get(1).name + "," + "exp_" + expression.name + ");" + "\n";
            }
            else if (expression.type == 2){
                traceStr += tabStr + expression.name+"_func_penalty = 0;\n"
                        + tabStr  + expression.name + "_count = getSupremumCount(" + expression.variables.get(0).name + "_count" + "," + expression.variables.get(1).name + "_count"  + ","+ varTitle_0 + expression.variables.get(0).name + "," + varTitle_1 +expression.variables.get(1).name + ");" + "\n"
                        + tabStr + "exp_" +expression.name + " = getSupremum(" + expression.variables.get(0).name + "_count" + "," + expression.variables.get(1).name + "_count"  + "," + varTitle_0 + expression.variables.get(0).name + "," + varTitle_1 +expression.variables.get(1).name +","+ "exp_"+ expression.name + ");" + "\n";
            }
            else if (expression.type == 3){
                traceStr += tabStr + expression.name+"_func_penalty = 0;\n"
                        + tabStr  + expression.name + "_count = getInfimumCount(" + expression.variables.get(0).name + "_count" + "," + expression.variables.get(1).name + "_count"  + ","+ varTitle_0 + expression.variables.get(0).name + "," + varTitle_1 +expression.variables.get(1).name + ");" + "\n"
                        + tabStr + "exp_" +expression.name + " = getInfimum(" + expression.variables.get(0).name + "_count" + "," + expression.variables.get(1).name + "_count"  + "," + varTitle_0 + expression.variables.get(0).name + "," + varTitle_1 +expression.variables.get(1).name +","+ "exp_"+ expression.name + ");" + "\n";
            }
            else if (expression.type == 4){
                String additionStr = expression.addition>0?""+expression.addition:"??";

                traceStr += tabStr + "int " + expression.name+"_addition = " + additionStr + ";\n";
                traceStr += tabStr + "assert " + expression.name+"_addition > 0 &&" + expression.name+"_addition < "+maxLength + ";";
                traceStr += tabStr  + expression.name + "_count = getDelayCount(" + expression.variables.get(0).name + "_count" + ","+ varTitle_0 + expression.variables.get(0).name + "," + expression.name + "_addition" + ");" + "\n"
                        + tabStr + "exp_" +expression.name + " = getDelay(" + expression.variables.get(0).name + "_count" + "," + varTitle_0 + expression.variables.get(0).name + "," + expression.name + "_addition"  +","+ "exp_"+ expression.name + ");" + "\n";
            }
            else if (expression.type == 5){
                String additionStr = expression.addition>0?""+expression.addition:"??";

                traceStr += tabStr + "int " + expression.name+"_addition = " + additionStr + ";\n";
                traceStr += tabStr + "assert " + expression.name+"_addition > 0 &&" + expression.name+"_addition < "+maxLength + ";";
                traceStr += tabStr  + expression.name + "_count = getPeriodicCount(" + expression.variables.get(0).name + "_count" + ","+ varTitle_0 + expression.variables.get(0).name + "," + expression.name + "_addition" + ");" + "\n"
                        + tabStr + "exp_" +expression.name + " = getPeriodic(" + expression.variables.get(0).name + "_count" + ","  + varTitle_0 + expression.variables.get(0).name + "," + expression.name + "_addition"  +","+ "exp_"+ expression.name + ");" + "\n";
            }
            else if (expression.type == 6){
                String additionStr = expression.addition>0?""+expression.addition:"??";

                traceStr += tabStr + "int " + expression.name+"_addition = " + additionStr + ";\n";
                traceStr += tabStr + "assert " + expression.name+"_addition > 0 &&" + expression.name+"_addition < "+maxLength + ";";
                traceStr += tabStr  + expression.name + "_count = getDelayForCount(" + expression.variables.get(0).name + "_count" + "," + expression.variables.get(1).name + "_count"  + ","+ varTitle_0 + expression.variables.get(0).name + "," + varTitle_1 +expression.variables.get(1).name + "," + expression.name + "_addition" + ");" + "\n"
                        + tabStr + "exp_" +expression.name + " = getDelayFor(" + expression.variables.get(0).name + "_count" + "," + expression.variables.get(1).name + "_count"  + ","+ varTitle_0 + expression.variables.get(0).name + "," + varTitle_1 +expression.variables.get(1).name + "," + expression.name + "_addition"  +","+ "exp_"+ expression.name + ");" + "\n";
            }else if (expression.type == 7){
                traceStr += tabStr + expression.name+"_func_penalty = 0;\n"
                        + tabStr  + expression.name + "_count = getSampledOnCount(" + expression.variables.get(0).name + "_count" + "," + expression.variables.get(1).name + "_count"  + ","+ varTitle_0 + expression.variables.get(0).name + "," + varTitle_1 +expression.variables.get(1).name + ");" + "\n"
                        + tabStr + "exp_" +expression.name + " = getSampledOn(" + expression.variables.get(0).name + "_count" + "," + expression.variables.get(1).name + "_count"  + "," + varTitle_0 + expression.variables.get(0).name + "," + varTitle_1 +expression.variables.get(1).name +","+ "exp_"+ expression.name + ");" + "\n";
            }
        }
        return traceStr;

    }

    public String encodingPenaltyStr(int tab){
        String tabStr = getTab(tab);
        String penaltyStr = "";
        for (int i = 0 ;i < penaltyCount; i++){
            penaltyStr += tabStr + "minimize(penalty_"+i+")" +";"+"\n";
        }
        return penaltyStr;
    }

    public String encodingClockPenaltyStr(int tab){
        String tabStr = getTab(tab);
        String penaltyStr = "";
        String penaltyCountStr = "";
        for (int i= 0 ; i< allVariables.size() ; i ++){
            penaltyCountStr +=  tabStr + "int "+allVariables.get(i).name+"_penalty_count = 1;"  + "\n"
                    +tabStr + "if("+allVariables.get(i).name +"_count == 0){"+"\n"
                    +tabStr + "    " + allVariables.get(i).name+"_penalty_count = 1;" + "\n"
                    +tabStr + "}else{"+"\n"
                    +tabStr + "    " + allVariables.get(i).name+"_penalty_count = " + allVariables.get(i).name+"_count;"+"\n"
                    +tabStr + "}"+"\n";

            String varHeader = allVariables.get(i).type==1?"exp_":"clock_";
            penaltyStr += tabStr + "int penalty_" + allVariables.get(i).name + " = "+callFuncGetPenalty(allVariables.get(i).name+"_count , "+varHeader+allVariables.get(i).name);
        }

        return penaltyCountStr + penaltyStr;
    }

    public String callFuncGetPenalty(String input){
        String funcStr = "getPenalty( " + input;
        for (int i = 0 ; i < allVariables.size();i++){
            String funcHeader = allVariables.get(i).type==1?"exp_":"clock_";
            funcStr+= "," + allVariables.get(i).name+ "_count " + ", " + funcHeader+ allVariables.get(i).name ;
        }
        funcStr += ");\n";
        return funcStr;
    }

    public boolean checkRelations(int type,ArrayList<Variable>clocks,int index){
        ArrayList<Relation> relations = RelationParser.getInstance().relations;

        Variable leftVariable = clocks.get(0);
        Variable rightVariable = clocks.get(1);
        if (leftVariable.type == 1){
            Expression expression = ExpressionParser.getInstance().getExpression(leftVariable.name);
            ArrayList<Variable> variables = expression.variables;
            for (int j = 0 ; j < variables.size();j++){
                if (variables.get(j).name.equals(rightVariable.name)){
                    return false;
                }
            }
        }
        if (rightVariable.type == 1){
            Expression expression = ExpressionParser.getInstance().getExpression(rightVariable.name);
            ArrayList<Variable> variables = expression.variables;
            for (int j = 0 ; j < variables.size();j++){
                if (variables.get(j).name.equals(leftVariable.name)){
                    return false;
                }
            }
        }


        for (int  i = 0; i < relations.size(); i ++){
            Relation relation = relations.get(i);
            if (i == index){
                continue;
            }
            if(relation.type == type){
                if( type!= 0 ){
                    Boolean isEqual = true;
                    for (int j = 0 ; j < clocks.size(); j++){
                        if(!clocks.get(j).name.equals(relation.variables.get(j).name)){
                            isEqual = false;
                            break;
                        }
                    }
                    if(isEqual){
                        return false;
                    }

                }
                if (type == 4){
                    ArrayList<Expression> expressions = ExpressionParser.getInstance().expressionList;
                    for (int j = 0;j < expressions.size();j++){
                        Expression expression = expressions.get(j);
                        if (expression.type == 0){
                            String expName = expression.name;
                            String leftName = expression.variables.get(0).name;
                            String rightName = expression.variables.get(1).name;
                            if (expName.equals(clocks.get(1).name)&&(leftName.equals(clocks.get(0).name)||rightName.equals(clocks.get(0).name))){
                                return false;
                            }
                        }else if(expression.type == 1){
                            String expName = expression.name;
                            String leftName = expression.variables.get(0).name;
                            String rightName = expression.variables.get(1).name;
                            if (expName.equals(clocks.get(0).name)&&(leftName.equals(clocks.get(1).name)||rightName.equals(clocks.get(1).name))){
                                return false;
                            }
                        }
                    }
                }else if (type == 2){
                    ArrayList<Expression> expressions = ExpressionParser.getInstance().expressionList;
                    for (int j = 0;j < expressions.size();j++){
                        Expression expression = expressions.get(j);
                        if (expression.type == 1){
                            String expName = expression.name;
                            String leftName = expression.variables.get(0).name;
                            String rightName = expression.variables.get(1).name;
                            if (expName.equals(clocks.get(1).name)&&(leftName.equals(clocks.get(0).name)||rightName.equals(clocks.get(0).name))){
                                return false;
                            }
                        }else if(expression.type == 0){
                            String expName = expression.name;
                            String leftName = expression.variables.get(0).name;
                            String rightName = expression.variables.get(1).name;
                            if (expName.equals(clocks.get(0).name)&&(leftName.equals(clocks.get(1).name)||rightName.equals(clocks.get(1).name))){
                                return false;
                            }
                        }
                    }
                }

            }else {
                if(clocks.get(0).name.equals(relation.variables.get(1).name)&&clocks.get(1).name.equals(relation.variables.get(0).name)){
                    return false;
                }
            }
        }
        return true;
    }

    public String encodingNagetiveCCSL(int tab){
        ArrayList<String>asserts = NagetiveCCSLParser.getInstance().getNagetiveCCSLAssert();
        String navetiveCCSLStr = "";
        for (int i = 0 ; i < asserts.size();i++){
            String assertStr = asserts.get(i);
            if (!assertStr.equals("")){
                navetiveCCSLStr += getTab(tab) + "assert ("+assertStr +") == 0;" + "\n";
            }
        }
        return navetiveCCSLStr;
    }

    public String getTab(int count){
        String tab = "";
        for (int i = 0 ; i < count;i++){
            tab += "    ";
        }
        return tab;
    }
}
