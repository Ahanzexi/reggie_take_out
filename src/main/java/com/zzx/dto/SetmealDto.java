package com.zzx.dto;

import com.zzx.entity.Setmeal;
import com.zzx.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
