package io.benchmarks.models;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;

import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

@State(Scope.Benchmark)
public class Event implements Serializable, Externalizable {
    
    public long val1 = 10L;
    public long val2 = 20L;
    public long val3 = 30L;
    public long val4 = 40L;
    
    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
	
	out.writeLong(val1);
	out.writeLong(val2);
	out.writeLong(val3);
	out.writeLong(val4);
    }
    
    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {

	try {
	    throw new Exception();
	    
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }
}
