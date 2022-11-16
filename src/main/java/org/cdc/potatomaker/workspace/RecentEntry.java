package org.cdc.potatomaker.workspace;

import lombok.Data;

/**
 * e-mail: 3154934427@qq.com
 *
 *
 * @author cdc123
 * @classname RecentEntry
 * @date 2022/11/15 21:15
 */
@Data
public class RecentEntry {
    private WorkspaceConfig workspaceConfig;

    public String getName(){
        return workspaceConfig.getWorkspaceName();
    }

    public String getGenerator(){
        return workspaceConfig.getGenerator();
    }
}
