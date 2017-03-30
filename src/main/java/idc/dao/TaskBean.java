package idc.dao;

import idc.utils.StringUtils;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by sky on 2017/3/11.
 */
public class TaskBean {

    private int taskId;
    private int lowerAge;
    private int higherAge;
    private String professional;
    private String city;
    private String searchWord;
    private String category;
    private Date startSessionTime;
    private Date endSessionTime;

    public TaskBean() {
    }

    @Override
    public String toString() {
        return "TaskBean{" +
                "taskId=" + getTaskId() +
                ", lowerAge=" + getLowerAge() +
                ", higherAge=" + getHigherAge() +
                ", professional='" + getProfessional() + '\'' +
                ", city='" + getCity() + '\'' +
                ", searchWord='" + getSearchWord() + '\'' +
                ", category='" + getCategory() + '\'' +
                ", startSessionTime=" + getStartSessionTime() +
                ", endSessionTime=" + getEndSessionTime() +
                '}';
    }

    public TaskBean(int taskId, int lowerAge, int higherAge, String professional, String city, String searchWord,
                    String category, Date startSessionTime, Date endSessionTime) {
        this.taskId = taskId;
        this.lowerAge = lowerAge;
        this.higherAge = higherAge;
        this.professional = professional;
        this.city = city;
        this.searchWord = searchWord;
        this.category = category;
        this.startSessionTime = startSessionTime;
        this.endSessionTime = endSessionTime;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public int getLowerAge() {
        return lowerAge;
    }

    public void setLowerAge(int lowerAge) {
        this.lowerAge = lowerAge;
    }

    public int getHigherAge() {
        return higherAge;
    }

    public void setHigherAge(int higherAge) {
        this.higherAge = higherAge;
    }

    public ArrayList<String> getProfessional() {
        return StringUtils.dumpJson(professional);
    }

    public void setProfessional(String professional) {
        this.professional = professional;
    }

    public ArrayList<String> getCity() {
        return StringUtils.dumpJson(city);
    }

    public void setCity(String city) {
        this.city = city;
    }

    public ArrayList<String> getSearchWord() {
        return StringUtils.dumpJson(searchWord);
    }

    public void setSearchWord(String searchWord) {
        this.searchWord = searchWord;
    }

    public ArrayList<String> getCategory() {
        return StringUtils.dumpJson(category);
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Date getStartSessionTime() {
        return startSessionTime;
    }

    public void setStartSessionTime(Date startSessionTime) {
        this.startSessionTime = startSessionTime;
    }

    public Date getEndSessionTime() {
        return endSessionTime;
    }

    public void setEndSessionTime(Date endSessionTime) {
        this.endSessionTime = endSessionTime;
    }

}
