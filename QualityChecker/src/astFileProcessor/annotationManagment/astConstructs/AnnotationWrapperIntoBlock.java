package astFileProcessor.annotationManagment.astConstructs;

import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import astFileProcessor.processors.DecoratorManipulationSettings;
import codeConstructsEvaluation.transformation.ASTConverterClient;


public class AnnotationWrapperIntoBlock {

	protected static int index = 0;
	public static enum AstPlaces {
		STATEMENTS("statements"),
		MEMBERS("members"),
		MODIFIERS("modifiers");
		
		public final String label;

	    private AstPlaces(String label) {
	        this.label = label;
	    }
	}// mostly statements are used
	public static enum DeclarationTypes {
		VAR("var"),
		LET("let"),
		CONST("const"),
		STATIC("static"),
		WITHOUT("");
	
		public final String label;

	    private DeclarationTypes(String label) {
	        this.label = label;
	    }
	}

	public static final String STARTING_WRAPPER_NAME = "EXPRESSION_START";
	public static final String ENDING_WRAPPER_NAME = "EXPRESSION_END";
	public static final String ENDING_WRAPPER_CONTENT = "---------------------------";
	
	
	public AnnotationWrapperIntoBlock() {	
	}
	
	protected static JSONObject getStartingASTWrapperPart(JSONObject expression, 
			int usedIndex, DeclarationTypes usedDeclarationType, 
			DecoratorManipulationSettings decoratorManipulationSettings) throws IOException, InterruptedException {
		JSONObject startingWrapperPart = expression;//new JSONObject();
		//startingWrapperPart.put(AnnotationWrapperIntoBlock.STARTING_WRAPPER_NAME, expression);
		return AnnotationWrapperIntoBlock.generateStartingInitializationAST(
				startingWrapperPart, usedIndex, usedDeclarationType, decoratorManipulationSettings);
	}
	
	protected static JSONObject getTerminatingASTWrapperPart(int usedIndex, 
			DeclarationTypes usedDeclarationType) throws IOException, InterruptedException {
		JSONObject terminatingWrapperPart = new JSONObject();
		terminatingWrapperPart.put(AnnotationWrapperIntoBlock.ENDING_WRAPPER_NAME, 
				AnnotationWrapperIntoBlock.ENDING_WRAPPER_CONTENT);
		return AnnotationWrapperIntoBlock.generateTerminatingInitializationAST(terminatingWrapperPart, usedIndex, usedDeclarationType);
	}
	
	protected static JSONObject generateStartingInitializationAST(JSONObject expression, 
			int usedIndex, DeclarationTypes usedDeclarationType, 
			DecoratorManipulationSettings decoratorManipulationSettings) throws IOException, InterruptedException {
		String initializedExpressionCodeConstruct;
		if (decoratorManipulationSettings.shouldRemoveConfigurationExpressions()) {
			initializedExpressionCodeConstruct = usedDeclarationType.label + " " + AnnotationWrapperIntoBlock.STARTING_WRAPPER_NAME + usedIndex + ";";
		} else {
			initializedExpressionCodeConstruct = usedDeclarationType.label + " " + AnnotationWrapperIntoBlock.STARTING_WRAPPER_NAME + 
					usedIndex + " = " + ASTConverterClient.convertJSONFromAST(expression, decoratorManipulationSettings.formatAnnotationInLine());
		}
		return ASTConverterClient.getFirstStatementFromASTFile(ASTConverterClient.convertFromCodeToASTJSON(initializedExpressionCodeConstruct));
	}
	
	protected static JSONObject generateTerminatingInitializationAST(JSONObject expression, 
			int usedIndex, DeclarationTypes usedDeclarationType) throws IOException, InterruptedException {
		String initializedExpressionCodeConstruct = usedDeclarationType.label + " " + AnnotationWrapperIntoBlock.ENDING_WRAPPER_NAME + 
				usedIndex + " = " + expression;
		return ASTConverterClient.getFirstStatementFromASTFile(ASTConverterClient.convertFromCodeToASTJSON(initializedExpressionCodeConstruct));
	}
	
