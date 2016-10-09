package com.github.czyzby.uedi.test.custom;

// Not scanned by the context, added manually.
public class CustomFactory {
    private final CustomComponent component = new CustomComponent();

    public CustomComponent getComponent() {
        return component;
    }

    public static class CustomComponent {
    }
}
