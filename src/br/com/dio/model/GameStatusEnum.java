package br.com.dio.model;

public enum GameStatusEnum {
    NON_STARTED("Nao iniciado"),
    INCOMPLETE("Incompleto"),
    COMPLETED("Concluido");


    private String label;

    GameStatusEnum(final String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

}
