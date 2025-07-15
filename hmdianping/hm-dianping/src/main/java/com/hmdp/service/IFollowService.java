package com.hmdp.service;

import com.hmdp.dto.Result;
import com.hmdp.entity.Follow;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 关注服务类
 */
public interface IFollowService extends IService<Follow> {

    /**
     * 关注或取关
     *
     * @param followUserId
     * @param isFollow
     * @return
     */
    Result follow(Long followUserId, Boolean isFollow);

    /**
     * 查询是否关注
     *
     * @param followUserId
     * @return
     */
    Result isFollow(Long followUserId);
}
