package astFileProcessor.annotationManagment.astConstructs;

import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import astFileProcessor.processors.DecoratorManipulationSettings;
import codeConstructsEvaluation.transformation.ASTConverterClient;


public class AnnotationAlternativeWrapperIntoBlock extends AnnotationWrapperIntoBlock {

	public static final String SEPARATOR_NAME = "ELSE";
	public static final String SEPARATING_CONTENT = "~~~~~~~~~~~~~~~~~~~~~~~~~";

	
	public AnnotationAlternativeWrapperIntoBlock() {
		super();
	}
	
	protected static JSONObject getSeparatingPart(int usedIndex, DeclarationTypes usedDeclarationType) throws IOException, InterruptedException {
		JSONObject separatingWrapperPart = new JSONObject();
		separatingWrapperPart.put(AnnotationAlternativeWrapperIntoBlock.SEPARATOR_NAME, 
				AnnotationAlternativeWrapperIntoBlock.SEPARATING_CONTENT);
		return AnnotationAlternativeWrapperIntoBlock.generateSeparatingInitializationAST(separatingWrapperPart, usedIndex, usedDeclarationType);
	}
	
	protected static JSONObject generateSeparatingInitializationAST(JSONObject expression, int usedIndex, DeclarationTypes usedDeclarationType) throws IOException, InterruptedException {
		String initializedExpressionCodeConstruct = usedDeclarationType.label + " " + AnnotationAlternativeWrapperIntoBlock.SEPARATOR_NAME  + 
				usedIndex + " = " + expression;
		return  ASTConverterClient.getFirstStatementFromASTFile(ASTConverterClient.convertFromCodeToASTJSON(
				initializedExpressionCodeConstruct));
	}

	public static void constructAnnotationsWrapperIntoAlternativeBlock(JSONObject expressionInAST, JSONObject alternativeCodeSequence, 
			JSONObject parentObject, JSONObject parentWithStatementObject, 
			DeclarationTypes usedDeclarationType, DecoratorManipulationSettings decoratorManipulationSettings) 
					                                      throws NotFoundBlockElementToWrap, IOException, InterruptedException {
		JSONArray codeSequence = (JSONArray) parentWithStatementObject.get("statements");
		JSONArray alternativeStatements = (JSONArray) alternativeCodeSequence.get("statements");
		int numberWrappedIf = 1;
		int indexOfElementInCodeSequence = codeSequence.indexOf(parentObject);;
		int numberWrappedElse = alternativeStatements.size();
		if (numberWrappedElse < 1) {
			throw new NotFoundBlockElementToWrap("Nothing to wrap!");
		}
		if (indexOfElementInCodeSequence < 0) {
			throw new NotFoundBlockElementToWrap("Element in block has not been found!!");
		}
		NearestAnnotatedVariablesMerger.transformAlternativeNonConflictingDeclarations(alternativeCodeSequence, parentObject);
		AnnotationAlternativeWrapperIntoBlock.constructAnnotationWrapperIntoAlternativeBlock(
				expressionInAST, codeSequence, alternativeCodeSequence, indexOfElementInCodeSequence, 
				numberWrappedIf, numberWrappedElse,usedDeclarationType, decoratorManipulationSettings);
	}

	public static void constructAnnotationWrapperIntoAlternativeBlock(JSONObject expressionInAST, 
			JSONArray codeSequence, JSONObject alternativeCodeSequence, int indexOfElementInCodeSequence,
			int numberWrappedIf, int numberWrappedElse, 
			DeclarationTypes usedDeclarationType, DecoratorManipulationSettings decoratorManipulationSettings) throws IOException, InterruptedException {
		int usedIndex = AnnotationWrapperIntoBlock.index;
		AnnotationWrapperIntoBlock.index = AnnotationWrapperIntoBlock.index + 1;
		JSONObject startingWrapperPart = AnnotationAlternativeWrapperIntoBlock.getStartingASTWrapperPart(
				expressionInAST, usedIndex, usedDeclarationType, decoratorManipulationSettings);
		JSONObject separatingPart =  AnnotationAlternativeWrapperIntoBlock.getSeparatingPart(usedIndex, usedDeclarationType);
		JSONObject endingWrapperPart = AnnotationAlternativeWrapperIntoBlock.getTerminatingASTWrapperPart(usedIndex, usedDeclarationType);
		codeSequence.add(indexOfElementInCodeSequence, startingWrapperPart);
		codeSequence.add(indexOfElementInCodeSequence + 1 + numberWrappedIf, separatingPart); //numberWrappedIf ==1
		codeSequence.add(indexOfElementInCodeSequence + 1 + numberWrappedIf + numberWrappedElse, alternativeCodeSequence); //numberWrappedElse ==1
		codeSequence.add(indexOfElementInCodeSequence + 2 + numberWrappedIf + numberWrappedElse , endingWrapperPart);
	}
}
