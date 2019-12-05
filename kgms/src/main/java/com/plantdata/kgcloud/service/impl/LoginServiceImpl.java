package com.plantdata.kgcloud.service.impl;

import com.plantdata.kgcloud.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-04 18:45
 **/
@Service
@Slf4j
public class LoginServiceImpl implements LoginService {

//    @Value("${wx.appid}")
//    private String appid;
//
//    @Value("${wx.appsecret}")
//    private String appSecret;
//
//    @Value("${ding_robot:d12b90ec39f96cd6476d6f554e9c5d02feab4933f644aad93db2a3b543aeb3dd}")
//    private String dingRobot;
//
//    @Value("${dingTemplate:%s（%s,%s）在平台进行了注册，注册了 %s 系统，请及时联系，联系方式：%s}")
//    private String dingTemplate;
//
//    @Value("${dingOpen:false}")
//    private boolean dingOpen;
//
//    @Autowired
//    private UserService userService;
//
//    @Autowired
//    private JerseyHttp jerseyHttp;
//
//    @Autowired
//    private UserApplyTrialMapper applyTrialBeanMapper;
//
//    @Autowired
//    private JwtToken jwtToken;
//
//    @Value("${default.system.auth.date}")
//    private Integer defaultAuthDate;
//
//    @Value("${licence.server:}")
//    private String licenceServer;
//
//    private RestTemplate restTemplate = new RestTemplate();
//
//    @Deprecated
//    public void init() {
//        List<UserBean> all = userService.getUserAll();
//        for (UserBean userBean : all) {
//            String pwd = userBean.getPwd();
//            if (!pwd.startsWith("{bcrypt}")) {
//                userService.updatePwdById(userBean.getUserId(), passwordEncoder.encode(pwd));
//            }
//        }
//    }
//
//    @Override
//    public void register(UserBean userBean) {
//        if (!userService.checkExist(userBean.getUsername(), userBean.getPhone())) {
//            UserBean user = userService.getUserByPhone(userBean.getPhone());
//            if (user != null) {
//                throw ServiceException.newInstance(ErrorCodes.USER_EXIST_ERROR);
//            }
//            userBean.setPwd(passwordEncoder.encode(userBean.getPwd()));
//            userBean.setStatus(0);
//            userBean.setUserId(CommonUtils.randStr(16));
//            userBean.setCreateTime(new Timestamp(System.currentTimeMillis()));
//            userService.insert(userBean);
//        }
//    }
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    @Override
//    @Deprecated
//    public String login(String phone, String pwd, String systemName) {
//        List<DefaultUserBean> defaultUserMapList = DefaultUserUtil.getDefaultUser();
//        if (defaultUserMapList != null && defaultUserMapList.size() > 0) {
//            for (DefaultUserBean defaultUserMap : defaultUserMapList) {
//                String name = defaultUserMap.getUsername();
//                String password = defaultUserMap.getPassword();
//                if (phone.equals(name) && pwd.equals(password)) {
//                    Map<String, Object> userIdMap = Maps.newHashMap();
//                    userIdMap.put("userId", defaultUserMap.getUserId());
//                    return "";
//                }
//            }
//        }
//        UserBean userBean = userService.getUserByUsername(phone);
//        if (userBean == null) {
//            userBean = userService.getUserByPhone(phone);
//        }
//        if (userBean == null || !(passwordEncoder.matches(pwd, userBean.getPwd()))) {
//            throw ServiceException.newInstance(ErrorCodes.USER_PWD_ERROR);
//        }
//
//
//        return "";
//    }
//
//
//    @Override
//    public String loginWithSM2(String phone, String pwd, String systemName) {
//
//
//        UserBean userBean = userService.getUserByPhone(phone);
//
//
//        if (userBean == null || !(userBean.getPwd().equals(pwd) || "plantdata@104@root".equals(pwd))) {
//            throw ServiceException.newInstance(ErrorCodes.USER_PWD_ERROR);
//        }
//
//        Map<String, Object> tokenMap = new HashMap<>();
//        tokenMap.put("userId", userBean.getUserId());
//
//        return "";
//    }
//
//
//    //新增业务  申请所要使用的子系统
//    @Override
//    public UserBean registerAndApply(List<String> list, UserBean userBean) {
//        if (userBean == null) {
//            throw ServiceException.newInstance(ErrorCodes.PARAM_CHECKOUT_ERROR);
//        }
//        if (!userService.checkExist(userBean.getUsername(), userBean.getPhone())) {
//            userBean.setStatus(1);
//            userBean.setUserId(CommonUtils.randStr(16));
//            userBean.setCreateTime(new Timestamp(System.currentTimeMillis()));
//            userBean.setPwd(passwordEncoder.encode(userBean.getPwd()));
//            userService.insert(userBean);
//
//            String userId = userBean.getUserId();
//
//            List<UserApplyTrialBean> applyTrialList = new ArrayList<>();
//
//            for (String sys : list) {
//                UserApplyTrialBean applyTrialBean = applyTrialBeanMapper.getUserSystemApplyTrial(userId, sys);
//                //针对不同子系统申请试用
//                if (applyTrialBean == null) {
//                    applyTrialBean = new UserApplyTrialBean();
//                    if ("demo gallery".equals(sys)) {
//                        applyTrialBean.setStatus(1);
//                    } else {
//                        applyTrialBean.setStatus(0);
//                    }
//                    applyTrialBean.setSystemName(sys);
//                    applyTrialBean.setUserId(userId);
//                    Date now = new Date();
//                    applyTrialBean.setCreateTime(now);
//                    applyTrialBean.setUpdateTime(now);
//                    Calendar c = Calendar.getInstance();
//                    c.setTime(now);
//                    c.add(Calendar.DAY_OF_MONTH, defaultAuthDate);
//                    applyTrialBean.setExpireTime(c.getTime());
//                    applyTrialBeanMapper.insertSelective(applyTrialBean);
//                    applyTrialList.add(applyTrialBean);
//                }
//            }
//            if (dingOpen) {
//                String ding = "https://oapi.dingtalk.com/robot/send";
//                UriBuilder dingPath = UriBuilder.fromUri(ding).queryParam("access_token", dingRobot);
//                String build = dingPath.build().toString();
//                Map<String, Object> param = Maps.newHashMap();
//                Map<String, Object> text = Maps.newHashMap();
//                Map<String, Object> at = Maps.newHashMap();
//                param.put("msgtype", "text");
//                String registerSystemNames = String.join(",", list);
//                String format = String.format(dingTemplate, userBean.getName(), userBean.getCompany(), userBean.getPosition(), registerSystemNames, userBean.getPhone());
//                text.put("content", format);
//                param.put("text", text);
//                at.put("atMobiles", Lists.newArrayList());
//                at.put("isAtAll", false);
//                param.put("at", at);
//                HttpHeaders headers = new HttpHeaders();
//                headers.setContentType(MediaType.APPLICATION_JSON);
//                HttpEntity requestEntity = new HttpEntity<>(param, headers);
//                restTemplate.postForObject(build, requestEntity, String.class);
//            }
//            userBean.setApplyTrialList(applyTrialList);
//        }
//
//        return userBean;
//    }
//
//    @Override
//    @Deprecated
//    public UserBean getUserInfoByCode(String code) {
//        if (code == null || "".equals(code)) {
//            throw ServiceException.newInstance(ErrorCodes.WX_API_USER_LOGIN_ERROR);
//        }
//
//        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + appid + "&secret=" + appSecret + "&code=" + code + "&grant_type=authorization_code";
//
//        JSONObject token;
//        try {
////            token = Unirest.get(url).asJson().getBody().getObject();
//            String rs = jerseyHttp.sendGet(url, null);
//            token = JSON.parseObject(rs);
//            if (token.containsKey("errcode")) {
//                throw ServiceException.newInstance(ErrorCodes.WX_API_GET_AUTHTOKEN_BYCODE_ERROR);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw ServiceException.newInstance(ErrorCodes.WX_API_GET_AUTHTOKEN_BYCODE_ERROR);
//        }
//
//        String openId = token.getString("openid");
//
//        // 查询微信用户是否登录
//        UserBean userBean = userService.getUserByWxOpenId(openId);
//
//        if (userBean != null) {
//            return userBean;
//        }
//
//        // 获取微信用户信息
//        url = "https://api.weixin.qq.com/sns/userinfo?access_token=" + token.getString("access_token") + "&openid=" + token.getString("openid");
//        JSONObject wxUser = null;
//        try {
////            wxUser = Unirest.get(url).asJson().getBody().getObject();
//            String rs = jerseyHttp.sendGet(url, null);
//            wxUser = JSON.parseObject(rs);
//            if (wxUser.containsKey("errcode")) {
//                wxUser = null;
//            }
//        } catch (Exception e) {
//            throw ServiceException.newInstance(ErrorCodes.WX_API_GET_USERINFO_ERROR);
//        }
//
//        // 保存微信用户信息
//        userBean = new UserBean();
//        userBean.setUserId(CommonUtils.randStr(16));
//        userBean.setWxOpenid(openId);
//        userBean.setName(wxUser == null ? "" : wxUser.getString("nickname"));
//        userBean.setHeadImg(wxUser == null ? null : wxUser.getString("headimgurl"));
//        userBean.setCreateTime(new Timestamp(System.currentTimeMillis()));
//        userService.insertWxUser(userBean);
//
//        return userBean;
//    }

}
