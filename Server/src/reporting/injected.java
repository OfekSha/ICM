package reporting;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/** Marking which methods were constructor injected for testing 
 * 
 *
 */
@Target(value = { ElementType.TYPE })
public @interface injected {

	String [] functionsNames();
	String nameOfInjectedVar();
	@Target(ElementType.METHOD)
	@interface injectedMethod{
		String functionName();
		
	}
}
