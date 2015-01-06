package org.marker.develop.freemarker;




import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import freemarker.template.ObjectWrapper;
import freemarker.template.TemplateHashModel;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

/**
 * 
 * 该类实例需要被持久化，如做服务器集群时
 * TemplateHashModel wrapper for a HttpSession attributes.
 * @author Attila Szegedi marker
 */

public final class SessionWrapperModel implements TemplateHashModel, Serializable
{
	/**  */
	public static final String ATTR_SESSION_MODEL = ".freemarker.Session";
	
    private static final long serialVersionUID = 1L;
    private transient HttpSession session;
    private transient final ObjectWrapper wrapper;
    
    private transient final HttpServletRequest request; 
    
    
    
    
    /**
     * Use this constructor when the session already exists.
     * @param session the session
     * @param wrapper an object wrapper used to wrap session attributes
     */
    public SessionWrapperModel(HttpSession session, ObjectWrapper wrapper)
    {
        this.session = session;
        this.wrapper = wrapper;
        this.request = null; 
    }

    /**
     * Use this constructor when the session isn't already created. It is passed
     * enough parameters so that the session can be properly initialized after
     * it's detected that it was created. 
     * 需要创建Session哦
     * @param request the actual request 
     * @param wrapper an object wrapper used to wrap session attributes
     */
    public SessionWrapperModel(HttpServletRequest request, ObjectWrapper wrapper)
    {
        this.wrapper = wrapper;
        this.request = request;
    }

    public TemplateModel get(String key) throws TemplateModelException
    {
        checkSessionExistence();
        return wrapper.wrap(session != null ? session.getAttribute(key) : "");
    }

    private void checkSessionExistence() throws TemplateModelException
    {
        if(session == null && request != null) {
            session = request.getSession(false);// 如果没有Session则创建并获取
            if(session != null ) {
            	SessionWrapperModel model = new SessionWrapperModel(session, wrapper);
                session.setAttribute(ATTR_SESSION_MODEL, model); 
            }
        }
    }

    boolean isOrphaned(HttpSession currentSession) {
        return (session != null && session != currentSession) || 
            (session == null && request == null);
    }
    
    public boolean isEmpty() throws TemplateModelException {
        checkSessionExistence();
        return session == null || !session.getAttributeNames().hasMoreElements();
    }
}
