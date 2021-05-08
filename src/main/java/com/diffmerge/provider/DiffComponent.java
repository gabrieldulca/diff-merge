package main.java.com.diffmerge.provider;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.util.BasicMonitor;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.internal.spec.ReferenceChangeSpec;
import org.eclipse.emf.compare.match.DefaultComparisonFactory;
import org.eclipse.emf.compare.match.DefaultEqualityHelperFactory;
import org.eclipse.emf.compare.match.DefaultMatchEngine;
import org.eclipse.emf.compare.match.IComparisonFactory;
import org.eclipse.emf.compare.match.IMatchEngine;
import org.eclipse.emf.compare.match.eobject.IEObjectMatcher;
import org.eclipse.emf.compare.match.impl.MatchEngineFactoryImpl;
import org.eclipse.emf.compare.match.impl.MatchEngineFactoryRegistryImpl;
import org.eclipse.emf.compare.merge.AttributeChangeMerger;
import org.eclipse.emf.compare.merge.BatchMerger;
import org.eclipse.emf.compare.merge.IBatchMerger;
import org.eclipse.emf.compare.merge.IMerger;
import org.eclipse.emf.compare.merge.ReferenceChangeMerger;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.compare.utils.UseIdentifiers;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.glsp.graph.GCompartment;
import org.eclipse.glsp.graph.GDimension;
import org.eclipse.glsp.graph.GGraph;
import org.eclipse.glsp.graph.GModelElement;
import org.eclipse.glsp.graph.GModelRoot;
import org.eclipse.glsp.graph.GNode;
import org.eclipse.glsp.graph.GPoint;
import org.eclipse.glsp.graph.GraphPackage;
import org.eclipse.glsp.graph.gson.EnumTypeAdapter;
import org.eclipse.glsp.graph.gson.GGraphGsonConfigurator;
import org.eclipse.glsp.graph.impl.GCompartmentImpl;
import org.eclipse.glsp.graph.impl.GGraphImpl;
import org.eclipse.glsp.graph.impl.GNodeImpl;

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
import com.google.common.collect.Lists;
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

	public abstract ComparisonDto getComparison(String left, String right, String origin)
			throws InvalidParametersException, IOException;

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
	
	protected Comparison mergeSingleChange(String left, String right, String origin, String elem, boolean revert)
			throws InvalidParametersException, IOException {
		if (left == null || right == null) {
			throw new InvalidParametersException("At least two valid models are required for comparison");
		}

		ResourceSet resourceSet1 = new ResourceSetImpl();
		ResourceSet resourceSet2 = new ResourceSetImpl();
		UMLResourcesUtil.init(resourceSet1);
		UMLResourcesUtil.init(resourceSet2);
		resourceSet1.getPackageRegistry().put(GraphPackage.eNS_URI, GraphPackage.eINSTANCE);
		resourceSet2.getPackageRegistry().put(GraphPackage.eNS_URI, GraphPackage.eINSTANCE);

		resourceSet1.createResource(URI.createFileURI(left));
		resourceSet2.createResource(URI.createFileURI(right));

		/*
		 * load(left, resourceSet1); load(right, resourceSet2);
		 */

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
		if (origin != null) {
			ResourceSet resourceSet3 = new ResourceSetImpl();
			UMLResourcesUtil.init(resourceSet3);
			// load(origin, resourceSet3);
			scope = EMFCompare.createDefaultScope(loadResource(left), loadResource(right), loadResource(origin));
		} else {
			if(revert == false) {
				String tmp = left;
				left = right;
				right = tmp;
			}
			scope = EMFCompare.createDefaultScope(loadResource(left), loadResource(right));
		}
		
		gsonConfigurator = new GGraphGsonConfigurator().withDefaultTypes();
		gsonConfigurator.withTypes(getModelTypes());

		GsonBuilder builder = new GsonBuilder().setPrettyPrinting();

		Gson gson = gsonConfigurator.configureGsonBuilder(builder).create();
		
		String jsonInString = gson.toJson((GGraphImpl) scope.getRight());
		File unmergedFile = new File(right.replaceAll(".wf", "") + "_UNMERGED.wf");
		if (!unmergedFile.exists()){
			Writer writer = new FileWriter(unmergedFile);
			gson.toJson((GGraphImpl) scope.getRight(), writer);
	        
	        writer.flush(); //flush data to file   <---
		
		}
		

		List<Match> submatches = new ArrayList<Match>();
		Comparison comparison = comparator.compare(scope);
		Match ggraph = null;
		for (Match match : comparison.getMatches()) {
			if (match.getLeft() != null) {
				if (match.getLeft() instanceof GGraphImpl) {
					submatches = match.getSubmatches();
					break;
				}
			}
		}
		
		List<Diff> toBeMerged = new ArrayList<>();
		for(Diff d: comparison.getDifferences()) {
			if(d instanceof ReferenceChangeSpec) {
				ReferenceChangeSpec r = (ReferenceChangeSpec)d;
				if(r.getValue() instanceof GModelElement) {
					if(((GModelElement)r.getValue()).getId().equals(elem)) {
						toBeMerged.add(d);
						break;
					}
				}
				
			}
		}

		for(Match sm: submatches) {
			if(sm.getLeft() instanceof GModelElement &&((GModelElement)sm.getLeft()).getId().equals(elem)) {
				toBeMerged.addAll(Lists.newArrayList(sm.getAllDifferences()));
				break;
			}
		}
		

		IMerger merger = new ReferenceChangeMerger();
		IMerger attributeMerger = new AttributeChangeMerger();
		Notifier rightResource = scope.getRight();
		Notifier leftResource = scope.getLeft();
		try {
			for(Diff tbm: toBeMerged) {
				if(merger.isMergerFor(tbm)) {
					ReferenceChangeSpec ref = (ReferenceChangeSpec)tbm;
					try {
						merger.copyLeftToRight(tbm, new BasicMonitor());
					} catch(NullPointerException e) {
						System.out.println(ref.getValue());
						e.printStackTrace();
					}
				} else if(attributeMerger.isMergerFor(tbm)) {
					attributeMerger.copyLeftToRight(tbm, new BasicMonitor());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (rightResource instanceof GGraphImpl && leftResource instanceof GGraphImpl) {
			
			jsonInString = gson.toJson((GGraphImpl) rightResource);
			FileWriter writer = new FileWriter(right);
			gson.toJson((GGraphImpl) rightResource, writer);
			
		    
	        
	        writer.flush(); //flush data to file   <---
	        writer.close();
		}

		// check that models are equal after batch merging
		Comparison assertionComparison = EMFCompare.builder().build().compare(scope);
		EList<Diff> assertionDifferences = assertionComparison.getDifferences();
		System.out.println("after batch merging: " + assertionDifferences.size());
		return assertionComparison;
	}

	protected Comparison merge(String left, String right, String origin)
			throws InvalidParametersException, IOException {
		if (left == null || right == null) {
			throw new InvalidParametersException("At least two valid models are required for comparison");
		}

		ResourceSet resourceSet1 = new ResourceSetImpl();
		ResourceSet resourceSet2 = new ResourceSetImpl();
		UMLResourcesUtil.init(resourceSet1);
		UMLResourcesUtil.init(resourceSet2);
		resourceSet1.getPackageRegistry().put(GraphPackage.eNS_URI, GraphPackage.eINSTANCE);
		resourceSet2.getPackageRegistry().put(GraphPackage.eNS_URI, GraphPackage.eINSTANCE);

		resourceSet1.createResource(URI.createFileURI(left));
		resourceSet2.createResource(URI.createFileURI(right));

		/*
		 * load(left, resourceSet1); load(right, resourceSet2);
		 */

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
		if (origin != null) {
			ResourceSet resourceSet3 = new ResourceSetImpl();
			UMLResourcesUtil.init(resourceSet3);
			// load(origin, resourceSet3);
			scope = EMFCompare.createDefaultScope(loadResource(left), loadResource(right), loadResource(origin));
		} else {
			scope = EMFCompare.createDefaultScope(loadResource(left), loadResource(right));
		}

		List<Diff> differences = new ArrayList<Diff>();
		Comparison comparison = comparator.compare(scope);
		for (Match match : comparison.getMatches()) {
			if (match.getLeft() != null) {
				//if (match.getLeft() instanceof GGraphImpl) {
					//differences.addAll(match.getDifferences());
				differences = match.getDifferences();
					//break;
				//}
			}
		}
		for (Diff diff : differences) {
			System.out.println(diff.toString());
		}

		// Let's merge every single diff
		// IMerger.Registry mergerRegistry = new IMerger.RegistryImpl();
		// IMerger.Registry mergerRegistry =
		// IMerger.RegistryImpl.createStandaloneInstance();
		IMerger merger = new ReferenceChangeMerger();
		IMerger attributeMerger = new AttributeChangeMerger();
		
		try {
			for (Diff diff : differences) {
				//copyChildren(merger, diff, comparison.getDifferences());
				if(merger.isMergerFor(diff)) {
					merger.copyLeftToRight(diff, new BasicMonitor());
				} else if(attributeMerger.isMergerFor(diff)) {
					attributeMerger.copyLeftToRight(diff, new BasicMonitor());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Notifier rightResource = scope.getRight();
		if (rightResource instanceof GGraphImpl) {
			gsonConfigurator = new GGraphGsonConfigurator().withDefaultTypes();
			gsonConfigurator.withTypes(getModelTypes());

			GsonBuilder builder = new GsonBuilder().setPrettyPrinting();

			Gson gson = gsonConfigurator.configureGsonBuilder(builder).create();
			String jsonInString = gson.toJson((GGraphImpl) rightResource);
			//Writer writer = new FileWriter(left.replaceAll(".wf", "") + "_MERGED.wf");
			Writer writer = new FileWriter(right.replaceAll(".wf", "") + "_MERGED.wf");

	        gson.toJson((GGraphImpl) rightResource, writer);
	        writer.flush(); //flush data to file   <---
	        writer.close();
		}

		// check that models are equal after batch merging
		Comparison assertionComparison = EMFCompare.builder().build().compare(scope);
		EList<Diff> assertionDifferences = assertionComparison.getDifferences();
		System.out.println("after batch merging: " + assertionDifferences.size());
		return assertionComparison;
	}

	private void copyChildren(IMerger merger, Diff diff, List<Diff> allDifferences) {
		if(diff == null) {
			return;
		}
		merger.copyLeftToRight(diff, new BasicMonitor());
		if(diff instanceof ReferenceChangeSpec) {
			if(((ReferenceChangeSpec) diff).getValue() instanceof GNodeImpl) {
				GNodeImpl diffNode = (GNodeImpl) ((ReferenceChangeSpec) diff).getValue();
				for(GModelElement child:diffNode.getChildren()) {
					copyChildren(merger, findDifference(child, allDifferences), allDifferences);
				}
				copyChildren(merger, findDifference(diffNode.getPosition(), allDifferences), allDifferences);
				copyChildren(merger, findDifference(diffNode.getSize(), allDifferences), allDifferences);
			} else if(((ReferenceChangeSpec) diff).getValue() instanceof GCompartment) {
				GCompartment diffNode = (GCompartment) ((ReferenceChangeSpec) diff).getValue();
				for(GModelElement child:diffNode.getChildren()) {
					copyChildren(merger, findDifference(child, allDifferences), allDifferences);
				} 
				
				
			}
			//TODO go through hierarchy till point
		}
	}
	private Diff findDifference(GDimension child, List<Diff> allDifferences) {
		if(child == null)
			return null;
		for(Diff d: allDifferences) {
			if(d instanceof ReferenceChangeSpec) {
				if(((ReferenceChangeSpec) d).getValue().getClass().equals(child.getClass())) {
					if(((ReferenceChangeSpec) d).getValue().equals(child)) {
							return d;
					}
				}
			}
		}
			return null;
	}
	
	private Diff findDifference(GPoint child, List<Diff> allDifferences) {
		if(child == null)
			return null;
		for(Diff d: allDifferences) {
			if(d instanceof ReferenceChangeSpec) {
				if(((ReferenceChangeSpec) d).getValue().getClass().equals(child.getClass())) {
					if(((ReferenceChangeSpec) d).getValue().equals(child)) {
							return d;
					}
				}
			}
		}
			return null;
	}
	
	private Diff findDifference(GModelElement child, List<Diff> allDifferences) {
		if(child == null)
			return null;
		for(Diff d: allDifferences) {
			if(d instanceof ReferenceChangeSpec) {
				if(((ReferenceChangeSpec) d).getValue().getClass().equals(child.getClass())) {
					if(((ReferenceChangeSpec) d).getValue().equals(child)) {
							return d;
					}
				}
			}
		}
			return null;
	}

	protected Comparison compare(String left, String right, String origin)
			throws InvalidParametersException, IOException {

		if (left == null || right == null) {
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

		/*
		 * load(left, resourceSet1); load(right, resourceSet2);
		 */

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
		if (origin != null) {
			ResourceSet resourceSet3 = new ResourceSetImpl();
			UMLResourcesUtil.init(resourceSet3);
			// load(origin, resourceSet3);
			scope = EMFCompare.createDefaultScope(loadResource(left), loadResource(right), loadResource(origin));
		} else {
			scope = EMFCompare.createDefaultScope(loadResource(left), loadResource(right));
		}

		return comparator.compare(scope);
	}

	private static void load(String absolutePath, ResourceSet resourceSet) {
		URI uri = URI.createFileURI(absolutePath);

		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("uml", new UMLResourceFactoryImpl());
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("xmi", new XMIResourceFactoryImpl());
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("ecore",
				new EcoreResourceFactoryImpl());
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("wf", new JsonResourceFactory());

		// Resource will be loaded within the resource set
		try {

			resourceSet.getResource(uri, true);
		} catch (Exception e) {
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

	public abstract ComparisonDto getMerge(String left, String right, String origin)
			throws InvalidParametersException, IOException;

	public ComparisonDto getMerge(String left, String right) throws InvalidParametersException, IOException {
		return getMerge(left, right, null);
	}

	public abstract ComparisonDto getMergeSingleChange(String left, String right, String origin, String elem,
			boolean revert);

	public void getSave(String left, String right, String origin) {
		try {
			Files.deleteIfExists(Paths.get(left.replace(".wf", "_UNMERGED.wf").substring(1)));
			Files.deleteIfExists(Paths.get(right.replace(".wf", "_UNMERGED.wf").substring(1)));
		
		if(origin != null) {
			Files.deleteIfExists(Paths.get(origin.replace(".wf", "_UNMERGED.wf").substring(1)));
		}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void getRevert(String left, String right, String origin) {
		revertFile(left);
		revertFile(right);
		if(origin != null) {
			revertFile(origin);
		}
	}
	
	public void revertFile(String filePath) {
		Path toBeCopied = Paths.get(filePath.replace(".wf", "_UNMERGED.wf").substring(1));
	    Path destination = Paths.get(filePath.substring(1));
	    try {
	    	if(Files.exists(toBeCopied)) {
	    		Files.copy(toBeCopied, destination, StandardCopyOption.REPLACE_EXISTING);
	    		Files.deleteIfExists(toBeCopied);
	    	}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
