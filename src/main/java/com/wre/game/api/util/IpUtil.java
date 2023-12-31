package com.wre.game.api.util;

import com.wre.game.api.data.IpBlock;
import com.wre.game.api.data.IpSection;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

public class IpUtil {

    public static IpBlock checkIpBlock(String ip, List<IpSection> blockIpList) {

        IpBlock ipBlock = new IpBlock();
        ipBlock.setIp(ip);
        try {
            if (blockIpList != null && blockIpList.size() > 0) {
                for (IpSection ipSection : blockIpList) {
                    String[] ipRanges = ipSection.getIpRanges();
                    if (ipRanges != null && ipRanges.length > 0) {
                        for (String ipRange : ipRanges) {
                            boolean exist = ipExistsInRange(ip, ipRange);
                            if (exist) {
                                ipBlock.setBlock(true);
                                ipBlock.setBlockRegion(ipSection.getRegion());
                                return ipBlock;
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ipBlock;
    }

    public static String getRemoteIp(HttpServletRequest request) {
        String remoteIps="";
        if (request.getHeader("x-forwarded-for") == null) {
            remoteIps = request.getRemoteAddr();
        }else {
            remoteIps = request.getHeader("x-forwarded-for");
        }
        String ip = remoteIps;
        if (remoteIps != null) {
            String[] ipArr = StringUtils.split(remoteIps, ",");
            if (ipArr != null && ipArr.length > 0) {
                ip = ipArr[0];
            }
        }
        return ip;
    }


    /*验证IP是否属于某个IP段

     *

     * ipSection    IP段（以'-'分隔）

     * ip           所验证的IP号码

     *

     */

    public static boolean ipExistsInRange(String ip, String ipSection) {

        ipSection = ipSection.trim();

        ip = ip.trim();

        int idx = ipSection.indexOf('-');

        String beginIP = ipSection.substring(0, idx);

        String endIP = ipSection.substring(idx + 1);

        /**
         * 2020年8月21日17:23:24 申欣武
         * 添加对于ip的判断
         */
        if(StringUtils.isBlank(beginIP)||StringUtils.isBlank(endIP)){
            return false;
        }

        return getIp2long(beginIP) <= getIp2long(ip) && getIp2long(ip) <= getIp2long(endIP);

    }

    public static long getIp2long(String ip) {
        long ip2long = 0L;
        if(StringUtils.isBlank(ip)){
           return  ip2long;
        }
        ip = ip.trim();
        String[] ips = ip.trim().split("\\.");
        for (int i = 0; i < 4; ++i) {
            if(StringUtils.isBlank(ips[i])){
                continue;
            }
            ip2long = ip2long << 8 | Integer.parseInt(ips[i]);

        }
        return ip2long;

    }

    public static long getIp2long2(String ip) {

        ip = ip.trim();

        String[] ips = ip.split("\\.");

        long ip1 = Integer.parseInt(ips[0]);

        long ip2 = Integer.parseInt(ips[1]);

        long ip3 = Integer.parseInt(ips[2]);

        long ip4 = Integer.parseInt(ips[3]);


        long ip2long = 1L * ip1 * 256 * 256 * 256 + ip2 * 256 * 256 + ip3 * 256 + ip4;

        return ip2long;

    }

    public static int getExists(String ip) {
        System.out.println("访问Ip：" + ip);
        InetAddress addr;
        int ext = 0;
        boolean exists = false;
        try {
            addr = InetAddress.getLocalHost();
            // String ip=addr.getHostAddress().toString(); //获取本机ip
            //String ipSection="10.163.64.00-10.163.71.255";
            String ipSection = "192.168.0.1-192.168.0.240";
            exists = ipExistsInRange(ip, ipSection);
            if (exists) {
                ext = 1;
            }

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ext;
    }

    public static void main(String[] args) throws Exception {

        //10.10.10.116 是否属于固定格式的IP段10.10.1.00-10.10.255.255
        /*  InetAddress addr = InetAddress.getLocalHost();  
            String ip=addr.getHostAddress().toString(); //获取本机ip  
            String hostName=addr.getHostName().toString(); //获取本机计算机名称
            //String ip="10.163.10.116";
 
            String ipSection="10.163.64.00-10.163.71.255";
 
           boolean exists=ipExistsInRange(ip,ipSection);
 
            System.out.println(exists);*/
        //System.out.println( getExists());
        //System.out.println(getIp2long(ip));
        //System.out.println(getIp2long2(ip));

    }


}
