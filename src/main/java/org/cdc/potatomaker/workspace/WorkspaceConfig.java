package org.cdc.potatomaker.workspace;

import lombok.Data;

/**
 * e-mail: 3154934427@qq.com
 * 工作区配置信息类
 *
 * @author cdc123
 * @classname WorkspaceConfig
 * @date 2022/11/15 21:16
 */
@Data
public class WorkspaceConfig {
    private String workspaceName;

    private String version;

    private String generator;
}
