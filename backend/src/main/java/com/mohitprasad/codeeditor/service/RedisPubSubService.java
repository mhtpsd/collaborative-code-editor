package com.mohitprasad.codeeditor.service;

public interface RedisPubSubService {
    void publish(String channel, Object message);
}
