package org.panda.generate.data;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Random;

public class DataGenerate {

    public static void main(String[] args) {
        int m = 500000000;
        generateBigData(m);
    }

    /**
     * 文件读写大数据  4g
     */
    private static void generateBigData(int m) {
        String fileName = System.getProperty("user.dir") + File.separator + "bigData.txt";
        try {
            RandomAccessFile randomAccessFile = new RandomAccessFile(fileName, "rw");
            // 根据Integer.MAX_VALUE来写入，可以根据内存实际情况缩小
            MappedByteBuffer mappedByteBuffer = randomAccessFile.getChannel().map(FileChannel.MapMode.READ_WRITE, 0, Integer.MAX_VALUE);

            Random random = new Random();
            // 生成m个随机数据
            long start = System.currentTimeMillis();
            for (int n = 0; n < m; n++) {
                String number = random.nextInt(999999999) + System.getProperty("line.separator");
                if ((mappedByteBuffer.position() + number.getBytes().length) >= 0 && (mappedByteBuffer.position() + number.getBytes().length) < mappedByteBuffer.limit()) {
                    mappedByteBuffer.put(number.getBytes());
                } else {
                    mappedByteBuffer.force();
                    mappedByteBuffer = randomAccessFile.getChannel().map(FileChannel.MapMode.READ_WRITE, mappedByteBuffer.position(), Integer.MAX_VALUE);
                }
            }

            mappedByteBuffer.force();
            long end = System.currentTimeMillis();

            System.out.println((end - start) / 1000);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
