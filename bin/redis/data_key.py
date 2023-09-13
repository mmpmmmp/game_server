#!/usr/bin/python
# -*- coding: UTF-8 -*-
import redis
import time
import logging
import json

logging.basicConfig(level=logging.INFO,
                    filename='move_redis_to_mysql.log',
                    filemode='a',
                    format='%(asctime)s - %(levelname)s: %(message)s'
                    )
pool = redis.ConnectionPool(
    host='172.16.0.15',
    port=6379,
    db=0,
    password='crs-b4ff61ej:WreRedis1234',
    decode_responses=True)

r_link = redis.Redis(connection_pool=pool)

its = 0
while True:
    # datas = r_link.hscan("data:user:", its, match='*', count=10000)
    datas = r_link.scan(its, match='data:user:*', count=100)
    print(datas[0])
    its = datas[0]
    if datas[0] == 0:
        print('OVER')
        break
    else:
        for x in datas[1]:
            print(x)

