ALTER TABLE `smarthealthc`.`mental_record` DROP COLUMN `point`;
ALTER TABLE `smarthealthc`.`mental_record`
    ADD COLUMN `status` BIT;