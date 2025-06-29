package com.utec.enums;

public enum TipoReporteEnum {
    INSCRIPCION("INSCRIPCION"),
    CANCELACION("CANCELACION"),
    AMBAS("AMBAS");
    
    private final String valor;
    
    TipoReporteEnum(String valor) {
        this.valor = valor;
    }
    
    public String getValor() {
        return valor;
    }
    
    public static TipoReporteEnum fromString(String texto) {
        for (TipoReporteEnum tipo : TipoReporteEnum.values()) {
            if (tipo.valor.equalsIgnoreCase(texto)) {
                return tipo;
            }
        }
        return AMBAS; // Valor por defecto
    }
} 