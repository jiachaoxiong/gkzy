package com.gkzy.college.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gkzy.college.entity.College;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CollegeMapper extends BaseMapper<College> {

    @Select("SELECT DISTINCT province FROM college ORDER BY province")
    List<String> selectProvinces();
}
