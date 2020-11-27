# CCSLSketch
A Tool for Clock Constraint Specification Language(CCSL)  Synthesis 

## Workflow
Our tool consists of three key components: Paser, Encoder, and Sketch synthesizer. All these tool are all developed using JAVA. So users should have a JAVA Runtime environment. We suggest the users to install the JAVA Runtime environment JAVA 1.8.0.  
Firstly, we parse the incomplete CCSL constraints and traces from xml file to our datastructure.
Secondly, we encode CCSL synthesis problem into sketching problem.
Finally, we using program synthesis tool SKETCH to synthesize incomplete CCSL specification.

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
Settings:  
4 clocks, 1 expression constraints, 3 relation constraints  
#### CCSL Constraint
![](data:image/png;base64,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)
//$$c_0\prec c_1$$  
//$$c_1=c_2$$  
//$$c_3\subseteq e_0$$  
//$$e_0=c_0*c_1$$

#### Incomplete CCSL Constraint  
case1  
![](data:image/png;base64,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)
$$c_0??c_1$$  
$$c_1??c_2$$  
$$c_3??e_0$$  
$$e_0=c_0*c_1$$  
case2  
$$??\prec c_1$$  
$$c_1=??$$  
$$c_3\subseteq ??$$  
$$e_0=c_0*c_1$$  
case3  
$$c_0?? c_1$$  
$$c_1??c_2$$  
$$c_3\subseteq e_0$$  
$$e_0=c_0??c_1$$  
case4  
$$c_0\prec ??$$  
$$??=c_2$$  
$$c_3\subseteq e_0$$  
$$e_0=??*c_1$$

### example 2
Settings: 10 clocks, 5 expression constraints, 10 relation constraints  
### example 3
Settings: 20 clocks, 6 expression constraints, 16 relation constraints  