	public static void constructAnnotationsWrapperIntoBlock(JSONObject expressionInAST, 
			JSONObject parentObject, JSONObject parentWithStatementObject, boolean unwantedCode,
			AstPlaces astPlace, DeclarationTypes usedDeclarationType, DecoratorManipulationSettings decoratorManipulationSettings) throws NotFoundBlockElementToWrap, IOException, InterruptedException {
		JSONArray codeSequence = (JSONArray) parentWithStatementObject.get(astPlace.label); //"statements"
		int numberWrappedIf = 1;
		int indexOfElementInCodeSequence = codeSequence.indexOf(parentObject);
		if (unwantedCode) {
			numberWrappedIf = numberWrappedIf + 1;
		}
	
		if (indexOfElementInCodeSequence < 0) {
			throw new NotFoundBlockElementToWrap("Element in block has not been found!!");
		}
		AnnotationWrapperIntoBlock.constructAnnotationWrapperIntoBlock(
				expressionInAST, codeSequence, indexOfElementInCodeSequence, numberWrappedIf, usedDeclarationType, decoratorManipulationSettings);
	}
	
	public static void constructAnnotationsWrapperIntoBlockNewCode(JSONObject expressionInAST, JSONObject newCodeAST,
			JSONObject parentObject, JSONObject parentWithStatementObject, boolean removePrevious, 
			AstPlaces astPlace, DeclarationTypes usedDeclarationType, DecoratorManipulationSettings decoratorManipulationSettings) throws NotFoundBlockElementToWrap, IOException, InterruptedException{
		JSONArray codeSequence = (JSONArray) parentWithStatementObject.get(astPlace.label); //"statements"
		int numberWrappedIf = 1;
		int indexOfElementInCodeSequence = codeSequence.indexOf(parentObject);

		if (indexOfElementInCodeSequence < 0) {
			throw new NotFoundBlockElementToWrap("Element in block has not been found!!");
		}
		AnnotationWrapperIntoBlock.constructAnnotationWrapperIntoBlockNewCode(
				expressionInAST, newCodeAST, codeSequence, indexOfElementInCodeSequence, 
				numberWrappedIf, removePrevious, usedDeclarationType, decoratorManipulationSettings);
	}
	
	public static void constructAnnotationsWrapperIntoBlockNewCodeStringSearch(JSONObject expressionInAST, JSONObject newCodeAST,
			JSONObject parentObject, JSONObject parentWithStatementObject, boolean removePrevious, 
			AstPlaces astPlace, DeclarationTypes usedDeclarationType, DecoratorManipulationSettings decoratorManipulationSettings) throws NotFoundBlockElementToWrap, IOException, InterruptedException{
		JSONArray codeSequence = (JSONArray) parentWithStatementObject.get(astPlace.label); //"statements"
		int numberWrappedIf = 1;
		int indexOfElementInCodeSequence = -1;
		for (int actualIndex = 0; actualIndex < codeSequence.size(); actualIndex++) {
			if (((JSONObject) codeSequence.get(actualIndex)).toString().equals(parentObject.toString())) {
				indexOfElementInCodeSequence = actualIndex;
				if (removePrevious) {
					codeSequence.remove(parentObject);
				}
				break;
			}
		}

		if (indexOfElementInCodeSequence < 0) {
			throw new NotFoundBlockElementToWrap("Element in block has not been found!!");
		}
		AnnotationWrapperIntoBlock.constructAnnotationWrapperIntoBlockNewCode(
				expressionInAST, newCodeAST, codeSequence, indexOfElementInCodeSequence, 
				numberWrappedIf, removePrevious, usedDeclarationType, decoratorManipulationSettings);
	}
	
	public static void constructAnnotationsWrapperIntoBlockNotRealNewCode(JSONObject expressionInAST, 
			JSONObject parentObject, JSONObject parentWithStatementObject, boolean removePrevious, 
			AstPlaces astPlace, DeclarationTypes usedDeclarationType, DecoratorManipulationSettings decoratorManipulationSettings) throws NotFoundBlockElementToWrap, IOException, InterruptedException {
		JSONArray codeSequence = (JSONArray) parentWithStatementObject.get(astPlace.label); //"statements"
		int numberWrappedIf = 1;
		int indexOfElementInCodeSequence = codeSequence.indexOf(parentObject);;

		if (indexOfElementInCodeSequence < 0) {
			throw new NotFoundBlockElementToWrap("Element in block has not been found!!");
		}
		AnnotationWrapperIntoBlock.constructAnnotationWrapperIntoBlockNewCode(
				expressionInAST, codeSequence, indexOfElementInCodeSequence, numberWrappedIf, 
				removePrevious, usedDeclarationType, decoratorManipulationSettings);
	}
	
