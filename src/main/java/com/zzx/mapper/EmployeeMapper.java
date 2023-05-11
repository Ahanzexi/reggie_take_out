package com.zzx.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zzx.entity.Employee;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}
