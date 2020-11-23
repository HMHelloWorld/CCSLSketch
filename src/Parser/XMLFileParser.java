package Parser;

import CCSLModel.Expression;
import CCSLModel.Relation;
import CCSLModel.Variable;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;

/**
 * Created by huming on 2019/6/24.
 */
public class XMLFileParser {
    public static void addTraceByFile(String filePath,String filename){
        TraceParser parser = TraceParser.getInstance();
        String fileStr = "";
        if(filePath == null || filePath.equals("")){
            fileStr = filename;
        }else {
            fileStr = filePath+"/"+filename;
        }
        Element element = null;
        File f = new File(fileStr);
        DocumentBuilder db = null;
        DocumentBuilderFactory dbf = null;
        try {
            // 返回documentBuilderFactory对象
            dbf = DocumentBuilderFactory.newInstance();
            // 返回db对象用documentBuilderFatory对象获得返回documentBuildr对象
            db = dbf.newDocumentBuilder();
            // 得到一个DOM并返回给document对象
            Document dt = db.parse(f);
            // 得到一个elment根元素
            element = dt.getDocumentElement();
            // 获得根节点
            NodeList childNodes = element.getChildNodes();
            // 遍历这些子节点

            ArrayList<ArrayList<String>> traceList = new ArrayList<>();
            ArrayList<String> clockIds = new ArrayList<>();

            for (int i = 0; i < childNodes.getLength(); i++) {
                // 获得每个对应位置i的结点
                Node referencesNode = childNodes.item(i);

                if ("references".equals(referencesNode.getNodeName())) {
                    NodeList refNodes = referencesNode.getChildNodes();
                    Node refNode = refNodes.item(5);
                    if ("elementRef".equals(refNode.getNodeName())){
                        NamedNodeMap refMap = refNode.getAttributes();
                        String refValue = refMap.getNamedItem("href").getNodeValue();
                        String [] values = refValue.split("\\.");
                        if(values[values.length-2].split("\\@")[1].equals("elements")){
                            String clockId = values[values.length-1];
                            clockIds.add(clockId);
                        }
                    }



                }
            }



            for (int i = 0; i < childNodes.getLength(); i++) {
                // 获得每个对应位置i的结点
                Node stepNode = childNodes.item(i);

                if ("logicalSteps".equals(stepNode.getNodeName())) {

                    NamedNodeMap stepMap = stepNode.getAttributes();
                    int globalCount = 0;
                    if(stepMap.getNamedItem("stepNumber")!=null){
                        globalCount = Integer.valueOf(stepMap.getNamedItem("stepNumber").getNodeValue());
                    }
                    System.out.println(globalCount);
                    NodeList clockNodes = stepNode.getChildNodes();
                    ArrayList<String> clockList = new ArrayList<>();
                    for (int j = 0;j< clockNodes.getLength();j++){
                        Node clockNode = clockNodes.item(j);

                        if ("eventOccurrences".equals(clockNode.getNodeName())){
                            NamedNodeMap map = clockNode.getAttributes();

                            if(map.getNamedItem("eState") == null){
                                String clockName = map.getNamedItem("referedElement").getNodeValue();
                                int clockCount = Integer.valueOf(map.getNamedItem("counter").getNodeValue());
                                System.out.println(clockName + " " + clockCount);
                                String[] name = clockName.split("\\.");
                                int id = Integer.valueOf(name[1]);
                                if(id < clockIds.size()){
                                    clockName = "c" + clockIds.get(id) ;
                                    clockList.add(clockName);
                                }


                            }

                        }
                    }
                    traceList.add(clockList);
                }
            }
            parser.setTraces(traceList);

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addTraceByFile(String filename){
        String fileStr = filename;
        TraceParser parser = TraceParser.getInstance();
        Element element = null;
        File f = new File(fileStr);
        DocumentBuilder db = null;
        DocumentBuilderFactory dbf = null;
        try {
            // 返回documentBuilderFactory对象
            dbf = DocumentBuilderFactory.newInstance();
            // 返回db对象用documentBuilderFatory对象获得返回documentBuildr对象
            db = dbf.newDocumentBuilder();
            // 得到一个DOM并返回给document对象
            Document dt = db.parse(f);
            // 得到一个elment根元素
            element = dt.getDocumentElement();
            // 获得根节点
            NodeList childNodes = element.getChildNodes();
            // 遍历这些子节点

            ArrayList<ArrayList<String>> traceList = new ArrayList<>();
            ArrayList<String> clockIds = new ArrayList<>();

            for (int i = 0; i < childNodes.getLength(); i++) {
                // 获得每个对应位置i的结点
                Node referencesNode = childNodes.item(i);

                if ("references".equals(referencesNode.getNodeName())) {
                    NodeList refNodes = referencesNode.getChildNodes();
                    Node refNode = refNodes.item(5);
                    if ("elementRef".equals(refNode.getNodeName())){
                        NamedNodeMap refMap = refNode.getAttributes();
                        String refValue = refMap.getNamedItem("href").getNodeValue();
                        String [] values = refValue.split("\\.");
                        if(values[values.length-2].split("\\@")[1].equals("elements")){
                            String clockId = values[values.length-1];
                            clockIds.add(clockId);
                        }
                    }



                }
            }

            for (int i = 0; i < childNodes.getLength(); i++) {
                // 获得每个对应位置i的结点
                Node stepNode = childNodes.item(i);

                if ("logicalSteps".equals(stepNode.getNodeName())) {

                    NamedNodeMap stepMap = stepNode.getAttributes();
                    int globalCount = 0;
                    if(stepMap.getNamedItem("stepNumber")!=null){
                        globalCount = Integer.valueOf(stepMap.getNamedItem("stepNumber").getNodeValue());
                    }
                    NodeList clockNodes = stepNode.getChildNodes();
                    ArrayList<String> clockList = new ArrayList<>();
                    for (int j = 0;j< clockNodes.getLength();j++){
                        Node clockNode = clockNodes.item(j);

                        if ("eventOccurrences".equals(clockNode.getNodeName())){
                            NamedNodeMap map = clockNode.getAttributes();

                            if(map.getNamedItem("eState") == null){
                                String clockName = map.getNamedItem("referedElement").getNodeValue();
                                int clockCount = Integer.valueOf(map.getNamedItem("counter").getNodeValue());
                                String[] name = clockName.split("\\.");
                                int id = Integer.valueOf(name[1]);
                                if(id < clockIds.size()){
                                    clockName = "c" + clockIds.get(id) ;
                                    clockList.add(clockName);
                                }


                            }

                        }
                    }
                    traceList.add(clockList);
                }
            }
            parser.setTraces(traceList);

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addConfigureByFile(String filePath,String fileName){
        String fileStr = "";
        if(filePath == null || filePath.equals("")){
            fileStr = fileName;
        }else {
            fileStr = filePath+fileName;
        }
        Element element = null;
        File f = new File(fileStr);
        DocumentBuilder db = null;
        DocumentBuilderFactory dbf = null;
        try {
            // 返回documentBuilderFactory对象
            dbf = DocumentBuilderFactory.newInstance();
            // 返回db对象用documentBuilderFatory对象获得返回documentBuildr对象
            db = dbf.newDocumentBuilder();
            // 得到一个DOM并返回给document对象
            Document dt = db.parse(f);
            // 得到一个elment根元素
            element = dt.getDocumentElement();
            // 获得根节点
            NodeList childNodes = element.getChildNodes();
            // 遍历这些子节点

            for (int i = 0; i < childNodes.getLength(); i++) {
                // 获得每个对应位置i的结点
                Node configureNode = childNodes.item(i);
                if ("expressions".equals(configureNode.getNodeName())){
                    NodeList expressionNodes = configureNode.getChildNodes();
                    for (int j = 0 ; j < expressionNodes.getLength();j++){
                        Node expressionNode = expressionNodes.item(j);
                        if ("expression".equals(expressionNode.getNodeName())) {
                            NamedNodeMap map = expressionNode.getAttributes();
                            ArrayList<String>clockList = new ArrayList<>();
                            String name = map.getNamedItem("name").getNodeValue();
                            String typeStr = map.getNamedItem("type").getNodeValue();
                            int type = Integer.parseInt(typeStr);
                            String leftClock = map.getNamedItem("leftClock").getNodeValue();
                            clockList.add(leftClock);
                            if( map.getNamedItem("rightClock")!= null){
                                String rightClock = map.getNamedItem("rightClock").getNodeValue();
                                clockList.add(rightClock);
                            }
                            int addition = 0;
                            if(map.getNamedItem("addition")!=null){
                                String additionStr = map.getNamedItem("addition").getNodeValue();
                                if(!additionStr.equals("")){
                                    addition = Integer.parseInt(additionStr);
                                }
                            }
                            ExpressionParser.getInstance().addExpression(name,clockList,type,addition);
                        }
                    }
                }

            }

            for (int i = 0; i < childNodes.getLength(); i++) {
                // 获得每个对应位置i的结点
                Node configureNode = childNodes.item(i);
                if("relations".equals(configureNode.getNodeName())) {
                    NodeList relationNodes = configureNode.getChildNodes();
                    for (int j = 0 ; j < relationNodes.getLength();j++){
                        Node relationNode = relationNodes.item(j);
                        if ("relation".equals(relationNode.getNodeName())) {
                            NamedNodeMap map = relationNode.getAttributes();
                            ArrayList<String>clockList = new ArrayList<>();
                            String typeStr = map.getNamedItem("type").getNodeValue();
                            int type = Integer.parseInt(typeStr);
                            String leftClock = map.getNamedItem("leftClock").getNodeValue();
                            clockList.add(leftClock);
                            if( map.getNamedItem("rightClock")!= null){
                                String rightClock = map.getNamedItem("rightClock").getNodeValue();
                                clockList.add(rightClock);
                            }
                            RelationParser.getInstance().addRelation(clockList,type);
                        }
                    }
                }

            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addNegativeCCSLByFile(String filePath,String fileName){
        String fileStr = "";
        if(filePath == null || filePath.equals("")){
            fileStr = fileName;
        }else {
            fileStr = filePath+"/"+fileName;
        }
        Element element = null;
        File f = new File(fileStr);
        DocumentBuilder db = null;
        DocumentBuilderFactory dbf = null;
        try {
            // 返回documentBuilderFactory对象
            dbf = DocumentBuilderFactory.newInstance();
            // 返回db对象用documentBuilderFatory对象获得返回documentBuildr对象
            db = dbf.newDocumentBuilder();
            // 得到一个DOM并返回给document对象
            Document dt = db.parse(f);
            // 得到一个elment根元素
            element = dt.getDocumentElement();
            // 获得根节点
            NodeList childNodes = element.getChildNodes();
            // 遍历这些子节点

            for (int i = 0; i < childNodes.getLength(); i++) {
                // 获得每个对应位置i的结点
                Node negativeNode = childNodes.item(i);
                if ("CCSLConfigure".equals(negativeNode.getNodeName())){
                    ArrayList<Expression>expressions = new ArrayList<>();
                    ArrayList<Relation>relations = new ArrayList<>();
                    for (int k = 0; k< negativeNode.getChildNodes().getLength();k++){
                        Node configureNode = negativeNode.getChildNodes().item(k);
                        if ("expressions".equals(configureNode.getNodeName())){
                            NodeList expressionNodes = configureNode.getChildNodes();
                            for (int j = 0 ; j < expressionNodes.getLength();j++){
                                Node expressionNode = expressionNodes.item(j);
                                if ("expression".equals(expressionNode.getNodeName())) {
                                    NamedNodeMap map = expressionNode.getAttributes();
                                    ArrayList<String>clockList = new ArrayList<>();
                                    String name = map.getNamedItem("name").getNodeValue();
                                    String typeStr = map.getNamedItem("type").getNodeValue();
                                    int type = Integer.parseInt(typeStr);
                                    String leftClock = map.getNamedItem("leftClock").getNodeValue();
                                    clockList.add(leftClock);
                                    if( map.getNamedItem("rightClock")!= null){
                                        String rightClock = map.getNamedItem("rightClock").getNodeValue();
                                        clockList.add(rightClock);
                                    }
                                    int addition = 0;
                                    if(map.getNamedItem("addition")!=null){
                                        String additionStr = map.getNamedItem("addition").getNodeValue();
                                        if(!additionStr.equals("")){
                                            addition = Integer.parseInt(additionStr);
                                        }
                                    }
                                    ArrayList<Variable> variables = new ArrayList<>();
                                    for (int clk_idx = 0 ; clk_idx < clockList.size();clk_idx++){
                                        String clock = clockList.get(clk_idx);
                                        Variable variable = new Variable(clock,1);
                                        variables.add(variable);
                                    }
                                    Expression expression = new Expression(0,name,variables,type,addition);
                                    expressions.add(expression);
                                }
                            }
                        }

                    }
                    for (int k = 0; k < negativeNode.getChildNodes().getLength(); k++) {
                        // 获得每个对应位置i的结点
                        Node configureNode = negativeNode.getChildNodes().item(k);
                        if("relations".equals(configureNode.getNodeName())) {
                            NodeList relationNodes = configureNode.getChildNodes();
                            for (int j = 0 ; j < relationNodes.getLength();j++){
                                Node relationNode = relationNodes.item(j);
                                if ("relation".equals(relationNode.getNodeName())) {
                                    NamedNodeMap map = relationNode.getAttributes();
                                    ArrayList<String>clockList = new ArrayList<>();
                                    String typeStr = map.getNamedItem("type").getNodeValue();
                                    int type = Integer.parseInt(typeStr);
                                    String leftClock = map.getNamedItem("leftClock").getNodeValue();
                                    clockList.add(leftClock);
                                    if( map.getNamedItem("rightClock")!= null){
                                        String rightClock = map.getNamedItem("rightClock").getNodeValue();
                                        clockList.add(rightClock);
                                    }

                                    ArrayList<Variable> variables = new ArrayList<>();
                                    for (int c = 0 ; c < clockList.size();c++){
                                        Variable variable = new Variable(clockList.get(c),0);
                                        variables.add(variable);
                                    }
                                    Relation relation = new Relation(variables,type);
                                    relations.add(relation);
                                }
                            }
                        }

                    }
                    NagetiveCCSLParser.getInstance().addNagetiveCCSL(expressions,relations);
                }



            }


        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}