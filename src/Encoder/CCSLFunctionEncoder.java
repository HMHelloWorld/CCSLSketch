package Encoder;

/**
 * Created by huming on 2019/6/24.
 */
public class CCSLFunctionEncoder {
    private int maxLength;
    public CCSLFunctionEncoder(int maxLength){
        this.maxLength = maxLength;
    }
    public String ccslFunctionStr(int tab){
        String funcStr = "";
        funcStr += encodingCoincidenceCheckFunc(tab) + "\n"
                +encodingStrictPrecedenceCheckFunc(tab) + "\n"
                +encodingNonStrictPrecedenceCheckFunc(tab) + "\n"
                +encodingExclusionCheckFunc(tab) + "\n"
                +encodingSubClockCheckFunc(tab) + "\n"
                +encodingAlternatesWithCheckFunc(tab) + "\n"
                +encodingAlternatesWithAndStrictPrecedenceCheckFunc(tab) + "\n"
                +encodingSubClockAndStrictPrecedenceCheckFunc(tab) + "\n"
                +encodingExclusionAndStrictPrecedenceCheckFunc(tab) + "\n"
                +encodingIrrelevantCheckFunc(tab) + "\n"
                +encodingUnionFunc(tab) + "\n"
                +encodingUnionCountFunc(tab) + "\n"
                +encodingIntersectionFunc(tab) + "\n"
                +encodingIntersectionCountFunc(tab) + "\n"
                +encodingSupremumFunc(tab) + "\n"
                +encodingSupremumCountFunc(tab)+ "\n"
                +encodingInfimumFunc(tab) + "\n"
                +encodingInfimumCountFunc(tab)+ "\n"
                +encodingDelayFunc(tab) + "\n"
                +encodingDelayCountFunc(tab)+ "\n"
                +encodingPeriodicFunc(tab) + "\n"
                +encodingPeriodicCountFunc(tab) + "\n"
                +encodingDelayForFunc(tab) + "\n"
                +encodingDelayForCountFunc(tab) + "\n"
                +encodingSampledOnFunc(tab) + "\n"
                +encodingSampledOnCountFunc(tab);
        return funcStr;
    }

    public String encodingCoincidenceCheckFunc(int tab){
        String tabStr= getTab(tab);
        String funcStr = tabStr + "int checkCoincidence(" + "int left_count" + "," + "int right_count" + "," + "int["+maxLength+"] left_clock" + "," + "int["+maxLength+"] right_clock" + "){" + "\n";

        funcStr +=  tabStr + "    " + "if(left_count!= right_count){\n" +
                tabStr + "        " + "return 0;\n" +
                tabStr + "    " + "}\n" +
                tabStr + "    " + "for (int  i = 0 ; i < left_count; i ++){\n" +
                tabStr + "        " + "if(left_clock[i]!= right_clock[i]){\n" +
                tabStr + "            " + "return 0;\n" +
                tabStr + "        " + "}\n" +
                tabStr + "    " + "}\n" +
                tabStr + "    " + "return 1;";

        funcStr += "\n" + tabStr + "}";
        return funcStr;
    }

    public String encodingStrictPrecedenceCheckFunc(int tab){
        String tabStr= getTab(tab);
        String funcStr = tabStr + "int checkStrictPrecedence(" + "int left_count" + "," + "int right_count" + "," + "int["+maxLength+"] left_clock" + "," + "int["+maxLength+"] right_clock" + "){" + "\n";

        funcStr +=  tabStr + "    " + "if(left_count < right_count){\n" +
                tabStr + "        " + "return 0;\n" +
                tabStr + "    " + "}\n" +
                tabStr + "    " + "for (int  i = 0 ; i < right_count; i ++){\n" +
                tabStr + "        " + "if(left_clock[i]>= right_clock[i]){\n" +
                tabStr + "            " + "return 0;\n" +
                tabStr + "        " + "}\n" +
                tabStr + "    " + "}\n" +
                tabStr + "    " + "return 1;";

        funcStr += "\n" + tabStr + "}";
        return funcStr;
    }

