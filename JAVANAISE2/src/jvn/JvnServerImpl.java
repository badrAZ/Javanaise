/***
 * JAVANAISE Implementation
 * JvnServerImpl class
 * Contact: 
 *
 * Authors: 
 */

package jvn;

import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.io.*;



public class JvnServerImpl 	
              extends UnicastRemoteObject 
							implements JvnLocalServer, JvnRemoteServer{
	
  // A JVN server is managed as a singleton 
	private static JvnServerImpl js = null;
	private JvnRemoteCoord jvnRemoteCoord = null;
	
	private HashMap<Integer,JvnObject> objP ;//enregistrer la liste d'objet posséder pour les recup
  /**
  * Default constructor
  * @throws JvnException
  **/
	private JvnServerImpl() throws Exception {
		super();
		// to be completed
        try{
        	jvnRemoteCoord= (JvnRemoteCoord) Naming.lookup("serveur_de_coordination");
        	if(jvnRemoteCoord!=null) System.out.println("Serveur de coordination trouvé dans le registre RMI");
        	objP=new HashMap<Integer,JvnObject>();
        }catch(NotBoundException e){
        	System.out.println("Erreur Serveur de coordination est null");
        }
			
	}
	
  /**
    * Static method allowing an application to get a reference to 
    * a JVN server instance
    * @throws JvnException
    **/
	public static JvnServerImpl jvnGetServer() {
		if (js == null){
			try {
				js = new JvnServerImpl();
			} catch (Exception e) {
				return null;
			}
		}
		return js;
	}
	
	/**
	* The JVN service is not used anymore
	* @throws JvnException
	**/
	public  void jvnTerminate()
	throws jvn.JvnException {
    // to be completed 
	} 
	
	/**
	* creation of a JVN object
	* @param o : the JVN object state
	* @throws JvnException
	**/
	public  JvnObject jvnCreateObject(Serializable o)
	throws jvn.JvnException { 
			int id = -1;
			try {
				 id = jvnRemoteCoord.jvnGetObjectId();
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			JvnObjectImpl objImp =new JvnObjectImpl(o,id,ObjectState.W);
			System.out.println("Object crée et locké en écriture");
			objP.put(id, objImp);
			return objImp;
		
	
	}
	
	/**
	*  Associate a symbolic name with a JVN object
	* @param jon : the JVN object name
	* @param jo : the JVN object 
	* @throws JvnException
	**/
	public  void jvnRegisterObject(String jon, JvnObject jo)
	throws jvn.JvnException {
		// to be completed 
		try {
			jvnRemoteCoord.jvnRegisterObject(jon, jo, this);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	* Provide the reference of a JVN object beeing given its symbolic name
	* @param jon : the JVN object name
	* @return the JVN object 
	* @throws JvnException
	**/
	public  JvnObject jvnLookupObject(String jon)
	throws jvn.JvnException {
    // to be completed 
		try {
			JvnObject obj=jvnRemoteCoord.jvnLookupObject(jon, this);
			if(obj == null){
				System.out.println("Objet non trouvé dans le registre"); 
				return null;
			}
			else 
			{
				System.out.println("Objet trouvé dans le registre"); 
				return obj;
			}
				
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}	
	
	/**
	* Get a Read lock on a JVN object 
	* @param joi : the JVN object identification
	* @return the current JVN object state
	* @throws  JvnException
	**/
   public Serializable jvnLockRead(int joi)
	 throws JvnException {
		// to be completed 
		try {
			return jvnRemoteCoord.jvnLockRead(joi, this);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}	
	/**
	* Get a Write lock on a JVN object 
	* @param joi : the JVN object identification
	* @return the current JVN object state
	* @throws  JvnException
	**/
   public Serializable jvnLockWrite(int joi)
	 throws JvnException {
		// to be completed 
		return null;
	}	

	
  /**
	* Invalidate the Read lock of the JVN object identified by id 
	* called by the JvnCoord
	* @param joi : the JVN object id
	* @return void
	* @throws java.rmi.RemoteException,JvnException
	**/
  public void jvnInvalidateReader(int joi)
	throws java.rmi.RemoteException,jvn.JvnException {
		// to be completed 
	};
	    
	/**
	* Invalidate the Write lock of the JVN object identified by id 
	* @param joi : the JVN object id
	* @return the current JVN object state
	* @throws java.rmi.RemoteException,JvnException
	**/
  public Serializable jvnInvalidateWriter(int joi)
	throws java.rmi.RemoteException,jvn.JvnException { 
		// to be completed 
		return null;
	};
	
	/**
	* Reduce the Write lock of the JVN object identified by id 
	* @param joi : the JVN object id
	* @return the current JVN object state
	* @throws java.rmi.RemoteException,JvnException
	**/
   public Serializable jvnInvalidateWriterForReader(int joi)
	 throws java.rmi.RemoteException,jvn.JvnException { 
		// to be completed 
	   JvnObject objInv=objP.get(joi);
	   
		return objInv.jvnInvalidateWriterForReader();
	 };

}

 