	public static void constructAnnotationsWrapperIntoBlockNewCode(JSONObject expressionInAST, JSONArray newCodeAST, 
			JSONObject parentObject, JSONObject parentWithStatementObject, boolean removePrevious, 
			AstPlaces astPlace, DeclarationTypes usedDeclarationType, DecoratorManipulationSettings decoratorManipulationSettings) throws NotFoundBlockElementToWrap, IOException, InterruptedException {
		JSONArray codeSequence = (JSONArray) parentWithStatementObject.get(astPlace.label); //"statements"
		int numberWrappedIf = 1;
		int indexOfElementInCodeSequence = codeSequence.indexOf(parentObject);

		if (indexOfElementInCodeSequence < 0) {
			throw new NotFoundBlockElementToWrap("Element in block has not been found!!");
		}
		AnnotationWrapperIntoBlock.constructAnnotationWrapperIntoBlockNewCode(
				expressionInAST, newCodeAST, codeSequence, indexOfElementInCodeSequence, 
				numberWrappedIf, removePrevious, usedDeclarationType, decoratorManipulationSettings);
	}
	
	public static void constructAnnotationsWrapperIntoBlockNewCodeStringSearch(JSONObject expressionInAST, JSONArray newCodeAST, 
			JSONObject parentObject, JSONObject parentWithStatementObject, boolean removePrevious, 
			AstPlaces astPlace, DeclarationTypes usedDeclarationType, DecoratorManipulationSettings decoratorManipulationSettings) throws NotFoundBlockElementToWrap, IOException, InterruptedException  {
		JSONArray codeSequence = (JSONArray) parentWithStatementObject.get(astPlace.label); //"statements"
		int numberWrappedIf = 1;
		int indexOfElementInCodeSequence = -1;
		for (int actualIndex = 0; actualIndex < codeSequence.size(); actualIndex++) {
			if (((JSONObject) codeSequence.get(actualIndex)).toString().equals(parentObject.toString())) {
				indexOfElementInCodeSequence = actualIndex;
				break;
			}
		}

		if (indexOfElementInCodeSequence < 0) {
			throw new NotFoundBlockElementToWrap("Element in block has not been found!!");
		}
		AnnotationWrapperIntoBlock.constructAnnotationWrapperIntoBlockNewCode(
				expressionInAST, newCodeAST, codeSequence, indexOfElementInCodeSequence, numberWrappedIf,
				removePrevious, usedDeclarationType, decoratorManipulationSettings);
	}
	
	public static void constructAnnotationWrapperIntoBlockNewCode(JSONObject expressionInAST, JSONObject newCodeAST,
			JSONArray codeSequence, int indexOfElementInCodeSequence, 
			int numberWrapped, boolean removePrevious, DeclarationTypes usedDeclarationType, 
			DecoratorManipulationSettings decoratorManipulationSettings) throws IOException, InterruptedException {
		int usedIndex = AnnotationWrapperIntoBlock.index;
		AnnotationWrapperIntoBlock.index = AnnotationWrapperIntoBlock.index + 1;
		JSONObject startingWrapperPart = AnnotationWrapperIntoBlock.getStartingASTWrapperPart(
				expressionInAST, usedIndex, usedDeclarationType, decoratorManipulationSettings);
		JSONObject endingWrapperPart = AnnotationWrapperIntoBlock.getTerminatingASTWrapperPart(usedIndex, usedDeclarationType);

		if (removePrevious) {
			for (int index=indexOfElementInCodeSequence + numberWrapped - 1; index >= indexOfElementInCodeSequence; index--) {
				codeSequence.remove(index);
			}
			numberWrapped = 0;
		}
		codeSequence.add(indexOfElementInCodeSequence, startingWrapperPart);
		codeSequence.add(indexOfElementInCodeSequence + 1, newCodeAST);
		codeSequence.add(indexOfElementInCodeSequence + 2 + numberWrapped, endingWrapperPart);
	}
	
