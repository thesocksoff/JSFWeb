package authorization;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import java.io.Serializable;

@ManagedBean
public class getInfo implements Serializable{
	private String login;
	private String password;
	//private String validate;

	private static final long serialVersionUID = 1L;
	
	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getValidate() {
		return "SUCSESS";
	}
	
	public String getIndex() {
		return "index.xhtml";
	}
	
	public void loginAction(ActionEvent actionEvent) {
		loginMessage("Welcome to index!!");
	}

	public void loginMessage(String summary) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, null);
		FacesContext.getCurrentInstance().addMessage(null, message);

	}
}
