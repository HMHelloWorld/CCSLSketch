import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

/**
 * Created by huming on 2019/10/22.
 */
public class CCSLSketchConfigure {
    public String rootDir="";
    public String sourceDir="";
    public String traceDir="";
    public String sourceFileName="";
    public String resultDir="";
    public String sketchDir="";
    public String sketchResultDir="";
    public String sketchResultName="";
    public int maxLength=0;
    private static CCSLSketchConfigure configure = null;
    public static CCSLSketchConfigure getConfigure(){
        if (configure == null){
            try {
                configure = new CCSLSketchConfigure();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return configure;
    }
    private CCSLSketchConfigure() throws ParserConfigurationException, IOException, SAXException {
        Element element = null;
        File f = new File("CCSLSketchConfigure");
        DocumentBuilder db = null;
        DocumentBuilderFactory dbf = null;
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
            if ("TestCase".equals(configureNode.getNodeName())) {
                NamedNodeMap map = configureNode.getAttributes();
                rootDir = map.getNamedItem("rootDir").getNodeValue();
                sourceDir = map.getNamedItem("sourceDir").getNodeValue();
                traceDir= map.getNamedItem("traceDir").getNodeValue();
                sourceFileName= map.getNamedItem("sourceFileName").getNodeValue();
                resultDir= map.getNamedItem("resultDir").getNodeValue();
                sketchDir= map.getNamedItem("sketchDir").getNodeValue();
                sketchResultDir= map.getNamedItem("sketchResultDir").getNodeValue();
                sketchResultName= map.getNamedItem("sketchResultName").getNodeValue();
                maxLength= Integer.parseInt(map.getNamedItem("maxLength").getNodeValue());
            }

        }
    }


}
