package org.cdc.potatomaker.exception;

import org.jetbrains.annotations.NotNull;

/**
 * e-mail: 3154934427@qq.com
 * 插件已经存在错误
 *
 * @author cdc123
 * @classname PluginExistException
 * @date 2022/11/14 15:23
 */
public class PluginExistException extends PluginException {
    public PluginExistException(@NotNull String mess) {
        super(mess);
    }
}
