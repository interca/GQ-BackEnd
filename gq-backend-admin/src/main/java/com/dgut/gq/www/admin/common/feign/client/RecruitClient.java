package com.dgut.gq.www.admin.common.feign.client;

import com.dgut.gq.www.admin.common.model.dto.DepartmentDto;
import com.dgut.gq.www.admin.common.model.dto.PositionDto;
import com.dgut.gq.www.common.common.SystemJsonResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * Feign招新模块客户端
 */
@FeignClient(name = "gq-backend-recruit",contextId = "recruit")
public interface RecruitClient {



    /**
     * 远程调用导出简历
     * @param
     * @return
     */
    @GetMapping("/feign-recruit/exportCurriculumVitae")
    SystemJsonResponse exportCurriculumVitae(@RequestParam String departmentId, @RequestParam Integer term);



    /**
     * 删除部门远程调用
     * @param id
     * @return
     */
    @DeleteMapping("/feign-recruit/deleteDepartment/{id}")
    SystemJsonResponse deleteDepartment(@PathVariable String id);



    /**
     * 删除职位远程调用
     * @param id
     * @return
     */
    @DeleteMapping("/feign-recruit/deletePosition/{id}")
    public SystemJsonResponse deletePosition(@PathVariable String id);


    /**
     * 新增或者修改部门远程调用
     * @param departmentDto
     * @return
     */
    @PostMapping("/feign-recruit/saveAndUpdateDep")
    SystemJsonResponse saveAndUpdateDep(@RequestBody DepartmentDto departmentDto);



    /**
     * 新增或者修改职位远程调用
     * @param positionDto
     * @return
     */
    @PostMapping("/feign-recruit/saveAndUpdatePos")
    SystemJsonResponse saveAndUpdatePos(@RequestBody PositionDto positionDto);


}
