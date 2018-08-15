package org.marker.mushroom.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;



/**
 *
 *
 * 统计在线人数
 *
 *
 * @author marker
 * */
public class SessionCounter implements HttpSessionListener {
    /**
     * 日志记录
     */
	private final Logger log = LoggerFactory.getLogger(SessionCounter.class);
	
	
	/** Session个数代码${sessions} */
	public static final String SESSION_COUNT = "sessions";
	
	
	@Override
	public void sessionCreated(HttpSessionEvent event) { 
        ServletContext ctx = event.getSession().getServletContext();
        Integer numSessions = (Integer) ctx.getAttribute(SESSION_COUNT); 
        if (numSessions == null) { 
            numSessions = new Integer(1); 
        } else { 
            int count = numSessions.intValue(); 
            numSessions = new Integer(count + 1); 
        }
        log.info("create a session. current sessions:" + numSessions+" count.");
        ctx.setAttribute(SESSION_COUNT, numSessions); 
    } 
	
	
	
	
    @Override
	public void sessionDestroyed(HttpSessionEvent event) { 
        ServletContext ctx = event.getSession().getServletContext( );
        Integer numSessions = (Integer) ctx.getAttribute(SESSION_COUNT); 
        if (numSessions == null) { 
            numSessions = new Integer(0); 
        } else { 
            int count = numSessions.intValue( );
            if( count > 0 ){
            	numSessions = new Integer(count - 1); 
            }
        }
        log.info("destroy a session. current sessions:" + numSessions+" count.");
        ctx.setAttribute(SESSION_COUNT, numSessions); 
    }
    
    
} 