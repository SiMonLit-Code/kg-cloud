package com.plantdata.kgcloud.service.impl;


import com.plantdata.kgcloud.service.GraphConfService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-04 18:45
 **/
@Service
@Slf4j
public class GraphConfServiceImpl implements GraphConfService {

//    @Autowired
//    private MyGraphMapper myGraphMapper;
//
//    @Autowired
//    private ConfigManage configManage;
//
//    @Autowired
//    private UserService userService;
//
//    @Autowired
//    private UserMapper userMapper;
//
//    @Autowired
//    private GraphService graphInitService;
//
//    @Autowired
//    private MongoClient mongoClient;
//
//
//    @Override
//    public RestData<MyGraphBean> getList(String kw, String userId, Integer pageNo, Integer pageSize) {
//
//        int start = (pageNo - 1) * pageSize;
//        int offset = pageSize;
//
//        if ("".equals(kw)) {
//            kw = null;
//        }
//
//        Map<String, Object> paramMap = new HashMap<>(4);
//        paramMap.put("start", start);
//        paramMap.put("offset", offset);
//        paramMap.put("kw", kw);
//        paramMap.put("userId", userId);
//
//        int rsCount = myGraphMapper.count(paramMap);
//        List<MyGraphBean> rsList = myGraphMapper.selectList(paramMap);
//
//        if (rsList == null) {
//            throw RestException.newInstance(ErrorCodes.KG_NULL_ERROR);
//        }
//
//        rsList.stream().filter(s -> s.getGraphName() != null).map(b -> {
//            b.setApk(CommonUtils.randStr(8) + Base64.getEncoder().encodeToString(b.getGraphName().getBytes()));
//            return b;
//        }).collect(Collectors.toList());
//
//        return new RestData<>(rsList, rsCount);
//    }
//
//    @Override
//    public MyGraphBean getByGraphName(String userId, String graphName) {
//        Map<String, Object> paramMap = new HashMap<>(4);
//        paramMap.put("graphName", graphName);
//        paramMap.put("userId", userId);
//        return myGraphMapper.selectByName(paramMap);
//    }
//
//
//    @Override
//    public MyGraphBean addPrivateGraph(MyGraphBean bean, String key) {
//        Integer count = myGraphMapper.countPrivate(bean.getUserId());
//        Integer maxNum = userMapper.getMaxGraphNum(bean.getUserId());
//
//        if (key == null) {
//            String string = Long.toHexString(System.currentTimeMillis()).substring(6);
//            key = "g_" + bean.getUserId() + "_" + string;
//        }
//        if (count >= maxNum) {
//            throw ServiceException.newInstance(ErrorCodes.ADD_LIMIT_ERROR);
//        }
//
//        boolean result = key.matches("^[a-zA-Z0-9][a-zA-Z_0-9]*$");
//        if (!result || key.length() < 4) {
//            throw RestException.newInstance(ErrorCodes.GRAPH_KEY_NULL_ERROR);
//        }
//        if (key.length() > 25) {
//            throw RestException.newInstance(ErrorCodes.GRAPH_TITLE_LENGTH_ERROR);
//        }
//        bean.setAllowEdit(true);
//        bean.setPrivate(true);
//        bean.setAddTime(new Date());
//        bean.setLastModifyTime(new Date());
//
//        String format_graph_name = key + "_" + bean.getUserId();
//
//        List<String> graphNameList = myGraphMapper.getUserGraphName(bean.getUserId());
//        for (String graphName : graphNameList) {
//            if (graphName.equals(format_graph_name)) {
//                format_graph_name = key + "_"+ Long.toHexString(System.currentTimeMillis());
//            }
//        }
//
//        bean.setGraphName(format_graph_name);
//
//        initGraph(bean.getGraphName(), bean.getTitle());
//
//        myGraphMapper.insert(bean);
//
//        return bean;
//    }
//
//    @Override
//    @Transactional
//    public MyGraphBean addDefaultGraph(MyGraphBean bean, String key) {
//
//        MyGraphBean myGraphBean = getByGraphName(bean.getUserId(), key);
//
//        // 用 edit_view_type 来实现默认图谱删除后不再自动创建的功能，3表示已删除的默认图谱
//        if (myGraphBean != null) {
//            return myGraphBean.getEditViewType() != 3 ? myGraphBean : null;
//        }
//
//        bean.setAllowEdit(true);
//        bean.setPrivate(true);
//        bean.setAddTime(McnUtils.getTime());
//        bean.setLastModifyTime(McnUtils.getTime());
//
//        bean.setGraphName(key);
//
//        myGraphMapper.insert(bean);
//
//        //复制默认图谱
//        graphInitService.init(key);
//        return bean;
//    }
//
//    @Override
//    public void updateDefaultGraph(List<String> userList, String title) {
//        if (userList == null || userList.isEmpty()) {
//            userList = userService.getUserAll().stream().map(s -> s.getUserId()).collect(Collectors.toList());
//        }
//        userList.forEach(s -> {
//            MyGraphBean bean = new MyGraphBean();
//            bean.setTitle(title);
//            bean.setUserId(s);
//            addDefaultGraph(bean, "default_graph_" + McnUtils.getTime() + "_" + s);
//        });
//    }
//
//    @Override
//    public void updateTitle(String userId, Integer id, String title) {
//        MyGraphBean myGraphBean = new MyGraphBean();
//        myGraphBean.setId(id);
//        myGraphBean.setUserId(userId);
//        myGraphBean.setTitle(title);
//        myGraphMapper.updateByPrimaryKeySelective(myGraphBean);
//    }
//
//    @Override
//    public void update(MyGraphBean myGraphBean){
//        myGraphMapper.updateByPrimaryKeySelective(myGraphBean);
//    }
//
//
//    @Override
//    public void delPrivateGraph(Integer id, String userId) {
//        MyGraphBean myGraphBean = get(userId, id);
//        if (Objects.isNull(userId)) {
//            return;
//        }
//        String url = configManage.getKgServiceEditPath() + "/kgedit/clear";
//        String graphName = myGraphBean.getGraphName();
//
//        //删除图谱
//        MultivaluedMap<String, Object> form = new MultivaluedHashMap<>();
//        form.add("kgName", graphName);
//        SseUtils.sendPost(url, null, form, new TypeToken<KGResultItem<String>>() {
//        }.getType());
//
//        String defaultGraph = "default_graph_" + myGraphBean.getUserId();
//        if (defaultGraph.equals(myGraphBean.getGraphName())) {//删除默认图
//            Map<String, Object> editViewTypeMap = Maps.newHashMap();
////            editViewTypeMap.put("is_private", 10);//艹 NM
//            editViewTypeMap.put("userId", userId);
//            editViewTypeMap.put("id", myGraphBean.getId());
//            myGraphMapper.updateEditViewType(editViewTypeMap);
//        } else {
//            Map<String, Object> editViewTypeMap = Maps.newHashMap();
//            editViewTypeMap.put("userId", userId);
//            editViewTypeMap.put("id", myGraphBean.getId());
//            myGraphMapper.updateToDelete(editViewTypeMap);
//        }
//
//        // delete log
//        String logColName = graphName + "_log";
//        mongoClient.getDatabase("kg_log").getCollection(logColName).drop();
//    }
//
//
//    @Override
//    public int updateEditViewType(Integer editViewType, Integer id, String userId) {
//        Map<String, Object> paramMap = new HashMap<>(4);
//        paramMap.put("id", id);
//        paramMap.put("userId", userId);
//        paramMap.put("editViewType", editViewType);
//        return myGraphMapper.updateEditViewType(paramMap);
//
//    }
//
//    @Override
//    public MyGraphBean get(String userId, Integer id) {
//        Map<String, Object> paramMap = new HashMap<>(3);
//        paramMap.put("id", id);
//        paramMap.put("userId", userId);
//        return myGraphMapper.select(paramMap);
//    }
//
//    /**
//     * @author hexiang
//     * @create_time 2017年3月24日 下午1:37:54
//     */
//    private void initGraph(String kgName, String kgDisplayName) {
//        String url = configManage.getKgServiceEditPath() + "/kgedit/init";
//        MultivaluedMap<String, Object> para = new MultivaluedHashMap<>();
//        para.add("kgName", kgName);
//        para.add("displayName", kgDisplayName);
//        SseUtils.sendPost(url, null, para, new TypeToken<KGResultItem<Object>>() {
//        }.getType());
//    }
//
//    @Override
//    public List<MyGraphBean> selectAllGraphName() {
//        return myGraphMapper.selectAllGraphName();
//    }
//
//
//    @Override
//    public 	void  copy(String kgName,String toKgName) {
//        String url = configManage.getKgServiceEditPath() + "/kgedit/copy";
//        MultivaluedMap<String, Object> para = new MultivaluedHashMap<>();
//        para.add("kgName", kgName);
//        para.add("toKgName", toKgName);
//        SseUtils.sendPost(url, null, para, new TypeToken<KGResultItem<Object>>() {
//        }.getType());
//    }
}
