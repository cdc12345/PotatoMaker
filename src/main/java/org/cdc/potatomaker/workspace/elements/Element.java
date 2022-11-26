package org.cdc.potatomaker.workspace.elements;

import lombok.Data;
import org.cdc.potatomaker.annotation.Open;

/**
 * e-mail: 3154934427@qq.com
 * 元素类
 *
 * @author cdc123
 * @classname Element
 * @date 2022/11/25 22:25
 */
@Data
@Open
public final class Element {
    /**
     * 元素名称
     */
    String name;
    /**
     * 元素类型
     */
    String type;

    /**
     * 元素渲染器
     */
    ElementRender render;

    /**
     * 元素模板
     */
    ElementModel model;

    public ElementRender getRender() {
        if (render == null) render = model.getElementRender(this);
        return render;
    }
}
