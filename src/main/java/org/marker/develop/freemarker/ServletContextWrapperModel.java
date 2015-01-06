package org.marker.develop.freemarker;
  
import javax.servlet.GenericServlet;
import javax.servlet.ServletContext;

import freemarker.template.ObjectWrapper;
import freemarker.template.TemplateHashModel;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

/**
 * TemplateHashModel wrapper for a ServletContext attributes.
 * @author Attila Szegedi marker
 * @version $Id: ServletContextHashModel.java,v 1.12 2003/01/12 23:40:14 revusky Exp $
 */
public final class ServletContextWrapperModel implements TemplateHashModel
{ 
    private final ServletContext ctx;
    private final ObjectWrapper wrapper;

    
    
    /**
     * 	
     * {@link #ServletContextHashModel(GenericServlet, ObjectWrapper)} instead.
     */
    public ServletContextWrapperModel(ServletContext servletctx, ObjectWrapper wrapper)
    { 
        this.ctx = servletctx;
        this.wrapper = wrapper;
    }

    public TemplateModel get(String key) throws TemplateModelException
    {
        return wrapper.wrap(ctx.getAttribute(key));
    }

    public boolean isEmpty()
    {
        return !ctx.getAttributeNames().hasMoreElements();
    }
    
}
