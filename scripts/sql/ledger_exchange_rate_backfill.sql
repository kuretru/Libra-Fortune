ALTER TABLE `ledger_v2_entry`
  ADD COLUMN `exchange_rate` decimal(14,4) NULL COMMENT '汇率，原始消费金额/结算金额'
  AFTER `settlement_currency`;

ALTER TABLE `ledger_v2_entry`
  ADD COLUMN `reverse_exchange_rate` decimal(14,4) NULL COMMENT '反向汇率，结算金额/原始消费金额'
  AFTER `exchange_rate`;

UPDATE `ledger_v2_entry`
SET `exchange_rate` = 0.0000
WHERE `settlement_amount` = 0;

UPDATE `ledger_v2_entry`
SET `exchange_rate` = ROUND(`original_amount` / `settlement_amount`, 4)
WHERE `settlement_amount` <> 0;

UPDATE `ledger_v2_entry`
SET `reverse_exchange_rate` = 0.0000
WHERE `original_amount` = 0;

UPDATE `ledger_v2_entry`
SET `reverse_exchange_rate` = ROUND(`settlement_amount` / `original_amount`, 4)
WHERE `original_amount` <> 0;

SELECT `id`, `original_amount`, `original_currency`, `settlement_amount`, `settlement_currency`
FROM `ledger_v2_entry`
WHERE `exchange_rate` IS NULL
   OR `reverse_exchange_rate` IS NULL;

ALTER TABLE `ledger_v2_entry`
  MODIFY COLUMN `exchange_rate` decimal(14,4) NOT NULL DEFAULT 1.0000 COMMENT '汇率，原始消费金额/结算金额';

ALTER TABLE `ledger_v2_entry`
  MODIFY COLUMN `reverse_exchange_rate` decimal(14,4) NOT NULL DEFAULT 1.0000 COMMENT '反向汇率，结算金额/原始消费金额';
