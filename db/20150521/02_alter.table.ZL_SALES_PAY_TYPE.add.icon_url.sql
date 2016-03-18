alter table ZL_SALES_PAY_TYPE add ICON_URL VARCHAR2(255);
alter table ZL_SALES_PAY_TYPE add ID number(20) not null;
alter table ZL_SALES_PAY_TYPE add constraint PK_ZL_SALES_PAY_TYPE primary key (ID);
