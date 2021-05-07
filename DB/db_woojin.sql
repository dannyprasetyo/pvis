/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     3/24/2021 10:45:48 PM                        */
/*==============================================================*/


drop table if exists TB_M_BANK_ACCOUNT;

drop table if exists TB_M_EMPLOYEE;

drop table if exists TB_M_SA_QUESTION;

drop table if exists TB_T_ATTENDANCE;

drop table if exists TB_T_DAILY_WORK;

drop table if exists TB_T_PERMISSION_REQ;

drop table if exists TB_T_SALARY;

drop table if exists TB_T_SELF_ASSESSMENT;

/*==============================================================*/
/* Table: TB_M_BANK_ACCOUNT                                     */
/*==============================================================*/
create table TB_M_BANK_ACCOUNT
(
   BA_ID                int not null,
   EMP_NIK              char(7),
   BA_NO                varchar(50) not null,
   BA_BANK_NAME         varchar(100) not null,
   BA_OWNER_NAME        varchar(255) not null,
   BA_LAST_ACTIVE_DATE  date,
   primary key (BA_ID)
);

/*==============================================================*/
/* Table: TB_M_EMPLOYEE                                         */
/*==============================================================*/
create table TB_M_EMPLOYEE
(
   EMP_NIK              char(7) not null,
   EMP_NAME             varchar(30),
   EMP_STATUS           char(1),
   EMP_POSITION         int,
   EMP_PASSWORD         varchar(255),
   primary key (EMP_NIK)
);

/*==============================================================*/
/* Table: TB_M_SA_QUESTION                                      */
/*==============================================================*/
create table TB_M_SA_QUESTION
(
   SAQ_ID               int not null,
   SAQ_QUESTION         varchar(255) not null,
   primary key (SAQ_ID)
);

/*==============================================================*/
/* Table: TB_T_ATTENDANCE                                       */
/*==============================================================*/
create table TB_T_ATTENDANCE
(
   ATT_ID               int not null,
   EMP_NIK              char(7),
   ATT_DATE             date not null,
   ATT_IN               datetime,
   ATT_OUT              datetime,
   primary key (ATT_ID)
);

/*==============================================================*/
/* Table: TB_T_DAILY_WORK                                       */
/*==============================================================*/
create table TB_T_DAILY_WORK
(
   EMP_NIK              char(7) not null,
   WORK_DAY             date not null,
   WORK_HOURS           int,
   SPESIFICATION        varchar(30),
   CLASSIFICATION       varchar(30),
   LOT_NO               varchar(10),
   MATERIAL_INPUT_QTY   int,
   PROCESSED_QTY        int,
   FAULTY_QTY           int,
   NOTE                 varchar(30),
   CONFIRM_FLAG         int,
   CONFIRM_USER         char(7),
   CONFIRM_DATE         date,
   primary key (EMP_NIK, WORK_DAY)
);

/*==============================================================*/
/* Table: TB_T_PERMISSION_REQ                                   */
/*==============================================================*/
create table TB_T_PERMISSION_REQ
(
   REQ_ID               int not null,
   EMP_NIK              char(7),
   REQ_TYPE             char(1) not null,
   REQ_DATE             date not null,
   REQ_STATUS           char(1),
   primary key (REQ_ID)
);

/*==============================================================*/
/* Table: TB_T_SALARY                                           */
/*==============================================================*/
create table TB_T_SALARY
(
   EMP_NIK              char(7) not null,
   PERIODE_DATE_START   date not null,
   PERIODE_DATE_END     date not null,
   HOURLY_WAGE          int,
   EXTENSION            int,
   NIGHT_SHIFT          int,
   OVER_TIME_WEEKEND    int,
   TRANSPORT_FEE        int,
   NORMAL_FEE           float,
   HOURLY_WAGE_AMOUNT   float,
   EXTENSION_AMOUNT     float,
   NIGHT_SHIFT_AMOUNT   float,
   OVER_TIME_WEEKEND_AMOUNT float,
   TRANSPORT_FEE_AMOUNT float,
   INCOME_TAX           float,
   PAY_TOTAL_AMOUNT     float,
   PENSION              float,
   HEALTH_INSURANCE     float,
   LONG_TERM_CARE_INSURANCE float,
   TOTAL_SALARY         float,
   TOTAL_DEDUCTION      float,
   TOTAL_HOURS          int,
   primary key (EMP_NIK, PERIODE_DATE_START, PERIODE_DATE_END)
);

/*==============================================================*/
/* Table: TB_T_SELF_ASSESSMENT                                  */
/*==============================================================*/
create table TB_T_SELF_ASSESSMENT
(
   SA_ID                int not null,
   EMP_NIK              char(7),
   SAQ_ID               int,
   SA_DATE              date not null,
   SA_ANSWER            int not null,
   primary key (SA_ID)
);

alter table TB_M_BANK_ACCOUNT add constraint FK_EMP_HAVE_BANK_ACCOUNT foreign key (EMP_NIK)
      references TB_M_EMPLOYEE (EMP_NIK) on delete restrict on update restrict;

