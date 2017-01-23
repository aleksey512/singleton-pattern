package info.biosfood.singleton;

import org.apache.log4j.Logger;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class FinalStaticFieldInnerClassSingletonExtendedTest extends AbstractTest {

    static final Logger LOG = Logger.getLogger(FinalStaticFieldInnerClassSingletonExtended.class);

    public Logger getLogger() {
        return LOG;
    }

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

}
