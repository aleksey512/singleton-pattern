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
        LOG.debug(">> CONSTRUCTOR OF " + this.getClass().getName());
    }

}
