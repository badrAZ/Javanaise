package jvn;

import java.io.Serializable;

public class JvnObjectImpl implements JvnObject {
	
	private int idObject =-1;
	private ObjectState etatObjet;
	private Serializable Objet;
	
	 /** 
	  * Default constructor
	  **/
	public JvnObjectImpl(Serializable o , int idObject, ObjectState etatObjet){
	 this.idObject=idObject;
	this.Objet=o;
	this.etatObjet=etatObjet;
	}
	
	/**
	* Get a Read lock on the object 
	* @throws JvnException
	**/
	public void jvnLockRead() throws JvnException {
		// TODO Auto-generated method stub
		synchronized (this) {
			if(etatObjet == ObjectState.RC) this.etatObjet=ObjectState.R;
			if(etatObjet == ObjectState.WC) this.etatObjet=ObjectState.RWC;
			 if(etatObjet == ObjectState.R){
				Serializable repObjet=JvnServerImpl.jvnGetServer().jvnLockRead(idObject);
				if(repObjet != null){
					this.Objet=repObjet;
					this.etatObjet=ObjectState.R;
				}
			}
		}
		System.out.println("L'object est en :" + etatObjet);
		
	}

	public void jvnLockWrite() throws JvnException {
		// TODO Auto-generated method stub

	}

	public void jvnUnLock() throws JvnException {
		// TODO Auto-generated method stub
		synchronized (this) {
			if(etatObjet == ObjectState.R) this.etatObjet=ObjectState.RC;
			if(etatObjet == ObjectState.W) this.etatObjet=ObjectState.WC;
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
		return Objet;
	}

	public void jvnInvalidateReader() throws JvnException {
		// TODO Auto-generated method stub

	}

	public Serializable jvnInvalidateWriter() throws JvnException {
		// TODO Auto-generated method stub
		
		return null;
	}

	public Serializable jvnInvalidateWriterForReader() throws JvnException {
		// TODO Auto-generated method stub
		
		synchronized (this) {
			//etape où il fait rien
			if(etatObjet == ObjectState.WC){
				etatObjet = ObjectState.RC;
			}
			if(etatObjet == ObjectState.RWC){
				etatObjet=ObjectState.R;
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
			
			return Objet;
		}
	
		
	}

}
