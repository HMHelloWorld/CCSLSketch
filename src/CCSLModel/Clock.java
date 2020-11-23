package CCSLModel;

/**
 * Created by huming on 2019/6/24.
 */
public class Clock {
    public int id;
    public String name;
    public int globalNum;
    public int num;
    public Clock(){
        id= 0;
        name = "";
        globalNum = 0;
        num = 0;
    }
    public Clock(int id,String name,int globalNum,int num){
        this.id= id;
        this.name = name;
        this.globalNum = globalNum;
        this.num = num;
    }

    @Override
    public String toString() {
        return "Clock:[id:"+id + "\t" + "name:"+name + "\t" + "globalNum:"+globalNum + "\t" + "num:" + num + "\t"+ " ]";
    }

    public String toSkString() {
        return "["+id + "," +globalNum + ","+ num + "]";
    }
}
