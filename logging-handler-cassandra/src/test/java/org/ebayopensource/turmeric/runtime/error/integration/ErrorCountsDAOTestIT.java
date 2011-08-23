package org.ebayopensource.turmeric.runtime.error.integration;

import static org.junit.Assert.*;

import me.prettyprint.cassandra.serializers.LongSerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.hector.api.beans.ColumnSlice;

import org.ebayopensource.turmeric.common.v1.types.ErrorCategory;
import org.ebayopensource.turmeric.common.v1.types.ErrorSeverity;
import org.ebayopensource.turmeric.runtime.error.cassandra.dao.ErrorCountsDAO;
import org.ebayopensource.turmeric.runtime.error.cassandra.model.Error;
import org.ebayopensource.turmeric.runtime.error.cassandra.model.ErrorValue;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class ErrorCountsDAOTestIT extends BaseIntegration {

    ErrorCountsDAO dao = null;

    @Before
    public void setUp() throws Exception {
        dao = new ErrorCountsDAO("Test Cluster", "localhost", "TurmericMonitoring");
    }

    @After
    public void tearDown() throws Exception {
        dao = null;
    }

    @Test
    public void testSaveErrorCounts() {
        Long now = System.currentTimeMillis();
        Error errorToSave = new Error();
        errorToSave.setCategory(ErrorCategory.REQUEST.toString());
        errorToSave.setSeverity(ErrorSeverity.ERROR.toString());
        errorToSave.setDomain("TestDomain");
        errorToSave.setErrorId(new Long(123));
        errorToSave.setName("TestError1");
        errorToSave.setOrganization("TestOrg1");
        errorToSave.setSubDomain("TestSubDomain");

        ErrorValue errorValue = new ErrorValue();
        errorValue.setErrorId(new Long(123));
        errorValue.setConsumerName("theTestConsumer");
        errorValue.setErrorMessage("The actual message");
        errorValue.setOperationName("Op1");
        errorValue.setServerName("TheServerName");
        errorValue.setServerSide(true);
        errorValue.setServiceAdminName("TheServiceAdminName");
        errorValue.setTimeStamp(now);
        int errorCountToStore = 1;

        String errorValueKey = "ErrorValueTestKey";
        dao.saveErrorCounts(errorToSave, errorValue, errorValueKey, now, errorCountToStore);

        // now, assert the count cf - first the category one
        ColumnSlice<Object, Object> categoryCountColumnSlice = getColumnValues("ErrorCountsByCategory",
                        "TheServerName-TheServiceAdminName-Op1-REQUEST", LongSerializer.get(),
                        StringSerializer.get(), new Long(now));
        assertValues(categoryCountColumnSlice, now, "ErrorValueTestKey");

        // now, assert the count cf - all ops
        ColumnSlice<Object, Object> categoryCountAllOpsColumnSlice = getColumnValues("ErrorCountsByCategory",
                        "TheServerName-TheServiceAdminName-All-REQUEST", LongSerializer.get(), StringSerializer.get(),
                        new Long(now));
        assertValues(categoryCountAllOpsColumnSlice, now, "ErrorValueTestKey");

        // now, assert the count cf - then the severity one
        ColumnSlice<Object, Object> severityCountColumnSlice = getColumnValues("ErrorCountsBySeverity",
                        "TheServerName-TheServiceAdminName-Op1-ERROR", LongSerializer.get(), StringSerializer.get(),
                        new Long(now));
        assertValues(severityCountColumnSlice, now, "ErrorValueTestKey");

        // now, assert the count cf - all ops
        ColumnSlice<Object, Object> severityCountAllOpsColumnSlice = getColumnValues("ErrorCountsBySeverity",
                        "TheServerName-TheServiceAdminName-All-ERROR", LongSerializer.get(), StringSerializer.get(),
                        new Long(now));
        assertValues(severityCountAllOpsColumnSlice, now, "ErrorValueTestKey");
    }

}
