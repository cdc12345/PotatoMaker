package org.cdc.potatomaker.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * e-mail: 3154934427@qq.com
 * 资源管理类
 *
 * @author cdc123
 * @classname ResourceUtil
 * @date 2022/11/5 12:10
 */
public class ResourceManager {
    private static final Logger LOGGER = LogManager.getLogger(ResourceManager.class.getSimpleName());
    private final File parent;

    public ResourceManager(@NotNull String parent){
        this.parent = new File(parent);
    }
    public ResourceManager(@NotNull File parent) {
        this.parent = parent;
    }


    public File getOuterResourceAsFile(@NotNull String outerPath){
        return new File(parent,outerPath);
    }

    public InputStream getOuterResourceAsStream(@NotNull String outerPath) {
        var outer = getOuterResourceAsFile(outerPath);
        outer.getParentFile().mkdirs();
        try {
            return new FileInputStream(outer);
        } catch (FileNotFoundException e) {
            return null;
        }
    }

    public static Reader fromInputStream(InputStream inputStream){
        return new InputStreamReader(inputStream);
    }

    /**
     * 得到外部资源的输出流,注意这个方法会提前建立好母目录
     * @param outerPath 外部目录
     * @return 输出流
     */

    public OutputStream getOuterResourceAsOutputStream(@NotNull String outerPath){
        var outer = getOuterResourceAsFile(outerPath);
        if (outer.isDirectory()){
            return null;
        }
        outer.getParentFile().mkdirs();
        try {
            return new FileOutputStream(outer);
        } catch (FileNotFoundException e) {
            return null;
        }
    }

    public static InputStream getInnerResourceAsStream(@NotNull String innerPath){
        return ResourceManager.class.getResourceAsStream("/"+innerPath);
    }

    public void extractResources(String... innerPaths) throws IOException {
        for (String inner:innerPaths){
            extractResource(inner);
        }
    }

    public void extractResource(String innerPath) throws IOException {
        extractResource(innerPath,innerPath);
    }

    public void extractResource(String innerPath,String outerPath) throws IOException {
        var inner = getInnerResourceAsStream(innerPath);
        if (inner != null){
            inner.transferTo(getOuterResourceAsOutputStream(outerPath));
        } else {
            throw new FileNotFoundException("Could not find innerSource: "+innerPath);
        }
    }

    public void writeResource(String text,String name){
        writeResource(new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8)),name);
    }

    public void writeResource(InputStream inputStream,String name){
        var out = getOuterResourceAsOutputStream(name);
        try {
            inputStream.transferTo(out);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public InputStream getResourceAsStream(String resourcePath){
        var outer = getOuterResourceAsStream(resourcePath);
        return outer==null? getInnerResourceAsStream(resourcePath):outer;
    }


}
