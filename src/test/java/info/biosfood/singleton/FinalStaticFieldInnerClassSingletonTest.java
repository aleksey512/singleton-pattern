package info.biosfood.singleton;

import org.apache.log4j.Logger;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class FinalStaticFieldInnerClassSingletonTest extends AbstractTest {

    static final Logger LOG = Logger.getLogger(FinalStaticFieldInnerClassSingleton.class);

    public Logger getLogger() {
        return LOG;
    }

    @Test
    public void test() {
        LOG.debug("--- Test FinalStaticFieldInnerClassSingleton ---");

        LOG.debug("++ accessing getInstance method");
        FinalStaticFieldInnerClassSingleton s = FinalStaticFieldInnerClassSingleton.getInstance();

        assertEquals(s, FinalStaticFieldInnerClassSingleton.getInstance());
    }

}
