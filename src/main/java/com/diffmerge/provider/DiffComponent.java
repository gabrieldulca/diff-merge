package main.java.com.diffmerge.provider;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.match.DefaultComparisonFactory;
import org.eclipse.emf.compare.match.DefaultEqualityHelperFactory;
import org.eclipse.emf.compare.match.DefaultMatchEngine;
import org.eclipse.emf.compare.match.IComparisonFactory;
import org.eclipse.emf.compare.match.IMatchEngine;
import org.eclipse.emf.compare.match.eobject.IEObjectMatcher;
import org.eclipse.emf.compare.match.impl.MatchEngineFactoryImpl;
import org.eclipse.emf.compare.match.impl.MatchEngineFactoryRegistryImpl;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.compare.utils.UseIdentifiers;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.glsp.graph.GGraph;
import org.eclipse.glsp.graph.GModelRoot;
import org.eclipse.glsp.graph.GraphPackage;
import org.eclipse.glsp.graph.gson.EnumTypeAdapter;
import org.eclipse.glsp.graph.gson.GGraphGsonConfigurator;
import static org.eclipse.glsp.graph.GraphPackage.Literals.*;
import org.eclipse.uml2.uml.internal.resource.UMLResourceFactoryImpl;

import org.eclipse.uml2.uml.resources.util.UMLResourcesUtil;
import org.emfjson.jackson.databind.EMFContext;
import org.emfjson.jackson.module.EMFModule;
import org.emfjson.jackson.resource.JsonResourceFactory;
import org.eclipse.glsp.api.di.GLSPModule;
import org.eclipse.glsp.api.factory.GraphGsonConfiguratorFactory;
import org.eclipse.glsp.api.json.*;
import com.fasterxml.jackson.databind.ObjectReader;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.google.inject.Guice;
import com.google.inject.Injector;

import main.java.com.diffmerge.dto.ComparisonDto;
import main.java.com.diffmerge.exception.InvalidParametersException;
import main.java.com.diffmerge.mapper.DiffMapper;
import io.typefox.sprotty.api.HtmlRoot;
import io.typefox.sprotty.api.PreRenderedElement;
import io.typefox.sprotty.api.SCompartment;
import io.typefox.sprotty.api.SEdge;
import io.typefox.sprotty.api.SGraph;
import io.typefox.sprotty.api.SLabel;
import io.typefox.sprotty.api.SModelElement;
import io.typefox.sprotty.api.SNode;

public abstract class DiffComponent {
	
	private String type;
	private DiffMapper mapper;
	private GGraphGsonConfigurator gsonConfigurator;
	
	public abstract ComparisonDto getComparison(String left, String right, String origin) throws InvalidParametersException, IOException;
	
	public ComparisonDto getComparison(String left, String right) throws InvalidParametersException, IOException {
		return getComparison(left, right, null);
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public DiffMapper getMapper() {
		return mapper;
	}

	public void setMapper(DiffMapper mapper) {
		this.mapper = mapper;
	}

	protected Comparison compare(String left, String right, String origin) throws InvalidParametersException, IOException {

		if(left == null || right == null) {
			throw new InvalidParametersException("At least two valid models are required for comparison");
		}
		
		ResourceSet resourceSet1 = new ResourceSetImpl();
		ResourceSet resourceSet2 = new ResourceSetImpl();
		UMLResourcesUtil.init(resourceSet1);
		UMLResourcesUtil.init(resourceSet2);
		resourceSet1.getPackageRegistry().put(GraphPackage.eNS_URI, GraphPackage.eINSTANCE);
		resourceSet2.getPackageRegistry().put(GraphPackage.eNS_URI, GraphPackage.eINSTANCE);

	    
		resourceSet1.createResource(URI.createFileURI(left));
		resourceSet2.createResource(URI.createFileURI(left));

		/*load(left, resourceSet1);
		load(right, resourceSet2);*/

		// Configure EMF Compare
		IEObjectMatcher matcher = DefaultMatchEngine.createDefaultEObjectMatcher(UseIdentifiers.WHEN_AVAILABLE);
		IComparisonFactory comparisonFactory = new DefaultComparisonFactory(new DefaultEqualityHelperFactory());
		IMatchEngine.Factory matchEngineFactory = new MatchEngineFactoryImpl(matcher, comparisonFactory);
	        matchEngineFactory.setRanking(20);
	        IMatchEngine.Factory.Registry matchEngineRegistry = new MatchEngineFactoryRegistryImpl();
	        matchEngineRegistry.add(matchEngineFactory);
		EMFCompare comparator = EMFCompare.builder().setMatchEngineFactoryRegistry(matchEngineRegistry).build();

		// Compare the models
		IComparisonScope scope = null;
		if(origin != null) {
			ResourceSet resourceSet3 = new ResourceSetImpl();
			UMLResourcesUtil.init(resourceSet3);
			//load(origin, resourceSet3);
			scope = EMFCompare.createDefaultScope(loadResource(left), loadResource(right),loadResource(origin));
		} else {
			scope = EMFCompare.createDefaultScope(loadResource(left), loadResource(right));
		}
		
		return comparator.compare(scope);
	}

	private static void load(String absolutePath, ResourceSet resourceSet) {
	  URI uri = URI.createFileURI(absolutePath);

	  resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("uml", new UMLResourceFactoryImpl());
	  resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("xmi", new XMIResourceFactoryImpl());
	  resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("ecore", new EcoreResourceFactoryImpl());
	  resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("wf", new JsonResourceFactory());

	  // Resource will be loaded within the resource set
	  try {
		
		  resourceSet.getResource(uri, true);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private GGraph loadResource(final String path) {
		gsonConfigurator = new GGraphGsonConfigurator().withDefaultTypes();
	      gsonConfigurator.withTypes(getModelTypes());

	      GsonBuilder builder = new GsonBuilder();

		Gson gson = gsonConfigurator.configureGsonBuilder(builder).create();
	    JsonReader jsonReader = null;
	    GGraph ggraph = null;
		try {
			jsonReader = new JsonReader(new FileReader(path));
			ggraph = gson.fromJson(jsonReader, GGraph.class);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    return ggraph;
   }
	
	public Map<String, EClass> getModelTypes() {
		Map<String, EClass> conf = new HashMap<>();

		conf.put("graph", GGRAPH);
		conf.put("label", GLABEL);
		conf.put("label:heading", GLABEL);
		conf.put("label:text", GLABEL);
		conf.put("comp", GCOMPARTMENT);
		conf.put("comp:comp", GCOMPARTMENT);
		conf.put("comp:header", GCOMPARTMENT);
		conf.put("label:icon", GLABEL);
		conf.put("edge", GEDGE);
		conf.put("edge:weighted", GEDGE);
		conf.put("icon", GCOMPARTMENT);
		conf.put("task:manual", GNODE);
		conf.put("task:automated", GNODE);
		conf.put("activityNode:merge", GNODE);
		conf.put("activityNode:decision", GNODE);
		conf.put("activityNode:fork", GNODE);
		conf.put("activityNode:join", GNODE);
		return conf;

	}
	

}
