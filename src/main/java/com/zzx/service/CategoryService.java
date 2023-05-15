package com.zzx.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zzx.entity.Category;

public interface CategoryService extends IService<Category> {
    public void remove(Long id);
}
