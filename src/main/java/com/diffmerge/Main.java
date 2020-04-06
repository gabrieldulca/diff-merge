package main.java.com.diffmerge;




import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.CompareFactory;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
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
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.uml2.uml.internal.resource.UMLResourceFactoryImpl;
import org.eclipse.uml2.uml.resources.util.UMLResourcesUtil;
import org.eclipse.uml2.uml.util.UMLUtil;

import main.java.com.diffmerge.controller.DiffController;
import main.java.com.diffmerge.provider.impl.DiagramDiffComponent;
import main.java.com.diffmerge.provider.impl.FormDiffComponent;
import main.java.com.diffmerge.provider.impl.TableDiffComponent;
import main.java.com.diffmerge.provider.impl.TreeDiffComponent;
import main.java.com.diffmerge.service.DiffManagerService;
import main.java.com.diffmerge.util.JsonSchemaGenerator;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		JsonSchemaGenerator jsonSchemaGenerator = new JsonSchemaGenerator();
		jsonSchemaGenerator.generateSchema();
		
    	DiffManagerService diffManagerService = DiffManagerService.getInstance();
    	diffManagerService.register(new DiagramDiffComponent());
    	diffManagerService.register(new FormDiffComponent());
    	diffManagerService.register(new TableDiffComponent());
    	diffManagerService.register(new TreeDiffComponent());
		
		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");

        Server jettyServer = new Server(8080);
        jettyServer.setHandler(context);

        ServletHolder jerseyServlet = context.addServlet(
             org.glassfish.jersey.servlet.ServletContainer.class, "/*");
        jerseyServlet.setInitOrder(0);

        // Tells the Jersey Servlet which REST service/class to load.
        jerseyServlet.setInitParameter(
           "jersey.config.server.provider.classnames",
           DiffController.class.getCanonicalName());

        try {
            jettyServer.start();
            jettyServer.join();
        } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
            jettyServer.destroy();
        }
		
	}
	
	


}