package cn.sessiontech.xcx.controller.sys;

import cn.sessiontech.xcx.aspect.AnnotationLogs;
import cn.sessiontech.xcx.common.PageResult;
import cn.sessiontech.xcx.common.QueryObject;
import cn.sessiontech.xcx.common.Result;
import cn.sessiontech.xcx.constant.GlobalConstants;
import cn.sessiontech.xcx.constant.GlobalTimeConstants;
import cn.sessiontech.xcx.dto.user.LoginUserDTO;
import cn.sessiontech.xcx.dto.user.TSysUserDTO;
import cn.sessiontech.xcx.dto.user.UserQueryDTO;
import cn.sessiontech.xcx.entity.TStudentCourseRule;
import cn.sessiontech.xcx.entity.TWeixinUser;
import cn.sessiontech.xcx.entity.sys.TSysUser;
import cn.sessiontech.xcx.enums.LoginEnum;
import cn.sessiontech.xcx.enums.RedisEnum;
import cn.sessiontech.xcx.enums.ResultCodeEnum;
import cn.sessiontech.xcx.enums.common.QueryEnum;
import cn.sessiontech.xcx.provider.CustomAuthenticationProvider;
import cn.sessiontech.xcx.service.TStudentCourseRuleService;
import cn.sessiontech.xcx.service.TWeixinUserService;
import cn.sessiontech.xcx.service.sys.TSysUserService;
import cn.sessiontech.xcx.utils.*;
import cn.sessiontech.xcx.vo.user.PersonVO;
import cn.sessiontech.xcx.vo.user.StudentVO;
import cn.sessiontech.xcx.vo.user.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author xbcai
 * @classname TSysUserCtrl
 * @description 系统用户模块
 * @date 2019/9/10 15:36
 */

