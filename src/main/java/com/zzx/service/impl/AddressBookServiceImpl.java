package com.zzx.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzx.entity.AddressBook;
import com.zzx.entity.User;
import com.zzx.mapper.AddressBookMapper;
import com.zzx.mapper.UserMapper;
import com.zzx.service.AddressBookService;
import com.zzx.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {

}
