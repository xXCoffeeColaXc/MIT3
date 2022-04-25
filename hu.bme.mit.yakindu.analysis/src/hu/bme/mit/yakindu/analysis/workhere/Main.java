package hu.bme.mit.yakindu.analysis.workhere;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.junit.Test;
import org.yakindu.sct.model.sgraph.State;
import org.yakindu.sct.model.sgraph.Statechart;
import org.yakindu.sct.model.sgraph.Transition;
import org.yakindu.sct.model.stext.stext.EventDefinition;
import org.yakindu.sct.model.stext.stext.VariableDefinition;

import hu.bme.mit.model2gml.Model2GML;
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
		int id = 0;
		System.out.println("public static void print(IExampleStatemachine s) {");
		while (iterator.hasNext()) {
			EObject content = iterator.next();
			/*if(content instanceof State) {
				State state = (State) content;
				if(state.getName() != "" && state.getName() != null) {
					System.out.println(state.getName());
				} else {
					String name = "UnknownName" + id++;
					System.out.println(name);
					state.setName();
				}
				
				if(state.getOutgoingTransitions().isEmpty()) {
					System.out.println(state.getName() + " is a trap state");
				}
			}
			if(content instanceof Transition) {
				Transition t = (Transition) content;
				System.out.println(t.getSource().getName() + " -> " + t.getTarget().getName());
			}
			if(content instanceof EventDefinition) {
				EventDefinition ed = (EventDefinition) content;
				System.out.println(ed.getName());
			}
			*/
			
			if(content instanceof VariableDefinition) {
				VariableDefinition vd = (VariableDefinition) content;
				System.out.println("\tSystem.out.println(\""+ vd.getName().toUpperCase().charAt(0) +
						" = \" + s.getSCInterface().get"+ vd.getName().toUpperCase().charAt(0) + vd.getName().substring(1) +"());");
			}
			
			
			
		}
		System.out.println("}");
		// Transforming the model into a graph representation
		String content = model2gml.transform(root);
		// and saving it
		manager.saveFile("model_output/graph.gml", content);
	}
}
