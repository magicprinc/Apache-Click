package net.sf.click.chart.jqplot.renderer;

import java.util.ArrayList;
import java.util.List;

import net.sf.click.chart.jqplot.Axis;
import net.sf.click.chart.jqplot.plugins.Plugin;

/**
 * A renderer for Categories axies. The renderer can be customized through its options.
 * @author steff
 * http://www.jqplot.com/docs/files/plugins/jqplot-dateAxisRenderer-js.html
 * <pre>
 * Dates can be passed into the axis in almost any recognizable value and will be parsed. 
 * They will be rendered on the axis in the format specified by tickOptions.formatString.  e.g. tickOptions.formatString = ‘%Y-%m-%d’.
Acceptable format codes are:

    Code    Result                  Description
                == Years ==
    %Y      2008                Four-digit year
    %y      08                  Two-digit year
                == Months ==
    %m      09                  Two-digit month
    %#m     9                   One or two-digit month
    %B      September           Full month name
    %b      Sep                 Abbreviated month name
                == Days ==
    %d      05                  Two-digit day of month
    %#d     5                   One or two-digit day of month
    %e      5                   One or two-digit day of month
    %A      Sunday              Full name of the day of the week
    %a      Sun                 Abbreviated name of the day of the week
    %w      0                   Number of the day of the week (0 = Sunday, 6 = Saturday)
    %o      th                  The ordinal suffix string following the day of the month
                == Hours ==
    %H      23                  Hours in 24-hour format (two digits)
    %#H     3                   Hours in 24-hour integer format (one or two digits)
    %I      11                  Hours in 12-hour format (two digits)
    %#I     3                   Hours in 12-hour integer format (one or two digits)
    %p      PM                  AM or PM
                == Minutes ==
    %M      09                  Minutes (two digits)
    %#M     9                   Minutes (one or two digits)
                == Seconds ==
    %S      02                  Seconds (two digits)
    %#S     2                   Seconds (one or two digits)
    %s      1206567625723       Unix timestamp (Seconds past 1970-01-01 00:00:00)
                == Milliseconds ==
    %N      008                 Milliseconds (three digits)
    %#N     8                   Milliseconds (one to three digits)
                == Timezone ==
    %O      360                 difference in minutes between local time and GMT
    %Z      Mountain Standard Time  Name of timezone as reported by browser
    %G      -06:00              Hours and minutes between GMT
                == Shortcuts ==
    %F      2008-03-26          %Y-%m-%d
    %T      05:06:30            %H:%M:%S
    %X      05:06:30            %H:%M:%S
    %x      03/26/08            %m/%d/%y
    %D      03/26/08            %m/%d/%y
    %#c     Wed Mar 26 15:31:00 2008  %a %b %e %H:%M:%S %Y
    %v      3-Sep-2008          %e-%b-%Y
    %R      15:31               %H:%M
    %r      3:31:00 PM          %I:%M:%S %p
                == Characters ==
    %n      \n                  Newline
    %t      \t                  Tab
    %%      %                   Percent Symbol
 * </pre>
 */
public class DateAxisRenderer extends AxisRenderer implements Plugin {

	public DateAxisRenderer() {
		this( null );
	}//cons

	public DateAxisRenderer( Axis axis ) {
		super( "$.jqplot.DateAxisRenderer", axis );
	}//cons
	
	@Override
	public List<String> getListJavaScriptImport() {
		List<String> listJavaScriptImport = new ArrayList<String>();
		//javascript imports
		listJavaScriptImport.add("/jqplot/plugins/jqplot.dateAxisRenderer.js");
		return listJavaScriptImport;
	}

	/** A class of a rendering engine for creating the ticks labels displayed on the plot, See $.jqplot.AxisTickRenderer. */
	private Renderer tickRenderer = new AxisTickRenderer();

	public Renderer getTickRenderer() {
		return tickRenderer;
	}//met

	public void setTickRenderer(Renderer tickRenderer) {
		this.tickRenderer = tickRenderer;
		//TODO that is ugly too, it should be tick renderer options
		options.add( "tickRenderer", tickRenderer, "tickOptions" );
	}//met
}//class
