package com.hmdp.intercept;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.hmdp.dto.UserDTO;
import com.hmdp.utils.RedisConstants;
import com.hmdp.utils.UserHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.hmdp.utils.RedisConstants.LOGIN_USER_TTL;


/**
 * 刷新token拦截器
 */
@Slf4j
public class RefreshTokenInterceptor implements HandlerInterceptor {

    //此处不能使用注解，为什么？LoginIntercept类是手动创建的，不是spring帮我创建的，无法依赖注入
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 构造函数
     *
     * @param stringRedisTemplate
     */
    public RefreshTokenInterceptor(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    /**
     * 拦截器
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        //获取请求中的token
        String token = request.getHeader("authorization");
        if (StrUtil.isBlank(token)) {
            //不存在，直接放行，再第二个拦截器做拦截
            return true;
        }

        //基于token获取redis中的用户
        String key = RedisConstants.LOGIN_USER_KEY + token;
        Map<Object, Object> userMap = stringRedisTemplate.opsForHash().entries(key);

        //检验用户是否存在
        if (userMap.isEmpty()) {
            return true;
        }

        //将查询到的信息转为userDTO
        UserDTO userDTO = BeanUtil.fillBeanWithMap(userMap, new UserDTO(), false);
        log.info("将查询到的信息转为userDTO:{}", userDTO);

        //存在，将用户信息存在threadlocal中
        UserHolder.saveUser(userDTO);

        //刷新token的时间
        stringRedisTemplate.expire(key, LOGIN_USER_TTL, TimeUnit.MINUTES);

        //放行
        return true;
    }

    /**
     * 释放拦截器
     *
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //移除用户
        UserHolder.removeUser();
    }
}
