package com.example.bean;

//from https://github.com/wildcreek/MultiChoice
public class QuestionOptionBean extends BaseBean {

    private String name;
    private String description;

    public QuestionOptionBean() {
        super();
    }

    public QuestionOptionBean(String name, String description) {
        super();
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "QuestionOptionBean [name=" + name + ", description="
                + description + "]";
    }

}