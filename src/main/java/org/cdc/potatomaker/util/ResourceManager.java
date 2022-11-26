package org.cdc.potatomaker.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.cdc.potatomaker.annotation.Open;
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
@Open
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

    public InputStream getOuterResourceAsStream(@NotNull String outerPath){
        return this.getOuterResourceAsStream(outerPath,null);
    }
    public InputStream getOuterResourceAsStream(@NotNull String outerPath,InputStream defaultInput) {
        var outer = getOuterResourceAsFile(outerPath);
        outer.getParentFile().mkdirs();
        try {
            return new FileInputStream(outer);
        } catch (FileNotFoundException e) {
            return defaultInput;
        }
    }

    /**
     * 把字节流转化为字符流
     * @param inputStream
     * @return
     */
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
        outer.getParentFile().mkdirs();
        try {
            return new FileOutputStream(outer);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static InputStream getInnerResourceAsStream(@NotNull String innerPath){
        return ResourceManager.class.getResourceAsStream("/"+innerPath);
    }

    /**
     * 提取内部的资源,内部路径和外部路径保持一致
     * @param innerPaths 内部路径
     * @throws IOException 提取失败
     */
    public void extractResources(String... innerPaths) throws IOException {
        for (String inner:innerPaths){
            extractResource(inner);
        }
    }

    public void extractResource(String innerPath) throws IOException {
        extractResource(innerPath,innerPath);
    }

    /**
     * 提取内部资源到外部路径
     * @param innerPath 内部路径
     * @param outerPath 外部路径
     * @throws IOException 提取失败
     */
    public void extractResource(String innerPath,String outerPath) throws IOException {
        var inner = getInnerResourceAsStream(innerPath);
        if (inner != null){
            inner.transferTo(getOuterResourceAsOutputStream(outerPath));
        } else {
            throw new FileNotFoundException("Could not find innerSource: "+innerPath);
        }
    }

    /**
     * 将内容写入外部资源
     * @param text 内容
     * @param name 资源路径
     */

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

    /**
     *
     * @param resourcePath 内部和外部统一的路径
     * @return 内部或者外部资源的流 如果外部资源是null 那么就会输出内部资源
     */
    public InputStream getInnerOrOuterResourceAsStream(String resourcePath){
        var outer = getOuterResourceAsStream(resourcePath);
        return outer==null? getInnerResourceAsStream(resourcePath):outer;
    }


}
