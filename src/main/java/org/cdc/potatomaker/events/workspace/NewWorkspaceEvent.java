package org.cdc.potatomaker.events.workspace;

import org.cdc.potatomaker.workspace.Workspace;

/**
 * e-mail: 3154934427@qq.com
 * 新建工程事件
 *
 * @author cdc123
 * @classname NewWorkspaceEvent
 * @date 2022/11/19 13:40
 */
public class NewWorkspaceEvent extends WorkspaceEvent {

    public NewWorkspaceEvent(Object source, Workspace workspace) {
        super(source, workspace);
    }
}
