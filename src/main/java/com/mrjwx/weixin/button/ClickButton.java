package com.mrjwx.weixin.button;

import lombok.Data;

@Data
public class ClickButton extends AbstractButton {
    private String type;
    private String key;
}
