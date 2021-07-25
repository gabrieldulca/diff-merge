package main.java.com.diffmerge.dto;

public class DiffDto {

	private TypeDto type;

	private String value;

	private KindDto kind;
	
	private ConflictDto conflict;
	
	private ReferenceDto referenceDto;
	
	private String matchId;
	
	private String source;

	public DiffDto() {
	}

	
	public String getMatchId() {
		return matchId;
	}


	public void setMatchId(String matchId) {
		this.matchId = matchId;
	}


	public KindDto getKind() {
		return kind;
	}

	public void setKind(KindDto kind) {
		this.kind = kind;
	}

	public TypeDto getType() {
		return type;
	}

	public void setType(TypeDto type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public ConflictDto getConflict() {
		return conflict;
	}

	public void setConflict(ConflictDto conflict) {
		this.conflict = conflict;
	}

	public ReferenceDto getReferenceDto() {
		return referenceDto;
	}

	public void setReferenceDto(ReferenceDto referenceDto) {
		this.referenceDto = referenceDto;
	}
	
	

	public String getSource() {
		return source;
	}


	public void setSource(String source) {
		this.source = source;
	}


	@Override
	public String toString() {
		return "DiffDto [type=" + type + ", value=" + value + ", kind=" + kind + ", conflict=" + conflict
				+ ", referenceDto=" + referenceDto + "]";
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((kind == null) ? 0 : kind.hashCode());
		result = prime * result + ((matchId == null) ? 0 : matchId.hashCode());
		result = prime * result + ((source == null) ? 0 : source.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DiffDto other = (DiffDto) obj;
		if (kind != other.kind)
			return false;
		if (matchId == null) {
			if (other.matchId != null)
				return false;
		} else if (!matchId.equals(other.matchId))
			return false;
		if (source == null) {
			if (other.source != null)
				return false;
		} else if (!source.equals(other.source))
			return false;
		if (type != other.type)
			return false;
		return true;
	}

	
}