    public String encodingNonStrictPrecedenceCheckFunc(int tab){
        String tabStr= getTab(tab);
        String funcStr = tabStr + "int checkNonStrictPrecedence(" + "int left_count" + "," + "int right_count" + "," + "int["+maxLength+"] left_clock" + "," + "int["+maxLength+"] right_clock" + "){" + "\n";

        funcStr +=  tabStr + "    " + "if(left_count < right_count){\n" +
                tabStr + "        " + "return 0;\n" +
                tabStr + "    " + "}\n" +
                tabStr + "    " + "for (int  i = 0 ; i < right_count; i ++){\n" +
                tabStr + "        " + "if(left_clock[i] > right_clock[i]){\n" +
                tabStr + "            " + "return 0;\n" +
                tabStr + "        " + "}\n" +
                tabStr + "    " + "}\n" +
                tabStr + "    " + "return 1;";

        funcStr += "\n" + tabStr + "}";
        return funcStr;
    }

    public String encodingSubClockCheckFunc(int tab){
        String tabStr= getTab(tab);
        String funcStr = tabStr + "int checkSubClock(" + "int left_count" + "," + "int right_count" + "," + "int["+maxLength+"] left_clock" + "," + "int["+maxLength+"] right_clock" + "){" + "\n";

        funcStr +=  tabStr + "    " + "if(left_count >= right_count){\n" +
                tabStr + "        " + "return 0;\n" +
                tabStr + "    " + "}\n" +
                tabStr + "    " + "int index = 0;\n" +
                tabStr + "    " + "for (int  i = 0 ; i < left_count; i ++){\n" +
                tabStr + "        " + "for (int  j = index ; j < right_count; j ++){\n" +
                tabStr + "            " + "if(left_clock[i] > right_clock[j]){\n" +
                tabStr + "                " + "index ++;\n" +
                tabStr + "            " + "}else if(left_clock[i] < right_clock[j]){\n" +
                tabStr + "                " + "return 0;\n" +
                tabStr + "            " + "}else {\n" +
                tabStr + "                " + "index++;\n" +
                tabStr + "                " + "j = right_count;\n" +
                tabStr + "            " + "}\n" +
                tabStr + "        " + "}\n" +
                tabStr + "    " + "}\n" +
                tabStr + "    " + "return 1;";

        funcStr += "\n" + tabStr + "}";
        return funcStr;
    }

    public String encodingAlternatesWithCheckFunc(int tab){
        String tabStr= getTab(tab);
        String funcStr = tabStr + "int checkAlternatesWith(" + "int left_count" + "," + "int right_count" + "," + "int["+maxLength+"] left_clock" + "," + "int["+maxLength+"] right_clock" + "){" + "\n";

        funcStr +=  tabStr + "    " + "int range = left_count - right_count;\n" +
                tabStr + "    if(range > 1 || range < -1){\n" +
                tabStr + "        return 0;\n" +
                tabStr + "    }\n" +
                tabStr + "    if(left_clock[0] < right_clock[0]){\n" +
                tabStr + "        for (int i = 0 ; i < right_count;i++){\n" +
                tabStr + "            if(left_count == i+1){\n" +
                tabStr + "                if(!(left_clock[i] < right_clock[i])){\n" +
                tabStr + "                    return 0;\n" +
                tabStr + "                }\n" +
                tabStr + "            }else if(!(left_clock[i] < right_clock[i] && right_clock[i] < left_clock[i+1])){\n" +
                tabStr + "                return 0;\n" +
                tabStr + "            }\n" +
                tabStr + "        }\n" +
                tabStr + "    }else{\n" +
                tabStr + "        for (int i = 0 ; i < left_count;i++){\n" +
                tabStr + "            if(right_count == i+1){\n" +
                tabStr + "                if(!(right_clock[i] < left_clock[i])){\n" +
                tabStr + "                    return 0;\n" +
                tabStr + "                }\n" +
                tabStr + "            }else if(!(right_clock[i] < left_clock[i] && left_clock[i] < right_clock[i+1])){\n" +
                tabStr + "                return 0;\n" +
                tabStr + "            }\n" +
                tabStr + "        }\n" +
                tabStr + "    }\n" +
                tabStr + "    return 1;";

        funcStr += "\n" + tabStr + "}";
        return funcStr;
    }

