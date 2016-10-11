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
			System.out.println("--------------------- Création de l'objet ...");
			jo = js.jvnCreateObject((Serializable) new Sentence());
		
			// after creation, I have a write lock on the object
			jo.jvnUnLock();
			
			System.out.println("--------------------- Enregistrement de l'objet dans le registre ...");
			js.jvnRegisterObject("IRC", jo);
			System.out.println("Objet Enregistré");
		}
		
		} catch (JvnException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.obj=obj;
	}


	//Intercepteur DynamicProxy
	public static Object newInstProxy(Sentence obj) throws JvnException{

		//Méthode static pour créer le dynamic proxy
		return  java.lang.reflect.Proxy.newProxyInstance(
				obj.getClass().getClassLoader(), // Obtenir la classe de l'objet 
				obj.getClass().getInterfaces(),    // Array d'interfaces à implementer, pas de Classes sinon error
				new ProxyInvocationHandler()); // Handler ajouté
		
	}
	
	// Define une clase Invocation Handler pour une interface spéficique
	// permet de realiser les invocations
	public Object invoke(Object proxy, Method m, Object[] args) throws Throwable {
	
		//return proxy;
		// un example d'implementation
        Object result = null;
        try {
            System.out.println("Avant la méthode " );

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
            System.out.println("Après la méthode " );

        }
        return result;
	}

}