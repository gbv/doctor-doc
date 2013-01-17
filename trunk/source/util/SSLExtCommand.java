package util;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.SecurePlugInInterface;
import org.apache.struts.chain.commands.ActionCommandBase;
import org.apache.struts.chain.contexts.ActionContext;
import org.apache.struts.chain.contexts.ServletActionContext;
import org.apache.struts.config.ActionConfig;
import org.apache.struts.config.SecureActionConfig;
import org.apache.struts.util.SecureRequestUtils;

public class SSLExtCommand extends ActionCommandBase {
    
    public boolean execute(final ActionContext actionContext) throws Exception {
        
        final ActionConfig mapping = actionContext.getActionConfig();
        if (!(mapping instanceof SecureActionConfig)) {
            return false; // continue processing
        }
        
        // Cast to ServletActionContext
        final ServletActionContext sacontext = (ServletActionContext) actionContext;
        final ServletContext context = sacontext.getContext();
        final HttpServletRequest request = sacontext.getRequest();
        final HttpServletResponse response = sacontext.getResponse();
        
        // Check if the SecurePlugIn is configured
        final SecurePlugInInterface securePlugin = (SecurePlugInInterface) context
                .getAttribute(SecurePlugInInterface.SECURE_PLUGIN);
        if (securePlugin == null) {
            return false; // continue processing
        }
        
        if (SecureRequestUtils.checkSsl((SecureActionConfig) mapping, context, request, response)) {
            return true; // DON'T continue (request is being re-directed)
        } else {
            return false; // continue processing
        }
    }
}
