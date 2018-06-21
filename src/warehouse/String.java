package warehouse;

import java.io.Serializable;

public class String implements Indexable<java.lang.String>, Serializable {
	
	private final java.lang.String string;
	
	public String(java.lang.String string) {
		this.string = string;
	}

	public java.lang.String getString() {
		return string;
	}

	@Override
	public java.lang.String getKey() {
		return this.string;
	}

}