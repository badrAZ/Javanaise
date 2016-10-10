package irc;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import jvn.JvnException;
import jvn.JvnObject;
import jvn.JvnServerImpl;

// https://docs.oracle.com/javase/8/docs/technotes/guides/reflection/proxy.html

public class ProxyInvocationHandler implements InvocationHandler {
	
	
	private JvnObject obj;

	public ProxyInvocationHandler(JvnObject obj) {
	
		this.obj=obj;
	}

	
	//Intercepteur DynamicProxy
	public static Object newInstProxy(JvnObject obj) throws JvnException{
			
		InvocationHandler proxyInvHandler = new ProxyInvocationHandler(obj);
		
		//M�thode static pour cr�er le dynamic proxy
		return  java.lang.reflect.Proxy.newProxyInstance(
				obj.getClass().getClassLoader(), // Obtenir la classe de l'objet 
				obj.getClass().getInterfaces(),    // Array d'interfaces � implementer, pas de Classes sinon error
				proxyInvHandler); // Handler ajout�
		
	}
	
	// Define une clase Invocation Handler pour une interface sp�ficique
	// permet de realiser les invocations
	public Object invoke(Object proxy, Method m, Object[] args) throws Throwable {
		
		
		
		
		m.getName();
	
		
		//return proxy;
		// un example d'implementation
        Object result;
        try {
            System.out.println("Avant la m�thode " + m.getName());
            result = m.invoke(obj, args);
        } catch (InvocationTargetException e) {
            throw e.getTargetException();
        } catch (Exception e) {
            throw new RuntimeException("unexpected invocation exception: " +
                                       e.getMessage());
        } finally {
            System.out.println("Apr�s la m�thode " + m.getName());
        }
        return result;
	}

}