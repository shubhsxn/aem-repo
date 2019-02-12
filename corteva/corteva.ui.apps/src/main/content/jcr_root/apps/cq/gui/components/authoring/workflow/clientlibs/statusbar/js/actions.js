/*
 * ADOBE CONFIDENTIAL
 *
 * Copyright 2016 Adobe Systems Incorporated
 * All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains
 * the property of Adobe Systems Incorporated and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Adobe Systems Incorporated and its
 * suppliers and may be covered by U.S. and Foreign Patents,
 * patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Adobe Systems Incorporated.
 */

/**
 * Implements the actions that can be triggered on workflow statuses.
 * (See com.day.cq.wcm.workflow.impl.WorkflowResourceStatusProvider.java)
 */
;(function ($, ns, channel, window, undefined) {
    'use strict';

    // path to the page displaying the details of a work item
    var detailPagePath = '/mnt/overlay/cq/inbox/content/inbox/details/workitem';

    /**
     * Implements the action with id 'workflow-complete' allowing to complete a work item.
     */
    channel.on('click', '.editor-StatusBar-action[data-status-action-id=workflow-complete]', function () {
        var statusData = $(this).data('statusData');
        CQ.Inbox.UI.commons.completeWorkitem(statusData.workItemId);
    });

    /**
     * Implements the action with id 'workflow-stepback' allowing to step back in the workflow.
     */
    channel.on('click', '.editor-StatusBar-action[data-status-action-id=workflow-stepback]', function () {
        var statusData = $(this).data('statusData');
        CQ.Inbox.UI.commons.stepBackWorkitem(statusData.workItemId);
    });

    /**
     * Implements the action with id 'workflow-delegate' allowing to delegate a work item.
     */
    channel.on('click', '.editor-StatusBar-action[data-status-action-id=workflow-delegate]', function () {
        var statusData = $(this).data('statusData');
        CQ.Inbox.UI.commons.delegateWorkitem(statusData.workItemId);
    });

    /**
     * Implements the action with id 'workflow-viewdetails' allowing to view the details of a work item.
     */
    channel.on('click', '.editor-StatusBar-action[data-status-action-id=workflow-viewdetails]', function () {
        var statusData = $(this).data('statusData');
        var workItemId = statusData.workItemId;
        var url = Granite.HTTP.externalize(detailPagePath + '.html?item=' + workItemId);
        window.open(url);
    });

}(jQuery, Granite.author, jQuery(document), this));