    public String encodingExclusionCheckFunc(int tab){
        String tabStr= getTab(tab);
        String funcStr = tabStr + "int checkExclusion(" + "int left_count" + "," + "int right_count" + "," + "int["+maxLength+"] left_clock" + "," + "int["+maxLength +"] right_clock" + "){" + "\n";

        funcStr +=  tabStr + "    " + "int index = 0;\n" +
                tabStr + "    " + "for (int  i = 0 ; i < right_count; i ++){\n" +
                tabStr + "        " + "for (int  j = index ; j < left_count; j ++){\n" +
                tabStr + "            " + "if(left_clock[i] == right_clock[j]){\n" +
                tabStr + "                " + "return 0;\n" +
                tabStr + "            " + "}else if(left_clock[i] > right_clock[j]){\n" +
                tabStr + "                " + "index ++;\n" +
                tabStr + "            " + "}else {\n" +
                tabStr + "                " + "j = left_count;\n" +
                tabStr + "            " + "}\n" +
                tabStr + "        " + "}\n" +
                tabStr + "    " + "}\n" +
                tabStr + "    " + "return 1;";

        funcStr += "\n" + tabStr + "}";
        return funcStr;
    }

    public String encodingAlternatesWithAndStrictPrecedenceCheckFunc(int tab){
        String tabStr= getTab(tab);
        String funcStr = tabStr + "int checkAlternatesWithAndStrictPrecedence(" + "int left_count" + "," + "int right_count" + "," + "int["+maxLength+"] left_clock" + "," + "int["+maxLength +"] right_clock" + "){" + "\n";

        funcStr +=  tabStr + "    " + "int rel1 = checkStrictPrecedence(left_count,right_count,left_clock,right_clock);\n" +
                tabStr + "    " + "int rel2 = checkAlternatesWith(left_count,right_count,left_clock,right_clock);\n" +
                tabStr + "    " + "if(rel1==1&&rel2==1){\n" +
                tabStr + "        " + "return 1;\n" +
                tabStr + "    " + "}\n" +
                tabStr + "    " + "return 0;";

        funcStr += "\n" + tabStr + "}";
        return funcStr;
    }

    public String encodingExclusionAndStrictPrecedenceCheckFunc(int tab){
        String tabStr= getTab(tab);
        String funcStr = tabStr + "int checkExclusionAndStrictPrecedence(" + "int left_count" + "," + "int right_count" + "," + "int["+maxLength+"] left_clock" + "," + "int["+maxLength +"] right_clock" + "){" + "\n";

        funcStr +=  tabStr + "    " + "int rel1 = checkStrictPrecedence(left_count,right_count,left_clock,right_clock);\n" +
                tabStr + "    " + "int rel2 = checkExclusion(left_count,right_count,left_clock,right_clock);\n" +
                tabStr + "    " + "if(rel1==1&&rel2==1){\n" +
                tabStr + "        " + "return 1;\n" +
                tabStr + "    " + "}\n" +
                tabStr + "    " + "return 0;";

        funcStr += "\n" + tabStr + "}";
        return funcStr;
    }

    public String encodingSubClockAndStrictPrecedenceCheckFunc(int tab){
        String tabStr= getTab(tab);
        String funcStr = tabStr + "int checkSubClockAndStrictPrecedence(" + "int left_count" + "," + "int right_count" + "," + "int["+maxLength+"] left_clock" + "," + "int["+maxLength +"] right_clock" + "){" + "\n";

        funcStr +=  tabStr + "    " + "int rel1 = checkStrictPrecedence(right_count,left_count,right_clock,left_clock);\n" +
                tabStr + "    " + "int rel2 = checkSubClock(left_count,right_count,left_clock,right_clock);\n" +
                tabStr + "    " + "if(rel1==1&&rel2==1){\n" +
                tabStr + "        " + "return 1;\n" +
                tabStr + "    " + "}\n" +
                tabStr + "    " + "return 0;";

        funcStr += "\n" + tabStr + "}";
        return funcStr;
    }

    public String encodingIrrelevantCheckFunc(int tab){
        String tabStr= getTab(tab);
        String funcStr = tabStr + "int checkIrrelevant(" + "int left_count" + "," + "int right_count" + "," + "int["+maxLength+"] left_clock" + "," + "int["+maxLength +"] right_clock" + "){" + "\n";

        funcStr +=  tabStr + "    " + "return 1;";

        funcStr += "\n" + tabStr + "}";
        return funcStr;
    }

