

ALTER TABLE `smarthealthc`.`account`
    CHANGE COLUMN `is_active` `is_active` BIT(1) NOT NULL ,
    CHANGE COLUMN `is_deleted` `is_deleted` BIT(1) NOT NULL ,
    CHANGE COLUMN `password` `password` VARCHAR(255) NOT NULL ,
    CHANGE COLUMN `type` `type` ENUM('ADMIN', 'CUSTOMER_SUPPORT', 'MEDICAL_SPECIALIST', 'USER') NOT NULL ,
    ADD UNIQUE INDEX `email_UNIQUE` (`email`);



ALTER TABLE `smarthealthc`.`activity_record`
    CHANGE COLUMN `date` `date` DATETIME(6) NOT NULL ,
    CHANGE COLUMN `plan_duration` `plan_duration` FLOAT NOT NULL ,
    CHANGE COLUMN `plan_type` `plan_type` ENUM('HEAVY', 'LIGHT', 'MEDIUM') NOT NULL ,
    CHANGE COLUMN `week_start` `week_start` DATETIME(6) NOT NULL ,
    CHANGE COLUMN `appuser_id` `appuser_id` INT NOT NULL ;


ALTER TABLE `smarthealthc`.`app_user`
    CHANGE COLUMN `cic` `cic` VARCHAR(255) CHARACTER SET utf8mb4  NOT NULL ,
    CHANGE COLUMN `height` `height` FLOAT NOT NULL ,
    CHANGE COLUMN`medical_specialist_note` `medical_specialist_note` VARCHAR(255) CHARACTER SET utf8mb4 ,
    CHANGE COLUMN `name` `name` VARCHAR(255) CHARACTER SET utf8mb4  NOT NULL ,
    CHANGE COLUMN `phone_number` `phone_number` VARCHAR(255) CHARACTER SET utf8mb4  NOT NULL ,
    CHANGE COLUMN `weight` `weight` FLOAT NOT NULL ,
    CHANGE COLUMN `account_id` `account_id` INT NOT NULL ;

ALTER TABLE `smarthealthc`.`blood_pressure_record`
    CHANGE COLUMN `date` `date` DATETIME(6) NOT NULL ,
    CHANGE COLUMN `diastole` `diastole` FLOAT NOT NULL ,
    CHANGE COLUMN `systole` `systole` FLOAT NOT NULL ,
    CHANGE COLUMN `week_start` `week_start` DATETIME(6) NOT NULL ,
    CHANGE COLUMN `appuser_id` `appuser_id` INT NOT NULL ;


ALTER TABLE `smarthealthc`.`cardinal_record`
    CHANGE COLUMN `date` `date` DATETIME(6) NOT NULL ,
    CHANGE COLUMN `time_measure` `time_measure` ENUM('AFTER_BREAKFAST', 'AFTER_DINNER', 'AFTER_LUNCH', 'BEFORE_BREAKFAST', 'BEFORE_DINNER', 'BEFORE_LUNCH') NOT NULL ,
    CHANGE COLUMN `week_start` `week_start` DATETIME(6) NOT NULL ,
    CHANGE COLUMN `appuser_id` `appuser_id` INT NOT NULL ;


ALTER TABLE `smarthealthc`.`diet_record`
    CHANGE COLUMN `date` `date` DATETIME(6) NOT NULL ,
    CHANGE COLUMN `dish_per_day` `dish_per_day` INT NOT NULL ,
    CHANGE COLUMN `week_start` `week_start` DATETIME(6) NOT NULL ,
    CHANGE COLUMN `appuser_id` `appuser_id` INT NOT NULL ;

ALTER TABLE `smarthealthc`.`faq`
    CHANGE COLUMN `answer` `answer` VARCHAR(255) CHARACTER SET utf8mb4 NOT NULL ,
    CHANGE COLUMN `question` `question` VARCHAR(255) CHARACTER SET utf8mb4 NOT NULL ;


ALTER TABLE `smarthealthc`.`forget_password_code`
    CHANGE COLUMN `code` `code` VARCHAR(255) NOT NULL ,
    CHANGE COLUMN `account_id` `account_id` INT NOT NULL ;

ALTER TABLE `smarthealthc`.`form_question`
    CHANGE COLUMN `question` `question` VARCHAR(255) CHARACTER SET utf8mb4 NOT NULL ,
    CHANGE COLUMN `question_number` `question_number` INT NOT NULL ,
    CHANGE COLUMN `type` `type` ENUM('SAT_SF', 'SF') NOT NULL ;

ALTER TABLE `smarthealthc`.`lesson`
    CHANGE COLUMN `content` `content` VARCHAR(255)  CHARACTER SET utf8mb4 NOT NULL ,
    CHANGE COLUMN `title` `title` VARCHAR(255)  CHARACTER SET utf8mb4 NOT NULL ,
    CHANGE COLUMN `video` `video` VARCHAR(255)  CHARACTER SET utf8mb4 NOT NULL ,
    CHANGE COLUMN `lesson_number` `lesson_number` INT NOT NULL ;


