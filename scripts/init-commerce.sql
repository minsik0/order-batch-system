-- scripts/init-commerce.sql
-- 주문 5만 건 생성 (최근 90일치)

CREATE TABLE IF NOT EXISTS orders (
                                      id            BIGSERIAL PRIMARY KEY,
                                      user_id       BIGINT        NOT NULL,
                                      product_id    BIGINT        NOT NULL,
                                      seller_id     BIGINT        NOT NULL,
                                      status        VARCHAR(20)   NOT NULL,
    amount        NUMERIC(12,2) NOT NULL,
    fee_rate      NUMERIC(5,4)  NOT NULL DEFAULT 0.035,
    ordered_at    TIMESTAMP     NOT NULL,
    completed_at  TIMESTAMP
    );

-- 50,000건 bulk insert
INSERT INTO orders (user_id, product_id, seller_id, status, amount, fee_rate, ordered_at, completed_at)
SELECT
    (random() * 1000 + 1)::BIGINT,                          -- user_id 1~1000
    (random() * 100 + 1)::BIGINT,                           -- product_id 1~100
    (random() * 20 + 1)::BIGINT,                            -- seller_id 1~20
    (ARRAY['COMPLETED','COMPLETED','COMPLETED','CANCELLED','REFUNDED'])
    [(random()*4+1)::INT],                               -- 완료 비율 높게
    (random() * 490000 + 10000)::NUMERIC(12,2),             -- 금액 1만~50만
    0.035,
    NOW() - (random() * 90 || ' days')::INTERVAL,           -- 최근 90일
    CASE WHEN status != 'COMPLETED' THEN NULL
         ELSE NOW() - (random() * 89 || ' days')::INTERVAL
END
FROM generate_series(1, 50000);

CREATE INDEX idx_orders_completed_at ON orders(completed_at);
CREATE INDEX idx_orders_status ON orders(status);
CREATE INDEX idx_orders_seller_id ON orders(seller_id);