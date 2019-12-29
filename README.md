# Thread-safe Singleton implementation

Sometimes it's important to have only an one instance of class in an application. For example, to store settings or 
display manager. Something that should exist in one copy. There is a singleton pattern which solves the task.

## Singleton
*Singleton pattern* description from [www.oodesign.com](http://www.oodesign.com/singleton-pattern.html)

>The singleton pattern is one of the simplest design patterns: it involves only one class which is responsible to instantiate itself, 
>to make sure it creates not more than one instance; in the same time it provides a global point of access to that instance. 
>In this case the same instance can be used from everywhere, being impossible to invoke directly the constructor each time.

## Implementations
### Double-check initialization
First approach, which I would like to discuss is `Double-check initialization`. Before you continue reading - I strongly 
do not recommend to use it in your application. The reason why I posted it here to mention about the approach and pursue you do not use it :). 
Check out the link [Double-checked locking issue](https://en.wikipedia.org/wiki/Double-checked_locking#Usage_in_Java). 
The problem is actual. The approach contains two problems: the first problem you can find by a link above and the second 
problem is performance. It's clear that the method `getInstance` is a bottle-neck. If many threads will try to get 
the value the only one `if` statement will slow down entire the system.

Lets take a look at the source code. A method `getInstance` return desired singleton. Take a look inside. In the first 
line of the method `if(instance == null) {` it's check if the instance is `null`. If it's `null` then a synchronization 
lock is applied for entire class and a value in a variable `instance` is examined again to verify if the value is still 
`null`. If the value is still `null` then a new instance of `DoubleCheckSingleton` is created.

The issue *Double-checked locking issue* is here. When during execution of thread A a new value is assigned 
to a variable `instance` and the synchronization lock is released the instance could be only partly created, but 
the reference has been already been assigned to the variable. The reference has been assigned, but the construction of 
the object is not over. When a next thread B tries to access the variable there is a risk that the thread B will receive 
a reference to a not fully created object and the object is not usable.

##### DoubleCheckSingleton.java
```java
package info.biosfood.singleton;

import org.apache.log4j.Logger;

public class DoubleCheckSingleton {
    
    private static final Logger LOG = Logger.getLogger(DoubleCheckSingleton.class);
    
    private static DoubleCheckSingleton instance = null;
    
    public static DoubleCheckSingleton getInstance() {
        if(instance == null) {
            LOG.debug("after a first check, instance is null, create a new one.");
            
            synchronized (DoubleCheckSingleton.class) {
                if(instance == null) {
                    LOG.debug("after a double-check, instance still is null, create a new one.");
                    instance = new DoubleCheckSingleton();
                }
            }
        }
    
        LOG.debug("returning the instance");
        return instance;
    }
    
    private DoubleCheckSingleton() {
        LOG.debug("&gt;&gt; CONSTRUCTOR OF " + this.getClass().getName());
    }

}
```

### Final static field
There is better approach how to initialize a singleton. We need to use `static final` modifier for a class field which stores an instance of the singleton. 
And we don't need to use any `if` statements to verify if the variable is initialized. `final` modifier is thread-safe, 
it mean that the reference will not be assigned to a field until the object is constructed fully. If in this case we 
don't need double-check locking, we don't need to check if the `final` field is `null`. It will not be null. 
Both approaches are lazy loaded, but they have differences in loading order. I explain the differences below 
after a description of the approaches.

### Final static field initialization
A class `FinalStaticFieldSingleton` is a singleton and it's lazy initialized. An instance of the class is stored in 
`private static final FinalStaticFieldSingleton instance = new FinalStaticFieldSingleton();` field. 
Since the field is static, then the variable will be loaded only when the class is loaded. When we write a code 
`FinalStaticFieldSingleton.getInstance()` the class is loaded and the static fields are initialized by a class loader 
before a method `getInstance()` is called.

##### FinalStaticFieldSingleton.java
```java
package info.biosfood.singleton;

import org.apache.log4j.Logger;

public class FinalStaticFieldSingleton {

    static final Logger LOG = Logger.getLogger(FinalStaticFieldSingleton.class);
    
    private static final FinalStaticFieldSingleton instance = new FinalStaticFieldSingleton();
    
    public static final FinalStaticFieldSingleton getInstance() {
        LOG.debug("return an instance of " + FinalStaticFieldSingleton.class.getName());
        return instance;
    }
    
    private FinalStaticFieldSingleton() {
        LOG.debug("&gt;&gt; CONSTRUCTOR OF " + this.getClass().getName());
    }

}
```

##### Test
```java
@Test
public void test() {
    LOG.debug("--- Test FinalStaticFieldSingleton ---");
    
    LOG.debug("++ accessing getInstance method");
    FinalStaticFieldSingleton s = FinalStaticFieldSingleton.getInstance();
    
    assertEquals(s, FinalStaticFieldSingleton.getInstance());
}
```

##### Output
```text
DEBUG FinalStaticFieldSingleton: [main] --- Test FinalStaticFieldSingleton ---
DEBUG FinalStaticFieldSingleton: [main] ++ accessing getInstance method
DEBUG FinalStaticFieldSingleton: [main] &gt;&gt; CONSTRUCTOR OF info.biosfood.singleton.FinalStaticFieldSingleton
DEBUG FinalStaticFieldSingleton: [main] return an instance of info.biosfood.singleton.FinalStaticFieldSingleton
DEBUG FinalStaticFieldSingleton: [main] return an instance of info.biosfood.singleton.FinalStaticFieldSingleton
```

### Final static field in an inner class initialization
The same idea of the single initialization, but the field with the instance holds an inner static class. 
The instance will be initialized only when inner static class is loaded and static blocks and fields are initialized. 
In the example before, the `instance` field is loaded when the class is loaded and static blocks and fields are initialized. 
This example gives more "laziness" :) in this example, if the singleton contains any static methods, blocks they will be 
loaded separately of creating and initializing singleton's instance.

