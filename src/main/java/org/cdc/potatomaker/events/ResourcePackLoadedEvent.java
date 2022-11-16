package org.cdc.potatomaker.events;

import lombok.Getter;
import org.cdc.potatomaker.annotation.Open;
import org.cdc.potatomaker.resourcepack.PackLoader;

/**
 * e-mail: 3154934427@qq.com
 * 资源包被载入时触发
 *
 * @author cdc123
 * @classname ResourcePackLoadedEvent
 * @date 2022/11/15 10:58
 */
@Getter
@Open
public final class ResourcePackLoadedEvent extends ResourcePackEvent{
    private final String packName;

    public ResourcePackLoadedEvent(Object source, PackLoader packLoader) {
        super(source,packLoader);
        this.packName = packLoader.getInfo().getName();
    }
}
