package com.hmall.common.interceptor;

import cn.hutool.core.util.StrUtil;
import com.hmall.common.utils.UserContext;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author FragrantXue
 * Create by 2025.09.17 14:34
 */

public class UserInfoInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1. 获取登录用户信息
        String userInfo = request.getHeader("User-Info");

        // 2. 判断是否获取了用户，如果有，则存入ThreadLocal
        if (StrUtil.isNotBlank(userInfo)) {
            Long userId = Long.valueOf(userInfo);
            UserContext.setUser(userId);
        }

        // 3. 不管否获取到用户信息，都放行
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 清除线程变量，防止内存泄漏
        UserContext.removeUser();
    }
}
