package com.github.nebula.graphics.window;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import static com.github.nebula.graphics.window.WindowHint.*;

public class WindowHints {
    protected final Map<WindowHint, Boolean> windowHintsMap;

    @SuppressWarnings("all")
    public WindowHints() {
        var windowHintSet = EnumSet.allOf(WindowHint.class);
        windowHintsMap = new HashMap<>();
        for (var windowHint : windowHintSet)
            windowHintsMap.put(windowHint, false);
    }

    public WindowHints defaultHints() {
        windowHintsMap.put(VISIBLE, true);
        windowHintsMap.put(RESIZABLE, true);
        windowHintsMap.put(DECORATED, true);
        windowHintsMap.put(FOCUSED, true);
        windowHintsMap.put(AUTO_ICONIFY, true);
        windowHintsMap.put(FOCUS_ON_SHOW, true);
        windowHintsMap.put(DOUBLEBUFFER, true);
        return this;
    }

    public WindowHints windowHint(WindowHint windowHint, boolean enabled) {
        windowHintsMap.put(windowHint, enabled);
        return this;
    }
}
