package com.sky.service.impl;

import com.sky.dto.SetmealDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.service.SetmealService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;


    /**
     * 新增套餐与菜品关系
     * @param setmealDTO
     */
    public void savWithDish(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        //套餐基础信息拷贝
        BeanUtils.copyProperties(setmealDTO, setmeal);
        //向套餐表中插入数据
        setmealMapper.insert(setmeal);

        Long setmealId = setmeal.getId();
        //向套餐菜品关系表中插入数据
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        if(setmealDishes!=null && setmealDishes.size() > 0) {
            //设置套餐id
            for (SetmealDish setmealDish : setmealDishes) {
                setmealDish.setSetmealId(setmealId);
            }
        }
        setmealDishMapper.insertBatch(setmealDishes);
    }
}
