package org.cdc.potatomaker.workspace.runConfig;


import lombok.Getter;
import org.cdc.potatomaker.annotation.Open;

/**
 * e-mail: 3154934427@qq.com
 * 启动元素类
 *
 * @author cdc123
 * @classname LaunchElement
 * @date 2022/11/25 21:40
 */
@Open
public abstract class RunConfiguration {
    /**
     * 启动类型
     */
    @Getter
    protected String actionType;
}
