package com.wre.game.api.context;

import com.wre.game.api.dao.AbTestInfoMapper;
import com.wre.game.api.data.entity.AbTestInfo;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Component
public class AbTestInfoStarted implements ServletContextAware {

    @Resource
    AbTestInfoMapper    abTestInfoMapper;
    /**
     * 集合
     */
    public static List<AbTestInfo> abTestMap =new ArrayList<>();

    public static  List<AbTestInfo> sycABTestMap=Collections.synchronizedList(abTestMap);



    @Override
    public void setServletContext(ServletContext servletContext) {
       // initAppRepeat();
      //  System.out.println("启动的时候启动:"+sycABTestMap);
    }

    public void initAppRepeat(){
        List<AbTestInfo> abTestInfos=abTestInfoMapper.selectByTime(new Date());
        sycABTestMap.addAll(abTestInfos);
    }

    public void reload(){
        sycABTestMap.clear();
        initAppRepeat();
    }

    public void add(AbTestInfo abTestInfo){
        sycABTestMap.add(abTestInfo);
    }

    public void del(AbTestInfo abTestInfo){
        sycABTestMap.remove(abTestInfo);
    }

}
