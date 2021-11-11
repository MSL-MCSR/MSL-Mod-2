package com.mcsrleague.mslmod.infograbber;

import com.google.gson.Gson;
import org.apache.commons.lang3.exception.ExceptionUtils;

public abstract class InfoGrabber {
    private String error;

    public abstract String grab() throws Exception;

    public String getError() {
        return error;
    }

    public GrabbedInfoJson fullGrab() {
        try {
            String grabString = grab();
            if (grabString == null || grabString.equals("")) {
                error = "InfoGrabber Error:\nReceived nothing from source.";
                return null;
            }
            try {
                GrabbedInfoJson grabbedInfoJson = new Gson().fromJson(grabString, GrabbedInfoJson.class);
                if (grabbedInfoJson.exception != null) {
                    grabbedInfoJson.error = grabbedInfoJson.exception;
                }
                if (grabbedInfoJson.error != null) {
                    error = grabbedInfoJson.error;
                    return null;
                }
                return grabbedInfoJson;
            } catch (Exception e) {
                error = "InfoGrabber Error:\nCould not parse JSON.";
                return null;
            }
        } catch (Exception e) {
            error = ExceptionUtils.getStackTrace(e);
            return null;
        }
    }
}
