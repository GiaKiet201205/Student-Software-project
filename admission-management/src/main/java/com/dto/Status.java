package com.dto;


import lombok.Getter;

public class Status {

    @Getter
    private Integer id;
    private String name;

    public Status(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString(){
        return name;
    }

}
