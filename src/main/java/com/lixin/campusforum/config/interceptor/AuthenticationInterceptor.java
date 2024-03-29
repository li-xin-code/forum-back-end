package com.lixin.campusforum.config.interceptor;

import com.lixin.campusforum.common.annotation.LoginRequired;
import com.lixin.campusforum.common.exception.AuthenticationException;
import com.lixin.campusforum.common.exception.TokenInvalidException;
import com.lixin.campusforum.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * @author lixin
 */
@Component
@RequiredArgsConstructor
public class AuthenticationInterceptor implements HandlerInterceptor {

    private final TokenService<String> tokenService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        // 如果不是映射到方法直接通过
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        Class<?> declaringClass = method.getDeclaringClass();
        boolean classAnnotation = declaringClass.isAnnotationPresent(LoginRequired.class);
        boolean methodAnnotation = method.isAnnotationPresent(LoginRequired.class);

        // 如果 类 和 方法 上都没有 LoginRequired 直接通过
        if (!classAnnotation && !methodAnnotation) {
            return true;
        }

        LoginRequired loginRequired = classAnnotation ?
                declaringClass.getAnnotation(LoginRequired.class) :
                method.getAnnotation(LoginRequired.class);

        if (!loginRequired.required()) {
            return true;
        }

        String token = request.getHeader("token");
        if (!StringUtils.hasLength(token)) {
            throw new AuthenticationException("Login required.");
        }
        if (!tokenService.contain(token)) {
            throw new TokenInvalidException();
        }
        return true;
    }
}
