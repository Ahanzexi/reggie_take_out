package com.zzx.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zzx.common.R;
import com.zzx.dto.DishDto;
import com.zzx.dto.SetmealDto;
import com.zzx.entity.Category;
import com.zzx.entity.Dish;
import com.zzx.entity.DishFlavor;
import com.zzx.entity.Setmeal;
import com.zzx.service.CategoryService;
import com.zzx.service.DishFlavorService;
import com.zzx.service.DishService;
import com.zzx.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RequestMapping("/setmeal")
@RestController
public class SetMealController {
    @Autowired
    private SetmealService setmealService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private DishService dishService;

    @Autowired
    private DishFlavorService dishFlavorService;
    /**
     * 新增套餐
     * @param setmealDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto){
        setmealService.saveWithDish(setmealDto);
        return R.success("新建套餐成功!");
    }

    /**
     * 分页查询套餐
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(Integer page,Integer pageSize,String name){
        // 分页构造器对象
        Page<Setmeal> pageInfo = new Page(page,pageSize);
        Page<SetmealDto> dtoPageInfo = new Page(page,pageSize);

        LambdaQueryWrapper<Setmeal>     queryWrapper = new LambdaQueryWrapper<>();
        // 查询条件
        queryWrapper.like(name!=null,Setmeal::getName,name);
        // 排序条件
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);

        setmealService.page(pageInfo,queryWrapper);

        // 对象拷贝
        BeanUtils.copyProperties(pageInfo,dtoPageInfo,"records");
        // 自己构造records
        final List<Setmeal> records = pageInfo.getRecords();
        final List<SetmealDto> newRecords;
        newRecords = records.stream().map((item)->{
            final SetmealDto setmealDto = new SetmealDto();
            // 拷贝数据
            BeanUtils.copyProperties(item,setmealDto);
            // 获取当前套餐分类的id 对应的分类
            final Category category = categoryService.getById(item.getCategoryId());
            if (category!=null){
                setmealDto.setCategoryName(category.getName());
            }
            return setmealDto;
        }).collect(Collectors.toList());
        dtoPageInfo.setRecords(newRecords);
        return R.success(pageInfo);
    }

    /**
     * 删除套餐
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids){
        setmealService.deleteWithDish(ids);
        return R.success("套餐删除成功!");
    }

    /**
     * 修改出售状态
     * @param ids
     * @param status
     * @return
     */
    @PostMapping("/status/{status}")
    public R<String> status(@RequestParam List<Long> ids, @PathVariable Integer status){
        List<Setmeal> setmealList= ids.stream().map((id)->{
            final Setmeal d = new Setmeal();
            d.setStatus(status);
            d.setId(id);
            return d;
        }).collect(Collectors.toList());
        setmealService.updateBatchById(setmealList);
        return R.success(status==0?"已禁售":"已启售");
    }

    /**
     * 编辑回显,根据id查询商品
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<SetmealDto> get(@PathVariable Long id){
        final SetmealDto setmealDto = setmealService.getByIdWithDish(id);
        return R.success(setmealDto);
    }


    /**
     * 修改菜品
     * @param setmealDto
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody SetmealDto setmealDto){
        log.info("修改菜品 {}",setmealDto);
        setmealService.updateWithDish(setmealDto);
        return R.success("修改菜品成功!");
    }

    public R<List<DishDto>> list(){

        return null;
    }

    /**
     * 根据条件查询套餐数据
     * @param setmeal
     * @return
     */
    @GetMapping("/list")
    public R<List<Setmeal>> list(Setmeal setmeal){
        final Long categoryId = setmeal.getCategoryId();
        final LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(categoryId!=null,Setmeal::getCategoryId,categoryId);
        wrapper.eq(Setmeal::getStatus,1);
        wrapper.orderByDesc(Setmeal::getUpdateTime);
        final List<Setmeal> list = setmealService.list(wrapper);
        return R.success(list);
    }
}
