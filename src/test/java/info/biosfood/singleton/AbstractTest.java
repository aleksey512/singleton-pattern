package info.biosfood.singleton;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;

abstract public class AbstractTest {

    @Before
    public void before() {
        getLogger().debug("\n\n");
    }

    @After
    public void after() {
        getLogger().debug("\n\n");
    }

    abstract Logger getLogger();

}
