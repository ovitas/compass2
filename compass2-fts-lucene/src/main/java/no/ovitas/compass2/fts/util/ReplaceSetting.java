/**
 * 
 */
package no.ovitas.compass2.fts.util;

/**
 * @author gyalai
 *
 */
public class ReplaceSetting {
	
	private String value;
	private String with;
	private String scope;
	
	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	
	public String getWith() {
		return with;
	}
	
	public void setWith(String with) {
		this.with = with;
	}
	
	public Scope getScopeEnum() {
		System.out.println(scope + " , " + value + " , " + with);
		return Scope.valueOf(scope.toUpperCase());
	}
//	
//	public void setScope(String scope) {
//		System.out.println(scope);
//		this.scope = scope;
//	}
	
	public String getScope() {
		return scope;
	}
	
	public void setScope(String scope) {
		this.scope = scope;
	}
	

}
