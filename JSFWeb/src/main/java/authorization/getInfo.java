package authorization;

import javax.faces.bean.ManagedBean;

@ManagedBean
public class getInfo {
	private String password;
	
	public String getPassword() {
        return password;
    }
 
    public void setPassword1(String password) {
    	this.password = password;
    }
}
