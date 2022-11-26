package org.cdc.potatomaker.events;

import org.cdc.potatomaker.annotation.events.EventNotSupportRegistered;
import org.cdc.potatomaker.events.workspace.WorkspaceEvent;
import org.cdc.potatomaker.workspace.Workspace;

import java.io.File;

/**
 * e-mail: 3154934427@qq.com
 * 文件渲染中事件,此事件不可直接注册
 *
 * @author cdc123
 * @classname FileRenderingEvent
 * @date 2022/11/19 21:57
 */
@EventNotSupportRegistered
public class FileRenderingEvent extends WorkspaceEvent {
    public FileRenderingEvent(Object source, Workspace workspace, File renderingFile) {
        super(source, workspace);
    }
}
