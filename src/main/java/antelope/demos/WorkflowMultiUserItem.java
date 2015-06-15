package antelope.demos;

import java.io.Serializable;

public class WorkflowMultiUserItem implements Serializable{
	private static final long serialVersionUID = 1L;
	
	public String candidateusers;
	public String businesssid;
	public String getCandidateusers() {
		return candidateusers;
	}
	public void setCandidateusers(String candidateusers) {
		this.candidateusers = candidateusers;
	}
	public String getBusinesssid() {
		return businesssid;
	}
	public void setBusinesssid(String businesssid) {
		this.businesssid = businesssid;
	}
	
	
}
