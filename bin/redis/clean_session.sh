#!/bin/bash

redis-cli -h 10.70.5.61 -p 4018 --scan --pattern "account:user:*" | xargs -L 2000 redis-cli -h 10.70.5.61 -p 4018 del

