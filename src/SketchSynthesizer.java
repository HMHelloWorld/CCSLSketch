import CCSLModel.Expression;
import CCSLModel.Relation;

import java.io.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by huming on 2019/10/21.
 */
public class SketchSynthesizer {
    private static SketchSynthesizer sketchSynthesizer = null;
    public static SketchSynthesizer  getSketchSynthesizer(){
        if(sketchSynthesizer == null){
            sketchSynthesizer = new SketchSynthesizer();
        }
        return sketchSynthesizer;
    }
    public String runSynthesizer(String address, int length,String resultPath,String fileName){
        String result = "";
        try {
            runSketch(address, length,resultPath);
            result = regexResult(resultPath,fileName + ".cpp");
        } catch (IOException e) {
            e.printStackTrace();
            return e.toString();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return e.toString();
        }
        return result;
    }
    public String runSketch(String address, int length,String resultPath) throws IOException, InterruptedException {
        Process proc = null;
        String cmdStr = "sketch --bnd-unroll-amnt "+ length + " "+ address + " --fe-output-code --fe-output-dir " + resultPath;
//        String cmdStr = "java -version";
        String[] cmd = {"/bin/sh","-c","source ~/.bash_profile && " + cmdStr};

//        proc = Runtime.getRuntime().exec("sh -c source .bash_profile & " + cmdStr);
        proc = Runtime.getRuntime().exec(cmd);
        proc.waitFor();
        InputStream inputStream = proc.getInputStream();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        InputStream errorStream = proc.getErrorStream();
        InputStreamReader errorStreamReader = new InputStreamReader(errorStream, "utf-8");
        BufferedReader errorBufferedReader = new BufferedReader(errorStreamReader);

        String line;
        StringBuffer output = new StringBuffer();
        while ((line = bufferedReader.readLine()) != null) {
            output.append(line + "\r\n");
        }

        while ((line = errorBufferedReader.readLine()) != null) {
            output.append(line + "\r\n");
        }
        System.out.println(output.toString());

        return output.toString();
    }
    public String regexResult(String resultPath,String fileName) throws IOException {
        ArrayList<String> results = readFromTextFile(resultPath + fileName);
        String result = "";
        boolean isCheckFunc = false;
        int tag = 1;
        for (int i = 0 ; i < results.size(); i ++){
            if (tag == 0){
                break;
            }
            if (!isCheckFunc){
                String pattern = ".*void check\\(.*\\).*\\{";
                isCheckFunc = Pattern.matches(pattern,results.get(i));
            }else {
                String pattern = ".*get([A-z]*)Count\\((.*)_count.*, (.*)_count.*,.*,.*, ([0-9]*), (.*)_count.*\\);";
                Pattern p = Pattern.compile(pattern);
                Matcher m = p.matcher(results.get(i));
                if (m.find()){
                    String expName = m.group(1);
                    String leftClk = m.group(2);
                    String rightClk = m.group(3);
                    String addition = m.group(4);
                    String expClkName = m.group(5);
                    result +=expClkName + " = " + leftClk + " " + Expression.getSymbol(expName) + " " +rightClk + " on "+ addition +'\n';
                }else {
                    pattern = ".*get([A-z]*)Count\\((.*)_count.*, (.*)_count.*,.*,.*, (.*)_count.*\\);";
                    p = Pattern.compile(pattern);
                    m = p.matcher(results.get(i));
                    if (m.find()){
                        String expName = m.group(1);
                        String leftClk = m.group(2);
                        String rightClk = m.group(3);
                        String expClkName = m.group(4);
                        result +=expClkName + " = " + leftClk + " " + Expression.getSymbol(expName) + " " +rightClk + '\n';
                    }else {
                        pattern = ".*get([A-z]*)Count\\((.*)_count.*,.*, ([0-9]*), (.*)_count.*\\);";
                        p = Pattern.compile(pattern);
                        m = p.matcher(results.get(i));
                        if (m.find()){
                            String expName = m.group(1);
                            String leftClk = m.group(2);
                            String addition = m.group(3);
                            String expClkName = m.group(4);
                            result +=expClkName + " = " + leftClk + " " + Expression.getSymbol(expName) + " " +addition + '\n';
                        }else {
                            String relPattern = ".*check([A-z]*)\\((.*)_count.*, (.*)_count.*,.*\\);";
                            p = Pattern.compile(relPattern);
                            m = p.matcher(results.get(i));
                            if(m.find()){
                                String relName = m.group(1);
                                String leftClk = m.group(2);
                                String rightClk = m.group(3);
                                result +=leftClk + " " + Relation.getSymbol(relName) + " " +rightClk + '\n';
                            }else {

                                String leftPattern = ".*\\{.*";
                                Boolean isLeft = Pattern.matches(leftPattern,results.get(i));
                                if (isLeft){
                                    tag++;
                                }
                                String rightPattern = ".*\\}.*";
                                Boolean isRight = Pattern.matches(rightPattern,results.get(i));
                                if (isRight){
                                    tag--;
                                }

                            }
                        }
                    }
                }

            }


        }
        return result;
    }
    public ArrayList<String> readFromTextFile(String pathname) throws IOException{
        ArrayList<String> strArray = new ArrayList<String>();
        File filename = new File(pathname);
        InputStreamReader reader = new InputStreamReader(new FileInputStream(filename));
        BufferedReader br = new BufferedReader(reader);
        String line = "";
        line = br.readLine();
        while(line != null) {
            strArray.add(line);
            line = br.readLine();
        }
        return strArray;
    }

    public void creatFile(String name,String filePath,String content) throws IOException {
        File file =new File(filePath+"/"+name);
        try {
            if(!file.exists()) {
                file.createNewFile();
            }
            FileWriter fileWriter =new FileWriter(file);
            fileWriter.write(content);
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
