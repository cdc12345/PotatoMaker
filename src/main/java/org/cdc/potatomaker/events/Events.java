package org.cdc.potatomaker.events;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * e-mail: 3154934427@qq.com
 * 事件工具
 *
 * @author cdc123
 * @classname Events
 * @date 2022/11/15 10:58
 */
public class Events {
    private static final ConcurrentHashMap<Class<? extends Event>, ArrayList<Consumer<Event>>> listeners = new ConcurrentHashMap<>();

    /**
     * 注册监听者
     * @param listener 监听者
     * @param event 监听事件
     * @param <E> Event
     */
    public static<E extends Event> void registerListener(Consumer<E> listener,Class<E> event){
        if (!listeners.containsKey(event)){
            listeners.put(event,new ArrayList<>());
        }
        listeners.get(event).add((Consumer<Event>) listener);
    }

    /**
     * 开始反射事件
     * @param event 事件
     */
    public static void invokeEvent(Event event){
        var listeners1 = listeners.get(event.getClass());
        if (listeners1 != null)
                listeners1.forEach(a->a.accept(event));
    }
}
