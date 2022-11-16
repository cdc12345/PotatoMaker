package org.cdc.potatomaker.events;

import lombok.Getter;
import org.cdc.potatomaker.annotation.Open;
import org.cdc.potatomaker.resourcepack.PackLoader;

/**
 * e-mail: 3154934427@qq.com
 * 资源包事件
 *
 * @author cdc123
 * @classname ResourcePackEvent
 * @date 2022/11/16 7:40
 */
@Getter
@Open
public class ResourcePackEvent extends Event{
    protected final PackLoader packLoader;

    public ResourcePackEvent(Object source, PackLoader packLoader) {
        super(source);
        this.packLoader = packLoader;
    }
}
