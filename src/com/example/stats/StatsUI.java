package com.example.stats;

import java.awt.Toolkit;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.annotation.WebServlet;

import com.github.wolfie.refresher.Refresher;
import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@Push
@SuppressWarnings("serial")
@Theme("stats")
public class StatsUI extends UI {

	@WebServlet(value = "/*", asyncSupported = true)
	@VaadinServletConfiguration(productionMode = false, ui = StatsUI.class)
	public static class Servlet extends VaadinServlet {
	}
	Double hehe = new Double(0);
	String nu = new String();
	Date date1 = null;
	Date date2 = null;
	final Double sutaupau = new Double(0.001534247); //LTL i minute
	
	 Label label = new Label( "Now : " );
	 Label ltl = new Label("Currently saved LTL: ");
	 Label autobusas = new Label("???");
	 
	final VerticalLayout layout = new VerticalLayout();

	 
	@Override
	protected void init(VaadinRequest request) {
		//final VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		setContent(layout);
		
		
		layout.addComponent(label);
		layout.addComponent(ltl);
		layout.addComponent(autobusas);
		

		
		 new FeederThread().start();
			
		

	}	
		
	public void tellTime ()
    {
        label.setValue( "Now : " + new java.util.Date() ); // If Java 8, use: Instant.now(). Or, in Joda-Time: DateTime.now().
    }

    class FeederThread extends Thread
    {

        int count = 0;

        @Override
        public void run ()
        {
            try {
                // Update the data for a while
                while ( count < 100 ) {
                    Thread.sleep( 1000 );

                    // Calling special 'access' method on UI object, for inter-thread communication.
                    access( new Runnable()
                    {
                        @Override
                        public void run ()
                        {
                            count ++;
                            tellTime();
                        	DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                			Calendar cal = Calendar.getInstance();
                		

                			String string1 = "2013/06/01 00:00:00";
                			String string2 = dateFormat.format(cal.getTime());

                			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.ENGLISH);

                			
                			try {
                				date1 = sdf.parse(string1);
                			} catch (ParseException e) {
                				// TODO Auto-generated catch block
                				e.printStackTrace();
                			}
                			
                			try {
                				date2 = sdf.parse(string2);
                			} catch (ParseException e) {
                				// TODO Auto-generated catch block
                				e.printStackTrace();
                			}
                			
                            long minutes = ((date2.getTime()-date1.getTime())/1000)/60;
 
            				hehe = minutes * sutaupau;
            				
            				nu = String.valueOf(hehe);
  
                            autobusas.setValue(nu);
                            
                        }
                    } );
                }

                // Inform that we have stopped running
                // Calling special 'access' method on UI object, for inter-thread communication.
                access( new Runnable()
                {
                    @Override
                    public void run ()
                    {
                        label.setValue( "Done." );
                    }
                } );
            } catch ( InterruptedException e ) {
                e.printStackTrace();
            }
        }
    }
		
		
		///////
		/*
		Calendar start = Calendar.getInstance();
		start.setTime(date1);
		Calendar end = Calendar.getInstance();
		end.setTime(date2);

		Integer[] elapsed = new Integer[6];
		Calendar clone = (Calendar) start.clone(); // Otherwise changes are been reflected.
		elapsed[0] = elapsed(clone, end, Calendar.YEAR);
		clone.add(Calendar.YEAR, elapsed[0]);
		elapsed[1] = elapsed(clone, end, Calendar.MONTH);
		clone.add(Calendar.MONTH, elapsed[1]);
		elapsed[2] = elapsed(clone, end, Calendar.DATE);
		clone.add(Calendar.DATE, elapsed[2]);
		elapsed[3] = (int) (end.getTimeInMillis() - clone.getTimeInMillis()) / 3600000;
		clone.add(Calendar.HOUR, elapsed[3]);
		elapsed[4] = (int) (end.getTimeInMillis() - clone.getTimeInMillis()) / 60000;
		clone.add(Calendar.MINUTE, elapsed[4]);
		elapsed[5] = (int) (end.getTimeInMillis() - clone.getTimeInMillis()) / 1000;

		System.out.format("%d years, %d months, %d days, %d hours, %d minutes, %d seconds", elapsed);
		/*/
	
	/*
	 * }
	public static int elapsed(Calendar before, Calendar after, int field) {
	    Calendar clone = (Calendar) before.clone(); // Otherwise changes are been reflected.
	    int elapsed = -1;
	    while (!clone.after(after)) {
	        clone.add(field, 1);
	        elapsed++;
	    }
	    return elapsed;
	}
*/
	
	
}
