package com.zzx.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zzx.entity.AddressBook;
import com.zzx.entity.User;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface AddressBookMapper extends BaseMapper<AddressBook> {
}