    public String encodingIntersectionFunc(int tab){
        String tabStr= getTab(tab);
        String funcStr = tabStr + "int["+maxLength+"] getIntersection(" + "int left_count" + "," + "int right_count" + "," + "int["+maxLength+"] left_clock" + "," + "int["+maxLength+"] right_clock" + "," + "int["+maxLength+"] exp_clock" + "){" + "\n";

        funcStr +=  tabStr + "    " + "int index = 0;\n" +
                tabStr + "    " + "int left_index = 0;\n" +
                tabStr + "    " + "int right_index = 0;\n" +
                tabStr + "    " + "while (left_index < left_count && right_index < right_count ){\n" +
                tabStr + "        " + "if(left_clock[left_index]==right_clock[right_index]){\n" +
                tabStr + "            " + "exp_clock[index] = left_clock[left_index];\n" +
                tabStr + "            " + "left_index++;\n" +
                tabStr + "            " + "right_index++;\n" +
                tabStr + "            " + "index++;\n" +
                tabStr + "        " + "}else if(left_clock[left_index] > right_clock[right_index]){\n" +
                tabStr + "            " + "right_index ++;\n" +
                tabStr + "        " + "}else {\n" +
                tabStr + "            " + "left_index ++;\n" +
                tabStr + "        " + "}\n" +
                tabStr + "    " + "}\n" +
                tabStr + "    " + "return exp_clock[0::"+maxLength+"];";

        funcStr += "\n" + tabStr + "}";
        return funcStr;
    }

    public String encodingIntersectionCountFunc(int tab){
        String tabStr= getTab(tab);
        String funcStr = tabStr + "int getIntersectionCount(" + "int left_count" + "," + "int right_count" + "," + "int["+maxLength+"] left_clock" + "," + "int["+maxLength+"] right_clock" + "){" + "\n";

        funcStr +=  tabStr + "    " + "int index = 0;\n" +
                tabStr + "    " + "int left_index = 0;\n" +
                tabStr + "    " + "int right_index = 0;\n" +
                tabStr + "    " + "while (left_index < left_count && right_index < right_count ){\n" +
                tabStr + "        " + "if(left_clock[left_index]==right_clock[right_index]){\n" +
                tabStr + "            " + "left_index++;\n" +
                tabStr + "            " + "right_index++;\n" +
                tabStr + "            " + "index++;\n" +
                tabStr + "        " + "}else if(left_clock[left_index] > right_clock[right_index]){\n" +
                tabStr + "            " + "right_index ++;\n" +
                tabStr + "        " + "}else {\n" +
                tabStr + "            " + "left_index ++;\n" +
                tabStr + "        " + "}\n" +
                tabStr + "    " + "}\n" +
                tabStr + "    " + "return index;";

        funcStr += "\n" + tabStr + "}";
        return funcStr;
    }

    public String encodingUnionFunc(int tab){
        String tabStr= getTab(tab);
        String funcStr = tabStr + "int["+maxLength+"] getUnion(" + "int left_count" + "," + "int right_count" + "," + "int["+maxLength+"] left_clock" + "," + "int["+maxLength+"] right_clock" + "," + "int["+maxLength+"] exp_clock" + "){" + "\n";

        funcStr +=  tabStr + "    " + "int index = 0;\n" +
                tabStr + "    " + "int left_index = 0;\n" +
                tabStr + "    " + "int right_index = 0;\n" +
                tabStr + "    " + "while (left_index < left_count || right_index < right_count ){\n" +
                tabStr + "        " + "if (left_index >= left_count){\n" +
                tabStr + "            " + "exp_clock[index] = right_clock[right_index];\n" +
                tabStr + "            " + "right_index++;\n" +
                tabStr + "            " + "index++;\n" +
                tabStr + "        " + "}else if (right_index >= right_count){\n" +
                tabStr + "            " + "exp_clock[index] = left_clock[left_index];\n" +
                tabStr + "            " + "left_index++;\n" +
                tabStr + "            " + "index++;\n" +
                tabStr + "        " + "}else if(left_clock[left_index]==right_clock[right_index]){\n" +
                tabStr + "            " + "exp_clock[index] = left_clock[left_index];\n" +
                tabStr + "            " + "left_index++;\n" +
                tabStr + "            " + "right_index++;\n" +
                tabStr + "            " + "index++;\n" +
                tabStr + "        " + "}else if(left_clock[left_index] > right_clock[right_index]){\n" +
                tabStr + "            " + "exp_clock[index] = right_clock[right_index];\n" +
                tabStr + "            " + "right_index ++;\n" +
                tabStr + "            " + "index++;\n" +
                tabStr + "        " + "}else {\n" +
                tabStr + "            " + "exp_clock[index] = left_clock[left_index];\n" +
                tabStr + "            " + "left_index ++;\n" +
                tabStr + "            " + "index++;\n" +
                tabStr + "        " + "}\n" +
                tabStr + "    " + "}\n" +
                tabStr + "    " + "return exp_clock[0::"+maxLength+"];";

        funcStr += "\n" + tabStr + "}";
        return funcStr;
    }

