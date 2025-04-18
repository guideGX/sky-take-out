package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.vo.SetmealVO;

import java.util.List;

public interface SetmealService {



    void savWithDish(SetmealDTO setmealDTO);

    PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);

    void deleteBacth(List<Long> ids);

    void update(SetmealDTO setmealDTO);

    SetmealVO getById(Long id);

    void startOrStop(Integer status, Long id);
}
