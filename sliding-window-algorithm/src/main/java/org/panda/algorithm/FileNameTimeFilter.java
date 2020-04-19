package org.panda.algorithm;


import java.io.File;
import java.io.FilenameFilter;
import java.util.List;

/**
 * 过滤文件名称
 */
public class FileNameTimeFilter implements FilenameFilter {


    private List<String> fileNames;

    public FileNameTimeFilter(List<String> fileNames) {
        this.fileNames = fileNames;
    }

    @Override
    public boolean accept(File dir, String name) {
        return fileNames.contains(name);
    }
}
