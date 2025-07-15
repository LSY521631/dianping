package com.hmdp.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hmdp.dto.Result;
import com.hmdp.entity.Follow;
import com.hmdp.mapper.FollowMapper;
import com.hmdp.service.IFollowService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hmdp.utils.UserHolder;
import org.springframework.stereotype.Service;

/**
 * 服务实现类
 */
@Service
public class FollowServiceImpl extends ServiceImpl<FollowMapper, Follow> implements IFollowService {

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

        //判断是关注还是取消
        if (isFollow) {
            //关注
            Follow follow = new Follow();
            follow.setUserId(userId);
            follow.setFollowUserId(followUserId);//关注用户
            save(follow);

        } else {
            //取关，删除
            remove(new QueryWrapper<Follow>()
                    .eq("user_id", userId).eq("follow_user_id", followUserId));
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
}
