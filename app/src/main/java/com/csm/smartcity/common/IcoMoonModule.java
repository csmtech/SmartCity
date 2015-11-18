package com.csm.smartcity.common;

import com.joanzapata.iconify.Icon;
import com.joanzapata.iconify.IconFontDescriptor;

/**
 * Created by rasmikant on 9/17/2015.
 */
public class IcoMoonModule implements IconFontDescriptor {
    @Override
    public String ttfFileName() {
        return "fonts/icomoon.ttf";
    }

    @Override
    public Icon[] characters() {
        return IcoMoonIcons.values();
    }
}
