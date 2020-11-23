import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;

/**
 * Created by huming on 2019/6/24.
 */
public class FileTool {
    public static void copyFile(String sourcePath,String destPath) throws IOException {
        File sourceFile = new File(sourcePath);
        File destFile = new File(destPath);

        Files.copy(sourceFile.toPath(),destFile.toPath());
    }
    public static void copyTraceFile(String sourcePath,String destPath) throws Exception {
        ArrayList<File> files = FileTool.getFiles(sourcePath);
        FileTool.judeDirExists(new File(destPath));
        for (int i = 0 ; i <files.size();i++){
            copyFile(files.get(i).getPath(),destPath+"/trace"+i+".trace");
        }


    }
    public static ArrayList<File> getFiles(String path) throws Exception {
        //目标集合fileList
        ArrayList<File> fileList = new ArrayList<File>();
        File file = new File(path);
        if(file.isDirectory()){
            File []files = file.listFiles();
            for(File fileIndex:files){
                //如果这个文件是目录，则进行递归搜索
                if(fileIndex.isDirectory()){
                    ArrayList<File> subFiles = getFiles(fileIndex.getPath());
                    for (int j = 0 ; j < subFiles.size() ; j ++){
                        fileList.add(subFiles.get(j));
                    }
                }else {
                    //如果文件是普通文件，则将文件句柄放入集合中
                    String curpath = fileIndex.getPath();
                    String [] values = curpath.split("\\.");
                    if (values[values.length - 1].equals("trace")){
                        fileList.add(fileIndex);
                    }
                }
            }
        }
        return fileList;
    }
    public static void judeDirExists(File file) {
        if (file.exists()) {
            if (file.isDirectory()) {
                System.out.println("dir exists");
            } else {
                System.out.println("the same name file exists, can not create dir");
            }
        } else {
            System.out.println("dir not exists, create it ...");
            file.mkdir();
        }
    }
}
