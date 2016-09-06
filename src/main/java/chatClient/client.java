/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatClient;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 *
 * @author Michael
 */
public class client {
    
    
    public String timeStamp() {
       Calendar calendar = new GregorianCalendar();
       int HOUR = calendar.get(Calendar.HOUR);
       int MINUTE = calendar.get(Calendar.MINUTE);
       int SECOND = calendar.get(Calendar.SECOND);
       
       return HOUR + ":" + MINUTE + ":" + SECOND + ": ";
       
    }
}
