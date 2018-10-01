package business;

public class IO {
	private IOenum type;
	private Object value;
	private AtomicComponent origin;
	
	public IO(IOenum type) {
		this.type = type;
	}
	
	public IO(IOenum type, Object value) {
		this.type = type;
		this.value = value;
	}
	
	public IO(IOenum type, Object value, AtomicComponent origin) {
		this.type = type;
		this.value = value;
		this.origin = origin;
	}
	
	public IO copy() {
		return(new IO(type, value));
	}
	
	public IOenum getType() {
		return type;
	}

	public void setType(IOenum type) {
		this.type = type;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
	
	@Override
	public boolean equals(Object obj) {
		return (type.equals(((IOenum)obj)));
	}
	
	@Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + (this.value != null ? this.value.hashCode() : 0);
        return hash;
    }

	public AtomicComponent getOrigin() {
		return origin;
	}

	public void setOrigin(AtomicComponent origin) {
		this.origin = origin;
	}
	
	@Override
	public String toString() {
		return(" [ " + type.toString() + " ; " + value  + " ]" );
	}

}
