package org.panda.algorithm;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class CpuUsageIntervalCalc {

    public static final String FILE_NAME_PREFIX = "CPU_";
    public static final String FILE_NAME_SUFFIX = ".txt";
    public static final String FILE_PATH = System.getProperty("user.dir") + File.separator + "data";

    public static void main(String[] args) {
        // 近N天 取前N天文件名
        Integer N = new Integer(args[0]);
        // M值
        Integer M = new Integer(args[1]);
        try {
            List<List<Integer>> lists = new ArrayList<>();
            List<File> files = generatorNFiles(FILE_PATH, N);
            if (null != files) {
                for (File file : files) {
                    List<Integer> list = readFileToList(file);
                    lists.add(list);
                }
                List<String> resultIndex = intervalCalc(lists, M);
                List<String> resultTime = convertIndex2Time(resultIndex);
                System.out.println(resultTime);
            }
        } catch (Exception e) {
            // do nothing
        }
    }


    /**
     * 读取文件成Map key 文件名 List 存储百分比的数值
     * @param file 目录
     * @return Map<String, List<Integer>>
     */
    private static List<Integer> readFileToList(File file) throws IOException {
        if (null != file && file.isFile()) {
            List<Integer> list = new ArrayList<>();
            FileInputStream in = new FileInputStream(file);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
            String line;
            String[] arrays;
            while ((line = bufferedReader.readLine()) != null) {
                arrays = line.split(" ");
                arrays = arrays[1].split("%");
                list.add(new Integer(arrays[0]));
            }

            return list;
        }
        return null;
    }

    /**
     * 类似滑动算法
     * 每日的CPU使用率符合条件区间计算
     * @param lists 每日的CPU市盈率信息
     * @return List<String> 00:01    01:01
     */
    private static List<String> intervalCalc(List<List<Integer>> lists, int m) {
        List<String> intervals = new ArrayList<>();
        int leftBoundary = 0;
        int rightBoundary = 1;

        int left = 0;
        int right = 1;
        while (left < 1380 && right < 1440) {
            for (int i = 0; i < lists.size(); i++) {
                if (lists.get(i).get(left) >= m) {
                    left++;
                    right++;
                    break;
                }
                if (lists.get(i).get(right) >= m) {
                    left = ++right;
                    right++;
                    if (rightBoundary - leftBoundary >= 60) {
                        intervals.add(leftBoundary + "," + rightBoundary);
                    }
                    break;
                }

                if (i == (lists.size() - 1)) {
                    leftBoundary = left;
                    rightBoundary = right;
                    right++;
                    if (right >= 1440) {
                        if (rightBoundary - leftBoundary >= 60) {
                            intervals.add(leftBoundary + "," + rightBoundary);
                        }
                    }
                }

            }
        }

        return intervals;
    }

    /**
     * 获取最近N天的CPU文档
     * @param m 最近N天
     * @return List<File>
     */
    private static List<File> generatorNFiles(String path, int m) {
        SimpleDateFormat df_1 = new SimpleDateFormat("MMdd");
        Calendar calendar = Calendar.getInstance();
        List<String> fileNames = new ArrayList<>();
        for (int i = 1; i <= m; i++) {
            calendar.add(Calendar.DATE, -1);
            fileNames.add(FILE_NAME_PREFIX + df_1.format(calendar.getTime()) + FILE_NAME_SUFFIX);
        }
        File[] files = new File(path).listFiles(new FileNameTimeFilter(fileNames));
        if (null != files) {
            return Arrays.asList(files);
        }

        return null;
    }

    /**
     * 转化数组位置到时间区间
     * @param indexs 数组位置
     * @return List<String>
     */
    private static List<String> convertIndex2Time(List<String> indexs) {
        SimpleDateFormat df_1 = new SimpleDateFormat("HH:mm");
        Calendar calendar = Calendar.getInstance();
        List<String> list = new ArrayList<>();
        for (String index : indexs) {
            String[] indexInner = index.split(",");
            calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 00, new Integer(indexInner[0]), 00);
            String start = df_1.format(calendar.getTime());
            calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 00, new Integer(indexInner[1]), 00);
            String end = df_1.format(calendar.getTime());
            list.add(start + "-" + end);
        }

        return list;
    }
}
