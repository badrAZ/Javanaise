package jvn;

import java.util.ArrayList;
import java.util.List;

public class JvnServersLockObject {
	
 private List<JvnRemoteServer> jss=new ArrayList<JvnRemoteServer>();// la liste des serveurs ayant un verrou sur l'objet
 private JvnObject jo;// l'opjet applicatif
 private ObjectState etatObject; // type de verrou;
 


	public JvnServersLockObject(JvnObject jo,ObjectState etatObject){
 		this.jo=jo;
 		this.etatObject=etatObject;
 	}

	public List<JvnRemoteServer> getJss() {
		return jss;
	}

	public JvnObject getJo() {
		return jo;
	}

	public ObjectState getEtatObject() {
		return etatObject;
	}
	
 	public void addJs(JvnRemoteServer js) {
	this.jss.add(js);
}

public void setJo(JvnObject jo) {
	this.jo = jo;
}

public void setEtatObject(ObjectState etatObject) {
	this.etatObject = etatObject;
}
}