ALTER TABLE `smarthealthc`.`medical_appointment`
    CHANGE COLUMN `hospital` `hospital` VARCHAR(255) CHARACTER SET utf8mb4 ,
    CHANGE COLUMN `status_medical_appointment` `status_medical_appointment` ENUM('CONFIRM', 'DONE', 'PENDING') NOT NULL ,
    CHANGE COLUMN `type_medical_appointment` `type_medical_appointment` ENUM('DIAGNOSIS', 'MEDICAL_CHECKUP') NOT NULL ,
    CHANGE COLUMN `appuser_id` `appuser_id` INT NOT NULL ;
--
ALTER TABLE `smarthealthc`.`medical_history`
    CHANGE COLUMN `name` `name` VARCHAR(255) CHARACTER SET utf8mb4  NOT NULL ,
    CHANGE COLUMN `type` `type` ENUM('ARTHRITIS', 'CARDINAL', 'HABIT', 'OTHERS', 'RESPIRATORY') NOT NULL ;


ALTER TABLE `smarthealthc`.`medicine_record`
    CHANGE COLUMN `date` `date` DATETIME(6) NOT NULL ,
    CHANGE COLUMN `week_start` `week_start` DATETIME(6) NOT NULL ,
    CHANGE COLUMN `appuser_id` `appuser_id` INT NOT NULL ;
--
ALTER TABLE `smarthealthc`.`medicine_type`
    CHANGE COLUMN `description` `description` VARCHAR(255) CHARACTER SET utf8mb4  ,
    CHANGE COLUMN `title` `title` VARCHAR(255) CHARACTER SET utf8mb4  ;
--
ALTER TABLE `smarthealthc`.`mental_record`
    CHANGE COLUMN `date` `date` DATETIME(6) NOT NULL ,
    CHANGE COLUMN `week_start` `week_start` DATETIME(6) NOT NULL ,
    CHANGE COLUMN `appuser_id` `appuser_id` INT NOT NULL ,
    CHANGE COLUMN `mental_rule_id` `mental_rule_id` INT NOT NULL ;
--
ALTER TABLE `smarthealthc`.`mental_rule`
    CHANGE COLUMN `description` `description` VARCHAR(255) CHARACTER SET utf8mb4  ,
    CHANGE COLUMN `title` `title` VARCHAR(255) CHARACTER SET utf8mb4  ;


ALTER TABLE `smarthealthc`.`monthly_record`
    CHANGE COLUMN `answer` `answer` INT NOT NULL ,
    CHANGE COLUMN `month_start` `month_start` DATETIME(6) NOT NULL ,
    CHANGE COLUMN `monthly_record_type` `monthly_record_type` ENUM('SAT_SF_C', 'SAT_SF_P', 'SAT_SF_I', 'SF_Medication', 'SF_Diet', 'SF_Activity', 'SF_Mental') NOT NULL ,
    CHANGE COLUMN `question` `question` VARCHAR(255)  CHARACTER SET utf8mb4 NOT NULL ,
    CHANGE COLUMN `question_number` `question_number` INT NOT NULL  ,
    CHANGE COLUMN `appuser_id` `appuser_id` INT NOT NULL ;


ALTER TABLE `smarthealthc`.`question`
    CHANGE COLUMN `body` `body` VARCHAR(255) CHARACTER SET utf8mb4 NOT NULL ,
    CHANGE COLUMN `answer` `answer` VARCHAR(255) CHARACTER SET utf8mb4 ,
    CHANGE COLUMN `question_date` `question_date` DATETIME(6) NOT NULL ,
    CHANGE COLUMN `title` `title` VARCHAR(255) CHARACTER SET utf8mb4 NOT NULL ,
    CHANGE COLUMN `type_user_question` `type_user_question` ENUM('ASSIGN_ADMIN', 'ASSIGN_MS') NOT NULL ,
    CHANGE COLUMN `appuser_id` `appuser_id` INT NOT NULL ;


ALTER TABLE `smarthealthc`.`sat_sf_c_record`
    CHANGE COLUMN `independence` `independence` INT NOT NULL ,
    CHANGE COLUMN `month_start` `month_start` DATETIME(6) NOT NULL ,
    CHANGE COLUMN `optimistic` `optimistic` INT NOT NULL ,
    CHANGE COLUMN `overall_point` `overall_point` INT NOT NULL ,
    CHANGE COLUMN `relationship` `relationship` INT NOT NULL ,
    CHANGE COLUMN `shared_story` `shared_story` INT NOT NULL ,
    CHANGE COLUMN `appuser_id` `appuser_id` INT NOT NULL ;


