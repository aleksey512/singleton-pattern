package info.biosfood.singleton;

import org.apache.log4j.Logger;

public class FinalStaticFieldSingleton {

    static final Logger LOG = Logger.getLogger(FinalStaticFieldSingleton.class);

    private static final FinalStaticFieldSingleton instance = new FinalStaticFieldSingleton();

    public static FinalStaticFieldSingleton getInstance() {
        LOG.debug("return an instance of " + FinalStaticFieldSingleton.class.getName());
        return instance;
    }

    private FinalStaticFieldSingleton() {
        LOG.debug(">> CONSTRUCTOR OF " + this.getClass().getName());
    }

}
