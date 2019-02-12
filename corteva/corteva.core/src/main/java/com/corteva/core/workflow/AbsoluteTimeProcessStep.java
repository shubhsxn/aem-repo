package com.corteva.core.workflow;

import java.util.Date;

import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowProcess;
import com.adobe.granite.workflow.metadata.MetaDataMap;



/**
 * The Class AbsoluteTimeProcessStep.
 */

@Component(service = WorkflowProcess.class,immediate = true,property = {"process.label = Absolute Time Processor"})
public class AbsoluteTimeProcessStep implements WorkflowProcess {
	
	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger(AbsoluteTimeProcessStep.class);

	/**
	 * The Constant is used to store page absolute time for page publish and
	 * unpublish
	 */
	private static final String ABSOLUTE_TIME = "absoluteTime";

	/* (non-Javadoc)
	 * @see com.adobe.granite.workflow.exec.WorkflowProcess#execute(com.adobe.granite.workflow.exec.WorkItem, com.adobe.granite.workflow.WorkflowSession, com.adobe.granite.workflow.metadata.MetaDataMap)
	 */
	@Override
	public void execute(WorkItem workItem, WorkflowSession session, MetaDataMap metaDataMap) throws WorkflowException {
		LOGGER.info("entering execute method of AbsoluteTimeProcessStep");

			//put the current time as absolute time when absoluteDate is not selected for immediate activation
			workItem.getWorkflowData().getMetaDataMap().put(ABSOLUTE_TIME,new Date().getTime());

		LOGGER.info("exiting execute method of AbsoluteTimeProcessStep");
	}

}
