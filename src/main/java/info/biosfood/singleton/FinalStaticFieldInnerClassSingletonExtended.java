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
        LOG.debug(">> CONSTRUCTOR OF " + this.getClass().getName());
    }

}
