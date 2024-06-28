package dev.inspector.springdemo.dto;


public class UserDto {

    private String nome;

    public UserDto() {
    }

    public UserDto(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }



}
