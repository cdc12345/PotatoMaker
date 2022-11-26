package org.cdc.potatomaker.workspace.runConfig;

import org.cdc.potatomaker.workspace.Workspace;

/**
 * e-mail: 3154934427@qq.com
 * 执行操作的逻辑
 *
 * @author cdc123
 * @classname LaunchCompiler
 * @date 2022/11/25 21:40
 */
public interface RunConfigurationRunner {
    void performAction(RunConfiguration action, Workspace workspace);
}
