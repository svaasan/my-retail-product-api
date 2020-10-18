#!/bin/bash

CQL="CREATE KEYSPACE IF NOT EXISTS my_retail WITH REPLICATION = { 'class' : 'org.apache.cassandra.locator.SimpleStrategy','replication_factor': '1'};

CREATE TABLE IF NOT EXISTS my_retail.product_detail  (
    id        int,
    current_price text,
    PRIMARY KEY (id)
);
INSERT INTO my_retail.product_detail (id, current_price) VALUES (13860428, '{\"value\": 14.49,\"currency_code\":\"USD\"}');
INSERT INTO my_retail.product_detail (id, current_price) VALUES (16483589, '{\"value\": 15.49,\"currency_code\":\"USD\"}');
INSERT INTO my_retail.product_detail (id, current_price) VALUES (16696652, '{\"value\": 16.49,\"currency_code\":\"USD\"}');
INSERT INTO my_retail.product_detail (id, current_price) VALUES (16752456, '{\"value\": 10.49,\"currency_code\":\"USD\"}');
INSERT INTO my_retail.product_detail (id, current_price) VALUES (15643793, '{\"value\": 3.49,\"currency_code\":\"USD\"}');
"

until echo $CQL | cqlsh; do
  echo "cqlsh: Cassandra is unavailable - retry later"
  sleep 2
done &

exec /docker-entrypoint.sh "$@"