ALTER TABLE `smarthealthc`.`sat_sf_i_record`
    CHANGE COLUMN `consistency` `consistency` INT NOT NULL ,
    CHANGE COLUMN `energy_conservation` `energy_conservation` INT NOT NULL ,
    CHANGE COLUMN `month_start` `month_start` DATETIME(6) NOT NULL ,
    CHANGE COLUMN `motivation` `motivation` INT NOT NULL ,
    CHANGE COLUMN `overall_point` `overall_point` INT NOT NULL ,
    CHANGE COLUMN `revision` `revision` INT NOT NULL ,
    CHANGE COLUMN `self_control` `self_control` INT NOT NULL ,
    CHANGE COLUMN `stress_facing` `stress_facing` INT NOT NULL ,
    CHANGE COLUMN `appuser_id` `appuser_id` INT NOT NULL ;


ALTER TABLE `smarthealthc`.`sat_sf_p_record`
    CHANGE COLUMN `healthy_environment` `healthy_environment` INT NOT NULL ,
    CHANGE COLUMN `life_pursuit` `life_pursuit` INT NOT NULL ,
    CHANGE COLUMN `month_start` `month_start` DATETIME(6) NOT NULL ,
    CHANGE COLUMN `overall_point` `overall_point` INT NOT NULL ,
    CHANGE COLUMN `planning` `planning` INT NOT NULL ,
    CHANGE COLUMN `priority_focus` `priority_focus` INT NOT NULL ,
    CHANGE COLUMN `right_decision` `right_decision` INT NOT NULL ,
    CHANGE COLUMN `appuser_id` `appuser_id` INT NOT NULL ;


ALTER TABLE `smarthealthc`.`sf_record`
    CHANGE COLUMN `activity` `activity` INT NOT NULL ,
    CHANGE COLUMN `activity_habit` `activity_habit` INT NOT NULL ,
    CHANGE COLUMN `activity_planning` `activity_planning` INT NOT NULL ,
    CHANGE COLUMN `diet` `diet` INT NOT NULL ,
    CHANGE COLUMN `diet_habit` `diet_habit` INT NOT NULL ,
    CHANGE COLUMN `healthy_diet` `healthy_diet` INT NOT NULL ,
    CHANGE COLUMN `medication` `medication` INT NOT NULL ,
    CHANGE COLUMN `medication_habit` `medication_habit` INT NOT NULL ,
    CHANGE COLUMN `month_start` `month_start` DATETIME(6) NOT NULL ,
    CHANGE COLUMN `plan_compliance` `plan_compliance` INT NOT NULL ,
    CHANGE COLUMN `positivity` `positivity` INT NOT NULL ,
    CHANGE COLUMN `vegetable_prioritization` `vegetable_prioritization` INT NOT NULL ,
    CHANGE COLUMN `appuser_id` `appuser_id` INT NOT NULL ;


ALTER TABLE `smarthealthc`.`step_record`
    CHANGE COLUMN `date` `date` DATETIME(6) NOT NULL ,
    CHANGE COLUMN `planned_step_per_day` `planned_step_per_day` INT NOT NULL ,
    CHANGE COLUMN `week_start` `week_start` DATETIME(6) NOT NULL ,
    CHANGE COLUMN `appuser_id` `appuser_id` INT NOT NULL ;


ALTER TABLE `smarthealthc`.`user_lesson`
    CHANGE COLUMN `lesson_date` `lesson_date` DATETIME(6) NOT NULL ,
    CHANGE COLUMN `appuser_id` `appuser_id` INT NOT NULL ,
    CHANGE COLUMN `lesson_id` `lesson_id` INT NOT NULL ;



ALTER TABLE `smarthealthc`.`user_medical_history`
    CHANGE COLUMN `appuser_id` `appuser_id` INT NOT NULL ,
    CHANGE COLUMN `condition_id` `condition_id` INT NOT NULL ;


ALTER TABLE `smarthealthc`.`web_user`
    CHANGE COLUMN `dob` `dob` DATETIME(6) NOT NULL ,
    CHANGE COLUMN `gender` `gender` BIT(1) NOT NULL ,
    CHANGE COLUMN `phone_number` `phone_number` VARCHAR(255)  CHARACTER SET utf8mb4  NOT NULL ,
    CHANGE COLUMN `user_name` `user_name` VARCHAR(255)  CHARACTER SET utf8mb4  NOT NULL ,
    CHANGE COLUMN `account_id` `account_id` INT NOT NULL ;


ALTER TABLE `smarthealthc`.`weight_record`
    CHANGE COLUMN `date` `date` DATETIME(6) NOT NULL ,
    CHANGE COLUMN `week_start` `week_start` DATETIME(6) NOT NULL ,
    CHANGE COLUMN `weight` `weight` FLOAT NOT NULL ,
    CHANGE COLUMN `appuser_id` `appuser_id` INT NOT NULL ;

