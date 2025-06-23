package app.entity.gims.pk;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Embeddable;

import enums.gims.ActionGims;

@Embeddable
public class SuiviGimsPk implements Serializable {
	private GimsPk gimsPk;
	private Date dateAction;
	private ActionGims action;

	public SuiviGimsPk() {}
	
	public SuiviGimsPk(GimsPk gimsPk, Date dateAction, ActionGims action) {
		super();
		this.gimsPk = gimsPk;
		this.dateAction = dateAction;
		this.action = action;
	}



	public GimsPk getGimsPk() {
		return gimsPk;
	}

	public void setNumeroFacture(GimsPk gimsPk) {
		this.gimsPk = gimsPk;
	}

	public Date getDateAction() {
		return dateAction;
	}

	public void setDateAction(Date dateAction) {
		this.dateAction = dateAction;
	}

	public ActionGims getAction() {
		return action;
	}

	public void setAction(ActionGims action) {
		this.action = action;
	}
}

