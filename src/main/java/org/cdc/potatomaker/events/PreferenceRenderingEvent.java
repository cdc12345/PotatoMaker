package org.cdc.potatomaker.events;

import lombok.Getter;

import java.util.HashMap;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * e-mail: 3154934427@qq.com
 * 设置条目正在渲染事件
 *
 * @author cdc123
 * @classname PreferenceRenderingEvent
 * @date 2022/11/13 22:38
 */
public final class PreferenceRenderingEvent extends Event {
    @Getter
    private final HashMap<String,Object> cache;
    private final Supplier<Object> getter;
    private final Consumer<Object> setter;
    public PreferenceRenderingEvent(Supplier<Object> getter, Consumer<Object> setter,HashMap<String,Object> cache){
        super(null);
        this.getter = getter;
        this.setter = setter;
        this.cache = cache;
    }

    public void setValue(Object obj){
        this.setter.accept(obj);
    }

    public Object getValue(){
        return this.getter.get();
    }

}
