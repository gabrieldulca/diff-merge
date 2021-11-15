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

	public ComparisonDto getMergeNoConflicts(String left, String right, String origin, String type, String[] changes) throws InvalidRepresentationException, InvalidParametersException, IOException {
		if(registeredComponents.containsKey(type)) {
			return registeredComponents.get(type).getMergeNoConflicts(left, right, origin, changes);
		} else {
			throw new InvalidRepresentationException("No registered component for " + type +" found");
		}
	}

	public ComparisonDto getMerge(String left, String right, String origin, String type) throws InvalidRepresentationException, InvalidParametersException, IOException {
		if(registeredComponents.containsKey(type)) {
			return registeredComponents.get(type).getMerge(left, right, origin);
		} else {
			throw new InvalidRepresentationException("No registered component for " + type +" found");
		}
	}
	
	public void getRevert(String left, String right, String origin, String type) throws InvalidRepresentationException, InvalidParametersException, IOException {
		if(registeredComponents.containsKey(type)) {
			registeredComponents.get(type).getRevert(left, right, origin);
		} else {
			throw new InvalidRepresentationException("No registered component for " + type +" found");
		}
	}
	
	public void getSave(String left, String right, String origin, String type) throws InvalidRepresentationException, InvalidParametersException, IOException {
		if(registeredComponents.containsKey(type)) {
			registeredComponents.get(type).getSave(left, right, origin);
		} else {
			throw new InvalidRepresentationException("No registered component for " + type +" found");
		}
	}

	
	public ComparisonDto getMergeSingleChange(String left, String right, String origin, String type, String elem, boolean revert) throws InvalidRepresentationException, InvalidParametersException, IOException {
		if(registeredComponents.containsKey(type)) {
			return registeredComponents.get(type).getMergeSingleChange(left, right, origin, elem, revert);
		} else {
			throw new InvalidRepresentationException("No registered component for " + type +" found");
		}
	}

}
