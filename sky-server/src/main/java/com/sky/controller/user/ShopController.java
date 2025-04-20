package com.sky.controller.user;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController("userShopController")
@RequestMapping("/user/shop")
@Api(tags = "店铺相关接口")
@Slf4j
//这里类的名称不可以和admin中相同，在ioc容器中会发生冲突
public class ShopController {

    public static final String KEY = "SHOP_STATUS";

    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping("/status")
    @ApiOperation("查询店铺状态")
    public Result<Long> getStatus() {
        Long status = (Long) redisTemplate.opsForValue().get(KEY);
        log.info("获取到店铺的营业状态为：{}", status == 1 ? "营业中" : "打样中");
        return Result.success(status);
    }
}