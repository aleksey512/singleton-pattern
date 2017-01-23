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
        LOG.debug(">> CONSTRUCTOR OF " + this.getClass().getName());
    }

}
