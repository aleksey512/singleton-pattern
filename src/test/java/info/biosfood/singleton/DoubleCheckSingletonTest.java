package info.biosfood.singleton;

import org.apache.log4j.Logger;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class DoubleCheckSingletonTest extends AbstractTest {

    static final Logger LOG = Logger.getLogger(DoubleCheckSingletonTest.class);

    public Logger getLogger() {
        return LOG;
    }

    @Test
    public void test() {
        LOG.debug("--- Test DoubleCheckSingleton ---");

        DoubleCheckSingleton s = DoubleCheckSingleton.getInstance();

        assertEquals(s, DoubleCheckSingleton.getInstance());
    }

}
