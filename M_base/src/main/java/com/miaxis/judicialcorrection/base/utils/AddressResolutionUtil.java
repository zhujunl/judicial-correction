package com.miaxis.judicialcorrection.base.utils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddressResolutionUtil {
    /**
     * 解析地址
     * @author lin
     * @param address
     * @return
     */
    public static List<Map<String,String>> addressResolution(String address){
        String regex="(?<province>[^省]+自治区|.*?省|.*?行政区|.*?市)(?<city>[^市]+自治州|.*?地区|.*?行政单位|.+盟|市辖区|.*?市|.*?县)(?<county>[^县]+县|.+区|.+市|.+旗|.+海域|.+岛)?(?<town>[^区]+区|.+镇)?(?<village>.*)";
        Matcher m= Pattern.compile(regex).matcher(address);
        String province,city,county,town,village;
        List<Map<String,String>> table=new ArrayList<>();
        Map<String,String> row;
        while(m.find()){
            row = new LinkedHashMap<>();
            province = m.group(1);
            row.put("province", province==null?"":province.trim());
            city = m.group(2);
            row.put("city", city==null?"":city.trim());
            county = m.group(3);
            row.put("county", county==null?"":county.trim());
            town = m.group(4);
            row.put("town", town==null?"":town.trim());
            village = m.group(5);
            row.put("village", village==null?"":village.trim());
            table.add(row);
        }
        return table;
    }
}
