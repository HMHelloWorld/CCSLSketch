import Encoder.CCSLEncoder;
import Parser.XMLFileParser;

import java.io.File;
import java.io.IOException;

/**
 * Created by huming on 2019/6/24.
 */
public class Main {
    public static void main(String args[]){
        String confStr = "CCSLSketchConfigure";
        if (args.length > 0){
            confStr = args[0];
        }
        CCSLSketchConfigure configure = CCSLSketchConfigure.getConfigure(confStr);
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


    }
}
