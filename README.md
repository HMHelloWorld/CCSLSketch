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

## Input file
### Configuration file
You need a configuration file to use the tool. The configuration file marks the path of the input file required by the tool, as well as the path of the output result. This is an example as follow:

    <Configure>
        <TestCase rootDir="/your_example_dir"
                  sourceDir="/your_ccsl_file_dir/"
                  traceDir="/your_trace_file_dir"
                  sourceFileName="your_ccsl_xml_file_name.xml"
                  resultDir="/your_result_dir"
                  sketchResultDir="/sketch_result_file_dir"
                  sketchResultName="sketch_result_file_name"
                  maxLength="100"
        />
    </Configure>


### CCSL Specification
We using XML file to indicate incomplete CCSL Specification.  
This is an example as follow:

    <CCSLConfigure>
        <relations>
            <relation type='-1' leftClock="c0" rightClock="c2"/>
            <relation type='-1' leftClock="c1" rightClock="c0"/>
            <relation type='4' leftClock="c3" rightClock=""/>
        </relations>
        <expressions>
            <expression name="e0" type='0' leftClock="c1" rightClock="c2" addition=""/>
        </expressions>
    </CCSLConfigure>
    
For relation/expression operator hole, we need set type='-1'  
For relation/expression clock hole, we need set leftClock/rightClock=""  

### Trace
We using XML file to indicate CCSL schedules. We use the same file format as CCSL simulation tool [TimeSquare](http://timesquare.inria.fr/simple-relation-example/).  

    <trace:Trace name="newfile2018_1113_161243">
        <logicalSteps nextStep="//@logicalSteps.1">
            <eventOccurrences referedElement="//@references.0" eState="noTick" fState="noTick"/>
            <eventOccurrences referedElement="//@references.1" eState="noTick" fState="noTick"/>
            <eventOccurrences referedElement="//@references.2" eState="noTick" fState="noTick"/>
            <eventOccurrences referedElement="//@references.3" counter="1"/>
        </logicalSteps>
        ...
    <\trace>

"nextStep" indicates the id of next step.  
"referedElement" indicates the clock id and we use a txt file to store the clock name with its id.  
"counter" indicates the tick times of corresponding clock.  
eState="noTick" indicates that the corresponding clock has no tick at the current step.  

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

### Benchmarks for single CCSL constraint
We give 11 specifications for each CCSL constraint. These examples are from [TimeSquare](http://timesquare.inria.fr/simple-relation-example/).  
| index |   Constaint  | Clock | Expression | Relation |
| ----  |     ----     | ----  |    ----    |   ----   |
|   1   | Coincidence  |   2   |     0      |    1     |
|   2   | Precedence   |   2   |     0      |    1     |
|   3   | Causality    |   2   |     0      |    1     |
|   4   | Subclock     |   2   |     0      |    1     |
|   5   | Exclusion    |   2   |     0      |    1     |
|   6   | Union        |   3   |     1      |    1     |
|   7   | Intersection |   3   |     1      |    1     |
|   8   | Infimum      |   3   |     1      |    1     |
|   9   | Supremum     |   3   |     1      |    1     |
|  10   | Delay        |   2   |     1      |    1     |
|  11   | Periodicity  |   2   |     1      |    1     |

### Benchmarks for CCSL constraints 
We give three specifications as benchmark. For each specification, we give four incomplete specifications and some expected timing behaviors (traces). Our tool can synthesize this incomplete specifications to generate complete specification.
#### example 1
4 clocks, 1 expression constraints, 3 relation constraints  
[Details](https://github.com/HMHelloWorld/CCSLSketch/blob/master/example/benchmark1/Readme.md)  

#### example 2
Settings: 10 clocks, 5 expression constraints, 10 relation constraints  
[Details](https://github.com/HMHelloWorld/CCSLSketch/blob/master/example/benchmark2/Readme.md)  
#### example 3
Settings: 20 clocks, 6 expression constraints, 16 relation constraints  
[Details](https://github.com/HMHelloWorld/CCSLSketch/blob/master/example/benchmark3/Readme.md)  
