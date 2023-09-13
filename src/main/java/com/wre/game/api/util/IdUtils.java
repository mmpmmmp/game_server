package com.wre.game.api.util;


import java.util.concurrent.atomic.AtomicInteger;

/**
 * 每秒100万个自增Id
 *
 * @author zs
 * @date 2020-03-21 03:19:32
 */
public class IdUtils {
    private static AtomicInteger addLong = new AtomicInteger();

    public static long getId(int serverId) {
        long add = addLong.incrementAndGet();
        if (add < 0) {
            addLong.set(0);
            add = addLong.incrementAndGet();
        }
        // 65535-----------3160天--------1048575
        // 16位serverId + 28位时间戳 + 20位自增
        return ((serverId * 1L & 0xFFFF) << 48) | (((System.currentTimeMillis() / 1000) & 0xFFFFFFF) << 20) | (add & 0xFFFFF);
    }

    public static long getId() {
        return getId(1);
    }


    public static int getServerById(long id) {
        return (int) (id >> 48);
    }

    public static int getIdByInt(){
        return  getServerById(getId());
    }

    public static void main(String[] args) throws InterruptedException {
        long id = getId(100);
        System.out.println(id);
        System.out.println(getServerById(id));
    }
}