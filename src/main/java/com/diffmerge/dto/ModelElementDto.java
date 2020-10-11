package main.java.com.diffmerge.dto;

// Should be expanded as it isn't complete
public class ModelElementDto {
	
	private String id;
	
	private String name;
	
	private String type;
	
	private String target;

	public ModelElementDto() {
		super();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "ModelElementDto [id=" + id + ", name=" + name + ", type=" + type + ", target=" + target + "]";
	}
	
	
	
}
