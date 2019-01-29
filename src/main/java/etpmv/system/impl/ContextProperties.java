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

    public String pcp_esb_canonical_adapter_port;
    public String pcp_esb_canonical_adapter_path;
    public String soap_functions_mapping_file;
    public String esb_async_answer_endpoint;
    public String esb_async_answer_component;
    public String esb_async_answer_user;
    public String esb_async_answer_password;
    public String esb_canonical_pts_id;

    public String getPcp_esb_canonical_adapter_port() {
        return pcp_esb_canonical_adapter_port;
    }

    public String getPcp_esb_canonical_adapter_path() {
        return pcp_esb_canonical_adapter_path;
    }

    public String getSoap_functions_mapping_file() {
        return soap_functions_mapping_file;
    }

    public String getEsb_async_answer_endpoint() {
        return esb_async_answer_endpoint;
    }

    public String getEsb_async_answer_component() {
        return esb_async_answer_component;
    }

    public String getEsb_async_answer_user() {
        return esb_async_answer_user;
    }

    public String getEsb_async_answer_password() {
        return esb_async_answer_password;
    }

    public String getEsb_canonical_pts_id() {
        return esb_canonical_pts_id;
    }

    public void synchronizeContext() {
        if (pcp_esb_canonical_adapter_port != null) {
            this.setProperty("pcp_esb_canonical_adapter_port", pcp_esb_canonical_adapter_port);
        }
        if (pcp_esb_canonical_adapter_path != null) {
            this.setProperty("pcp_esb_canonical_adapter_path", pcp_esb_canonical_adapter_path);
        }
        if (soap_functions_mapping_file != null) {
            this.setProperty("soap_functions_mapping_file", soap_functions_mapping_file);
        }

        if (esb_async_answer_component != null) {
            this.setProperty("esb_async_answer_component", esb_async_answer_component);
        }
        if (esb_async_answer_user != null) {
            this.setProperty("esb_async_answer_user", esb_async_answer_user);
        }
        if (esb_async_answer_password != null) {
            this.setProperty("esb_async_answer_password", esb_async_answer_password);
        }
        if (esb_async_answer_endpoint != null) {
            this.setProperty("esb_async_answer_endpoint", esb_async_answer_endpoint);
        }
        if (esb_canonical_pts_id != null) {
            this.setProperty("esb_canonical_pts_id", esb_canonical_pts_id);
        }
    }
}