package org.cdc.potatomaker.exception;

/**
 * e-mail: 3154934427@qq.com
 * 插件错误统一母类
 *
 * @author cdc123
 * @classname PluginException
 * @date 2022/11/15 9:41
 */
public class PluginException extends Exception{
    public PluginException(String mess){
        super(mess);
    }
}
