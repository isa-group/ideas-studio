package es.us.isa.ideas.app.controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import es.us.isa.ideas.app.entities.Tag;
import es.us.isa.ideas.app.entities.Workspace;
import es.us.isa.ideas.app.services.TagService;
import es.us.isa.ideas.app.services.WorkspaceService;

/**
 *
 * @author danyal
 */
@Controller
@RequestMapping("/tags")
public class TagController extends AbstractController {

    @Autowired
    TagService tagService;

    @Autowired
    WorkspaceService workspaceService;

    @RequestMapping(value = "", method = RequestMethod.GET, 
                    produces = "application/json")
    @ResponseBody
    public Collection<Tag> list() {
        Collection<Tag> tags = tagService.findAll();
        return tags;
    }

    @RequestMapping(value = "/{tagName}/workspaces", method = RequestMethod.GET, 
                    produces = "application/json")
    @ResponseBody
    public Collection<Workspace> getTaggedWorkspaces(@PathVariable("tagName") String tagName) {
        Collection<Workspace> res = workspaceService.findByTagName(tagName);
        return res;
    }
}