@RestController
@RequestMapping("/sys")
@Slf4j
public class TSysUserCtrl {
    @Autowired
    private RedisKeyUtils redisKeyUtils;
    @Autowired
    private CustomAuthenticationProvider customAuthenticationProvider;
    @Autowired
    private TSysUserService tSysUserService;
    @Autowired
    private TStudentCourseRuleService tStudentCourseRuleService;
    @Autowired
    private TWeixinUserService tWeixinUserService;
    /**
     * 登陆入口
     */
    @AnnotationLogs(description = "登录")
    @PostMapping(value = "/login")
    public Result login(@RequestBody @Valid LoginUserDTO dto, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            return Result.fail(ResultCodeEnum.LOCK_NECESSARY_PARAMS,bindingResult.getAllErrors());
        }
        boolean validUser = isValidUser(dto.getOpenid(), dto.getUsername());
        //账号已和微信绑定，只能使用绑定的微信进行登录
        if(!validUser){
            return Result.fail(ResultCodeEnum.ILLEGAL_LOGIN);
        }
        try {
            Authentication request = new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getOpenid());
            Authentication result = customAuthenticationProvider.authenticate(request);
            SecurityContextHolder.getContext().setAuthentication(result);
            //登录成功从缓存中获取系统用户信息
            Map<String, String> sysUserMap = redisKeyUtils.hgetAll(RedisEnum.HOME_SYS_USER.getCode() + dto.getOpenid());
            //将用户信息设置进缓存
            redisKeyUtils.hmset(RedisEnum.HOME_SYS_USER.getCode()+dto.getOpenid(),sysUserMap);
            ThreadUtils.getOhterThreadPool().execute(()->bindUserOpenid(dto.getOpenid(),sysUserMap));
            return Result.success(sysUserMap);
        } catch (AuthenticationException e) {
            log.info("Authentication failed:{} ",e.getMessage());
            return Result.fail(ResultCodeEnum.TOKEN_USER_LOGIN_ERROR,e.getMessage());
        }
    }



    /**
     * 所有公众号后台退出操作
     * @return 返回是否成功
     */
    @AnnotationLogs(description = "退出")
    @GetMapping("/signOut")
    public Result signOut(HttpServletRequest req){
        String openid = req.getHeader("openid");
        //删除缓存用户信息
        redisKeyUtils.del(RedisEnum.HOME_SYS_USER.getCode()+openid);
        SecurityContextHolder.clearContext();
        return Result.success();
    }

    @AnnotationLogs(description = "获取页面结构权限")
    @GetMapping("/getRoleMenusByUserId/{userId}")
    public Result getRoleMenusByUserId(@PathVariable("userId") String userId){
        return Result.success(tSysUserService.getRoleMenusByUserId(userId));
    }

    @AnnotationLogs(description = "新增用户资料")
    @PostMapping("/addUser")
    public Result addUser(@RequestBody TSysUserDTO user,BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return Result.fail(ResultCodeEnum.LOCK_NECESSARY_PARAMS);
        }
        //如果账号不为空，则判断该账号是否已经存在，如果已存在，则直接返回
        if(StringUtils.isNotEmpty(user.getUsername())){
            TSysUser u = new TSysUser();
            u.setUsername(user.getUsername());
            List<TSysUser> byParams = tSysUserService.findByParams(u);
            if(byParams.size()>0){
                return Result.fail(ResultCodeEnum.ACCOUNT_HAD_REGISTER);
            }
        }

        TSysUser newUser = ConverterUtils.convert(user, TSysUser.class);
        newUser.setFlag(LoginEnum.login_user_flag_Y.getKey());
        newUser = tSysUserService.saveAndFlush(newUser);
        //如果是学生或老师，则将其上课规则添加进去
        if(StringUtils.equals(user.getUserIdentity(),LoginEnum.LOGIN_USER_IDENTIFY_STUDENT.getKey())
        ||StringUtils.equals(user.getUserIdentity(),LoginEnum.LOGIN_USER_IDENTIFY_TEACHER.getKey())){
            final String userId = newUser.getId();
            TStudentCourseRule rule = ConverterUtils.convert(user, TStudentCourseRule.class);
            rule.setTakeEffectTime(DateJdk8Utils.getNowTime(GlobalTimeConstants.YYYY_MM_DD));
            rule.setUserId(newUser.getId());
            tStudentCourseRuleService.saveAndFlush(rule);
            //如果是学生，则异步将该学生的家长姓名冗余进用户表
            if(StringUtils.equals(user.getUserIdentity(),LoginEnum.LOGIN_USER_IDENTIFY_STUDENT.getKey())){
                ThreadUtils.getOhterThreadPool().execute(()->{
                    TSysUser parent = tSysUserService.findById(user.getUserParentId());
                    TSysUser student = tSysUserService.findById(userId);
                    student.setUserParentName(parent.getFullName());
                    tSysUserService.saveAndFlush(student);
                });
            }

        }
        return Result.success();
    }

    /**
     * 查询用户列表信息
     * @param dto 参数条件
     * @param bindingResult 校验信息
     */
    @PostMapping("/getUsers")
    public Result getUsers(@RequestBody UserQueryDTO dto, BindingResult bindingResult,HttpServletRequest req){
        if(bindingResult.hasErrors()){
            return Result.fail(ResultCodeEnum.LOCK_NECESSARY_PARAMS);
        }
        List<QueryObject<String,Object>> lists = new ArrayList<>(5);
        lists.add(new QueryObject<>("userIdentity",dto.getUserIdentity()));
        //设置关键字查询
        if(StringUtils.isNotEmpty(dto.getKeyword())){
            lists.add(new QueryObject<>("username,fullName,tel",dto.getKeyword(), QueryEnum.QUERY_OR));
        }
        //默认 老师信息所有人都可以看到
        if(!StringUtils.equals(dto.getUserIdentity(), LoginEnum.LOGIN_USER_IDENTIFY_TEACHER.getKey())){
            PageResult pageResult = filterByUserIdentity(lists, req);
            if(pageResult!=null){return pageResult;}
        }

        PageResult result = tSysUserService.findByParams(lists,QueryEnum.QUERY_AND,
                new JpaUtils().getPageable(dto.getCurrentPage(),dto.getPageSize(), GlobalConstants.DESC,"createTime"));
        return JpaUtils.convertResult(result);
    }

    /**
     * 根据身份进行过滤，即运营者及系统管理员及老师可以查询所有信息 ，学生及家长只能查自己范围内的信息
     * @param lists
     * @param req
     * @return
     */
    @SuppressWarnings("all")
    private PageResult filterByUserIdentity(List<QueryObject<String,Object>> lists,HttpServletRequest req){
        String openid = req.getHeader("openid");
        List<String> userList = redisKeyUtils.hmget(RedisEnum.HOME_SYS_USER.getCode() + openid, "id","userIdentity");
        String userIdentity = userList.get(1);
        //如果登录的账号角色是学生，只能查看该学生及自己的家长的信息
        if(StringUtils.equals(userIdentity, LoginEnum.LOGIN_USER_IDENTIFY_STUDENT.getKey())){
            String pId = tSysUserService.findById(userList.get(0)).getUserParentId();
            lists.add(new QueryObject<>("id",userList.get(0)+","+pId,QueryEnum.IN));

        }else if(StringUtils.equals(userIdentity,LoginEnum.LOGIN_USER_IDENTIFY_PARENT.getKey())){// 如果是家长，只查家长及自己的孩子的信息
            TSysUser user = new TSysUser();
            user.setUserParentId(userList.get(0));
            //查询该家长孩子信息
            List<TSysUser> byParams = tSysUserService.findByParams(user);
            //如果没有孩子信息，则直接返回空信息
            if(byParams.size()==0){
                PageResult result = new PageResult();
                result.setCode(200);
                result.setCurrentPage(0);
                result.setFirst(true);
                result.setPageSize(10);
                result.setTotalCount(0);
                result.setTotalPage(0);
                return result;
            }else{
                List<String> ids = new ArrayList<>();
                byParams.forEach(item-> ids.add(item.getId()));
                ids.add(userList.get(0));
                String idString = String.join(",",ids);
                lists.add(new QueryObject<>("id",idString,QueryEnum.IN));
            }
        }
        return null;
    }

    /**
     * 查询学生详细信息
     * @param id 学生主键
     */
    @GetMapping("/getStudentDetail/{id}")
    public Result getStudentDetail(@PathVariable("id")String id){
        TSysUser user = tSysUserService.findById(id);
        TSysUser parent = tSysUserService.findById(user.getUserParentId());
        StudentVO studentVO = ConverterUtils.convert(user, StudentVO.class);
        StudentVO p = ConverterUtils.convert(parent,StudentVO.class);
        studentVO.setParent(p);
        return Result.success(studentVO);
    }

    /**
     * 查看家长详细信息
     * @param id 家长id
     */
    @GetMapping("/getParentDetail/{id}")
    public Result getParentDetail(@PathVariable("id")String id){
        //家长信息
        TSysUser parent = tSysUserService.findById(id);
        UserVO user = ConverterUtils.convert(parent,UserVO.class);
        TSysUser entity = new TSysUser();
        entity.setUserParentId(id);
        //家长孩子信息
        List<TSysUser> childs = tSysUserService.findByParams(entity);
        List<UserVO> users = ConverterUtils.convert(childs, UserVO.class);
        //将孩子设置进去
        user.setChilds(users);
        return Result.success(user);
    }

    /**
     * 查询用户信息
     * @param identity 身份 super:超级用户,operator：运营者,student：学生,teacher：老师,parent：家长
     */
    @GetMapping("/getPersons")
    public Result getPersons(@RequestParam String identity){
        TSysUser user = new TSysUser();
        user.setUserIdentity(identity);
        List<TSysUser> users = tSysUserService.findByParams(user);
        List<PersonVO> persons = ConverterUtils.convert(users, PersonVO.class);
        return Result.success(persons);
    }

    /**
     * 校验登录的账号是不是本人微信号登录的，如果是，返回true,否则返回false
     * @param openid 微信用户openid
     * @param username 登录的账号
     */
    private boolean isValidUser(String openid,String username){
        TWeixinUser user = new TWeixinUser();
        user.setUsername(username);
        user = tWeixinUserService.findEntityByParams(user);
        //如果该账号还没有绑定openid或者该账号就是本人微信登录的，则校验通过
        return user==null||StringUtils.equals(openid, user.getOpenid());
    }

    /**
     * 绑定用户与openid
     * @param openid 微信openid
     * @param sysUserMap 系统用户
     */
    private void bindUserOpenid(String openid, Map<String, String> sysUserMap){
        TWeixinUser user = new TWeixinUser();
        user.setOpenid(openid);
        user = tWeixinUserService.findEntityByParams(user);
        if(user!=null){
            user.setUserId(sysUserMap.get("id"));
            user.setUsername(sysUserMap.get("username"));
            user.setFullName(sysUserMap.get("fullName"));
            user.setUserIdentity(sysUserMap.get("userIdentity"));
        }
        tWeixinUserService.saveAndFlush(user);
    }

    public static void main(String[] args) {
        TSysUserDTO dto = new TSysUserDTO();
        System.out.println(JsonUtils.obj2String(dto));
    }


}
