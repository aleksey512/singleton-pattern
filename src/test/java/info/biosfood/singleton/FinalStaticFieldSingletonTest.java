package info.biosfood.singleton;

import org.apache.log4j.Logger;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class FinalStaticFieldSingletonTest extends AbstractTest {

    static final Logger LOG = Logger.getLogger(FinalStaticFieldSingleton.class);

    public Logger getLogger() {
        return LOG;
    }



    @Test
    public void test() {
        LOG.debug("--- Test FinalStaticFieldSingleton ---");

        LOG.debug("++ accessing getInstance method");
        FinalStaticFieldSingleton s = FinalStaticFieldSingleton.getInstance();

        assertEquals(s, FinalStaticFieldSingleton.getInstance());
    }

}
