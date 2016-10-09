/***
 * JAVANAISE Implementation
 * JvnServerImpl class
 * Contact: 
 *
 * Authors: 
 */

package jvn;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.LinkedList;
import java.io.IOException;
import java.io.Serializable;

 class JvnCoordImpl 	
              extends UnicastRemoteObject 
							implements JvnRemoteCoord{
	 
private int idobjet;	
private static JvnCoordImpl jc = null;

//le registre qui contient les objets
private HashMap<String,JvnObject> nameMap = new HashMap<String,JvnObject>();

//Pour récuper la liste des serveurs qui ont un verrou sur l'objet
private HashMap<Integer,JvnServersLockObject> objectServersLock = new HashMap<Integer,JvnServersLockObject>();

 
  /** 
  * Default constructor
  * @throws JvnException
  **/
	private JvnCoordImpl() throws Exception {
		LocateRegistry.createRegistry(1099);
		// to be completed
		
		/*------ Enregistrer le serveur de coordination dans le registre RMI ---------------- */
		Naming.rebind("serveur_de_coordination", this);
		System.out.println("Serveur de coordination enregistré dans le registre RMI");
		idobjet = 0;
	}
	
	/*-------- Lancer le serveur de coordination -----------------*/
public static void main(String argv[]) throws Exception {
	if(jc==null){
		jc = new JvnCoordImpl();
	}
}
  /**
  *  Allocate a NEW JVN object id (usually allocated to a 
  *  newly created JVN object)
  * @throws java.rmi.RemoteException,JvnException
  **/
  public int jvnGetObjectId()
  throws java.rmi.RemoteException,jvn.JvnException {
    synchronized (this) {
		return(++idobjet);
	}
  }
  
  /**
  * Associate a symbolic name with a JVN object
  * @param jon : the JVN object name
  * @param jo  : the JVN object 
  * @param joi : the JVN object identification
  * @param js  : the remote reference of the JVNServer
  * @throws java.rmi.RemoteException,JvnException
  **/
  public void jvnRegisterObject(String jon, JvnObject jo, JvnRemoteServer js)
  throws java.rmi.RemoteException,jvn.JvnException{
    // to be completed 
	  synchronized (this) {
		// enregistrer l'objet dans le registre du coordinateur
		  nameMap.put(jon, jo);
		//ajouter l'objet avec son état et enregister le serveur qui a le verrou sur l'objet
		  JvnServersLockObject serversLO = new JvnServersLockObject(jo,ObjectState.W);
		  serversLO.addJs(js);
		  // Garder la trace de la relation objet/serveurs qui ont le verrou sur l'objet
		  objectServersLock.put(jo.jvnGetObjectId(),serversLO);
	
	  }
	   
  }
  
  /**
  * Get the reference of a JVN object managed by a given JVN server 
  * @param jon : the JVN object name
  * @param js : the remote reference of the JVNServer
  * @throws java.rmi.RemoteException,JvnException
  **/
  public JvnObject jvnLookupObject(String jon, JvnRemoteServer js)
  throws java.rmi.RemoteException,jvn.JvnException{
    // to be completed 
	  synchronized (this) {
		  // Récupération des serveurs qui ont un verrou sur l'objet
		  JvnObject obj = nameMap.get(jon);
		 JvnServersLockObject serversLO = objectServersLock.get(obj.jvnGetObjectId());
		 if(serversLO!=null)
		 {
			// changer l'état de l'objet en NL
			 serversLO.setEtatObject(ObjectState.NL);
		 }
			   return nameMap.get(jon);
		}
  }
  
  /**
  * Get a Read lock on a JVN object managed by a given JVN server 
  * @param joi : the JVN object identification
  * @param js  : the remote reference of the server
  * @return the current JVN object state
  * @throws java.rmi.RemoteException, JvnException
  **/
   public Serializable jvnLockRead(int joi, JvnRemoteServer js)
   throws java.rmi.RemoteException, JvnException{
	   synchronized (this) {
		   JvnServersLockObject serversLO = objectServersLock.get(joi);
		   Serializable etatO=null;
		   
		   if(serversLO.getEtatObject()== ObjectState.NL){
			   serversLO.getJss().clear();
			   serversLO.setEtatObject(ObjectState.R);
			   serversLO.addJs(js);
		   }
		   
		   // Il y a pas de problemme de concurrence d'accès quand le verrou est en read
		   if(serversLO.getEtatObject() == ObjectState.R){
			   serversLO.addJs(js);
		   }
		   
		   if(serversLO.getEtatObject() == ObjectState.W){
			   // il y a qu' un seul serveur qui a le verrou en right
			   
			   JvnRemoteServer serv=serversLO.getJss().iterator().next();
			   
			   if(!serv.equals(js))
			   // le server qui a le verrou en écriture son verrou va changer en lecture (cache)
			   etatO=serv.jvnInvalidateWriterForReader(joi);
			   
			   // l'etat de verrou qui ont les serveur vont changer en read
			   serversLO.setEtatObject(ObjectState.R);
			   
			   // ajout du serveur dans la liste des serv qui ont le verrou en lecture sur l'objet
			   serversLO.addJs(serv);
		   }
		   return etatO;
	   
	   }
	   
    // to be completed
   }

  /**
  * Get a Write lock on a JVN object managed by a given JVN server 
  * @param joi : the JVN object identification
  * @param js  : the remote reference of the server
  * @return the current JVN object state
  * @throws java.rmi.RemoteException, JvnException
  **/
   public Serializable jvnLockWrite(int joi, JvnRemoteServer js)
   throws java.rmi.RemoteException, JvnException{
    // to be completed
    return null;
   }

	/**
	* A JVN server terminates
	* @param js  : the remote reference of the server
	* @throws java.rmi.RemoteException, JvnException
	**/
    public void jvnTerminate(JvnRemoteServer js)
	 throws java.rmi.RemoteException, JvnException {
	 // to be completed
    }
}

 
