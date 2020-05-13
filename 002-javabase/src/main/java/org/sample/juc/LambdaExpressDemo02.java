package org.sample.juc;

interface Dog {
    void sayHello();
}

//@FunctionalInterface  定义函数接口 如果是仅有一个函数 jdk8会默认
interface Cat {
    String playWith(String name,String game);

    default String info(String name, int age) {
        return "我叫" + name + " 年龄："+ age;
    }
}

//@FunctionalInterface
interface Rabbit {
    void sayHello();

    default String info(String name, int age) {
        return "我叫" + name + " 年龄："+ age;
    }

    default String info2(String name, int age) {
        return "-----我叫" + name + " 年龄："+ age;
    }

    static void go(){
        System.out.println("go");
    }
    static void gogo(){
        System.out.println("gogogo");
    }
}

/**
 * 函数式编程
 * 拷贝中括号  写死右箭头  关闭大括号
 */
public class LambdaExpressDemo02 {

    public static void main(String[] args) {

/*
        //匿名内部类方式
        Dog foo = new Dog() {
            @Override
            public void sayHello() {
                System.out.println("hello---------");
            }
        };
        foo.sayHello();

        //Lambda表达式方式 无参数无返回
        Dog dog = () -> {
            System.out.println("----------");
        };
*/

        //Lambda表达式方式 有参有返回
        Cat cat = (String name,String game) -> {
            return "和" + name + "玩" + game;
        };
        System.out.println(cat.playWith("小兔","跳绳"));
        System.out.println(cat.info("小兔",3));


    }

}
