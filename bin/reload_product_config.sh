#!/bin/bash
echo "reload wre-prd-01"
curl http://127.0.0.1:8080/gameapi/v1/admin/config/reloadProductListConfig

echo ""
echo "reload wre-prd-02"
curl http://10.70.16.204:8080/gameapi/v1/admin/config/reloadProductListConfig?from=redis

echo ""

