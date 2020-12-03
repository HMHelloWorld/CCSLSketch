# CCSLSketch
A Tool for Clock Constraint Specification Language(CCSL)  Synthesis 

## Workflow
Our tool consists of three key components: Paser, Encoder, and Sketch synthesizer. All these tool are all developed using JAVA. So users should have a JAVA Runtime environment. We suggest the users to install the JAVA Runtime environment JAVA 1.8.0.  
Firstly, we parse the incomplete CCSL constraints and traces from xml file to our datastructure.
Secondly, we encode CCSL synthesis problem into sketching problem.
Finally, we using program synthesis tool SKETCH to synthesize incomplete CCSL specification.

## Requirement
Our tool use Sketch synthesizer. Thus, you have to install it first. Download the [sketch-1.7.0.tgz](http://people.csail.mit.edu/jsjeon/adaptive-concretization/sketch-1.7.0.tgz) and follow the instruction in it. You may need to set your environment variables as follows:

    export SKETCH_HOME=/path/to/sketch/runtime
    export PATH=$PATH:$SKETCH_HOME/..
A harder way is to install it from source code: front-end/back-end. In that case, build architecture-independent version of front-end via:

    <sketch-frontend> $ make assemble-noarch
and then set your environment variables as follows:

    export SKETCH_HOME=/path/to/sketch-frontend
    export PATH=$PATH:$SKETCH_HOME/target/sketch-1.7.0-noarch-launchers

## Structure
CCSLSketch  
&emsp;&emsp;│──CCSLModel  
&emsp;&emsp;│&emsp;&emsp;&emsp;│──Clock.java  
&emsp;&emsp;│&emsp;&emsp;&emsp;│──Expression.java  
&emsp;&emsp;│&emsp;&emsp;&emsp;│──Relation.java  
&emsp;&emsp;│&emsp;&emsp;&emsp;│──RelationMap.java  
&emsp;&emsp;│&emsp;&emsp;&emsp;│──RelationMapItem.java  
&emsp;&emsp;│&emsp;&emsp;&emsp;│──Varible.java  
&emsp;&emsp;│&emsp;&emsp;&emsp;└──Trace.java  
&emsp;&emsp;│──Parser  
&emsp;&emsp;│&emsp;&emsp;&emsp;│──ClockPaser.java  
&emsp;&emsp;│&emsp;&emsp;&emsp;│──ExpressionPaser.java  
&emsp;&emsp;│&emsp;&emsp;&emsp;│──RelationPaser.java  
&emsp;&emsp;│&emsp;&emsp;&emsp;│──TracePaser.java  
&emsp;&emsp;│&emsp;&emsp;&emsp;└──XMLFilePaser.java  
&emsp;&emsp;│──Encoder  
&emsp;&emsp;│&emsp;&emsp;&emsp;│──CCSLEncoder.java  
&emsp;&emsp;│&emsp;&emsp;&emsp;│──CCSLFunctionEncoder.java  
&emsp;&emsp;│&emsp;&emsp;&emsp;│──CheckFuncEncoder.java  
&emsp;&emsp;│&emsp;&emsp;&emsp;└──HarnessEncoder.java  
&emsp;&emsp;│──CCSLSKetchConfigure.java  
&emsp;&emsp;│──FileTool.java  
&emsp;&emsp;│──SketchSynthesizer.java  
&emsp;&emsp;└──Main.java
   
## Benchmarks
We give three specifications as benchmark. For each specification, we give four incomplete specifications and some expected timing behaviors (traces). Our tool can synthesize this incomplete specifications to generate complete specification.
### example 1
4 clocks, 1 expression constraints, 3 relation constraints  
[Details](https://github.com/HMHelloWorld/CCSLSketch/blob/master/example/benchmark1/Readme.md)  

### example 2
Settings: 10 clocks, 5 expression constraints, 10 relation constraints  
[Details](https://github.com/HMHelloWorld/CCSLSketch/blob/master/example/benchmark2/Readme.md)  
### example 3
Settings: 20 clocks, 6 expression constraints, 16 relation constraints  
[Details](https://github.com/HMHelloWorld/CCSLSketch/blob/master/example/benchmark3/Readme.md)  
