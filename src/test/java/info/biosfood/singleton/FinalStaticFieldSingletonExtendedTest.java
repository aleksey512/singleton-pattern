package info.biosfood.singleton;

import org.apache.log4j.Logger;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class FinalStaticFieldSingletonExtendedTest extends AbstractTest {

    static final Logger LOG = Logger.getLogger(FinalStaticFieldSingletonExtendedTest.class);

    public Logger getLogger() {
        return LOG;
    }

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

}
