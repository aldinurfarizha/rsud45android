package com.ali.rsud45.Config;

public class ServerUrl {


  //Root URL
  private static final String ROOT_URL = "http://45.156.24.136/rsud45web/api/";

  //Getting Image Conf URL
  public static final String GET_IMAGE ="";

  //SubRoot URL
  public static final String LOGIN_DOKTER= ROOT_URL + "login_dokter";
  public static final String LOGIN_PASIEN= ROOT_URL + "login_pasien";
  public static final String DASHBOARD_DOKTER= ROOT_URL + "dashboard_dokter";
  public static final String DASHBOARD_PASIEN= ROOT_URL + "dashboard_pasien";
  public static final String GET_DOKTER= ROOT_URL + "get_dokter";
  public static final String ANTRI_POLI= ROOT_URL + "daftar_poli";
  public static final String STATUS_ANTRIAN= ROOT_URL + "status_antrian";
  public static final String BATAL= ROOT_URL + "batal";
}
