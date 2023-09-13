package com.wre.game.api.util;

import cn.hutool.core.lang.WeightRandom;
import cn.hutool.core.util.RandomUtil;
import com.wre.game.api.message.ABMEssage;

import java.util.ArrayList;
import java.util.List;

public class test {

    public static void main(String[] args) {
        List<WeightRandom.WeightObj<String>> weightList = new ArrayList<WeightRandom.WeightObj<String>>();
        WeightRandom.WeightObj<String>  a=new WeightRandom.WeightObj<String>("default_group", 1);
        WeightRandom.WeightObj<String>  b=new WeightRandom.WeightObj<String>("test_group", 9);
        weightList.add(a);
        weightList.add(b);
        WeightRandom wr=   RandomUtil.weightRandom(weightList);
        System.out.println(wr.next());
        int num_a=0;
        int num_b=0;
        String str="";
        for(int i=0;i<1000000;i++){
            str=wr.next().toString();
            switch (str){
                case "default_group":
                    num_a=num_a+1;
                    break;
                case "test_group":
                    num_b=num_b+1;
                    break;
            }
        }
        System.out.println("default_group:"+num_a);
        System.out.println("test_group:"+num_b);
        System.out.println("p:"+(Fn.toDouble(num_b)/(num_a+num_b)));
    }
}
