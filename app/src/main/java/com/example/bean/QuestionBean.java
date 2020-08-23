package com.example.bean;

import java.util.List;

//from https://github.com/wildcreek/MultiChoice
public class QuestionBean extends BaseBean {

    private String questionId;
    private String description;
    private int questionType;
    private String knowledgePointName;
    private String knowledgePointId;
    private List<QuestionOptionBean> questionOptions; // 选项集合
    private String correct;

    public QuestionBean() {
        super();
    }

    public QuestionBean(String questionId, String description,
                        int questionType, String knowledgePointName,
                        String knowledgePointId, List<QuestionOptionBean> questionOptions,String correct) {
        super();
        this.questionId = questionId;
        this.description = description;
        this.questionType = questionType;
        this.knowledgePointName = knowledgePointName;
        this.knowledgePointId = knowledgePointId;
        this.questionOptions = questionOptions;
        this.correct = correct;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQuestionType() {
        return questionType;
    }

    public String getCorrect(){
        return correct;
    }

    public void setQuestionType(int questionType) {
        this.questionType = questionType;
    }

    public String getKnowledgePointName() {
        return knowledgePointName;
    }

    public void setKnowledgePointName(String knowledgePointName) {
        this.knowledgePointName = knowledgePointName;
    }

    public String getKnowledgePointId() {
        return knowledgePointId;
    }

    public void setKnowledgePointId(String knowledgePointId) {
        this.knowledgePointId = knowledgePointId;
    }

    public List<QuestionOptionBean> getQuestionOptions() {
        return questionOptions;
    }

    public void setQuestionOptions(List<QuestionOptionBean> questionOptions) {
        this.questionOptions = questionOptions;
    }

    @Override
    public String toString() {
        return "QuestionBean [questionId=" + questionId + ", description="
                + description + ", questionType=" + questionType
                + ", knowledgePointName=" + knowledgePointName
                + ", knowledgePointId=" + knowledgePointId
                + ", questionOptions=" + questionOptions + "]";
    }

}