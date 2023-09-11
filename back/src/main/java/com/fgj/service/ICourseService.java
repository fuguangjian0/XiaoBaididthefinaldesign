package com.fgj.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fgj.entity.Course;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author 付广建
 * @since 2023-09-06
 */
public interface ICourseService extends IService<Course> {

    Page<Course> findPage(Page<Course> page, String name);

    void setStudentCourse(Integer courseId, Integer studentId);

}
