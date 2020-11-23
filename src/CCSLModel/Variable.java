package CCSLModel;

/**
 * Created by huming on 2019/6/24.
 */
public class Variable {
    public String name;
    /**
     * 0        clock
     * 1        expression
     */
    public int type;

    public Variable(String name,int type){

        this.name = name;
        this.type = type;
    }

    public Variable(Variable variable){

        this.name = variable.name;
        this.type = variable.type;
    }
    public boolean equals(Variable variable){
        return name.equals(variable.name);
    }
}
