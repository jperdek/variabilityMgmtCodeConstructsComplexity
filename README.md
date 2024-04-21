# PROJECT CONTENTS      

- canvas-based SPL written in Angular: ./canvasSPLforSPA  
- NodeJS complexity evaluator server with API: ./astConverterAndComplexityEvaluator  
- the framework for complexity evaluation of variability management constructs: ./QualityChecker  
- scripts to evaluate a significant difference between forms written in R: ./QualityChecker/results/resultAnalysis  
		(used test http://www.sthda.com/english/wiki/paired-samples-wilcoxon-test-in-r)  
	- previously evaluated [in ./QualityChecker/results]:  
		 - complexities for each form and their differences in scenarios in CSVs  
		 - scenarios: differences of complexities between selected forms  
		 - associated compared files for compared forms realized in a particular scenario (form BEFORE and form AFTER)  
		 - Graphs and results in Excel (QQ-plots, scatter-plots, etc.)
	- visual comparison of particular constructs for selected compared forms (realized in scenarios): ./qualityForms  
		- with a note about if the form can be evaluated with complexity library (TyphonJS), compiled  
		- with a note if a given construct supports positive or negative variability    
	


# USAGE AND VISUALIZATION
      
![USAGE AND VISUALIZATION POSTER](https://github.com/jperdek/variabilityMgmtCodeConstructsComplexity/blob/master/codeComplexityPosterEN.png)


# LAUNCH STEPS  

* 1) Unpack the project ZIP to the chosen directory  
	cd chosenDirectory  



## a.) NODE JS COMPLEXITY EVALUATOR   


  * 1.a) Switch to complexity evaluator folder    
	```cd astConverterAndComplexityEvaluator```   


  * 2.a) Install dependecies  
	- you should have npm already installed or install it using https://nodejs.org/en/download/  
	```npm i --save-dev```     


  * 3.a) Substitute the following files from installed libraries (if skipped or set improperly, then Babel will have problems processing some decorators)  
	```cp ./changed defaults/BabelParser.js ./node_modules/@typhonjs/babel-parser/dist/BabelParser.js```     
	```cp ./changed defaults/eslint-patches.js ./node_modules/eslintcc/source/lib/eslint-patches.js```     

	or  

	```copy "changedDefaults\BabelParser.js" "node_modules\@typhonjs\babel-parser\dist\BabelParser.js"```    
	```copy "changedDefaults\eslint-patches.js" "node_modules\eslintcc\source\lib\eslint-patches.js"```   


  * 4.a) Launch server  
	- the default port is set to 5001  
	```npm start```  


  * 5.a) Optionally test functionality with Postman  
	- load testAPI.postman_collection.json  
	- run each of 9 tests and verify outputs (each should return 200 status code)  





## b) FRAMEWORK TO COMPARE CODE-BASED VARIABILITY CONSTRUCTS  

 
  * 1.b) Switch to code-based variability constructs complexity evaluator framework folder  
	```cd QualityChecker```  


  * 2.b) Open project with Eclipse (steps will follow Eclipse, but it can be launched from the command line)  
	- open the project workspace inside the copied projects directory   
	- File -> open project from file system -> directory > QualityChecker -> finish   
	- load used/associated libraries (if are not)   
		- left click on project name in Package Explorer -> properties -> Java Build Path -> libraries -> ModulePath -> add External Jars -> lib (+select all) -> open -> apply and close   
	- add JRE library  
		- left click on project name in Package Explorer -> properties -> Java Build Path -> libraries -> ModulePath -> add Library -> JRE system library -> next -> finish  (I used Java 18.0.1.1)
	- set compiler to newer Java (18 and better)   
		- left click on project name in Package Explorer -> properties -> Java Compiler -> Compiler compliance level > 18 or more > apply > apply and close  


  * 2.b) Set configuration variables to analyze annotated SPL   
	- in [./QualityChecker/src/scenarios/Scenario.java] change line [String pathToProjectTree = "file:///E:/aspects/canvasSPLforSPA/canvasSPLforSPA/src";] to your path ["file:///C:ABSOLUTE PATH TO YOUR/canvasSPLforSPA/canvasSPLforSPA/src"]  
	- in [./QualityChecker/src/SPLComplexityEvaluator.java] change line [String pathToProjectTree = "file:///E:/aspects/canvasSPLforSPA/canvasSPLforSPA/src";] to your path ["file:///C:ABSOLUTE PATH TO YOUR/canvasSPLforSPA/canvasSPLforSPA/src"]  


  * 3.b) Run all forms:  
	- left click on one ./QualityChecker/scenarions/TransformationForms.java -> run > TransformationForms.java    
	launch of specific FORM can be done commenting this lines except demanded one in 
		./QualityChecker/scenarions/TransformationForms.java [also to prevent file overwriting: 
``` 
		TransformationForms.evaluateForm1();  
		TransformationForms.evaluateForm2();  
		TransformationForms.evaluateForm3();  
		TransformationForms.evaluateForm4();  
		TransformationForms.evaluateForm5();  
```

  * 4.b) See results in:  
	- evaluated differences in CSV located in: ./QualityChecker (such as generalAGGREGATETYPHONE.csv) 
	- generated files in two forms (BEFORE and AFTER in file name) in: ./QualityChecker/fileComparison [FOR THIS CASE THEY WILL OVERWRITTEN, but this is useful for Scenarios - this functionality is intended for  comparison]  


  * 5.b) Run specific scenario (1-7):   
	- left click on one of ./QualityChecker/scenarions/scenarioX.java [from ./QualityChecker/scenarions/scenario1.java up to ./QualityChecker/scenarions/scenario7.java] -> run > scenario1.java    


  * 6.b) See results in:  
	- generated files in two forms (AFTER AND BEFORE) in: ./QualityChecker/fileComparison  
	- evaluated differences in CSV located in ./QualityChecker (such as generalAGGREGATETYPHONE.csv)    


  * 7.b) Erase or move created files to launch new scenario according to steps 5-6   






## c) TEST SPL PRODUCT (only optional to verify canvas-baed SPL [use case])  


  * 1.c) Switch to canvas-based SPL folder  
	```cd canvasSPLforSPA```  


  * 2.c) Install dependencies with npm (as in steps for Nodejs server) - in this case --force flag is required to install some dependencies   
	```npm i --save-dev --force```  


  * 3.c) Replace fixed files substituting their previous installed version:  
	```cp ./dependencyFixes/carousel.module.d.ts ./node_modules/@ngbmodule/material-carousel/lib/carousel.module.d.ts```   
	```cp ./dependencyFixes/types/aspectjs/index.d.ts ./node_modules/aspectjs/index.d.ts```   
	```cp ./dependencyFixes/types/toAop/toAop.d.ts ./node_modules/to-aop/dist/toAop.d.ts```  

	or  

	```copy "dependencyFixes\carousel.module.d.ts" "node_modules\@ngbmodule/material-carousel\lib\carousel.module.d.ts"```    
	```copy "dependencyFixes\types\aspectjs\index.d.ts" "node_modules\aspectjs\index.d.ts"```  
	```copy "dependencyFixes\types\toAop\toAop.d.ts" "node_modules\to-aop\dist\toAop.d.ts"```  


  * 4.c) Launch resulting SPL (all set functionality will be available)  
	```npm start```    


