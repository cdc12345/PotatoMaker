package org.cdc.potatomaker.events;

import lombok.Getter;
import org.cdc.potatomaker.annotation.Open;
import org.cdc.potatomaker.resourcepack.PackLoader;

/**
 * e-mail: 3154934427@qq.com
 * 资源包被启用事件
 *
 * @author cdc123
 * @classname ResourcePackEnabledEvent
 * @date 2022/11/16 7:40
 */
@Getter
@Open
public class ResourcePackEnabledEvent extends ResourcePackEvent{
    private final String packName;

    public ResourcePackEnabledEvent(Object source, PackLoader packLoader) {
        super(source, packLoader);
        packName = packLoader.getInfo().getName();
    }
}