    public String encodingUnionCountFunc(int tab){
        String tabStr= getTab(tab);
        String funcStr = tabStr + "int getUnionCount(" + "int left_count" + "," + "int right_count" + "," + "int["+maxLength+"] left_clock" + "," + "int["+maxLength+"] right_clock" + "){" + "\n";

        funcStr +=  tabStr + "    " + "int index = 0;\n" +
                tabStr + "    " + "int left_index = 0;\n" +
                tabStr + "    " + "int right_index = 0;\n" +
                tabStr + "    " + "while (left_index < left_count || right_index < right_count ){\n" +
                tabStr + "        " + "if (left_index >= left_count){\n" +
                tabStr + "            " + "right_index++;\n" +
                tabStr + "            " + "index++;\n" +
                tabStr + "        " + "}else if (right_index >= right_count){\n" +
                tabStr + "            " + "left_index++;\n" +
                tabStr + "            " + "index++;\n" +
                tabStr + "        " + "}else if(left_clock[left_index]==right_clock[right_index]){\n" +
                tabStr + "            " + "left_index++;\n" +
                tabStr + "            " + "right_index++;\n" +
                tabStr + "            " + "index++;\n" +
                tabStr + "        " + "}else if(left_clock[left_index] > right_clock[right_index]){\n" +
                tabStr + "            " + "right_index ++;\n" +
                tabStr + "            " + "index++;\n" +
                tabStr + "        " + "}else {\n" +
                tabStr + "            " + "left_index ++;\n" +
                tabStr + "            " + "index++;\n" +
                tabStr + "        " + "}\n" +
                tabStr + "    " + "}\n" +
                tabStr + "    " + "return index;";

        funcStr += "\n" + tabStr + "}";
        return funcStr;
    }

    public String encodingSupremumFunc(int tab){
        String tabStr= getTab(tab);
        String funcStr = tabStr + "int["+maxLength+"] getSupremum(" + "int left_count" + "," + "int right_count" + "," + "int["+maxLength+"] left_clock" + "," + "int["+maxLength+"] right_clock" + "," + "int["+maxLength+"] exp_clock" + "){" + "\n";

        funcStr +=  tabStr + "    " + "int count = 0;\n" +
                tabStr + "    " + "if(left_count >= right_count){\n" +
                tabStr + "        " + "count = right_count;\n" +
                tabStr + "    " + "}else{\n" +
                tabStr + "        " + "count = left_count;\n" +
                tabStr + "    " + "}\n"+
                tabStr + "    " + "for (int i = 0 ; i < count ; i++){\n" +
                tabStr + "        " + "if(left_clock[i]>=right_clock[i]){\n" +
                tabStr + "            " + "exp_clock[i] = left_clock[i];\n" +
                tabStr + "        " + "}else {\n" +
                tabStr + "            " + "exp_clock[i] = right_clock[i];\n" +
                tabStr + "        " + "}\n" +
                tabStr + "    " + "}\n" +
                tabStr + "    " + "return exp_clock[0::"+maxLength+"];";

        funcStr += "\n" + tabStr + "}";
        return funcStr;
    }

    public String encodingSupremumCountFunc(int tab){
        String tabStr= getTab(tab);
        String funcStr = tabStr + "int getSupremumCount(" + "int left_count" + "," + "int right_count" + "," + "int["+maxLength+"] left_clock" + "," + "int["+maxLength+"] right_clock" + "){" + "\n";

        funcStr +=  tabStr + "    " + "if(left_count >= right_count){\n" +
                tabStr + "        " + "return right_count;\n" +
                tabStr + "    " + "}else{\n" +
                tabStr + "        " + "return left_count;\n" +
                tabStr + "    " + "}";

        funcStr += "\n" + tabStr + "}";
        return funcStr;
    }


