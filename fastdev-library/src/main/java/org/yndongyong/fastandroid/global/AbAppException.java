package org.yndongyong.fastandroid.global;

import org.apache.http.conn.ConnectTimeoutException;
import org.yndongyong.fastandroid.utils.AbStrUtil;

import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

/**
 * Created by Dong on 2016/6/10.
 */
public class AbAppException {

    public  static String getError(Exception e) {
        String msg = null;
            /*if ((e instanceof HttpHostConnectException))
                msg= AbAppConfig.UNKNOWN_HOST_EXCEPTION;
            else */
        if ((e instanceof ConnectException))
            msg = AbAppConfig.UNKNOWN_HOST_EXCEPTION;
        else if ((e instanceof ConnectTimeoutException))
            msg = AbAppConfig.CONNECT_EXCEPTION;
        else if ((e instanceof UnknownHostException))
            msg = AbAppConfig.UNKNOWN_HOST_EXCEPTION;
        else if ((e instanceof SocketException))
            msg = AbAppConfig.SOCKET_EXCEPTION;
        else if ((e instanceof SocketTimeoutException))
            msg = AbAppConfig.SOCKET_TIMEOUT_EXCEPTION;
        else if ((e instanceof NullPointerException))
            msg = AbAppConfig.NULL_POINTER_EXCEPTION;
            /*else if ((e instanceof ClientProtocolException)) {
                msg= AbAppConfig.CLIENT_PROTOCOL_EXCEPTION;
            }*/
        else if (e instanceof RuntimeException) {
            switch (e.getMessage()) {
                case "400":
                    msg = AbAppConfig.REQUEST_ERROR_EXCEPTION;
                    break;
                case "404":
                    msg = AbAppConfig.NOT_FOUND_EXCEPTION;
                    break;
                case "500":
                    msg = AbAppConfig.REMOTE_SERVICE_EXCEPTION;
                    break;
                
            }
        } else if ((e == null) || (AbStrUtil.isEmpty(e.getMessage())))
            msg = AbAppConfig.NULL_MESSAGE_EXCEPTION;
        else
            msg = e.getMessage();
        return msg;
    }


}
