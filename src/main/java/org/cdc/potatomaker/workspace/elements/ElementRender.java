package org.cdc.potatomaker.workspace.elements;

import java.awt.*;

/**
 * e-mail: 3154934427@qq.com
 * 元素可视化渲染器
 *
 * @author cdc123
 * @classname ElementRender
 * @date 2022/11/26 7:15
 */
public interface ElementRender {
    /**
     * 得到渲染的模板
     * @param element 被渲染的元素
     * @return 渲染结果
     */
    Component getElementComponent(Element element);
}
