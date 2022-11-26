package org.cdc.potatomaker.workspace.type;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * e-mail: 3154934427@qq.com
 * 工作区模板管理类
 *
 * @author cdc123
 * @classname WorkspaceModelManager
 * @date 2022/11/25 15:28
 */
public class WorkspaceModelManager {
    private static final ConcurrentHashMap<String,WorkspaceModelPack> modelConcurrentHashMap = new ConcurrentHashMap<>();

    public static void register(String type,String fork,WorkspaceModel model){

        modelConcurrentHashMap.put(type+"-"+fork,new WorkspaceModelPack(type, fork, model));
    }

    public static void unregister(String type,String fork){

        modelConcurrentHashMap.remove(type+"-"+fork);
    }

    public static WorkspaceModel get(String generator){
        return modelConcurrentHashMap.get(generator).model;
    }

    public static WorkspaceModelPack[] getModelsByType(String type){
        var result = new HashSet<WorkspaceModelPack>();
        modelConcurrentHashMap.forEach((key,value)->{
            if (Objects.equals(type,value.type)) result.add(value);
        });
        return result.toArray(new WorkspaceModelPack[0]);
    }

    //额外创建一个成员变量是为了让newWorkspaceDialog等,可以动态显示,以防止每次刷新都要重新造实例,浪费性能
    private static final ConcurrentHashMap<String,HashSet<String>> typesAndForks = new ConcurrentHashMap<>();
    public static Set<String> getForks(String type){
        return typesAndForks.get(type);
    }

    public static Set<String> getTypes(){
        return typesAndForks.keySet();
    }

    public static record WorkspaceModelPack(String type,String fork,WorkspaceModel model){
        @Override
        public String toString() {
            return type+"-"+fork;
        }
    }
}
