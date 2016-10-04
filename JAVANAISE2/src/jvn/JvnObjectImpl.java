package jvn;

import java.io.Serializable;

public class JvnObjectImpl implements JvnObject {
	
	private int idObject =-1;
	private ObjectState etatDesObjets;
	private Serializable etatObjet;
	public JvnObjectImpl(Serializable o , int idObject){
	 this.idObject=idObject;
	this.etatObjet=o;
	}

	public void jvnLockRead() throws JvnException {
		// TODO Auto-generated method stub
		if(etatObjet==etatDesObjets.RC) this.etatObjet=etatDesObjets.R;
		else if(etatObjet==etatDesObjets.R){}
		else{
			
		}
	}

	public void jvnLockWrite() throws JvnException {
		// TODO Auto-generated method stub

	}

	public void jvnUnLock() throws JvnException {
		// TODO Auto-generated method stub
		synchronized (this) {
			this.etatObjet=ObjectState.NL;
			this.notifyAll();
		}
	}

	public int jvnGetObjectId() throws JvnException {
		// TODO Auto-generated method stub
		return this.idObject;
	}

	public Serializable jvnGetObjectState() throws JvnException {
		// TODO Auto-generated method stub
		return etatObjet;
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
		return null;
	}

}