alter table TB_T_ATTENDANCE add constraint FK_EMP_ENTRY_ATTENDANCE foreign key (EMP_NIK)
      references TB_M_EMPLOYEE (EMP_NIK) on delete restrict on update restrict;

alter table TB_T_DAILY_WORK add constraint FK_EMP_ENTRY_DAILY_WORK foreign key (EMP_NIK)
      references TB_M_EMPLOYEE (EMP_NIK) on delete restrict on update restrict;

alter table TB_T_PERMISSION_REQ add constraint FK_EMP_ENTRY_PERMISSION_REQ foreign key (EMP_NIK)
      references TB_M_EMPLOYEE (EMP_NIK) on delete restrict on update restrict;

alter table TB_T_SALARY add constraint FK_EMP_GET_SALARY foreign key (EMP_NIK)
      references TB_M_EMPLOYEE (EMP_NIK) on delete restrict on update restrict;

alter table TB_T_SELF_ASSESSMENT add constraint FK_EMP_ENTRY_SELF_ASSESSMENT foreign key (EMP_NIK)
      references TB_M_EMPLOYEE (EMP_NIK) on delete restrict on update restrict;

alter table TB_T_SELF_ASSESSMENT add constraint FK_SELF_ASSESSMENT_HAS_QUESTION foreign key (SAQ_ID)
      references TB_M_SA_QUESTION (SAQ_ID) on delete restrict on update restrict;


INSERT INTO `tb_m_employee` (`EMP_NIK`, `EMP_NAME`, `EMP_STATUS`, `EMP_POSITION`, `EMP_PASSWORD`) VALUES
('1603171', 'A Maulana ', '1', 0, 'tes'),
('1603172', 'Desi Anggraeni', '0', 3, '12345'),
('1603173', 'Chila', '1', 1, 'tes'),
('1603174', 'Tess', '1', 3, 'tes');


INSERT INTO `tb_t_daily_work` (`EMP_NIK`, `WORK_DAY`, `WORK_HOURS`, `SPESIFICATION`, `CLASSIFICATION`, `LOT_NO`, `MATERIAL_INPUT_QTY`, `PROCESSED_QTY`, `FAULTY_QTY`, `NOTE`, `CONFIRM_FLAG`, `CONFIRM_USER`, `CONFIRM_DATE`) VALUES
('1603171', '2016-06-22', 2, '0', '1', '1', 1, 1, 1, '1', 1, '1603172', '2016-07-10'),
('1603172', '2016-06-22', 8, '0', 't', '1', 11, 1, 1, 'Note', 1, '1603172', '2016-07-10'),
('1603171', '2016-06-23', 8, '1', 'b', '2', 22, 2, 2, 'c', 0, '0', '0000-00-00'),
('1603172', '2016-06-23', 8, '0', 'cC', '1', 11, 1, 0, 'bB', 0, '0', '0000-00-00'),
('1603172', '2016-07-01', 5, '1', 'Tes Classification', '12', 23, 33, 4, 'Tes Note', 0, 'null', 'null'),
('1603172', '2016-07-02', 80, '2', 'u', '9', 9, 9, 9, 'tes', 1, '1603173', '2016-07-10'),
('1603172', '2016-07-04', 5, '0', 'tes', '2', 3, 3, 3, '3', 0, 'null', 'null'),
('1603172', '2016-07-10', 80, '2', 'u', '9', 9, 9, 9, 'tes', 1, '1603173', '2016-07-10'),
('1603172', '2016-07-11', 90, '2', 'u', '9', 9, 9, 9, 'tes', 1, '1603173', '2016-07-10');


INSERT INTO `tb_t_salary` (`EMP_NIK`, `PERIODE_DATE_START`, `PERIODE_DATE_END`, `HOURLY_WAGE`, `EXTENSION`, `NIGHT_SHIFT`, `OVER_TIME_WEEKEND`, `TRANSPORT_FEE`, `NORMAL_FEE`, `HOURLY_WAGE_AMOUNT`, `EXTENSION_AMOUNT`, `NIGHT_SHIFT_AMOUNT`, `OVER_TIME_WEEKEND_AMOUNT`, `TRANSPORT_FEE_AMOUNT`, `INCOME_TAX`, `PAY_TOTAL_AMOUNT`, `PENSION`, `HEALTH_INSURANCE`, `LONG_TERM_CARE_INSURANCE`, `TOTAL_SALARY`, `TOTAL_DEDUCTION`, `TOTAL_HOURS`) VALUES
('1603171', '2016-06-01', '2016-06-30', 2, 1, 2, 3, 4, 12140, 12140, 9105, 18210, 27315, 12000, 3338.5, 66770, 2470.49, 1335.4, 333.85, 65525.3, 4139.74, 2),
('1603172', '2016-06-01', '2016-06-23', 16, 1, 2, 3, 4, 97120, 97120, 9105, 18210, 27315, 12000, 7587.5, 151750, 5614.75, 3035, 758.75, 145236, 9408.5, 16);

