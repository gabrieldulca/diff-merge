package main.java.com.diffmerge.service;

import java.io.IOException;
import java.util.HashMap;

import main.java.com.diffmerge.dto.ComparisonDto;
import main.java.com.diffmerge.exception.InvalidParametersException;
import main.java.com.diffmerge.exception.InvalidRepresentationException;
import main.java.com.diffmerge.provider.DiffComponent;
import org.eclipse.glsp.graph.GModelRoot;

public class DiffManagerService {
	
	private static final DiffManagerService INSTANCE = new DiffManagerService();
	private HashMap<String, DiffComponent> registeredComponents;

    private DiffManagerService() {
    	registeredComponents = new HashMap<String, DiffComponent>();
    }

    public static DiffManagerService getInstance() {
        return INSTANCE;
    }
	

	public void register(DiffComponent diffComponent) {
		registeredComponents.put(diffComponent.getType(), diffComponent);
		
	}
	
	public ComparisonDto getDiff(String left, String right, String origin, String type) throws InvalidRepresentationException, InvalidParametersException, IOException {
		if(registeredComponents.containsKey(type)) {
			return registeredComponents.get(type).getComparison(left, right, origin);
		} else {
			throw new InvalidRepresentationException("No registered component for " + type +" found");
		}
		
	}

}
