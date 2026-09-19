ALTER TABLE `ledger_v2_entry`
  ADD COLUMN `exchange_rate` decimal(14,4) NULL COMMENT '汇率，原始消费金额/结算金额'
  AFTER `settlement_currency`;

UPDATE `ledger_v2_entry`
SET `exchange_rate` = 0.0000
WHERE `settlement_amount` = 0;

UPDATE `ledger_v2_entry`
SET `exchange_rate` = 1.0000
WHERE `settlement_amount` <> 0
  AND `original_currency` = `settlement_currency`;

UPDATE `ledger_v2_entry`
SET `exchange_rate` = ROUND(`original_amount` / `settlement_amount`, 4)
WHERE `settlement_amount` <> 0
  AND `original_currency` <> `settlement_currency`;

SELECT `id`, `original_amount`, `original_currency`, `settlement_amount`, `settlement_currency`
FROM `ledger_v2_entry`
WHERE `exchange_rate` IS NULL;

ALTER TABLE `ledger_v2_entry`
  MODIFY COLUMN `exchange_rate` decimal(14,4) NOT NULL DEFAULT 1.0000 COMMENT '汇率，原始消费金额/结算金额';