    public String encodingInfimumFunc(int tab){
        String tabStr= getTab(tab);
        String funcStr = tabStr + "int["+maxLength+"] getInfimum(" + "int left_count" + "," + "int right_count" + "," + "int["+maxLength+"] left_clock" + "," + "int["+maxLength+"] right_clock" + "," + "int["+maxLength+"] exp_clock" + "){" + "\n";

        funcStr +=  tabStr + "    " + "int count = 0;\n" +
                tabStr + "    " + "int shot_count = 0;\n" +
                tabStr + "    " + "int choose = 0;\n" +
                tabStr + "    " + "if(left_count <= right_count){\n" +
                tabStr + "        " + "count = right_count;\n" +
                tabStr + "        " + "shot_count = left_count;\n" +
                tabStr + "        " + "choose = 1;\n" +
                tabStr + "    " + "}else{\n" +
                tabStr + "        " + "count = left_count;\n" +
                tabStr + "        " + "shot_count = right_count;\n" +
                tabStr + "    " + "}\n"+
                tabStr + "    " + "for (int i = 0 ; i < count ; i++){\n" +
                tabStr + "        " + "if(shot_count>i){\n" +
                tabStr + "            " + "if(left_clock[i]<=right_clock[i]){\n" +
                tabStr + "                " + "exp_clock[i] = left_clock[i];\n" +
                tabStr + "            " + "}else {\n" +
                tabStr + "                " + "exp_clock[i] = right_clock[i];\n" +
                tabStr + "            " + "}\n" +
                tabStr + "        " + "}\n" +
                tabStr + "        " + "else{\n" +
                tabStr + "            " + "if(choose == 1){\n" +
                tabStr + "                " + "exp_clock[i] = right_clock[i];\n" +
                tabStr + "            " + "}else {\n" +
                tabStr + "                " + "exp_clock[i] = left_clock[i];\n" +
                tabStr + "            " + "}\n" +
                tabStr + "        " + "}\n" +
                tabStr + "    " + "}\n" +
                tabStr + "    " + "return exp_clock[0::"+maxLength+"];";

        funcStr += "\n" + tabStr + "}";
        return funcStr;
    }

    public String encodingInfimumCountFunc(int tab){
        String tabStr= getTab(tab);
        String funcStr = tabStr + "int getInfimumCount(" + "int left_count" + "," + "int right_count" + "," + "int["+maxLength+"] left_clock" + "," + "int["+maxLength+"] right_clock" + "){" + "\n";

        funcStr +=  tabStr + "    " + "if(left_count <= right_count){\n" +
                tabStr + "        " + "return right_count;\n" +
                tabStr + "    " + "}else{\n" +
                tabStr + "        " + "return left_count;\n" +
                tabStr + "    " + "}";

        funcStr += "\n" + tabStr + "}";
        return funcStr;
    }


    public String encodingDelayFunc(int tab){
        String tabStr= getTab(tab);
        String funcStr = tabStr + "int["+maxLength+"] getDelay(" + "int delay_count" + "," + "int["+maxLength+"] delay_clock" + "," + "int delay_addition" + "," + "int["+maxLength+"] exp_clock" + "){" + "\n";
        funcStr += tabStr + "    " +"assert delay_count - delay_addition > 0;"+"\n";
        funcStr += tabStr + "    " +"int maxLength = "+maxLength + ";"+"\n" +
                tabStr + "    " +"int count = 0;"+"\n";
        funcStr += tabStr + "    " + "for(int i= 0 ; i < delay_count - delay_addition ; i++){\n" +
                tabStr + "        " + "exp_clock[i] = delay_clock[i + delay_addition];\n" +
                tabStr + "    " + "}\n" +
                tabStr + "    " + "return exp_clock[0::"+maxLength+"];";

        funcStr += "\n" + tabStr + "}";
        return funcStr;
    }

