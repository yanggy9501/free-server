package com.freeing.resourceserver.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class InfoController {

//    @PostMapping("/course/list")
//    @PreAuthorize("hasAnyAuthority('course_find_list')")
//  public PageResult<CourseBase> list(PageParams params, @RequestBody QueryCourseParamsDto queryCourseParamsDto){
//        //当前登录用户
//        SecurityUtil.XcUser user = SecurityUtil.getUser();
//        //机构id
//        String companyId = user.getCompanyId();
//        //调用service获取数据
//        PageResult<CourseBase> courseBasePageResult = courseBaseInfoService.queryCourseBaseList(params, queryCourseParamsDto);
//
//        return  courseBasePageResult;
//    }

//    @PostMapping("/course")
//    public CourseBaseInfoDto createCourseBase(@RequestBody @Validated(ValidationGroups.Inster.class) AddCourseDto addCourseDto){
//
//        Long companyId = 22L;
//        return courseBaseInfoService.createCourseBase(companyId,addCourseDto);
//    }


    @GetMapping("/course/{courseId}")
    public Object getCourseBaseById(@PathVariable Long courseId) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println(principal);
        return principal;
    }

//    @PutMapping("/course")
//    public CourseBaseInfoDto modifyCourseBase(@RequestBody EditCourseDto dto){
//
//        Long companyId =1232141425L;
//        return courseBaseInfoService.updateCourseBase(companyId,dto);
//    }
}
