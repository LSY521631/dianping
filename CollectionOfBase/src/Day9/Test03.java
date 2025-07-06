package Day9;

import java.util.ArrayList;
import java.util.HashMap;

public class Test03 {
    public static void main(String[] args) {
        /**
         * key=省份
         * value=市区
         */
        HashMap<String, ArrayList<String>> map = new HashMap<>();
        // 山东省的城市
        ArrayList<String> shandong = new ArrayList<>();
        shandong.add("济南市");
        shandong.add("济宁市");
        shandong.add("青岛市");
        map.put("山东省", shandong);

        // 浙江省的城市
        ArrayList<String> zhejiang = new ArrayList<>();
        zhejiang.add("杭州市");
        zhejiang.add("绍兴市");
        zhejiang.add("宁波市");
        map.put("浙江省", zhejiang);

        // 遍历输出
        for (String key : map.keySet()) {
            System.out.println("省份:" + key);
            System.out.print("城市:");
            System.out.println(map.get(key));
            System.out.println("/***********************************/");
        }

    }
}
