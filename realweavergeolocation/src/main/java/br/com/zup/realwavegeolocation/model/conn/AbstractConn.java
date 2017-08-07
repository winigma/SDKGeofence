package br.com.zup.realwavegeolocation.model.conn;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import br.com.zup.realwavegeolocation.model.exceptions.NetworkStatusException;
import br.com.zup.realwavegeolocation.model.util.Constants;

/**
 * Created by wisle on 01/08/2017.
 */

public abstract class AbstractConn {

    protected URL url;
    private HttpURLConnection con;
    private static final String LINE = "\r\n";
    protected static final String METHOD_GET = "GET";
    protected static final String METHOD_POST = "POST";
    protected static final String SEMILICON = ",";
    protected static final int MEDIUM_TIMEOUT = 5000;
    protected static final int MAX_TIMEOUT = 10000;

    private boolean canConnect() {
        return url != null;
    }

    /**
     * connection on server by URL
     *
     * @param method
     *            post or get
     * @param timeout
     *            for connection
     * @throws NetworkStatusException
     *             when any exception was raised
     */
    protected void connect(final String method, final int timeout)
            throws NetworkStatusException
    {
        if (!canConnect()) {
            return;
        }
        try {
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("content-type", "application/json;  charset=utf-8");
            con.setRequestProperty("x-application-id", "3ba3a25db5409198e079b8d71ce825ed5dd45f87");
            con.setRequestProperty("x-ssl-auth", "LS0tLS1CRUdJTiBDRVJUSUZJQ0FURS0tLS0tDQpNSUlEempDQ0FyWUNDUURTWkFoZm1sbWFmekFOQmdrcWhraUc5dzBCQVFzRkFEQ0JxREVMTUFrR0ExVUVCaE1DDQpRbEl4RlRBVEJnTlZCQWdUREUxcGJtRnpJRWRsY21GcGN6RVRNQkVHQTFVRUJ4UUtWV0psY216aWJtUnBZVEVoDQpNQjhHQTFVRUNoTVlXbFZRSUVsT1ZFVlNUa1ZVSUZORlVsWkZVaUJNVkVSQk1RNHdEQVlEVlFRTEV3VkpibVp5DQpZVEVaTUJjR0ExVUVBeFFRS2k1aGNHbHlaV0ZzZDJGMlpTNXBiekVmTUIwR0NTcUdTSWIzRFFFSkFSWVFhVzVtDQpjbUZBZW5Wd0xtTnZiUzVpY2pBZUZ3MHhOakE0TXpFeE1qUTNNamhhRncweE5qQTVNekF4TWpRM01qaGFNSUdvDQpNUXN3Q1FZRFZRUUdFd0pDVWpFVk1CTUdBMVVFQ0JNTVRXbHVZWE1nUjJWeVlXbHpNUk13RVFZRFZRUUhGQXBWDQpZbVZ5Yk9KdVpHbGhNU0V3SHdZRFZRUUtFeGhhVlZBZ1NVNVVSVkpPUlZRZ1UwVlNWa1ZTSUV4VVJFRXhEakFNDQpCZ05WQkFzVEJVbHVabkpoTVJrd0Z3WURWUVFERkJBcUxtRndhWEpsWVd4M1lYWmxMbWx2TVI4d0hRWUpLb1pJDQpodmNOQVFrQkZoQnBibVp5WVVCNmRYQXVZMjl0TG1KeU1JSUJJakFOQmdrcWhraUc5dzBCQVFFRkFBT0NBUThBDQpNSUlCQ2dLQ0FRRUFwcTdlUWpQRERaVXRONUNnNU5xUjVwdUdKNXRQaWVjbkF4TE5CTmplalhBdzBFakxJUVBaDQpBOWlhNGpNQmNrTjh1TjlHeXlXbzB6aUpyMkJlanJXTjNVRTMwL0YyVWdreHUzQjZrdlBSbUJCZHltVTJ5eXNkDQpCK1FFUkJsdFJ2S2xDN0Rrc0pQMWRPdHRiUHBRQ3M4TmhNRkRPYkNMZjRWN29wSERxd1NjbWZocmtpNjFUTzZkDQptSmJJWkNPbTlIUUtlMDZYbEZTNTd2K3Y5YlhoZE9PSXNlTmJoR1ZacTY3TVRiMFR6UlhXNTZONGFSejdMVm4xDQpJdXNNbFV3WmRrNlhPM3hIMHhuY3A2SDc3NS9aR0M0cmZUVHRSL0hVYWdyV1c4U25wWC9uNnRNK09OQXNraW1rDQovRzdkdlpCZHlJVWpnaE5FM0VOUElrdnArNGt6RDJtUW13SURBUUFCTUEwR0NTcUdTSWIzRFFFQkN3VUFBNElCDQpBUUJxdGZoMitzcVJZeGlLUlZSNUpMOHloZmFnTWtiRkJ5UzJSVlJOMVZCMW9yMTRsaVhkOWI3ZWRvUzBrdHJ2DQp3MFptbWthViszU0xNdEdEdU1jSUw5SzRiK0l2Q24xWXY5MlI5M1Y4cnNZMmNBZDR4a3pUaThNYUpiVTNUVVY2DQpJampkQzJSUjQ3U3dwVnUvWFpvWlplQzBVQndzVEYvM2x4dGRiVm9NUXJYNmwzVWt4UWxkUmszdUV2RG9lK0NpDQpMdnIzN2kxb056RVUvNENOMXFuTDVEcFJjUmJ0WDZwTUZ2MHZ3bDdod0VuRGJjVXVsT3RrMndmeVNnOER6RjdZDQp4bi9nR2cwNkJaOTM2amRSUGlyYmlyaVFEWlZKaVc5T3NwZDM5ZDJIZ1laVEVKajN5RnpLSFJZN080UU1URTN1DQozSGZnQjUrRWNWSVhIV08zbjVmNFBpamkNCi0tLS0tRU5EIENFUlRJRklDQVRFLS0tLS0=");
            con.setRequestProperty("x-organization-slug", "zup");

            con.setDoInput(true);
            con.setConnectTimeout(timeout);
            con.connect();
        } catch (IOException e) {
            throw new NetworkStatusException();
        }
    }


