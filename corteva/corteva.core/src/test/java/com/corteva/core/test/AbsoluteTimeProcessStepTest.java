package com.corteva.core.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.Workflow;
import com.adobe.granite.workflow.exec.WorkflowData;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import com.adobe.granite.workflow.metadata.SimpleMetaDataMap;
import com.corteva.core.base.tests.BaseAbstractTest;
import com.corteva.core.constants.CortevaConstant;
import com.corteva.core.workflow.AbsoluteTimeProcessStep;

/**
 * The Class AbsoluteTimeProcessStepTest.
 */
public class AbsoluteTimeProcessStepTest extends BaseAbstractTest {

	/** The workflow session. */
	@Mock
	private WorkflowSession workflowSession;

	/** The process. */
	@InjectMocks
	private AbsoluteTimeProcessStep process;

	/** The work flow. */
	@Mock
	private Workflow workFlow;

	/** The work item. */
	private WorkItem workItem;

	/** The meta data. */
	private MetaDataMap metaData;

	/** The work flow data. */
	@Mock
	private WorkflowData workFlowData;

	/**
	 * Sets the up.
	 *
	 * @throws Exception
	 *             the exception
	 */
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		metaData = new SimpleMetaDataMap();
		workItem = Mockito.mock(WorkItem.class);
		Mockito.when(workItem.getWorkflow()).thenReturn(workFlow);
		Mockito.when(workItem.getWorkflowData()).thenReturn(workFlowData);
		Mockito.when(workFlowData.getMetaDataMap()).thenReturn(metaData);
		Mockito.when(workFlow.getMetaDataMap()).thenReturn(metaData);

	}

	/**
	 * Test put absolute time when selected.
	 *
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testPutAbsoluteTimeWhenSelected() throws Exception {
		String absoluteDate = "2018-06-09T07:45+10:00";
		metaData.put(CortevaConstant.ABSOLUTE_DATE, absoluteDate);
		process.execute(workItem, workflowSession, metaData);
	}

	/**
	 * Test put absolute time when not selected.
	 *
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testPutAbsoluteTimeWhenNotSelected() throws Exception {
		process.execute(workItem, workflowSession, metaData);
	}

}
