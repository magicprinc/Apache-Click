/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.click.examples.quartz;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * Provides a simple example Quartz Job class which writes 'Hello World' to
 * System.out and then counts to 10.
 */
public class ExampleJob implements Job {

    /**
     * {@link org.quartz.Job#execute(JobExecutionContext)}
     */
    @Override public void execute(JobExecutionContext context) throws JobExecutionException {
        System.out.println("Hello World from " + getClass().getSimpleName() + "@" + hashCode());
        System.out.println("I can count to 10.");

        for (int i = 1; i <= 10; i++) {
            System.out.print(i + " ");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ie) {
                // ignore
            }
        }

        System.out.println("\nSee I did it.");
    }

}