package main.java.com.diffmerge.controller;
import java.util.Collections;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import main.java.com.diffmerge.dto.ComparisonDto;
import main.java.com.diffmerge.service.DiffManagerService;
import main.java.com.diffmerge.service.DiffService;


@Path("/diff")
public class DiffController {
	
	    private static final String RESOURCE_PATH = "src/main/resources/";

	    @GET
	    @Path("compareold/{representation}")
	    @Produces(MediaType.APPLICATION_JSON)
	    public ComparisonDto testComp(@PathParam("representation") String representation) throws Exception {
	    	DiffService diffService = new DiffService();
	    	
	        return diffService.sendComparison(representation);
	    }
	    
	    @GET
	    @Path("compareecore/{type}/{example}")
	    @Produces(MediaType.APPLICATION_JSON)
	    public ComparisonDto getDiff(@PathParam("type") String type, @PathParam("example") String example) throws Exception {
	    	String example1 = RESOURCE_PATH + example + "/" + example + ".ecore";
	    	String example2 = RESOURCE_PATH + example + "/changes/" + example + ".ecore";
	    	
	    	DiffManagerService diffManagerService = DiffManagerService.getInstance();
	    	
	        return diffManagerService.getDiff(example2, example1, null, type);
	    }
	    
	    @GET
	    @Path("compare3w/{type}/{example}")
	    @Produces(MediaType.APPLICATION_JSON)
	    public ComparisonDto getDiff3w(@PathParam("type") String type, @PathParam("example") String example) throws Exception {
	    	String example1 = RESOURCE_PATH + example + "/base/" + example + ".ecore";
	    	String example2 = RESOURCE_PATH + example + "/changes1/" + example + ".ecore";
	    	String example3 = RESOURCE_PATH + example + "/changes2/" + example + ".ecore";
	    	
	    	DiffManagerService diffManagerService = DiffManagerService.getInstance();
	    	
	        return diffManagerService.getDiff(example2, example3, example1, type);
	    }

	    @GET
	    @Path("compare/{type}")
	    @Produces(MediaType.APPLICATION_JSON)
	    public ComparisonDto getDiffWf(@PathParam("type") String type, @QueryParam("file1") String file1, @QueryParam("file2") String file2) throws Exception {
	    	
	    	System.out.println("First file path : " + file1);
	    	System.out.println("Second file path: " + file2);
	    	
	    	DiffManagerService diffManagerService = DiffManagerService.getInstance();
	    	
	        return diffManagerService.getDiff(file1, file2, null, type);
	    }
	    
	    @GET
	    @Path("compareThreeWay/{type}")
	    @Produces(MediaType.APPLICATION_JSON)
	    public ComparisonDto get3WDiffWf(@PathParam("type") String type, @QueryParam("base") String base, @QueryParam("file1") String file1, @QueryParam("file2") String file2) throws Exception {
	    	System.out.println("First file path : " + base);
	    	System.out.println("First file path : " + file1);
	    	System.out.println("Second file path: " + file2);
	    	
	    	DiffManagerService diffManagerService = DiffManagerService.getInstance();
	    	
	        return diffManagerService.getDiff(file1, file2, base, type);
	    }
	
}
