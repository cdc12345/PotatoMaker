package org.cdc.potatomaker.workspace;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.cdc.potatomaker.workspace.elements.Element;
import org.cdc.potatomaker.workspace.runConfig.RunConfigurationList;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import static org.cdc.potatomaker.util.ResourceManager.fromInputStream;

/**
 * e-mail: 3154934427@qq.com
 * 工作区实例管理类
 *
 * @author cdc123
 * @classname WorkspaceManager
 * @date 2022/11/23 12:29
 */
public final class WorkspaceManager {
    private static final ConcurrentHashMap<File,Workspace> workspaceConcurrentHashMap = new ConcurrentHashMap<>();
    public static Workspace getOrCreateWorkspace(File workspaceFolder){
        if (workspaceConcurrentHashMap.containsKey(workspaceFolder)) {
            return workspaceConcurrentHashMap.get(workspaceFolder);
        }
        var result = new Workspace();
        var pmFolder = new File(workspaceFolder,".pm");
        //为工作区添加pm的标志
        pmFolder.mkdirs();
        //这个方法不论有没有config配置文件,都可以获取结果
        result.workspaceConfig = loadWorkspaceConfig(workspaceFolder);
        result.launchActionList = loadWorkspaceLaunchActions(workspaceFolder);
        workspaceConcurrentHashMap.put(workspaceFolder,result);
        return result;
    }

    public static boolean isWorkspaceInit(@NotNull File workspaceFolder){
        var workspaceConfig = new File(workspaceFolder,".pm/workspaceConfig.json");
        var elements = new File(workspaceFolder,"elements");
        return workspaceConfig.exists()&&elements.isDirectory()&&elements.exists();
    }

    private static RunConfigurationList loadWorkspaceLaunchActions(@NotNull File workspaceFolder) {
        var launchConfigFolder = new File(workspaceFolder,".pm/runConfigurations.json");
        //确保它不为空
        try{
            Files.copy(new ByteArrayInputStream("".getBytes(StandardCharsets.UTF_8)),launchConfigFolder.toPath());
        } catch (IOException ignore){}
        RunConfigurationList result = null;
        try{
            result = new Gson().fromJson(fromInputStream(new FileInputStream(launchConfigFolder)), RunConfigurationList.class);
        } catch (FileNotFoundException ignore) {}
        if (result == null) result = new RunConfigurationList();
        return result;
    }

    public static WorkspaceConfig loadWorkspaceConfig(@NotNull File workspaceFolder){
        var workspaceConfigPath = new File(workspaceFolder,".pm/workspaceConfig.json");
        try {
            //这个报错一般情况是因为文件已经存在,所以可以忽略
            Files.copy(new ByteArrayInputStream("{}".getBytes(StandardCharsets.UTF_8)),workspaceConfigPath.toPath());
        } catch (IOException ignore) {}
        WorkspaceConfig  workspaceConfig= null;
        try {
            //前面除了特殊情况外,基本不可能没有文件
            workspaceConfig = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().
                    fromJson(fromInputStream(new FileInputStream(workspaceConfigPath)), WorkspaceConfig.class);
        } catch (FileNotFoundException ignore) {}
        //还是稍微谨慎一点
        if (workspaceConfig == null) workspaceConfig = new WorkspaceConfig();
        workspaceConfig.setWorkspaceConfigFolder(workspaceConfigPath);
        workspaceConfig.setWorkspaceFolder(workspaceFolder);
        return workspaceConfig;
    }


    public static List<Element> loadElements(Workspace workspace) {
        List<Element> elementList = new ArrayList<>();
        var elementsDir = new File(workspace.getPath(),"elements");
        var elements = Objects.requireNonNull(elementsDir.listFiles((dir, name) -> "element".equals(FilenameUtils.getExtension(name))));
        for (File element: elements){
            try {
                elementList.add(new Gson().fromJson(new FileReader(element),Element.class ));
            } catch (FileNotFoundException ignore){}
        }
        return elementList;
    }

    public static void storeWorkspaceConfig(Workspace workspace) throws IOException {
        var configPath = workspace.workspaceConfig.getWorkspaceConfigFolder();
        FileUtils.writeStringToFile(configPath,new GsonBuilder().
                excludeFieldsWithoutExposeAnnotation().create().toJson(workspace.workspaceConfig),StandardCharsets.UTF_8,false);
    }

    public static void storeWorkspaceLaunchList(Workspace workspace) throws IOException {
        var launchPath = new File(workspace.getPMPath(),"runConfiguration.json");
        FileUtils.writeStringToFile(launchPath,new Gson().toJson(workspace.launchActionList),StandardCharsets.UTF_8,false);
    }

    public static void storeWorkspaceElements(Workspace workspace) throws IOException {
        var elementsPath = new File(workspace.getPath(),"elements");
        elementsPath.mkdirs();
        for (Element element:workspace.elements){
            var elementPath = new File(elementsPath,element.getName()+".element");
            FileUtils.writeStringToFile(elementPath,new Gson().toJson(element),StandardCharsets.UTF_8
                    ,false);
        }
    }

    public static void storeWorkspace(Workspace workspace) throws IOException {
        storeWorkspaceConfig(workspace);
        storeWorkspaceLaunchList(workspace);
        storeWorkspaceElements(workspace);
    }

}
