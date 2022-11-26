package org.cdc.potatomaker.events.workspace;

import lombok.Getter;
import org.cdc.potatomaker.events.workspace.WorkspaceEvent;
import org.cdc.potatomaker.workspace.Workspace;

/**
 * e-mail: 3154934427@qq.com
 * 工作区已经打开事件
 *
 * @author cdc123
 * @classname WorkspaceOpenedEvent
 * @date 2022/11/18 14:03
 */
@Getter
public class WorkspaceOpenedEvent extends WorkspaceEvent {
    public WorkspaceOpenedEvent(Object source,Workspace workspace) {
        super(source,workspace);
    }
}