##### FinalStaticFieldInnerClassSingleton.java
```java
package info.biosfood.singleton;

import org.apache.log4j.Logger;

public class FinalStaticFieldInnerClassSingleton {

    private static final Logger LOG = Logger.getLogger(FinalStaticFieldInnerClassSingleton.class);
    
    public static FinalStaticFieldInnerClassSingleton getInstance() {
        LOG.debug("return an instance of " + FinalStaticFieldInnerClassSingleton.class.getName());
        
        return SingletonHolder.instance;
    }
    
    private static class SingletonHolder {
        static final FinalStaticFieldInnerClassSingleton instance = new FinalStaticFieldInnerClassSingleton();
    }
    
    private FinalStaticFieldInnerClassSingleton() {
        LOG.debug("&gt;&gt; CONSTRUCTOR OF " + this.getClass().getName());
    }

}
```

##### Test
```java
@Test
public void test() {
LOG.debug("--- Test FinalStaticFieldInnerClassSingleton ---");

LOG.debug("++ accessing getInstance method");
FinalStaticFieldInnerClassSingleton s = FinalStaticFieldInnerClassSingleton.getInstance();

assertEquals(s, FinalStaticFieldInnerClassSingleton.getInstance());
}
```

##### Output
```text
DEBUG FinalStaticFieldInnerClassSingleton: [main] --- Test FinalStaticFieldInnerClassSingleton ---
DEBUG FinalStaticFieldInnerClassSingleton: [main] ++ accessing getInstance method
DEBUG FinalStaticFieldInnerClassSingleton: [main] return an instance of info.biosfood.singleton.FinalStaticFieldInnerClassSingleton
DEBUG FinalStaticFieldInnerClassSingleton: [main] &gt;&gt; CONSTRUCTOR OF info.biosfood.singleton.FinalStaticFieldInnerClassSingleton
DEBUG FinalStaticFieldInnerClassSingleton: [main] return an instance of info.biosfood.singleton.FinalStaticFieldInnerClassSingleton
```

