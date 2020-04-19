package org.panda.data;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

/**
 * 测试数据生成
 */
public class DataGenerator {

    public static final String CPU_PREFIX = "CPU_";
    public static final String FILE_NAME_SUFFIX = ".txt";
    public static final String FILE_PATH = System.getProperty("user.dir") + File.separator + "data";

    public static void main(String[] args) {
        try {
            DataGenerator.generator(9);
        } catch (IOException e) {
            // do nothing
        }
    }

    /**
     * 随机生成测试文件 当天前的 preday 天
     * @param preDay 当日前的天数
     */
    public static void generator(int preDay) throws IOException {

        // 删除data目录下所有文件
        File file = new File(FILE_PATH);
        file.mkdirs();
        File[] files = file.listFiles();
        if (null != files) {
            for (File file1 : files) {
                file1.delete();
            }
        }

        // 天 文件名
        SimpleDateFormat df_1 = new SimpleDateFormat("MMdd");
        SimpleDateFormat df_2 = new SimpleDateFormat("HH:mm");

        // 当天最大时间
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 00, 00, 00);
//        calendar.add(Calendar.DATE, 1);
        Date tomorrow = calendar.getTime();

        Random random = new Random();
        String cpuUsageMessage = "";
        // 循环生成CPU测试文件
        for (int i = 0; i < preDay; i++) {
            calendar.add(Calendar.DATE, -1);
            Date today = calendar.getTime();
            File fileCpu = new File(FILE_PATH + File.separator + CPU_PREFIX + df_1.format(today) + FILE_NAME_SUFFIX);
            fileCpu.createNewFile();
            OutputStream out = new FileOutputStream(fileCpu);

            // 生成cpu使用率文件
            while (today.before(tomorrow)) {
                cpuUsageMessage += (df_2.format(today) + " " +  random.nextInt(100) + "%" /*+ 9*/ + System.getProperty("line.separator"));
                today.setTime(today.getTime() + 60 * 1000);
            }

            out.write(cpuUsageMessage.getBytes(Charset.defaultCharset()));
            out.flush();

            cpuUsageMessage = "";
            tomorrow = calendar.getTime();
        }
    }
}

