package com.fgj.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fgj.entity.Course;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 付广建
 * @since 2023-09-06
 */
public interface CourseMapper extends BaseMapper<Course> {

    Page<Course> findPage(Page<Course> page, @Param("name") String name);

    void setStudentCourse(@Param("courseId") Integer courseId, @Param("studentId") Integer studentId);
    void deleteStudentCourse(@Param("courseId") Integer courseId, @Param("studentId") Integer studentId);


}
