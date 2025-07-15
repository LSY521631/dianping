package com.hmdp.service;

import com.hmdp.dto.Result;
import com.hmdp.entity.Blog;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author 虎哥
 * @since 2021-12-22
 */
public interface IBlogService extends IService<Blog> {

    /**
     * 查询最热博客
     *
     * @param current
     * @return
     */
    Result queryHotBlog(Integer current);


    /**
     * 根据id查询博客详情
     *
     * @param id
     * @return
     */
    Result queryBlogById(Long id);

    /**
     * 点赞博客
     *
     * @param id
     * @return
     */
    Result likeBlog(Long id);

    /**
     * 查询博客点赞排行榜
     *
     * @param id
     * @return
     */
    Result queryBlogLikes(Long id);

    /**
     * 保存博客
     *
     * @param blog
     * @return
     */
    Result saveBlog(Blog blog);


    /**
     * 分页查询用户关注人发布得博客
     *
     * @param max
     * @param offset
     * @return
     */
    Result queryBlogOfFollow(Long max, Integer offset);
}
