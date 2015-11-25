package com.csm.smartcity.common;

import com.joanzapata.iconify.Icon;

public enum IcoMoonIcons implements Icon {
//    ic_sewage('\ue624'),
//    ic_drinkingwater('\ue625'),
//    ic_mycitymypride('\ue621');
    ic_badge('\ue900'),
    ic_sad('\ue901');
    char character;

    IcoMoonIcons(char character) {
        this.character = character;
    }

    @Override
    public String key() {
        return name().replace('_', '-');
    }

    @Override
    public char character() {
        return character;
    }
}