alter table ZL_SOFTWARE_VERSION
  add constraint UNI_ZL_SOFTWARE_VERSION_P_VER unique (PLAT, VER_MAIN, VER_SUB, VER_DEPLOY, VER_BUILD)
  using index;