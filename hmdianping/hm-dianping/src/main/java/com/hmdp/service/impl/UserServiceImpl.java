package com.hmdp.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hmdp.dto.LoginFormDTO;
import com.hmdp.dto.Result;
import com.hmdp.entity.User;
import com.hmdp.mapper.UserMapper;
import com.hmdp.service.IUserService;
import com.hmdp.utils.RegexUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

import static com.hmdp.utils.SystemConstants.USER_NICK_NAME_PREFIX;

/**
 * 服务实现类
 */
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    /**
     * 发送验证码
     *
     * @param phone
     * @param session
     * @return
     */
    public Result sendCode(String phone, HttpSession session) {
        //校验手机号是否正确
        if (RegexUtils.isPhoneInvalid(phone)) {
            return Result.fail("手机号格式错误");
        }

        //符合，生成验证码
        String code = RandomUtil.randomNumbers(6);

        //保存验证码到 session
        session.setAttribute("code", code);

        log.info("发送短信成功，生成的验证码为：{}", code);

        return Result.ok();
    }

    /**
     * 实现登录功能
     *
     * @param loginForm
     * @param session
     * @return
     */
    public Result login(LoginFormDTO loginForm, HttpSession session) {
        //校验手机号
        String phone = loginForm.getPhone();
        if (RegexUtils.isPhoneInvalid(phone)) {
            //如果不符合，返回错误信息
            return Result.fail("手机号格式错误");
        }

        //校验验证码
        Object cacheCode = session.getAttribute("code");
        String code = loginForm.getCode();
        if (cacheCode == null || !cacheCode.toString().equals(code)) {
            //不一致，返回错误信息
            return Result.fail("验证码错误");
        }

        //用户与验证码一致，跟据手机号查用户
        User user = query().eq("phone", phone).one();

        //判断用户是否存在
        if (user == null) {
            //不存在，创建新用户并保存
            user = createUserWithPhone(phone);
        }

        //保存用户信息到session
        session.setAttribute("user", user);
        return Result.ok();
    }

    /**
     * 创建保存新用户
     *
     * @param phone
     * @return
     */
    private User createUserWithPhone(String phone) {
        //创建新用户
        User user = new User();
        user.setPhone(phone);
        user.setNickName(USER_NICK_NAME_PREFIX + RandomUtil.randomString(10));

        //保存用户
        save(user);
        return user;
    }
}
