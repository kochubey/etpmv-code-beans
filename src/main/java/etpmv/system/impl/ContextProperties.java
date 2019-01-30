package etpmv.system.impl;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Properties;

public class ContextProperties extends Properties {
    private Object arguments;

    public ContextProperties() {
    }

    public ContextProperties(Object arguments) {
        this.arguments = arguments;
    }

    @Override
    public String getProperty(String key) {
        if (arguments == null) return super.getProperty(key);
        String rs = null;
        try {
            if (arguments instanceof HashMap) {
                rs = (String) ((HashMap) arguments).get(key);
            } else {
                Method getProperty = arguments.getClass().getDeclaredMethod(
                        "getProperty", new Class[]{String.class});
                getProperty.setAccessible(true);
                rs = (String) getProperty.invoke(arguments, key);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (rs == null) ? super.getProperty(key) : rs;
    }

    public Object getArguments() {
        return arguments;
    }

    public void setArguments(Object arguments) {
        this.arguments = arguments;
    }
}