package org.sample.base;

import lombok.Data;
import org.sample.base.bean.UserBean;

import java.io.*;

/**
 * 测试实现Externalizable接口时transient关键字
 *
 * 另外: 静态变量不管是不是transient关键字修饰，都不会被序列化 在[面试知识树]有代码验证过程
 */
public class TransientStudy1 {
    public static void main(String[] args) throws Exception {
        //序列化
        new TransientStudy1().serializeUser();
        //反序列化
        new TransientStudy1().deSerializeUser();
    }
    private void serializeUser() throws IOException {
        UserBean user = new UserBean();
        user.setName("张三");
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("D://tmp//test1"));
        oos.writeObject(user);
        oos.close();
        System.out.println("使用了Externalizable接口， 添加了transient关键字序列化之前" + user.toString());
    }
    private void deSerializeUser() throws IOException, ClassNotFoundException {
        File file = new File("D://tmp//test1");
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
        UserBean newUser = (UserBean)ois.readObject();
        System.out.println("使用了Externalizable接口，添加了transient关键字反序列化之后" + newUser.toString());
    }
}
