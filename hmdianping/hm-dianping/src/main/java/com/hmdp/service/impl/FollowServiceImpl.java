package com.hmdp.service.impl;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hmdp.dto.Result;
import com.hmdp.dto.UserDTO;
import com.hmdp.entity.Follow;
import com.hmdp.mapper.FollowMapper;
import com.hmdp.service.IFollowService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hmdp.service.IUserService;
import com.hmdp.utils.UserHolder;
import jodd.util.CollectionUtil;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.hmdp.utils.RedisConstants.FOLLOWS;

/**
 * 服务实现类
 */
@Service
public class FollowServiceImpl extends ServiceImpl<FollowMapper, Follow> implements IFollowService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private IUserService userService;


    /**
     * 关注或取关
     *
     * @param followUserId
     * @param isFollow
     * @return
     */
    @Override
    public Result follow(Long followUserId, Boolean isFollow) {
        //获取登录用户
        Long userId = UserHolder.getUser().getId();
        String key = FOLLOWS + userId;

        //判断是关注还是取消
        if (isFollow) {
            //关注
            Follow follow = new Follow();
            follow.setUserId(userId);
            follow.setFollowUserId(followUserId);//关注用户
            boolean isSuccess = save(follow);

            //把关注成功的用户id,存入redis的set集合
            if (isSuccess) {
                stringRedisTemplate.opsForSet().add(key, followUserId.toString());
            }

        } else {
            //取关，删除
            boolean isSuccess = remove(new QueryWrapper<Follow>()
                    .eq("user_id", userId).eq("follow_user_id", followUserId));

            //把取关的用户id,在redis的set集合移除
            if (isSuccess) {
                stringRedisTemplate.opsForSet().remove(key, followUserId.toString());
            }

        }

        //返回
        return Result.ok();
    }


    /**
     * 判断是否关注
     *
     * @param followUserId
     * @return
     */
    @Override
    public Result isFollow(Long followUserId) {
        //获取登录用户
        Long userId = UserHolder.getUser().getId();
        //查询
        Integer count = query().eq("user_id", userId).eq("follow_user_id", followUserId).count();
        //判断
        return Result.ok(count > 0);
    }

    /**
     * 共同关注
     *
     * @param id
     * @return
     */
    @Override
    public Result followCommon(Long id) {
        //获取当前登录用户
        Long userId = UserHolder.getUser().getId();
        String key = FOLLOWS + userId;
        //获取查询用户的key
        String key2 = FOLLOWS + id;

        //求交集
        Set<String> intersect = stringRedisTemplate.opsForSet().intersect(key, key2);

        //无交集
        if (intersect == null || intersect.isEmpty()) {
            return Result.ok(Collections.emptyList());
        }

        //解析id集合
        List<Long> ids = intersect.stream()
                .map(Long::valueOf)
                .collect(Collectors.toList());

        //查询用户
        List<UserDTO> userDTOS = userService.listByIds(ids)
                .stream()
                .map(user -> BeanUtil.copyProperties(user, UserDTO.class))
                .collect(Collectors.toList());

        return Result.ok(userDTOS);
    }
}
