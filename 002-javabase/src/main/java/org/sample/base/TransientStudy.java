package org.sample.base;

import lombok.Data;

import java.io.*;

// 内部类如果需要序列化 则外部也要实现序列化接口
public class TransientStudy implements Serializable {
    public static void main(String[] args) throws Exception {

        //序列化
        new TransientStudy().serializeUser();

        //反序列化
        new TransientStudy().deSerializeUser();

    }
    private void serializeUser() throws IOException {
        User user = new User();
        user.setName("张三");
        user.setAge(35);
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("D://tmp//test"));
        oos.writeObject(user);
        oos.close();
        System.out.println("添加了transient关键字序列化:age=" + user.getAge() );
    }
    private void deSerializeUser() throws IOException, ClassNotFoundException {
        File file = new File("D://tmp//test");
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
        User newUser = (User)ois.readObject();
        System.out.println("添加了transient关键字反序列化:age=" + newUser.getAge());
    }
    @Data
    public class User implements Serializable {
        private static final long serialVersionUID = 123456L;
        private transient int age;
        private String name;
    }
}
