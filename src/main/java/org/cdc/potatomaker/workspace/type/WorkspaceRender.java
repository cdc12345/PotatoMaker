package org.cdc.potatomaker.workspace.type;

import java.awt.*;

/**
 * e-mail: 3154934427@qq.com
 * 工作区UI渲染类
 *
 * @author cdc123
 * @classname WorkspaceRender
 * @date 2022/11/25 21:35
 */
public interface WorkspaceRender {
    /**
     * 编辑器面板的实现方法
     * @param tabName tab名字
     * @return 渲染的面板
     */
    Component getTabComponent(String tabName);

    /**
     * 根据提供的名字来渲染
     * @param name 面板名字
     * @return 面板
     */
    Component getComponentByName(String name);
}
