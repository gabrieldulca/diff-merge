package main.java.com.diffmerge.controller;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

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
import org.eclipse.uml2.uml.internal.resource.UMLResourceFactoryImpl;
import org.eclipse.uml2.uml.resources.util.UMLResourcesUtil;

import main.java.com.diffmerge.dto.ComparisonDto;
import main.java.com.diffmerge.service.DiffService;


@Path("/diff")
public class DiffController {

	    @GET
	    @Path("test")
	    @Produces(MediaType.TEXT_PLAIN)
	    public String test() {
	    	DiffService diffService = new DiffService();
	    	diffService.computeDifferences();
	        return "Test";
	    }
	    
	    @GET
	    @Path("compare")
	    @Produces(MediaType.APPLICATION_JSON)
	    public ComparisonDto testComp() {
	    	DiffService diffService = new DiffService();
	    	
	        return diffService.sendComparison();
	    }

	    public static Comparison compare() {
			// Load the two input models
			ResourceSet resourceSet1 = new ResourceSetImpl();
			ResourceSet resourceSet2 = new ResourceSetImpl();
			UMLResourcesUtil.init(resourceSet1);
			UMLResourcesUtil.init(resourceSet2);
			
			String xmi1 = "src/main/resources/My.uml";
			String xmi2 = "src/main/resources/My2.uml";
			load(xmi1, resourceSet1);
			load(xmi2, resourceSet2);

			// Configure EMF Compare
			IEObjectMatcher matcher = DefaultMatchEngine.createDefaultEObjectMatcher(UseIdentifiers.WHEN_AVAILABLE);
			IComparisonFactory comparisonFactory = new DefaultComparisonFactory(new DefaultEqualityHelperFactory());
			IMatchEngine.Factory matchEngineFactory = new MatchEngineFactoryImpl(matcher, comparisonFactory);
		        matchEngineFactory.setRanking(20);
		        IMatchEngine.Factory.Registry matchEngineRegistry = new MatchEngineFactoryRegistryImpl();
		        matchEngineRegistry.add(matchEngineFactory);
			EMFCompare comparator = EMFCompare.builder().setMatchEngineFactoryRegistry(matchEngineRegistry).build();

			// Compare the two models
			IComparisonScope scope = EMFCompare.createDefaultScope(resourceSet1, resourceSet2);
			return comparator.compare(scope);
		}

		private static void load(String absolutePath, ResourceSet resourceSet) {
		  URI uri = URI.createFileURI(absolutePath);

		  resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("uml", new UMLResourceFactoryImpl());

		  // Resource will be loaded within the resource set
		  resourceSet.getResource(uri, true);
		}
	
}
