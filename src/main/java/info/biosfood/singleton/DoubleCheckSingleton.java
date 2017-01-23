package info.biosfood.singleton;

import org.apache.log4j.Logger;

public class DoubleCheckSingleton {

    static final Logger LOG = Logger.getLogger(DoubleCheckSingleton.class);

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
        LOG.debug(">> CONSTRUCTOR OF " + this.getClass().getName());
    }

}