### Differences in FinalStaticFieldSingleton and FinalStaticFieldInnerClassSingleton
In the beginning I would write they are just examples to show how the fields are initialized.
I prepared a bit extended version of `FinalStaticFieldSingleton`. It contains one static block, one public static field 
`SOME_VALUE` and one more public static method `test()` which are read and invoked respectively before a method `getInstance()` is invoked.

##### FinalStaticFieldSingletonExtended
```java
package info.biosfood.singleton;

import org.apache.log4j.Logger;

public class FinalStaticFieldSingletonExtended {

    static final Logger LOG = Logger.getLogger(FinalStaticFieldSingletonExtended.class);
    
    public static final int SOME_VALUE = 1;
    
    static {
        LOG.debug("static block initialization");
    }
    
    private static final FinalStaticFieldSingletonExtended instance = new FinalStaticFieldSingletonExtended();
    
    public static void test() {
        LOG.debug("static method test");
    }
    
    public static FinalStaticFieldSingletonExtended getInstance() {
        LOG.debug("return an instance of " + FinalStaticFieldSingletonExtended.class.getName());
        
        return instance;
    }
    
    private FinalStaticFieldSingletonExtended() {
        LOG.debug("&gt;&gt; CONSTRUCTOR OF " + this.getClass().getName());
    }

}
```

##### Test
```java
@Test
public void testExtended() {
LOG.debug("--- Test FinalStaticFieldSingletonExtended ---");

LOG.debug("++ accessing public static field");
LOG.debug("static value: " + FinalStaticFieldSingletonExtended.SOME_VALUE);

LOG.debug("++ accessing public static method");
FinalStaticFieldSingletonExtended.test();

LOG.debug("++ accessing getInstance method");
FinalStaticFieldSingletonExtended s = FinalStaticFieldSingletonExtended.getInstance();

assertEquals(s, FinalStaticFieldSingletonExtended.getInstance());
}
```

##### Test output
```text
DEBUG FinalStaticFieldSingletonExtendedTest: [main] --- Test FinalStaticFieldSingletonExtended ---
DEBUG FinalStaticFieldSingletonExtendedTest: [main] ++ accessing public static field
DEBUG FinalStaticFieldSingletonExtendedTest: [main] static value: 1
DEBUG FinalStaticFieldSingletonExtendedTest: [main] ++ accessing public static method
DEBUG FinalStaticFieldSingletonExtended: [main] static block initialization
DEBUG FinalStaticFieldSingletonExtended: [main] &gt;&gt; CONSTRUCTOR OF info.biosfood.singleton.FinalStaticFieldSingletonExtended
DEBUG FinalStaticFieldSingletonExtended: [main] static method test
DEBUG FinalStaticFieldSingletonExtendedTest: [main] ++ accessing getInstance method
DEBUG FinalStaticFieldSingletonExtended: [main] return an instance of info.biosfood.singleton.FinalStaticFieldSingletonExtended
DEBUG FinalStaticFieldSingletonExtended: [main] return an instance of info.biosfood.singleton.FinalStaticFieldSingletonExtended
```

As you see the constructor of the singleton has been invoked before the method `getInstance()` is invoked. 
The idea of lazy initialization is to create a resource only when the resource is retrieved. In this case the resource 
has been created before it was retrieved.

Lets take a look at extended version of `FinalStaticFieldInnerClassSingleton`. I added the same static block, field, method.

