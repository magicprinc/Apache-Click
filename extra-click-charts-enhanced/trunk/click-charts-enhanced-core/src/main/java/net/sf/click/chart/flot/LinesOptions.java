package net.sf.click.chart.flot;


public class LinesOptions extends LinesPointsBarsOptions {

    private boolean steps;
  
	public boolean getSteps() {
		return steps;
	}
	public void setSteps(boolean steps) {
		this.steps = steps;
		add( "steps", steps );
	}

    
}//met
