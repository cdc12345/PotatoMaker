package org.cdc.potatomaker.events.workspace;

import lombok.Getter;
import org.cdc.potatomaker.events.Event;
import org.cdc.potatomaker.workspace.Workspace;

/**
 * e-mail: 3154934427@qq.com
 * 工作区事件超类
 *
 * @author cdc123
 * @classname WorkspaceEvent
 * @date 2022/11/19 13:40
 */
@Getter
public class WorkspaceEvent extends Event {
    private final Workspace workspace;
    public WorkspaceEvent(Object source, Workspace workspace) {
        super(source);
        this.workspace = workspace;
    }
}
