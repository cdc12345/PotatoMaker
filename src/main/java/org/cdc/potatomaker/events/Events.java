package org.cdc.potatomaker.events;

import org.cdc.potatomaker.annotation.Open;
import org.cdc.potatomaker.annotation.events.EventNotSupportRegistered;

import java.util.ArrayList;
import java.util.Comparator;
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
@Open
public class Events {
    private static final ConcurrentHashMap<Class<? extends Event>, ArrayList<EventListener<Event>>> listeners = new ConcurrentHashMap<>();
    /**
     * 注册监听者
     * @param listener 监听者
     * @param event 监听事件
     * @param <E> Event
     */
    public static<E extends Event> boolean registerListener(Class<E> event,EventListener<E> listener){
        if (event.isAnnotationPresent(EventNotSupportRegistered.class)){
            return false;
        }
        if (!listeners.containsKey(event)){
            listeners.put(event,new ArrayList<>());
        }
        List<EventListener<Event>> listeners = Events.listeners.get(event);
        listeners.add((EventListener<Event>) listener);
        return true;
    }

    public static<E extends Event> void unregisterListener(Class<E> event,String remarkName){
        var listeners = Events.listeners.get(event);
        listeners.forEach(a->{
            if (remarkName.equals(a.listenerMark)){
                listeners.remove(a);
            }
        });
    }

    /**
     * 开始反射事件
     * @param event 事件
     */
    public static<V extends Event> void invokeEvent(V event){
        //先执行当前类的
        var clas = event.getClass();
        var listeners1 = listeners.get(clas);
        if (listeners1 != null)
                listeners1.stream().sorted(Comparator.comparingInt(EventListener::priority)).forEach(a->a.listener.accept(event));
    }

    public static<V extends Event> boolean isEmpty(Class<V> vClass){
        var listeners1 = listeners.get(vClass);
        return listeners1.isEmpty();
    }

    public record EventListener<E>(String pluginName, String listenerMark,
                                   Consumer<E> listener,int priority) { }

}
