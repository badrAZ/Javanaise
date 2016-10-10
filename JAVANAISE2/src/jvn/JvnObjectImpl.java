package jvn;

import java.io.Serializable;

import irc.Sentence;

public class JvnObjectImpl implements JvnObject {
	private int idObject =-1;
	private ObjectState etatObjet;
	private Serializable objet;
	
	 /** 
	  * Default constructor
	  **/
	public JvnObjectImpl(Serializable o , int idObject, ObjectState etatObjet){
	 this.idObject=idObject;
	this.objet=o;
	this.etatObjet=etatObjet;
	}
	
	/**
	* Get a Read lock on the object 
	* @throws JvnException
	**/
	public void jvnLockRead() throws JvnException {
		System.out.println("--------------------- Lock Read ");
		// TODO Auto-generated method stub
		synchronized (this) {
			if(etatObjet == ObjectState.RC) this.etatObjet=ObjectState.R;
			if(etatObjet == ObjectState.WC) this.etatObjet=ObjectState.RWC;
			 if(etatObjet == ObjectState.NL){
				 objet=JvnServerImpl.jvnGetServer().jvnLockRead(idObject);
				
					this.etatObjet=ObjectState.R;
					Sentence o=(Sentence) objet;
					o.read();
			}
		}
		System.out.println("L'object est en :" + etatObjet);
		
	}
	
	/**
	* Get a Write lock on the object 
	* @throws JvnException
	**/
	public void jvnLockWrite() throws JvnException {
		System.out.println("--------------------- Lock Write");
		// TODO Auto-generated method stub
		synchronized (this) {
			if(etatObjet == ObjectState.WC || etatObjet == ObjectState.RWC) etatObjet=ObjectState.W;

			if(etatObjet == ObjectState.NL || etatObjet == ObjectState.R || etatObjet == ObjectState.RC){
				Serializable repObjet = JvnServerImpl.jvnGetServer().jvnLockWrite(idObject);
					this.etatObjet=ObjectState.W;
				
			}
			System.out.println("L'object est en :" + etatObjet);
		}
	
	}

	public void jvnUnLock() throws JvnException {
		// TODO Auto-generated method stub
		System.out.println("--------------------- Unlock");
		synchronized (this) {
			if(etatObjet == ObjectState.R) this.etatObjet=ObjectState.RC;
			if(etatObjet == ObjectState.W || etatObjet == ObjectState.RWC ) this.etatObjet=ObjectState.WC;
			this.notifyAll();
			System.out.println("L'object est en :" + etatObjet);
		}
	}

	public int jvnGetObjectId() throws JvnException {
		// TODO Auto-generated method stub
		return this.idObject;
	}

	public Serializable jvnGetObjectState() throws JvnException {
		// TODO Auto-generated method stub
		return objet;
	}

	public void jvnInvalidateReader() throws JvnException {
		// TODO Auto-generated method stub
		System.out.println("--------------------- InvalidateReader ");
		synchronized (this) {
			
			// Il y a que deux cas R et RC(pas besoins d'attendre il n'utilise pas les verr)
			if(etatObjet == ObjectState.R){
				while(etatObjet==ObjectState.R){
					try {
						this.wait(); // on attend jusqu'à ce qu'il finit
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
				
					
					etatObjet=ObjectState.NL;
					System.out.println("L'object est en :" + etatObjet);

		}
		
	}

	public Serializable jvnInvalidateWriter() throws JvnException {
		// TODO Auto-generated method stub
		System.out.println("--------------------- InvalidateWriter");
		synchronized (this) {
			// Il y a que deux cas W et WC(pas besoins d'attendre il n'utilise pas les verr)
			if(etatObjet == ObjectState.W){
				try {
					this.wait(); // on attend jusqu'à ce qu'il finit
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			etatObjet=ObjectState.NL;
			System.out.println("L'object est en :" + etatObjet);
			return objet;
		}
		
	}

	public Serializable jvnInvalidateWriterForReader() throws JvnException {
		// TODO Auto-generated method stub
		System.out.println("--------------------- InvalidateWriterForReader");
		synchronized (this) {
			if(etatObjet == ObjectState.RWC){
				etatObjet=ObjectState.R;
			}
			if(etatObjet == ObjectState.WC){
				etatObjet=ObjectState.RC;
			}
			// ca cause problemme de concurrence d'accès
			if(etatObjet==ObjectState.W){
				while(etatObjet==ObjectState.W){
					try {
						this.wait(); // on attend jusqu'à ce qu'il finit
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					etatObjet=ObjectState.RC;
				}
			}
			
			System.out.println("L'object est en :" + etatObjet);
			
			return objet;
		}
	
		
	}

}
