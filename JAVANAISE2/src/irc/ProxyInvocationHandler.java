package irc;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import jvn.JvnException;
import jvn.JvnObject;
import jvn.JvnServerImpl;

// https://docs.oracle.com/javase/8/docs/technotes/guides/reflection/proxy.html

public class ProxyInvocationHandler implements InvocationHandler {
	
	
	private JvnObject obj;

	public ProxyInvocationHandler() {
		try {
		// initialize JVN
		JvnServerImpl js = JvnServerImpl.jvnGetServer();
	
		// look up the IRC object in the JVN server
		// if not found, create it, and register it in the JVN server
		System.out.println(" --------------------- Recherche de l'objet dans le registre ...");
		JvnObject jo = js.jvnLookupObject("IRC");
		
		if (jo == null) {
			System.out.println("--------------------- Cr�ation de l'objet ...");
			jo = js.jvnCreateObject((Serializable) new Sentence());
		
			// after creation, I have a write lock on the object
			jo.jvnUnLock();
			
			System.out.println("--------------------- Enregistrement de l'objet dans le registre ...");
			js.jvnRegisterObject("IRC", jo);
			System.out.println("Objet Enregistr�");
		}
		
		} catch (JvnException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.obj=obj;
	}


	//Intercepteur DynamicProxy
	public static Object newInstProxy(Sentence obj) throws JvnException{

		//M�thode static pour cr�er le dynamic proxy
		return  java.lang.reflect.Proxy.newProxyInstance(
				obj.getClass().getClassLoader(), // Obtenir la classe de l'objet 
				obj.getClass().getInterfaces(),    // Array d'interfaces � implementer, pas de Classes sinon error
				new ProxyInvocationHandler()); // Handler ajout�
		
	}
	
	// Define une clase Invocation Handler pour une interface sp�ficique
	// permet de realiser les invocations
	public Object invoke(Object proxy, Method m, Object[] args) throws Throwable {
	
		//return proxy;
		// un example d'implementation
        Object result = null;
        try {
            System.out.println("Avant la m�thode " );

    		String nomMethode=m.getName();
    		System.out.println(nomMethode);
    		if(nomMethode.lastIndexOf("Read")==-1){
    			obj.jvnLockRead();
    			
    		}
    		if(nomMethode.lastIndexOf("Write")==-1){
    			obj.jvnLockWrite();
    		}
            result = m.invoke(obj.jvnGetObjectState(), args);
            obj.jvnUnLock();
        } catch (InvocationTargetException e) {
            throw e.getTargetException();
        } catch (Exception e) {
           
        } finally {
            System.out.println("Apr�s la m�thode " );

        }
        return result;
	}

}