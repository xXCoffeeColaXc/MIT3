package hu.bme.mit.yakindu.analysis.workhere;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.junit.Test;
import org.yakindu.sct.model.sgraph.State;
import org.yakindu.sct.model.sgraph.Statechart;
import org.yakindu.sct.model.sgraph.Transition;
import org.yakindu.sct.model.stext.stext.EventDefinition;
import org.yakindu.sct.model.stext.stext.VariableDefinition;

import hu.bme.mit.model2gml.Model2GML;
import hu.bme.mit.yakindu.analysis.RuntimeService;
import hu.bme.mit.yakindu.analysis.TimerService;
import hu.bme.mit.yakindu.analysis.example.ExampleStatemachine;
import hu.bme.mit.yakindu.analysis.example.IExampleStatemachine;
import hu.bme.mit.yakindu.analysis.modelmanager.ModelManager;

public class Main {
	@Test
	public void test() {
		main(new String[0]);
	}
	
	public static void main(String[] args) {
		ModelManager manager = new ModelManager();
		Model2GML model2gml = new Model2GML();
		
		// Loading model
		EObject root = manager.loadModel("model_input/example.sct");
		
		// Reading model
		Statechart s = (Statechart) root;
		TreeIterator<EObject> iterator = s.eAllContents();
		
		List<VariableDefinition> vars = new ArrayList<>();
		List<EventDefinition> events = new ArrayList<>();
		
		System.out.println("public class RunStatechart {\n" + 
				"\n" + 
				"	public static void main(String[] args) throws IOException {\n" + 
				"		ExampleStatemachine s = new ExampleStatemachine();\n" + 
				"		s.setTimer(new TimerService());\n" + 
				"		RuntimeService.getInstance().registerStatemachine(s, 200);\n" + 
				"		s.init();\n" + 
				"		s.enter();\n" + 
				"		s.runCycle();\n" + 
				"		\n" + 
				"		Scanner sc = new Scanner(System.in);\n" + 
				"		boolean exit = false;\n" + 
				"		while(!exit) {\n" + 
				"			String line = sc.nextLine();\n" +
				"			switch(line) {"
				);
		
		while (iterator.hasNext()) {
			EObject content = iterator.next();

			if(content instanceof VariableDefinition) {
				VariableDefinition vd = (VariableDefinition) content;
				vars.add(vd);
			}
			if(content instanceof EventDefinition) {
				EventDefinition ed = (EventDefinition) content;
				events.add(ed);
			}
		}
		
		for(EventDefinition event: events) {
			System.out.println("			case: \"" + event.getName() + "\":");
			System.out.println("				s.raise" + event.getName().toUpperCase().charAt(0) + event.getName().substring(1) + "();");
			System.out.println("				break;");
		}
		
		System.out.println(
				"			case \"exit\":\n" + 
				"				exit = true;\n" + 
				"				break;\n" + 
				"			default: break;\n" + 
				"			}\n" + 
				"		print(s);\n" + 
				"		s.runCycle();\n" + 
				"		}\n" + 
				"	System.exit(0);\n" + 
				"}"
				);
		
		System.out.println("public static void print(IExampleStatemachine s) {");
		for(VariableDefinition var: vars) {
			System.out.println("\tSystem.out.println(\""+ var.getName().toUpperCase().charAt(0) +
					" = \" + s.getSCInterface().get"+ var.getName().toUpperCase().charAt(0) + var.getName().substring(1) +"());");
		}
		System.out.println("}");
		
		// Transforming the model into a graph representation
		String content = model2gml.transform(root);
		// and saving it
		manager.saveFile("model_output/graph.gml", content);
	}
}
