package com.senai.Volksway.models;

public enum TipoModel {
    ADMIN("admin"),
    ADM_FROTA("adm_frota"),
    PROPRIETARIO("proprietario"),
    MOTORISTA("motorista");

    private String tipo;

    TipoModel(String tipoRecebido){
        this.tipo = tipoRecebido;
    }

    public String getRole(){
        return tipo;
    }
}
