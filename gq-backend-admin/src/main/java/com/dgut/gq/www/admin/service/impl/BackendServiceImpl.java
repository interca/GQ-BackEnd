package com.dgut.gq.www.admin.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dgut.gq.www.admin.feign.client.UserClient;
import com.dgut.gq.www.admin.model.dto.DepartmentDto;
import com.dgut.gq.www.admin.model.dto.LectureDto;
import com.dgut.gq.www.admin.model.dto.PositionDto;
import com.dgut.gq.www.admin.model.dto.PosterTweetDto;
import com.dgut.gq.www.admin.service.BackendService;


import com.dgut.gq.www.common.common.GlobalResponseCode;
import com.dgut.gq.www.common.common.RedisGlobalKey;
import com.dgut.gq.www.common.common.SystemJsonResponse;
import com.dgut.gq.www.common.excetion.GlobalSystemException;
import com.dgut.gq.www.common.model.entity.LoginUser;
import com.dgut.gq.www.common.model.entity.User;
import com.dgut.gq.www.common.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


;import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * 后台管理
 * @since  2022-10-8
 * @author  hyj
 * @version  1.0
 */
@Service
public class BackendServiceImpl implements BackendService, UserDetailsService {


    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private UserClient userClient;

    @Override
    public SystemJsonResponse login(String userName, String password) {
        //后台管理密码,
        UsernamePasswordAuthenticationToken authenticationToken=
                new UsernamePasswordAuthenticationToken(userName,password);

        Authentication authenticate = authenticationManager.authenticate(authenticationToken);

        //认证失败
        if(Objects.isNull(authenticate)) {
            throw new GlobalSystemException(
                    GlobalResponseCode.ACCOUNT_NOT_EXIST.getCode(),
                    GlobalResponseCode.ACCOUNT_NOT_EXIST.getMessage());
        }

        //认证成功把全部数据封装为LoginUser存入redis  方便后续权限的管理
        String key = RedisGlobalKey.PERMISSION+userName;
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(loginUser));

        //设置超时时间
        stringRedisTemplate.expire(key,10, TimeUnit.DAYS);
        String jwt = JwtUtil.createJWT(userName);
        return  SystemJsonResponse.success(jwt);
    }

    @Override
    public void logout() {
        //获取 SecurityContextHolder信息
        UsernamePasswordAuthenticationToken authentication =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        LoginUser loginUser= (LoginUser) authentication.getPrincipal();
        String userName = loginUser.getUser().getUserName();

        //删除redis对应信息
        String key = RedisGlobalKey.PERMISSION+userName;
        stringRedisTemplate.delete(key);
    }

    @Override
    public SystemJsonResponse getAttendLectureUser(int page, int pageSize, String id, Integer status) {
        return SystemJsonResponse.success(userClient.getAttendLectureUser(page,pageSize,id,status));
    }

    @Override
    public SystemJsonResponse updateOrSaveLecture(LectureDto lectureDto) {
        return null;
    }

    @Override
    public SystemJsonResponse saveUpdatePosterTweet(PosterTweetDto posterTweetDto) {
        return null;
    }

    @Override
    public SystemJsonResponse getLecture(int page, int pageSize, String name) {
        return null;
    }

    @Override
    public SystemJsonResponse exportUser(String id, Integer status) {
        return null;
    }

    @Override
    public SystemJsonResponse exportCurriculumVitae(String departmentId, Integer term) {
        return null;
    }

    @Override
    public SystemJsonResponse deleteLecture(String id) {
        return null;
    }

    @Override
    public SystemJsonResponse deleteDepartment(String id) {
        return null;
    }

    @Override
    public SystemJsonResponse deletePosition(String id) {
        return null;
    }

    @Override
    public SystemJsonResponse saveAndUpdateDep(DepartmentDto departmentDto) {
        return null;
    }

    @Override
    public SystemJsonResponse saveAndUpdatePos(PositionDto positionDto) {
        return null;
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        //远程调用用户模块
        User user = userClient.getUserByUserName(userName);
        //判断用户是否存在数据库中
        Optional<User> optionalUser=Optional.ofNullable(user);

        //不存在就抛出异常
        if(!optionalUser.isPresent()){
            throw  new GlobalSystemException(
                    GlobalResponseCode.OPERATE_FAIL.getCode(),
                    "账户不存在");
        }

        //权限信息
        String permission = user.getPermission();
        List<String> list = new ArrayList<>();
        list.add(permission);

        return new LoginUser(user,list);
    }
}

