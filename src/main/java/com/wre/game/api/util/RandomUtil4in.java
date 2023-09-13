package com.wre.game.api.util;

import cn.hutool.core.lang.WeightRandom;
import cn.hutool.core.util.RandomUtil;

import java.util.ArrayList;
import java.util.List;

public class RandomUtil4in {
    public final static String defaultGroup="default_group";
    public final static String testGroup="test_group";

    /**
     * 随机进行ab分组
     * @param n
     * @return
     */
    public static String getRandom4ABTest(int n){
        if(n<1){
            return defaultGroup;
        }
        List<WeightRandom.WeightObj<String>> weightList = new ArrayList<WeightRandom.WeightObj<String>>();
        WeightRandom.WeightObj<String>  a=new WeightRandom.WeightObj<String>(defaultGroup, 100-n);
        WeightRandom.WeightObj<String>  b=new WeightRandom.WeightObj<String>(testGroup, n);
        weightList.add(a);
        weightList.add(b);
        WeightRandom wr=  RandomUtil.weightRandom(weightList);
        return wr.next().toString();
    }
}
