/*
 *  Copyright 2015 Adobe Systems Incorporated
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.aem.demo.service.schedulers;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A simple demo for cron-job like tasks that get executed regularly.
 * It also demonstrates how property values can be set. Users can
 * set the property values in /system/console/configMgr
 */
@Designate(ocd = SimpleScheduledTask.Config.class)
@Component(service = Runnable.class)
public class SimpleScheduledTask implements Runnable {
	/**
     * The Interface Config.
     */
    @ObjectClassDefinition(name = "A scheduled task",
                           description = "Simple demo for cron-job like task with properties")
    public static @interface Config {

    	 /**
         * Scheduler expression.
         *
         * @return the string
         */
        @AttributeDefinition(name = "Cron-job expression")
        String scheduler_expression() default "*/30 * * * * ?";

        /**
         * Concurrent task.
         *
         * @return the boolean
         */
        @AttributeDefinition(name = "Concurrent task",
                             description = "Whether or not to schedule this task concurrently")
        boolean scheduler_concurrent() default false;

        /**
         * A parameter.
         *
         * @return the string
         */
        @AttributeDefinition(name = "A parameter",
                             description = "Can be configured in /system/console/configMgr")
        String myParameter() default "";
    }

    /**
     * The logger
     */
    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * my parameter
     */
    private String myParameter;
    
    /**
     * run method
     */
    @Override
    public void run() {
        logger.debug("SimpleScheduledTask is now running, myParameter='{}'", myParameter);
    }

    /**
     * activate method
     * @param config
     * 		the config
     */
    @Activate
    protected void activate(final Config config) {
        myParameter = config.myParameter();
    }

}
