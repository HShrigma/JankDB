// JankDBTestSuite.java
package jankdb;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
        DBFileTest.class,
        RecordTest.class,
        TableTest.class,
        REPLCLIManagerTest.class
})

public class JankDBTestSuite {

}