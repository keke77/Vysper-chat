package org.jahia.modules.minaChat.client;

import org.jahia.api.Constants;
import org.jahia.bin.Action;
import org.jahia.bin.ActionResult;
import org.jahia.bin.Render;
import org.jahia.modules.minaChat.MinaServerService;
import org.jahia.services.content.JCRNodeWrapper;
import org.jahia.services.content.JCRSessionFactory;
import org.jahia.services.content.JCRSessionWrapper;
import org.jahia.services.render.RenderContext;
import org.jahia.services.render.Resource;
import org.jahia.services.render.URLResolver;
import org.slf4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: fabriceaissah
 * Date: 10/15/12
 * Time: 4:11 PM
 * To change this template use File | Settings | File Templates.
 */

public class RegisterUser extends Action {
    private transient static Logger logger = org.slf4j.LoggerFactory.getLogger(RegisterUser.class);
    private MinaServerService minaServerService;

    public void setMinaServerService(MinaServerService minaServerService){
        this.minaServerService = minaServerService;
    }

    public ActionResult doExecute(HttpServletRequest req, RenderContext renderContext, Resource resource,
                                                JCRSessionWrapper session, Map<String, List<String>> parameters, URLResolver urlResolver) throws Exception {

        String nodePath = session.getUser().getLocalPath();
        JCRNodeWrapper node = session.getNode(nodePath);
        JCRSessionWrapper jcrSessionWrapper = node.getSession();

        if(!node.isNodeType("chatmix:chatUser")) {
            node.checkout();
            node.addMixin("chatmix:chatUser");
            jcrSessionWrapper.save();
            logger.info("mixin chatUser added to user:" + node.getPath());
        }else{
            logger.info(node.getDisplayableName() + " already added to mina chat");
        }

        minaServerService.addUser(node.getName());


        return new ActionResult(HttpServletResponse.SC_OK, node.getPath(), Render.serializeNodeToJSON(node));
    }
}
