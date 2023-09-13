#!/usr/bin/python
# -*- coding: UTF-8 -*-
import redis
import time
import logging
import json

logging.basicConfig(level=logging.INFO,
                    filename='clean_session.log',
                    filemode='a',
                    format='%(asctime)s - %(message)s'
                    )
r = redis.Redis(
    host="172.16.0.15",
    port=6379,
    password='crs-b4ff61ej:WreRedis1234'
)

for i in range(1, 16314271):
    userKey = 'account:user:' + str(i)

    info = r.hgetall(userKey)
    dict = {}
    for (key, value) in info.items():
        dict[key.decode("utf-8")] = value.decode("utf-8")

    if (i % 10000 == 0):
        print(i)
        time.sleep(1)

    for v in dict.values():
        tokenKey = "account:token:" + v
        if (r.exists(tokenKey) == 0):
            r.delete(userKey)
            print('del ' + userKey)
            logging.info(userKey + ', ' + json.dumps(dict))

r.close()

