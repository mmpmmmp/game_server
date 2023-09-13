package com.wre.game.api.message;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;

/**
 * 空的类
 * @author shenxinwu
 */
public class NullObject implements Serializable {
        private static final long serialVersionUID = 8979506594072987974L;
        private NullObject(){};
        private static  volatile NullObject nullObject;
        public  static NullObject getInstance(){
                if(nullObject==null){
                        synchronized (NullObject.class){
                                if(nullObject==null){
                                        nullObject=new NullObject();
                                }
                        }
                }
                return nullObject;
        }
        public  static  String getNull(){
                return JSONObject.toJSONString(getInstance());
        }
}