	public static void constructAnnotationWrapperIntoBlockNewCode(JSONObject expressionInAST,
			JSONArray codeSequence, int indexOfElementInCodeSequence, 
			int numberWrapped, boolean removePrevious, DeclarationTypes usedDeclarationType, 
			DecoratorManipulationSettings decoratorManipulationSettings) throws IOException, InterruptedException {
		int usedIndex = AnnotationWrapperIntoBlock.index;
		AnnotationWrapperIntoBlock.index = AnnotationWrapperIntoBlock.index + 1;
		JSONObject startingWrapperPart = AnnotationWrapperIntoBlock.getStartingASTWrapperPart(
				expressionInAST, usedIndex, usedDeclarationType, decoratorManipulationSettings);
		JSONObject endingWrapperPart = AnnotationWrapperIntoBlock.getTerminatingASTWrapperPart(usedIndex, usedDeclarationType);

		if (removePrevious) {
			for (int index=indexOfElementInCodeSequence + numberWrapped - 1; index >= indexOfElementInCodeSequence; index--) {
				codeSequence.remove(index);
			}
			numberWrapped = 0;
		}

		codeSequence.add(indexOfElementInCodeSequence, startingWrapperPart);
		codeSequence.add(indexOfElementInCodeSequence + 1 + numberWrapped, endingWrapperPart);
	}
	
	public static void constructAnnotationWrapperIntoBlockNewCode(JSONObject expressionInAST, JSONArray newCodeAST,
			JSONArray codeSequence, int indexOfElementInCodeSequence, 
			int numberWrapped, boolean removePrevious, DeclarationTypes usedDeclarationType, 
			DecoratorManipulationSettings decoratorManipulationSettings) throws IOException, InterruptedException {
		int usedIndex = AnnotationWrapperIntoBlock.index;
		int actualCloseIndex = indexOfElementInCodeSequence;
		AnnotationWrapperIntoBlock.index = AnnotationWrapperIntoBlock.index + 1;
		JSONObject startingWrapperPart = AnnotationWrapperIntoBlock.getStartingASTWrapperPart(
				expressionInAST, usedIndex, usedDeclarationType, decoratorManipulationSettings);
		JSONObject endingWrapperPart = AnnotationWrapperIntoBlock.getTerminatingASTWrapperPart(usedIndex,usedDeclarationType);
		if (removePrevious) {
			for (int index=indexOfElementInCodeSequence + numberWrapped - 1; index >= indexOfElementInCodeSequence; index--) {
				codeSequence.remove(index);
			}
			numberWrapped = 0;
		}
		codeSequence.add(indexOfElementInCodeSequence, startingWrapperPart);
		for(Object newCodeObject: newCodeAST) {
			actualCloseIndex = actualCloseIndex + 1;
			codeSequence.add(actualCloseIndex, (JSONObject) newCodeObject);
		}
		codeSequence.add(actualCloseIndex + 1 + numberWrapped, endingWrapperPart);
	}
	
	public static void constructAnnotationWrapperIntoBlock(JSONObject expressionInAST, 
			JSONArray codeSequence, int indexOfElementInCodeSequence, 
			int numberWrapped, DeclarationTypes usedDeclarationType, DecoratorManipulationSettings decoratorManipulationSettings) throws IOException, InterruptedException {
		int usedIndex = AnnotationWrapperIntoBlock.index;
		AnnotationWrapperIntoBlock.index = AnnotationWrapperIntoBlock.index + 1;
		JSONObject startingWrapperPart = AnnotationWrapperIntoBlock.getStartingASTWrapperPart(
				expressionInAST, usedIndex, usedDeclarationType, decoratorManipulationSettings);
		JSONObject endingWrapperPart = AnnotationWrapperIntoBlock.getTerminatingASTWrapperPart(usedIndex, usedDeclarationType);
		codeSequence.add(indexOfElementInCodeSequence, startingWrapperPart);
		codeSequence.add(indexOfElementInCodeSequence + 1 + numberWrapped, endingWrapperPart);
	}
}
