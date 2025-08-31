package com.cy.easyhttp;

import okhttp3.OkHttpClient;

import java.util.HashMap;
import java.util.Map;

/**
 * 配置类
 *
 * @author cy
 * @since v1.0.0
 */
public class HttpClientConfiguration {
    /**
     * 基础 URL
     */
    private final String baseUrl;
    /**
     * 请求头
     */
    private final Map<String, String> headers;
    /**
     * okHttpClient
     */
    private final OkHttpClient okHttpClient;

    //增加连接超时限制以及重试次数
    private final int connectTimeout; // 连接超时，单位秒
    private final int readTimeout;    // 读取超时，单位秒
    private final int writeTimeout;   // 写入超时，单位秒
    private final int maxRetry;       // 最大重试次数


    // 私有构造函数，只能通过Builder创建实例
    private HttpClientConfiguration(Builder builder) {
        this.baseUrl = builder.baseUrl;
        this.headers = builder.headers;
        this.connectTimeout = builder.connectTimeout;
        this.readTimeout = builder.readTimeout;
        this.writeTimeout = builder.writeTimeout;
        this.maxRetry = builder.maxRetry;

        // 如果用户没有传 OkHttpClient，则用 Builder 配置创建
        if (builder.okHttpClient != null) {
            this.okHttpClient = builder.okHttpClient;
        } else {
            this.okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(connectTimeout, java.util.concurrent.TimeUnit.SECONDS)
                    .readTimeout(readTimeout, java.util.concurrent.TimeUnit.SECONDS)
                    .writeTimeout(writeTimeout, java.util.concurrent.TimeUnit.SECONDS)
                    .build();
        }
    }

    // 静态方法：创建Builder实例（更符合常见用法）
    public static Builder newBuilder() {
        return new Builder();
    }

    // Getter方法
    public String getBaseUrl() {
        return baseUrl;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }

    public int getConnectTimeout() { return connectTimeout; }

    public int getReadTimeout() { return readTimeout; }

    public int getWriteTimeout() { return writeTimeout; }

    public int getMaxRetry() { return maxRetry; }

    // Builder类
    public static class Builder {
        private String baseUrl;
        private Map<String, String> headers;
        private OkHttpClient okHttpClient;
        private int connectTimeout = 10; // 默认 10 秒
        private int readTimeout = 30;    // 默认 30 秒
        private int writeTimeout = 30;   // 默认 30 秒
        private int maxRetry = 3;        // 默认 3 次重试

        // Builder构造函数设为私有，强制通过外层的newBuilder()创建
        private Builder() {
            this.headers = new HashMap<>();
        }

        // 设置baseUrl并返回Builder
        public Builder baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        // 设置headers并返回Builder
        public Builder headers(Map<String, String> headers) {
            this.headers = headers;
            return this;
        }

        // 添加单个请求头
        public Builder addHeader(String key, String value) {
            this.headers.put(key, value);
            return this;
        }

        // 设置okHttpClient并返回Builder
        public Builder okHttpClient(OkHttpClient okHttpClient) {
            this.okHttpClient = okHttpClient;
            return this;
        }

        public Builder connectTimeout(int seconds) { this.connectTimeout = seconds; return this; }

        public Builder readTimeout(int seconds) { this.readTimeout = seconds; return this; }

        public Builder writeTimeout(int seconds) { this.writeTimeout = seconds; return this; }

        public Builder maxRetry(int maxRetry) { this.maxRetry = maxRetry; return this; }


        // 构建Configuration实例
        public HttpClientConfiguration build() {
            return new HttpClientConfiguration(this);
        }
    }
}