    public String encodingDelayCountFunc(int tab){
        String tabStr= getTab(tab);
        String funcStr = tabStr + "int getDelayCount(" + "int delay_count" + "," + "int["+maxLength+"] delay_clock" + "," + "int delay_addition" + "){" + "\n";

        funcStr += tabStr + "    " + "return delay_count - delay_addition;";

        funcStr += "\n" + tabStr + "}";
        return funcStr;
    }


    public String encodingPeriodicFunc(int tab){
        String tabStr= getTab(tab);
        String funcStr = tabStr + "int["+maxLength+"] getPeriodic(" + "int periodic_count" + "," + "int["+maxLength+"] periodic_clock" + "," + "int periodic_addition" + "," + "int["+maxLength+"] exp_clock" + "){" + "\n";
        funcStr += tabStr + "    " +"assert periodic_count - periodic_addition > 0;"+"\n";
        funcStr += tabStr + "    " +"int maxLength = "+maxLength + ";"+"\n" +
                tabStr + "    " +"int count = 0;"+"\n";
        funcStr += tabStr + "    " + "for(int i= 0 ; i < periodic_count / periodic_addition ; i++){\n" +
                tabStr + "        " + "exp_clock[i] = periodic_clock[(i+1) * periodic_addition - 1];\n" +
                tabStr + "    " + "}\n" +
                tabStr + "    " + "return exp_clock[0::"+maxLength+"];";

        funcStr += "\n" + tabStr + "}";
        return funcStr;
    }

    public String encodingPeriodicCountFunc(int tab){
        String tabStr= getTab(tab);
        String funcStr = tabStr + "int getPeriodicCount(" + "int periodic_count" + "," + "int["+maxLength+"] periodic_clock" + "," + "int periodic_addition" + "){" + "\n";

        funcStr += tabStr + "    " + "return periodic_count / periodic_addition;";

        funcStr += "\n" + tabStr + "}";
        return funcStr;
    }

    public String encodingDelayForFunc(int tab){
        String tabStr= getTab(tab);
        String funcStr = tabStr + "int["+maxLength+"] getDelayFor(" + "int delay_count" + "," + "int delayfor_count" + "," + "int["+maxLength+"] delay_clock" + "," + "int["+maxLength+"] delayfor_clock" + ","  + "int delay_addition" + "," + "int["+maxLength+"] exp_clock" + "){" + "\n";

        funcStr += tabStr + "    " +"int maxLength = "+maxLength + ";"+"\n" +
                tabStr + "    " +"int count = 0;"+"\n";
        funcStr +=
                tabStr + "    " + "for(int i= delay_addition-1 ; i < delayfor_count; i++){\n" +
                tabStr + "        " + "int isAdd = 0;\n" +
                tabStr + "        " + "for(int j= 0 ; j < delay_count; j++){\n" +
                tabStr + "            " + "if(isAdd == 0){\n" +
                tabStr + "                " + "if(i==delay_addition-1&&delay_clock[j]<delayfor_clock[0]){\n" +
                tabStr + "                    " + "exp_clock[count] = delayfor_clock[i];\n" +
                tabStr + "                    " + "count++;\n" +
                tabStr + "                    " + "isAdd = 1;\n" +
                tabStr + "                " + "}\n" +
                tabStr + "                " + "else if(i>delay_addition-1&&delay_clock[j]>=delayfor_clock[i - delay_addition]&&delay_clock[j]<delayfor_clock[i - delay_addition +1]){\n" +
                tabStr + "                    " + "exp_clock[count] = delayfor_clock[i];\n" +
                tabStr + "                    " + "count++;\n" +
                tabStr + "                    " + "isAdd = 1;\n" +
                tabStr + "                " + "}\n" +
                tabStr + "            " + "}\n" +
                tabStr + "        " + "}\n" +
                tabStr + "        " + "isAdd = 0;\n" +
                tabStr + "    " + "}\n" +
                tabStr + "    " + "return exp_clock[0::"+maxLength+"];";

        funcStr += "\n" + tabStr + "}";
        return funcStr;
    }

