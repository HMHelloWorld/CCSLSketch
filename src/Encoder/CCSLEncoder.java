package Encoder;

import CCSLModel.Expression;
import CCSLModel.Variable;
import Parser.ClockParser;
import Parser.ExpressionParser;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by huming on 2019/6/24.
 */
public class CCSLEncoder {
    private int maxLength;
    private ArrayList<Variable> allVariables;


    public CCSLEncoder(int maxLength){
        allVariables = new ArrayList<>();
        this.maxLength = maxLength;
        refreshVar();
    }
    public void refreshVar(){
        ArrayList<Expression> expressions = ExpressionParser.getInstance().expressionList;
        ArrayList<String> clocks = ClockParser.getInstance().clockList;
        for (int i = 0 ; i < expressions.size() ; i++){
            allVariables.add(new Variable(expressions.get(i).name,1));
        }
        for (int i = 0 ; i < clocks.size() ; i++){
            allVariables.add(new Variable(clocks.get(i),0));
        }

    }
    public String generateSKFileString(Boolean isStartExtend){


        String skStr = "";
        HarnessEncoder harnessEncoder = new HarnessEncoder(maxLength);
        CheckFuncEncoder checkFuncEncoder = new CheckFuncEncoder(maxLength,allVariables,isStartExtend);
        CCSLFunctionEncoder ccslFunctionEncoder = new CCSLFunctionEncoder(maxLength);
        skStr += harnessEncoder.encodingHarness(0) + "\n"
                + checkFuncEncoder.encodingCheckFunc(0) + "\n"
                + ccslFunctionEncoder.ccslFunctionStr(0) + "\n"
                + encodingGetPenaltyFunc(0);
        return skStr;
    }
    public String encodingGetPenaltyFunc(int tab){
        String tabStr = getTab(tab);
        String funcStr = tabStr + "int getPenalty( int current_count , int["+maxLength+"] current_max";
        String funcBodyStr = "";
        for (int i = 0 ; i < allVariables.size();i++){
            funcStr+= "," + " int count"+i+" " + "," + " int["+maxLength+"] count"+i+"_max ";
            funcBodyStr += tabStr + "    if(count"+i+" < current_count){\n"
                    + tabStr + "         " + "penalty = penalty+1;"+"\n"
                    + tabStr + "    " + "}"+"\n"
                    + tabStr + "    else if(count"+i+" == current_count){\n"
                    + tabStr + "        int isAdd = 0;\n"
                    + tabStr + "        for(int i=current_count-1;i >= 0;i--){\n"
                    + tabStr + "            if(count"+i+"_max[i] > current_max[i] && isAdd == 0){\n"
                    + tabStr + "                 " + "penalty = penalty+1;"+"\n"
                    + tabStr + "                 " + "isAdd = 1;"+"\n"
                    + tabStr + "             }"+"\n"
                    + tabStr + "        }"+"\n"
                    + tabStr + "    }"+"\n";
        }
        funcStr += "){" + "\n";

        funcStr +=tabStr +  "    int penalty = 0;" + "\n";
        funcStr += funcBodyStr;
        funcStr +=tabStr +  "    return penalty;" + "\n";
        funcStr +=tabStr +  "}" + "\n";
        return funcStr;
    }
    public String getTab(int count){
        String tab = "";
        for (int i = 0 ; i < count;i++){
            tab += "    ";
        }
        return tab;
    }
    public boolean generateSKETCHFile(String filepath, String filename,Boolean isStartExtend){
        String fileStr = "";
        if(filepath == null || filepath.equals("")){
            fileStr = filename;
        }else {
            fileStr = filepath+filename+".sk";
        }
        File file=new File(fileStr);
        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        try {
            FileWriter fileWriter =new FileWriter(file);
            fileWriter.write(generateSKFileString(isStartExtend));
            fileWriter.flush();
            fileWriter.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }


    }
}
