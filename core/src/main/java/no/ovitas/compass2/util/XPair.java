/**
 * 
 */
package no.ovitas.compass2.util;

import java.io.Serializable;

/**
 * @author magyar
 *
 */
public class XPair<T extends Serializable, K extends Serializable> implements Serializable {

	protected T key;
	protected K value;
	public T getKey() {
		return key;
	}
	public void setKey(T key) {
		this.key = key;
	}
	public K getValue() {
		return value;
	}
	public void setValue(K value) {
		this.value = value;
	}
	public XPair(T key, K value) {
		super();
		this.key = key;
		this.value = value;
	}
	public XPair() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