    public String encodingDelayForCountFunc(int tab){
        String tabStr= getTab(tab);
        String funcStr = tabStr + "int getDelayForCount(" + "int delay_count" + "," + "int delayfor_count" + "," + "int["+maxLength+"] delay_clock" + "," + "int["+maxLength+"] delayfor_clock" + "," + "int delay_addition" + "){" + "\n";
        funcStr += tabStr + "    " +"int count = 0;"+"\n";
        funcStr +=
                tabStr + "    " + "for(int i= delay_addition-1 ; i < delayfor_count; i++){\n" +
                tabStr + "        " + "int isAdd = 0;\n" +
                tabStr + "        " + "for(int j= 0 ; j < delay_count; j++){\n" +
                tabStr + "            " + "if(isAdd == 0){\n" +
                tabStr + "                " + "if(i==delay_addition-1&&delay_clock[j]<delayfor_clock[0]){\n" +
                tabStr + "                    " + "count++;\n" +
                tabStr + "                    " + "isAdd = 1;\n" +
                tabStr + "                " + "}\n" +
                tabStr + "                " + "else if(i>delay_addition-1&&delay_clock[j]>=delayfor_clock[i - delay_addition]&&delay_clock[j]<delayfor_clock[i - delay_addition +1]){\n" +
                tabStr + "                    " + "count++;\n" +
                tabStr + "                    " + "isAdd = 1;\n" +
                tabStr + "                " + "}\n" +
                tabStr + "            " + "}\n" +
                tabStr + "        " + "}\n" +
                tabStr + "        " + "isAdd = 0;\n" +
                tabStr + "    " + "}\n" +
                tabStr + "    " + "return count;";

        funcStr += "\n" + tabStr + "}";
        return funcStr;
    }


    public String encodingSampledOnFunc(int tab){
        String tabStr= getTab(tab);
        String funcStr = tabStr + "int["+maxLength+"] getSampledOn(" + "int left_count" + "," + "int right_count" + "," + "int["+maxLength+"] left_clock" + "," + "int["+maxLength+"] right_clock" + "," + "int["+maxLength+"] exp_clock" + "){" + "\n";

        funcStr +=  tabStr + "    " + "int count = 0;\n" +
                tabStr + "    " + "for (int i = 1 ; i < right_count ; i++){\n" +
                tabStr + "        " + "int current_count = 0 ;\n" +
                tabStr + "        " + "int is_add = 0 ;\n" +
                tabStr + "        " + "for (int j = current_count ; j < left_count ; j++){\n" +
                tabStr + "             " + "if(is_add == 0 && left_clock[j] <= right_clock[i] && left_clock[j] > right_clock[i-1] ){\n"+
                tabStr + "                 " + "exp_clock[count] = right_clock[i];\n"+
                tabStr + "                 " + "count = count++;\n"+
                tabStr + "                 " + "is_add = 1;\n"+
                tabStr + "             " + "}\n"+
                tabStr + "         " + "}\n" +
                tabStr + "         " + "is_add = 0;\n" +
                tabStr + "    " + "}\n" +
                tabStr + "    " + "return exp_clock[0::"+maxLength+"];";

        funcStr += "\n" + tabStr + "}";
        return funcStr;
    }

    public String encodingSampledOnCountFunc(int tab){
        String tabStr= getTab(tab);
        String funcStr = tabStr + "int getSampledOnCount(" + "int left_count" + "," + "int right_count" + "," + "int["+maxLength+"] left_clock" + "," + "int["+maxLength+"] right_clock" + "){" + "\n";

        funcStr +=  tabStr + "    " + "int count = 0;\n" +
                tabStr + "    " + "for (int i = 1 ; i < right_count ; i++){\n" +
                tabStr + "        " + "int current_count = 0 ;\n" +
                tabStr + "        " + "int is_add = 0 ;\n" +
                tabStr + "        " + "for (int j = current_count ; j < left_count ; j++){\n" +
                tabStr + "             " + "if(is_add == 0 && left_clock[j] <= right_clock[i] && left_clock[j] > right_clock[i-1] ){\n"+
                tabStr + "                 " + "count = count++;\n"+
                tabStr + "                 " + "is_add = 1;\n"+
                tabStr + "             " + "}\n"+
                tabStr + "         " + "}\n" +
                tabStr + "         " + "is_add = 0;\n" +
                tabStr + "    " + "}\n" +
                tabStr + "    " + "return count;\n";

        funcStr += "\n" + tabStr + "}";
        return funcStr;
    }

    public String getTab(int count){
        String tab = "";
        for (int i = 0 ; i < count;i++){
            tab += "    ";
        }
        return tab;
    }
}
