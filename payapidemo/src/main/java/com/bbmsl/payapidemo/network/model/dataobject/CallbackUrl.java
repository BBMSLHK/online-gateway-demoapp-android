package com.bbmsl.payapidemo.network.model.dataobject;

import static com.bbmsl.payapidemo.config.SampleCallbackURL.CALLBACKURL_BASE;
import static com.bbmsl.payapidemo.config.SampleCallbackURL.CALLBACK_CANCELLED;
import static com.bbmsl.payapidemo.config.SampleCallbackURL.CALLBACK_FAIL;
import static com.bbmsl.payapidemo.config.SampleCallbackURL.CALLBACK_NOTIFY;
import static com.bbmsl.payapidemo.config.SampleCallbackURL.CALLBACK_SUCCESS;

public class CallbackUrl {
    private String success =    CALLBACKURL_BASE + CALLBACK_SUCCESS;
    private String fail =       CALLBACKURL_BASE + CALLBACK_FAIL;
    private String cancel =     CALLBACKURL_BASE + CALLBACK_CANCELLED;
    private String notify =     CALLBACKURL_BASE + CALLBACK_NOTIFY;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getFail() {
        return fail;
    }

    public void setFail(String fail) {
        this.fail = fail;
    }

    public String getCancel() {
        return cancel;
    }

    public void setCancel(String cancel) {
        this.cancel = cancel;
    }

    public String getNotify() {
        return notify;
    }

    public void setNotify(String notify) {
        this.notify = notify;
    }
}
