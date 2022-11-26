package org.cdc.potatomaker.workspace;

import lombok.Getter;
import org.cdc.potatomaker.annotation.Open;
import org.cdc.potatomaker.util.ResourceManager;
import org.cdc.potatomaker.workspace.elements.Element;
import org.cdc.potatomaker.workspace.runConfig.RunConfigurationList;
import org.cdc.potatomaker.workspace.type.WorkspaceModel;
import org.cdc.potatomaker.workspace.type.WorkspaceModelManager;
import org.cdc.potatomaker.workspace.type.WorkspaceRender;

import java.io.*;
import java.util.List;

/**
 * e-mail: 3154934427@qq.com
 * 工作空间类
 *
 * @author cdc123
 * @classname Workspace
 * @date 2022/11/16 16:46
 */
@Open
public class Workspace {

    @Getter
    WorkspaceConfig workspaceConfig;

    State workspaceState = State.New;

    WorkspaceModel model;

    @Getter
    RunConfigurationList launchActionList;
    @Getter
    List<Element> elements;

    @Getter
    String[] west;
    @Getter
    String[] east;
    @Getter
    String[] south;
    Workspace(){

    }

    public String getGenerator(){
        return workspaceConfig.getType()+"-"+workspaceConfig.getFork();
    }

    public File getPMPath(){return new File(workspaceConfig.getWorkspaceFolder(),".pm");}
    public File getPath(){
        return workspaceConfig.getWorkspaceFolder();
    }

    public ResourceManager getWorkspaceManager(){
        return new ResourceManager(workspaceConfig.getWorkspaceFolder());
    }


    public String getName(){return workspaceConfig.getWorkspaceName();}


    public void update(Workspace workspace){
        this.workspaceConfig = workspace.workspaceConfig;
    }

    public WorkspaceModel getModel() {
        if (model == null) model = WorkspaceModelManager.get(getGenerator());
        return model;
    }

    public WorkspaceRender getRender(){
        return model==null?null: model.getRender(this);
    }

    /**
     * 工作区分为多种状态,分别为New(工作区刚实例化的状态) Creating(工作区正在构建基础环境)Building(工作区正在构建)Opening(工作区正常打开)
     */
    public static enum State{
        New,Creating,Building,Opening
    }
}
