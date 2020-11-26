# CCSLSketch
A Tool for Clock Constraint Specification Language(CCSL)  Synthesis 

## Workflow
Firstly, we parse the incomplete CCSL constraints and traces from xml file to our datastructure
Secondly, we encode CCSL synthesis problem into sketching problem.
Finally, we using program synthesis tool SKETCH to synthesize incomplete CCSL specification.

## Structure
CCSLSketch  
   │──CCSLModel  
   │   │──Clock.java  
   │   │──Expression.java  
   │   │──Relation.java  
   │   │──RelationMap.java  
   │   │──RelationMapItem.java  
   │   │──Varible.java  
   │   └──Trace.java  
   ├──Parser  
   │   │──ClockPaser.java  
   │   │──ExpressionPaser.java  
   │   │──RelationPaser.java  
   │   │──TracePaser.java  
   │   └──XMLFilePaser.java  
   ├──Encoder  
   │   │──CCSLEncoder.java  
   │   │──CCSLFunctionEncoder.java  
   │   │──CheckFuncEncoder.java  
   │   └──HarnessEncoder.java  
   │──CCSLSKetchConfigure.java  
   │──FileTool.java  
   │──SketchSynthesizer.java  
   └──Main.java
   
## Examples
We give three specifications as benchmark. For each specification, we give four incomplete specifications and some expected timing behavior (traces). Our tool can synthesize this incomplete specifications to generate complete specification.
### Benchmark 1
4 clocks
1 expression constraints
3 relation constraints
### Benchmark 2
10 clocks
5 expression constraints
10 relation constraints
### Benchmark 3
20 clocks
6 expression constraints
16 relation constraints
