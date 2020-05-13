package org.sample.base.bean;

import lombok.Data;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

@Data
public class UserBean implements Externalizable {
    private transient String name;
    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(name);
    }
    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        name = (String)in.readObject();
    }
}
