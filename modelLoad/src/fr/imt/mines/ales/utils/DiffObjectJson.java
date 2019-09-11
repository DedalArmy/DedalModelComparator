package fr.imt.mines.ales.utils;

import org.eclipse.emf.compare.ReferenceChange;
import org.json.JSONArray;
import org.json.JSONObject;

public class DiffObjectJson {

	private String differenceKind;
	private String dedalType;
	private String dedalElementId;
	private JSONArray violatedRules;
	private Boolean susbstituability;
	private String parent;
	private String parentType;
	
	public DiffObjectJson(			
			String differenceKind,
			String dedalType,
			String dedalElementName,
			JSONArray violatedRulesArrayList,
			Boolean susbstituability, 
			String parent,
			String parentType) {
		this.differenceKind = differenceKind;
		this.dedalType = dedalType;
		this.dedalElementId = dedalElementName;
		this.violatedRules = violatedRulesArrayList;
		this.susbstituability = susbstituability;
		this.parent = parent;
		this.parentType = parentType;
	}
	
	public DiffObjectJson() {
		violatedRules = new JSONArray();
	}
	
	public JSONObject toJsonObject() {
		JSONObject jsonObjectDiff = new JSONObject();
		jsonObjectDiff.put("differenceKind", differenceKind);
		jsonObjectDiff.put("dedalType", dedalType);
		jsonObjectDiff.put("dedalElementId", dedalElementId);
		jsonObjectDiff.put("susbstituability", susbstituability);
		jsonObjectDiff.put("violatedRules", violatedRules);
		jsonObjectDiff.put("parent", parent);
		jsonObjectDiff.put("parentType", parentType);
		return jsonObjectDiff;
	}

	public String getDifferenceKind() {
		return differenceKind;
	}

	public void setDifferenceKind(String differenceKind) {
		this.differenceKind = differenceKind;
	}

	public String getDedalType() {
		return dedalType;
	}
	
	public void setDedalType(String dedalType) {
		this.dedalType = dedalType;
	}
	
	public void setDedalType(ReferenceChange referenceChange) {
		this.dedalType = referenceChange.getValue().toString().split("@")[0];
	}

	public String getDedalElementId() {
		return dedalElementId;
	}

	public void setDedalElementId(String dedalElementId) {
		this.dedalElementId = dedalElementId;
	}
	
	public void setDedalElementName(ReferenceChange referenceChange) {
		this.dedalElementId = referenceChange.getValue().toString().substring(referenceChange.getValue().toString().indexOf(" ")+1);
	}

	public JSONArray getViolatedRules() {
		return violatedRules;
	}

	public void setViolatedRulesArrayList(JSONArray violatedRules) {
		this.violatedRules = violatedRules;
	}

	public Boolean getSusbstituability() {
		return susbstituability;
	}

	public void setSusbstituability(Boolean susbstituability) {
		this.susbstituability = susbstituability;
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public String getParentType() {
		return parentType;
	}

	public void setParentType(String parentType) {
		this.parentType = parentType;
	}
}
