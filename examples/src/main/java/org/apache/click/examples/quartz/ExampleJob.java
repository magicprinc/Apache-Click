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
      } catch (InterruptedException unused){
        unused.printStackTrace();
      }
    }

    System.out.println("\nSee I did it.");
  }

}