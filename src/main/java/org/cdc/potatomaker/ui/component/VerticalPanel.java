package org.cdc.potatomaker.ui.component;

import javax.swing.*;

/**
 * e-mail: 3154934427@qq.com
 * 垂直显示面板
 *
 * @author cdc123
 * @classname VerticalPanel
 * @date 2022/11/14 9:43
 */
public class VerticalPanel extends JPanel {

    public VerticalPanel(){
        setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
    }
}