   /* *//**
     * connection on server by URL
     *
     * @param method
     *            post or get
     * @param timeout
     *            for connection
     * @param data
     *            the json post
     * @throws NetworkStatusException
     *             when any exception was raised
     *//*
    protected void connect(final String method, final int timeout,
                           final String data) throws NetworkStatusException {
        if (!canConnect()) {
            return;
        }
        try {
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod(method);
            con.setRequestProperty("content-type", "application/json;  charset=utf-8");
            con.setRequestProperty("x-application-id", "3ba3a25db5409198e079b8d71ce825ed5dd45f87");
            con.setRequestProperty("x-ssl-auth", "LS0tLS1CRUdJTiBDRVJUSUZJQ0FURS0tLS0tDQpNSUlEempDQ0FyWUNDUURTWkFoZm1sbWFmekFOQmdrcWhraUc5dzBCQVFzRkFEQ0JxREVMTUFrR0ExVUVCaE1DDQpRbEl4RlRBVEJnTlZCQWdUREUxcGJtRnpJRWRsY21GcGN6RVRNQkVHQTFVRUJ4UUtWV0psY216aWJtUnBZVEVoDQpNQjhHQTFVRUNoTVlXbFZRSUVsT1ZFVlNUa1ZVSUZORlVsWkZVaUJNVkVSQk1RNHdEQVlEVlFRTEV3VkpibVp5DQpZVEVaTUJjR0ExVUVBeFFRS2k1aGNHbHlaV0ZzZDJGMlpTNXBiekVmTUIwR0NTcUdTSWIzRFFFSkFSWVFhVzVtDQpjbUZBZW5Wd0xtTnZiUzVpY2pBZUZ3MHhOakE0TXpFeE1qUTNNamhhRncweE5qQTVNekF4TWpRM01qaGFNSUdvDQpNUXN3Q1FZRFZRUUdFd0pDVWpFVk1CTUdBMVVFQ0JNTVRXbHVZWE1nUjJWeVlXbHpNUk13RVFZRFZRUUhGQXBWDQpZbVZ5Yk9KdVpHbGhNU0V3SHdZRFZRUUtFeGhhVlZBZ1NVNVVSVkpPUlZRZ1UwVlNWa1ZTSUV4VVJFRXhEakFNDQpCZ05WQkFzVEJVbHVabkpoTVJrd0Z3WURWUVFERkJBcUxtRndhWEpsWVd4M1lYWmxMbWx2TVI4d0hRWUpLb1pJDQpodmNOQVFrQkZoQnBibVp5WVVCNmRYQXVZMjl0TG1KeU1JSUJJakFOQmdrcWhraUc5dzBCQVFFRkFBT0NBUThBDQpNSUlCQ2dLQ0FRRUFwcTdlUWpQRERaVXRONUNnNU5xUjVwdUdKNXRQaWVjbkF4TE5CTmplalhBdzBFakxJUVBaDQpBOWlhNGpNQmNrTjh1TjlHeXlXbzB6aUpyMkJlanJXTjNVRTMwL0YyVWdreHUzQjZrdlBSbUJCZHltVTJ5eXNkDQpCK1FFUkJsdFJ2S2xDN0Rrc0pQMWRPdHRiUHBRQ3M4TmhNRkRPYkNMZjRWN29wSERxd1NjbWZocmtpNjFUTzZkDQptSmJJWkNPbTlIUUtlMDZYbEZTNTd2K3Y5YlhoZE9PSXNlTmJoR1ZacTY3TVRiMFR6UlhXNTZONGFSejdMVm4xDQpJdXNNbFV3WmRrNlhPM3hIMHhuY3A2SDc3NS9aR0M0cmZUVHRSL0hVYWdyV1c4U25wWC9uNnRNK09OQXNraW1rDQovRzdkdlpCZHlJVWpnaE5FM0VOUElrdnArNGt6RDJtUW13SURBUUFCTUEwR0NTcUdTSWIzRFFFQkN3VUFBNElCDQpBUUJxdGZoMitzcVJZeGlLUlZSNUpMOHloZmFnTWtiRkJ5UzJSVlJOMVZCMW9yMTRsaVhkOWI3ZWRvUzBrdHJ2DQp3MFptbWthViszU0xNdEdEdU1jSUw5SzRiK0l2Q24xWXY5MlI5M1Y4cnNZMmNBZDR4a3pUaThNYUpiVTNUVVY2DQpJampkQzJSUjQ3U3dwVnUvWFpvWlplQzBVQndzVEYvM2x4dGRiVm9NUXJYNmwzVWt4UWxkUmszdUV2RG9lK0NpDQpMdnIzN2kxb056RVUvNENOMXFuTDVEcFJjUmJ0WDZwTUZ2MHZ3bDdod0VuRGJjVXVsT3RrMndmeVNnOER6RjdZDQp4bi9nR2cwNkJaOTM2amRSUGlyYmlyaVFEWlZKaVc5T3NwZDM5ZDJIZ1laVEVKajN5RnpLSFJZN080UU1URTN1DQozSGZnQjUrRWNWSVhIV08zbjVmNFBpamkNCi0tLS0tRU5EIENFUlRJRklDQVRFLS0tLS0=");

            con.setDoInput(true);
            con.setDoOutput(true);
            con.setConnectTimeout(timeout);
            if (method.equals(METHOD_POST))
                this.setData(data);
            con.connect();
        } catch (IOException e) {
            throw new NetworkStatusException();
        }
    }*/

    private void setData(final String rawData) throws IOException {
        if (con != null) {
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.write(rawData.getBytes(Constants.UTF_8));
            wr.flush();
            wr.close();
        }
    }
    /**
     * Return data of server InputStream
     *
     * @return {@link String}
     * @throws IOException
     */
    protected String getData() throws NetworkStatusException {
        String result = "";
        final StringBuffer buffer = new StringBuffer();
        try {
            InputStream mIs = con.getInputStream();
            BufferedReader mBr = new BufferedReader(new InputStreamReader(mIs,
                    Constants.UTF_8));
            String line = null;
            while ((line = mBr.readLine()) != null) {
                buffer.append(line + LINE);
            }
            mIs.close();
            con.disconnect();
            result = buffer.toString();

        } catch (IOException e) {
            throw new NetworkStatusException();
        }
        return result;
    }

    /**
     * abstract method for create URL for connect to server
     *
     * @throws MalformedURLException
     */
    public abstract void createUrl() throws MalformedURLException;

    public abstract void execute() throws NetworkStatusException;


}
