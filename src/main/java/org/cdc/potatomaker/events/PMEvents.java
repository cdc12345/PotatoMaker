package org.cdc.potatomaker.events;

import java.util.function.Consumer;

/**
 * e-mail: 3154934427@qq.com
 * PM内部API
 *
 * @author cdc123
 * @classname PMEvents
 * @date 2022/11/17 17:17
 */
public class PMEvents {
    public static<E extends Event> void registerListener(Class<E> cla, String remark,Consumer<E> listener){
        Events.registerListener(cla,new Events.EventListener<>("potatoMaker-core","pm",listener,5));
    }
}
