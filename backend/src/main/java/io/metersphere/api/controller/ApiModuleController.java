package io.metersphere.api.controller;

import io.metersphere.api.dto.definition.ApiModuleDTO;
import io.metersphere.api.dto.definition.DragModuleRequest;
import io.metersphere.api.service.ApiModuleService;
import io.metersphere.base.domain.ApiModule;
import io.metersphere.commons.constants.RoleConstants;
import io.metersphere.service.CheckOwnerService;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RequestMapping("/api/module")
@RestController
@RequiresRoles(value = {RoleConstants.ADMIN, RoleConstants.TEST_MANAGER, RoleConstants.TEST_USER, RoleConstants.TEST_VIEWER, RoleConstants.ORG_ADMIN}, logical = Logical.OR)
public class ApiModuleController {

    @Resource
    ApiModuleService apiModuleService;
    @Resource
    private CheckOwnerService checkOwnerService;

    @GetMapping("/list/{projectId}/{protocol}")
    public List<ApiModuleDTO> getNodeByProjectId(@PathVariable String projectId,@PathVariable String protocol) {
        checkOwnerService.checkProjectOwner(projectId);
        return apiModuleService.getNodeTreeByProjectId(projectId,protocol);
    }

    @GetMapping("/list/plan/{planId}/{protocol}")
    public List<ApiModuleDTO> getNodeByPlanId(@PathVariable String planId, @PathVariable String protocol) {
        checkOwnerService.checkTestPlanOwner(planId);
        return apiModuleService.getNodeByPlanId(planId, protocol);
    }

    @PostMapping("/add")
    @RequiresRoles(value = {RoleConstants.TEST_USER, RoleConstants.TEST_MANAGER}, logical = Logical.OR)
    public String addNode(@RequestBody ApiModule node) {
        return apiModuleService.addNode(node);
    }

    @PostMapping("/edit")
    @RequiresRoles(value = {RoleConstants.TEST_USER, RoleConstants.TEST_MANAGER}, logical = Logical.OR)
    public int editNode(@RequestBody DragModuleRequest node) {
        return apiModuleService.editNode(node);
    }

    @PostMapping("/delete")
    @RequiresRoles(value = {RoleConstants.TEST_USER, RoleConstants.TEST_MANAGER}, logical = Logical.OR)
    public int deleteNode(@RequestBody List<String> nodeIds) {
        //nodeIds 包含删除节点ID及其所有子节点ID
        return apiModuleService.deleteNode(nodeIds);
    }

    @PostMapping("/drag")
    @RequiresRoles(value = {RoleConstants.TEST_USER, RoleConstants.TEST_MANAGER}, logical = Logical.OR)
    public void dragNode(@RequestBody DragModuleRequest node) {
        apiModuleService.dragNode(node);
    }
}
