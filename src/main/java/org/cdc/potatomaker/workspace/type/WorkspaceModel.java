package org.cdc.potatomaker.workspace.type;

import org.cdc.potatomaker.workspace.Workspace;
import org.cdc.potatomaker.workspace.WorkspacePanelDirection;
import org.cdc.potatomaker.workspace.runConfig.RunConfigurationList;

import java.awt.*;

/**
 * e-mail: 3154934427@qq.com
 * 工作区模板
 *
 * @author cdc123
 * @classname WorkspaceModel
 * @date 2022/11/25 15:27
 */
public interface WorkspaceModel {

    Component getNewWorkspaceComponent();

    /**
     * 工作区状态任务,创建时
     * @param workspace 工作区实例
     */
    void Create(Workspace workspace);

    /**
     * 工作区状态任务,打开时
     * @param workspace 工作区实例
     */
    void Open(Workspace workspace);

    /**
     * 得到相应方位的面板的所有UI容器
     * @param direction 方位
     * @return 容器名称
     */
    String[] getComponentNames(WorkspacePanelDirection direction);

    /**
     * 得到UI渲染器
     * @param workspace 工作区实例
     * @return UI渲染
     */
    WorkspaceRender getRender(Workspace workspace);

    /**
     * 得到默认的启动选项
     */
    RunConfigurationList getRunConfigurations();

}
