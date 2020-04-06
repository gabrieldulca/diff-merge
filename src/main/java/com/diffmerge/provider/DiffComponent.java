package main.java.com.diffmerge.provider;

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
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.glsp.graph.GModelRoot;
import org.eclipse.glsp.graph.GraphPackage;
import org.eclipse.uml2.uml.internal.resource.UMLResourceFactoryImpl;
import org.eclipse.uml2.uml.resources.util.UMLResourcesUtil;

import main.java.com.diffmerge.dto.ComparisonDto;
import main.java.com.diffmerge.exception.InvalidParametersException;
import main.java.com.diffmerge.mapper.DiffMapper;

public abstract class DiffComponent {
	
	private String type;
	private DiffMapper mapper;
	
	public abstract ComparisonDto getComparison(String left, String right, String origin) throws InvalidParametersException;
	
	public ComparisonDto getComparison(String left, String right) throws InvalidParametersException {
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

	protected Comparison compare(String left, String right, String origin) throws InvalidParametersException {

		if(left == null || right == null) {
			throw new InvalidParametersException("At least two valid models are required for comparison");
		}
		
		ResourceSet resourceSet1 = new ResourceSetImpl();
		ResourceSet resourceSet2 = new ResourceSetImpl();
		UMLResourcesUtil.init(resourceSet1);
		UMLResourcesUtil.init(resourceSet2);
		resourceSet1.getPackageRegistry().put(GraphPackage.eNS_URI, GraphPackage.eINSTANCE);
		resourceSet2.getPackageRegistry().put(GraphPackage.eNS_URI, GraphPackage.eINSTANCE);
		load(left, resourceSet1);
		load(right, resourceSet2);

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
			load(origin, resourceSet3);
			scope = EMFCompare.createDefaultScope(resourceSet1, resourceSet2, resourceSet3);
		} else {
			scope = EMFCompare.createDefaultScope(resourceSet1, resourceSet2);
		}
		
		return comparator.compare(scope);
	}

	private static void load(String absolutePath, ResourceSet resourceSet) {
	  URI uri = URI.createFileURI(absolutePath);

	  resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("uml", new UMLResourceFactoryImpl());
	  resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("xmi", new XMIResourceFactoryImpl());
	  resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("ecore", new EcoreResourceFactoryImpl());
	  resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("wf", new EcoreResourceFactoryImpl());// TODO change to JSONRresourceFaxtory

	  // Resource will be loaded within the resource set
	  resourceSet.getResource(uri, true);
	}
	

}
