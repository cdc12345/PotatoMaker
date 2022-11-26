package org.cdc.potatomaker.workspace.elements;

import org.cdc.potatomaker.annotation.Open;


/**
 * e-mail: 3154934427@qq.com
 * 元素模板,此类会被提供给生成器作为代码生成的参照
 *
 * @author cdc123
 * @classname ElementModel
 * @date 2022/11/25 22:25
 */
@Open
public interface ElementModel {

    ElementRender getElementRender(Element element);
}