##### FinalStaticFieldInnerClassSingletonExtended
```java
package info.biosfood.singleton;

import org.apache.log4j.Logger;

public class FinalStaticFieldInnerClassSingletonExtended {

    private static final Logger LOG = Logger.getLogger(FinalStaticFieldInnerClassSingletonExtended.class);
    
    public static final int SOME_VALUE = 1;
    
    static {
        LOG.debug("static block initialization");
    }
    
    public static void test() {
        LOG.debug("static method test");
    }
    
    public static FinalStaticFieldInnerClassSingletonExtended getInstance() {
        LOG.debug("return an instance of " + FinalStaticFieldInnerClassSingletonExtended.class.getName());
        
        return SingletonHolder.instance;
    }
    
    private static class SingletonHolder {
        static {
            LOG.debug("SingletonHolder static block initialization");
        }
        static final FinalStaticFieldInnerClassSingletonExtended instance = new FinalStaticFieldInnerClassSingletonExtended();
    }
    
    private FinalStaticFieldInnerClassSingletonExtended() {
        LOG.debug("&gt;&gt; CONSTRUCTOR OF " + this.getClass().getName());
    }

}
```

##### Test
```java
@Test
public void testExtended() {
LOG.debug("--- Test FinalStaticFieldInnerClassSingletonExtended ---");

LOG.debug("++ accessing public static field");
LOG.debug(" static value: " + FinalStaticFieldInnerClassSingletonExtended.SOME_VALUE);

LOG.debug("++ accessing public static method");
FinalStaticFieldInnerClassSingletonExtended.test();

LOG.debug("++ accessing getInstanceMethod");

FinalStaticFieldInnerClassSingletonExtended s = FinalStaticFieldInnerClassSingletonExtended.getInstance();

assertEquals(s, FinalStaticFieldInnerClassSingletonExtended.getInstance());
}
```

##### Test output
```text
DEBUG FinalStaticFieldInnerClassSingletonExtended: [main] --- Test FinalStaticFieldInnerClassSingletonExtended ---
DEBUG FinalStaticFieldInnerClassSingletonExtended: [main] ++ accessing public static field
DEBUG FinalStaticFieldInnerClassSingletonExtended: [main]  static value: 1
DEBUG FinalStaticFieldInnerClassSingletonExtended: [main] ++ accessing public static method
DEBUG FinalStaticFieldInnerClassSingletonExtended: [main] static block initialization
DEBUG FinalStaticFieldInnerClassSingletonExtended: [main] static method test
DEBUG FinalStaticFieldInnerClassSingletonExtended: [main] ++ accessing getInstanceMethod
DEBUG FinalStaticFieldInnerClassSingletonExtended: [main] return an instance of info.biosfood.singleton.FinalStaticFieldInnerClassSingletonExtended
DEBUG FinalStaticFieldInnerClassSingletonExtended: [main] SingletonHolder static block initialization
DEBUG FinalStaticFieldInnerClassSingletonExtended: [main] &gt;&gt; CONSTRUCTOR OF info.biosfood.singleton.FinalStaticFieldInnerClassSingletonExtended
DEBUG FinalStaticFieldInnerClassSingletonExtended: [main] return an instance of info.biosfood.singleton.FinalStaticFieldInnerClassSingletonExtended
```

As you see the constructor is called only when the `instance` variable is read. It allows to add some static 
initialization to singleton and separate creation of the instance of the singleton.

## Conclusion
In this I covered two common ways of Singleton patter implementation in Java. Which is use is up to you, it depends only 
on your requirements. I prefer to use `FinalStaticFieldInnerClassSingleton` approach all time when I need to implement 
a Singleton pattern - I don't not encounter with issues of in advance initialization instead of lazy initialization.

## Useful links
- [Java specification. Loading, Linking, and Initializing](https://docs.oracle.com/javase/specs/jvms/se7/html/jvms-5.html)
- [Final modifier in a memory model](http://www.cs.umd.edu/~pugh/java/memoryModel/jsr-133-faq.html#finalRight)
- [JSR 133](http://www.cs.umd.edu/~pugh/java/memoryModel/jsr-133-faq.html)
