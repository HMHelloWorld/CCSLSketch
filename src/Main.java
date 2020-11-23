import Encoder.CCSLEncoder;
import Parser.TraceParser;
import Parser.XMLFileParser;

import java.io.File;
import java.io.IOException;

/**
 * Created by huming on 2019/6/24.
 */
public class Main {
    public static void main(String args[]){
        String caseStr = "/example11";
        String traceStr = "trace";
        String testConfig = "configure_t1.xml";//需要综合的CCSL的路径
//        String nagetiveConfig = "nagetive_configure.xml";
        String traceSourcePath = "/Users/huming/eclipse-workspace/CCSLProject/trace";
        String traceDestPath = "../data/"+caseStr+"/" + traceStr;

        String sketchFilePath = "/Users/huming/Desktop/sketch/testResult/";
        /** control
         * 0   synthesize
         * 1   check
         * 2   copy trace
         */
        int control = 0;
        switch (control){
            case 0: {
                CCSLSketchConfigure configure = CCSLSketchConfigure.getConfigure();
                int traceCount = 5;
                int count = 0;
                File traceDir = new File(configure.rootDir + configure.traceDir);//trace文件的路径
                File[] files = traceDir.listFiles();
                for (File fileIndex : files) {
                    String curpath = fileIndex.getPath();
                    String[] values = curpath.split("\\.");
                    if (values[values.length - 1].equals("trace")) {
                        if (count>=traceCount){
                            break;
                        }
                        count++;
                        XMLFileParser.addTraceByFile(curpath);
                    }
                }


                XMLFileParser.addConfigureByFile(configure.rootDir, configure.sourceDir + configure.sourceFileName);//需要综合的未补全的CCSL
//                XMLFileParser.addNegativeCCSLByFile("../data/" + caseStr, nagetiveConfig);//剔除错误的CCSL结果

                CCSLEncoder generator = new CCSLEncoder(configure.maxLength);

                generator.generateSKETCHFile(configure.sketchDir+"/", configure.sketchResultName,true);//生成SKETCH文件的路径

                String result = SketchSynthesizer.getSketchSynthesizer().runSynthesizer(configure.sketchDir+"/" + configure.sketchResultName+".sk",configure.maxLength,configure.sketchResultDir+"/",configure.sketchResultName);
                System.out.println(result);
                try {
                    SketchSynthesizer.getSketchSynthesizer().creatFile(configure.sketchResultName + ".txt",configure.sketchResultDir,result);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }break;
            case 1: {
                File traceDir = new File("../data/" + caseStr + "/" + traceStr);
                File[] files = traceDir.listFiles();
                for (File fileIndex : files) {
                    String curpath = fileIndex.getPath();
                    String[] values = curpath.split("\\.");
                    if (values[values.length - 1].equals("trace")) {
                        XMLFileParser.addTraceByFile(curpath);
                    }
                }
                XMLFileParser.addConfigureByFile("../data/" + caseStr, testConfig);

                double rate = 0;
                for (int i = 0; i < TraceParser.getInstance().allTraces.size(); i++) {
                    CCSLCheck check = new CCSLCheck();
                    Boolean result = check.check(TraceParser.getInstance().allTraces.get(i));
                    if (result) {
                        rate += 1;
                    }
                }
                rate = rate / TraceParser.getInstance().allTraces.size() * 100;
                System.out.println(rate + "%");
            }break;
            case 2:{
                try {
                    FileTool.copyTraceFile(traceSourcePath,traceDestPath);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }break;
            default:


        }


    }
}
